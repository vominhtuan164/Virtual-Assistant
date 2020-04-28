package sample.controller;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sourceforge.javaflacencoder.FLACFileWriter;
import sample.Main;

import javax.sound.sampled.LineUnavailableException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, GSpeechResponseListener {

    // Initial variable
    private int clickCounter = 0;
    private Microphone micro = new Microphone(FLACFileWriter.FLAC);
    public static final String API_KEY = "AIzaSyClXZmx76lcqzkDbXbgxRS6dMmOBBYysrA";
    private GSpeechDuplex duplex = new GSpeechDuplex(API_KEY);
    private static Main mainInstance = new Main();

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField recTextView;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton maximizeButton;

    @FXML
    private JFXButton minimizeButton;

    @FXML
    private JFXHamburger hamAnimation;

    @FXML
    private JFXButton startButton;

    @FXML
    private VBox drawer;

    @FXML
    private StackPane backgroundStackPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialize");

        // Initial, StackPane is hided
        backgroundStackPane.setVisible(false);
        backgroundStackPane.setOpacity(0.8);

        // Hamburger transition
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamAnimation);
        transition.setRate(-1);

        hamAnimation.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();

            TranslateTransition openNav = new TranslateTransition(new Duration(500), drawer);
            openNav.setToX(0);

            TranslateTransition closeNav = new TranslateTransition(new Duration(500), drawer);

            if (drawer.getTranslateX() != 0) {

                System.out.println("Navigation Opened.");
                openNav.play();

            } else {

                System.out.println("Navigation Closed.");
                closeNav.setToX(-(drawer.getWidth()));
                closeNav.play();

            }
        });
    }

    @FXML
    private void handleMaximizeButtonClicked(ActionEvent event) {
        clickCounter++;
        if (clickCounter % 2 == 0) {
            System.out.println("Going Windowed Mode.");
            (
                    (Stage) (
                            (Node) event.getSource()
                    ).getScene().getWindow()
            ).setFullScreen(false);

        } else {
            System.out.println("Going FullScreen Mode.");
            (
                    (Stage) (
                            (Node) event.getSource()
                    ).getScene().getWindow()
            ).setFullScreen(true);
        }
    }

    @FXML
    private void handleMinimizeButtonClicked(ActionEvent event) {
        System.out.println("Going Minimize.");
        (
                (Stage) (
                        (Node) event.getSource()
                ).getScene().getWindow()
        ).setIconified(true);
    }


    @FXML
    private void handleExitNavButtonClicked(ActionEvent event) {
        System.out.println("Exiting application. See you again!");
        Platform.exit();
    }

    @FXML
    private void handleCloseButtonClicked(ActionEvent event) {
        System.out.println("Exiting application. See you again!");
        stopSpeechRecognition();
        Platform.exit();
    }

    private void stopSpeechRecognition() {
        micro.close();
    }

    @FXML
    private void handleAboutButtonClicked(ActionEvent event) {
        System.out.println("About Button Clicked.");

        JFXDialogLayout aboutDialogLayout = new JFXDialogLayout();
        aboutDialogLayout.setHeading(new Text("About"));
        aboutDialogLayout.setBody(new Text("Minh Tuan VO - 4ème année en STI - Option: Commerce Electronique"));

        JFXDialog aboutDialog = new JFXDialog(backgroundStackPane,
                aboutDialogLayout,
                JFXDialog.DialogTransition.CENTER);
        backgroundStackPane.setVisible(true);
        JFXButton aboutDialogOkayButton = new JFXButton("Okay");
        aboutDialog.setOnDialogOpened( aboutDialogOpened -> {
            System.out.println("About Dialog Box Opened.");
            aboutDialogOkayButton.setOnAction((aboutDialogOpenedEvent) -> {

                aboutDialog.close();
                backgroundStackPane.setVisible(false);
            });
            aboutDialogLayout.setActions(aboutDialogOkayButton);
        });
        aboutDialog.show();
    }

    @FXML
    private void handleStartButtonClicked(ActionEvent event) {
        System.out.println("Start Button Clicked");
        startRec();

    }

    private void startRec() {
        duplex.setLanguage("en");
        duplex.addResponseListener(new GSpeechResponseListener() {

            String old_text = "";

            @Override
            public void onResponse(GoogleResponse googleResponse) {
                System.out.println("In response");
                String output = "";
                output = googleResponse.getResponse();

                if (googleResponse.getResponse() == null) {

                    this.old_text = recTextView.getText();

                    if (this.old_text.contains("(")) {

                        this.old_text = this.old_text.substring(0, this.old_text.indexOf('('));
                    }
                    System.out.println("Paragraph Line Added");

                    this.old_text = (recTextView.getText() + "\n");
                    this.old_text = this.old_text.replace(")", "").replace("( "
                            , "");

                    recTextView.setText(this.old_text);
                    return;
                }
                if (output.contains("(")) {

                    output = output.substring(0, output.indexOf('('));
                }
                if (!googleResponse.getOtherPossibleResponses().isEmpty()) {

                    output = output + " (" + (String) googleResponse.getOtherPossibleResponses().get(0) + ")";
                }

                System.out.println("||-> " + output);
                recTextView.setText("");

                recTextView.appendText(this.old_text);
                recTextView.appendText(output);
            }
        });
        startSpeechRecognition();
    }

    private void startSpeechRecognition() {

        new Thread(
                () -> {
                    try {

                        duplex.recognize(micro.getTargetDataLine(), micro.getAudioFormat());

                    } catch (InterruptedException | LineUnavailableException exception) {

                        System.out.println("Exception caused : " + exception.getMessage());
                    }
                }).start();
    }

    @FXML
    private void handleSettingsButtonClicked(ActionEvent event) {
        System.out.println("Setting Button Clicked");
    }

    @FXML
    private void handleUserButtonClicked(ActionEvent event) {
        System.out.println("User Button Clicked.");
        mainInstance.openLinkInBrowser("https://github.com/vominhtuan164");
    }

    @Override
    public void onResponse(GoogleResponse googleResponse) {
        // Do nothing
    }
}