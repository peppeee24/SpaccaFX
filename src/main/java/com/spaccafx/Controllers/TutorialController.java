package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TutorialController {




    public void tutorialTorneo(ActionEvent actionEvent) throws IOException  // (LOGIN) ADMIN MENU
    {
        AudioManager.bottoneSuono();
        FXMLLoader Login = new FXMLLoader(Spacca.class.getResource("tutorialTorneo.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Login.load());
        stage.setScene(scene);
        stage.show();
    }

    public void tutorialPartita(ActionEvent actionEvent) throws IOException { // PLAY
        AudioManager.bottoneSuono();
        FXMLLoader playerScreen = new FXMLLoader(Spacca.class.getResource("tutorialPartita.fxml"));
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








}



