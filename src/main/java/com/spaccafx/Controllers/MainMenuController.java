package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MainMenuController {

    private ShareData shareData;


    public void setShareData(ShareData shareData) {
        this.shareData = shareData;
    }

    public void initialize() throws URISyntaxException {

        // Fai partire la musica
        if (shareData != null && shareData.getAudioController() != null) {
            System.out.println("Playing audio...");
            shareData.getAudioController().playAudio1();
        } else {
            // System.out.println("AudioController not found in ShareData");
        }

        if (shareData != null && shareData.getAudioController() != null) {
            System.out.println("Playing audio...");
            shareData.getAudioController().playAudio2();
        } else {
            //  System.out.println("AudioController not found in ShareData");
        }
    }

    //region #BUTTONS

    public void startSetting(ActionEvent actionEvent) throws IOException  // (LOGIN) ADMIN MENU
    {
        AudioManager.bottoneSuono();
        FXMLLoader Login = new FXMLLoader(Spacca.class.getResource("Login.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Login.load());
        stage.setScene(scene);

        stage.show();
    }

    public void startGame(ActionEvent actionEvent) throws IOException { // PLAY
        AudioManager.bottoneSuono();
        FXMLLoader playerScreen = new FXMLLoader(Spacca.class.getResource("SelectionMenuGiocatore.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = playerScreen.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }


    public void startTutorial(ActionEvent actionEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader playerScreen = new FXMLLoader(Spacca.class.getResource("Tutorial.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = playerScreen.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    public void indietro(MouseEvent mouseEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }


    public void telegram(MouseEvent mouseEvent) {
        AudioManager.bottoneSuono();
        new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI("https://t.me/+tdRVfYk5QM4xZWRk"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void setting(MouseEvent mouseEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("Audio.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }

    // endregion

}



