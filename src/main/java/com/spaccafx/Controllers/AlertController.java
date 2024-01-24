package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Spacca;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

public class AlertController {

    @FXML
    Label errorTypeLabel;
    @FXML
    Button okButtonError;
    @FXML
     Button noButtonConfirm;

    private Consumer<Boolean> okNoHandler;

    private boolean isOkButtonPressed = false;

    public boolean isOkButtonPressed() {
        return isOkButtonPressed;
    }


    public void initialize() // si attiva da SelectionMenuController
    {
        setErrorLabel("Errore");
    }


    private void setErrorLabel(String errorMessage) {
        errorTypeLabel.setText(errorMessage);

    }


    public void okButton(ActionEvent actionEvent) throws IOException  // (LOGIN) ADMIN MENU
    {
        AudioManager.bottoneSuono();
        Stage stage = (Stage) okButtonError.getScene().getWindow();

        // Chiudi la finestra
        stage.close();
    }


    public void okButtonConfirm(ActionEvent actionEvent) throws IOException  // (LOGIN) ADMIN MENU
    {
        AudioManager.bottoneSuono();
        Stage stage = (Stage) okButtonError.getScene().getWindow();

        // Chiudi la finestra
        stage.close();

        isOkButtonPressed = true;

        if (okNoHandler != null) {
            okNoHandler.accept(true);
        }
    }


    public void noButton(ActionEvent actionEvent) throws IOException  // (LOGIN) ADMIN MENU
    {
        AudioManager.bottoneSuono();
        Stage stage = (Stage) okButtonError.getScene().getWindow();

        // Chiudi la finestra
        stage.close();

      //  Platform.exit();
      //  System.exit(0);
    }





    public static void showErrore(String errorMessage) {
        try {
            FXMLLoader loader = new FXMLLoader(Spacca.class.getResource("Error.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            AlertController errorController = loader.getController();
            //errorTypeLabel.setText(errorMessage);
            errorController.setErrorLabel(errorMessage);

            stage.setTitle("Errore");
            stage.setResizable(false);
            stage.setScene(scene);

            // Blocca l'interazione con la finestra principale
            //  stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void showWarning(String errorMessage) {
        try {
            AudioManager.warningSuono();
            FXMLLoader loader = new FXMLLoader(Spacca.class.getResource("Warning.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            AlertController errorController = loader.getController();
            //errorTypeLabel.setText(errorMessage);
            errorController.setErrorLabel(errorMessage);

            stage.setTitle("Warning");
            stage.setResizable(false);
            stage.setScene(scene);

            // Blocca l'interazione con la finestra principale
            //  stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean showConfirm(String errorMessage) {
        try {
            AudioManager.confirmSuono();
            FXMLLoader loader = new FXMLLoader(Spacca.class.getResource("Confirmation.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            AlertController errorController = loader.getController();
            //errorTypeLabel.setText(errorMessage);
            errorController.setErrorLabel(errorMessage);

            stage.setTitle("Confirmation");
            stage.setResizable(false);
            stage.setScene(scene);

            // Blocca l'interazione con la finestra principale
            //  stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();
            return errorController.isOkButtonPressed();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}