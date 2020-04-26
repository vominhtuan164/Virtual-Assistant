package sample.controller;

import com.darkprograms.speech.microphone.Microphone;
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

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // Initial variable
    private int clickCounter = 0;
    private Microphone micro = new Microphone(FLACFileWriter.FLAC);

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

//    @FXML
//    private StackPane backgroundStackPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialize");

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

//        JFXDialogLayout aboutDialogLayout = new JFXDialogLayout();
//        aboutDialogLayout.setHeading(new Text("About"));
//        aboutDialogLayout.setBody(new Text("Made by : Minh Tuan VO - 4ASTI - Option: Commerce Electronique"));
//        aboutDialogLayout.setOpacity(1);
//        aboutDialogLayout.setStyle("-fx-background-color: rgba(250,250,250,0.15)");
//        aboutDialogLayout.setStyle("-fx-background-radius: 20%");
//        aboutDialogLayout.applyCss();
//
//        JFXDialog aboutDialog = new JFXDialog(backgroundStackPane,
//                aboutDialogLayout,
//                JFXDialog.DialogTransition.CENTER);
//        backgroundStackPane.setVisible(true);
//
//        JFXButton aboutDialogOkayButton = new JFXButton("Okay");
//
//        aboutDialog.setOnDialogOpened( aboutDialogOpened -> {
//            System.out.println("About Dialog Box Opened.");
//            aboutDialogOkayButton.setOnAction((aboutDialogOpenedEvent) -> {
//
//                aboutDialog.close();
//                backgroundStackPane.setVisible(false);
//            });
//
//            aboutDialogLayout.setActions(aboutDialogOkayButton);
//
//
//        });
//
//        aboutDialog.show();
    }

    @FXML
    private void handleStartButtonClicked(ActionEvent event) {
    }

    @FXML
    private void handleSettingsButtonClicked(ActionEvent event) {
    }

    @FXML
    private void handleUserButtonClicked(ActionEvent event) {
        System.out.println("User Button Clicked.");

    }
}