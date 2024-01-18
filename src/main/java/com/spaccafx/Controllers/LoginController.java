package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class LoginController {


    @FXML
    TextField userField;
    @FXML
    PasswordField passwordField;


    private String user = "root";
    private String pwd = "root";

    public void initialize() {
        // Imposta il focus sul campo userField quando la schermata viene inizializzata
        userField.requestFocus();
    }

    public void loginAction(ActionEvent actionEvent) throws IOException {
        AudioManager.bottoneSuono();
        String UserField = userField.getText();
        String PasswordField = passwordField.getText();

        if (user.compareTo(UserField) == 0 && pwd.compareTo(PasswordField) == 0) {

            boolean isOkPressed = AlertController.showConfirm("Accesso Eseguito, stai per essere reindirizzato");

            if (isOkPressed) {

                FXMLLoader SelectionMenu = new FXMLLoader(Spacca.class.getResource("SelectionMenu.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(SelectionMenu.load());
                stage.setScene(scene);
                stage.show();
            }

            } else {
                AudioManager.erroreSuono();
                AlertController.showErrore("Errore: Credenziali errate!");

            }
        }



    public void indietro(MouseEvent mouseEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }

    public void telegram(MouseEvent mouseEvent) throws IOException {
        AudioManager.bottoneSuono();


        boolean isOkPressed = AlertController.showConfirm("Stai per essere reinderizzato su Telegram");

        if (isOkPressed) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().browse(new URI("https://t.me/+tdRVfYk5QM4xZWRk"));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }).start();

        }


    }


}


