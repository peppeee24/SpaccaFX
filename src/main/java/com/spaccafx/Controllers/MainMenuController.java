package com.spaccafx.Controllers;

import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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


    public void startGame(ActionEvent actionEvent) throws IOException {
// TODO ci pensiamo quando avremo fatto i file, altrimenti è complicato leggere


    }




    public void startTutorial(ActionEvent actionEvent) throws IOException{

    }

    public void startSetting(ActionEvent actionEvent) throws IOException{
        FXMLLoader Login = new FXMLLoader(Spacca.class.getResource("Login.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Login.load());
        stage.setScene(scene);
        stage.show();
    }

    public void indietro(MouseEvent mouseEvent) throws IOException {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }



    public void telegram(MouseEvent mouseEvent) {
        new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI("https://t.me/+tdRVfYk5QM4xZWRk"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void setting(MouseEvent mouseEvent) throws IOException {
        // TODO fare pagina per impotazioni audio e crediti
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("Setting2.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }





    @FXML
    Toggle italian;

    @FXML
    Toggle english;

    // TODO da impostare cambio lingua



}



