package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Files.FileManager;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class PlayerScreenController
{
    private int passwordPartita, codicePartita;

    @FXML
    PasswordField passwordField;

    public PlayerScreenController() { // costruttore

        //this.partitaClassicaController = ShareData.getInstance().getPartitaClassicaController();
        //this.pwd=partitaClassicaController.P.getPasswordPartita();
    }

    public void setInfoPartita(int codicePartita, int passwordPartita)
    {
        this.passwordPartita = passwordPartita;
        this.codicePartita = codicePartita;
        System.out.println("Sto prendendo la PARTITA con ID: " + codicePartita);
        System.out.println("Il match ha la Password: " + passwordPartita);
    }

    public void loginAction(ActionEvent actionEvent) throws IOException // bottone inizia
    {
        AudioManager.bottoneSuono();
        // TODO CREARE UNA PASSWORD PER OGNI PARTITA != DAL ID PARTITA

        int PasswordField = Integer.parseInt(passwordField.getText());
        // TODO SISTEMARE ERRORE DI CONVERSIONE NEL CASO INSERISCA UNA STRINGA

        System.out.println("Codice per accedere PARTITA: " + this.passwordPartita);
        System.out.println("Codice INSERITO:" + PasswordField);


        if (passwordPartita == PasswordField)
        {
           /* Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Accesso Eseguito");
            alert.setContentText("Stai per entrare nel gioco");
            Optional<ButtonType> result = alert.showAndWait();

            */

            AlertController.showConfirm("Conferma: Accesso Eseguito, stai per entrare nel gioco");


            FXMLLoader loaderTavolo = new FXMLLoader(Spacca.class.getResource("Tavolo3Prototype.fxml"));
            //FXMLLoader loaderTavolo = new FXMLLoader(Spacca.class.getResource("PreGame.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent root = loaderTavolo.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            //stage.setFullScreen(true); // todo FIXXARE PROBLEMI GRAFICA ADATTIVA
            stage.show();


            TavoloController tavoloController = loaderTavolo.getController();
            tavoloController.inizializzaClassePartita(this.codicePartita);

        }
        else // se sbagliato
        {
            AudioManager.erroreSuono();
        /*    Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText("Codice partita Errato");
            Optional<ButtonType> result = alert.showAndWait();

         */
            AlertController.showErrore("Errore: Codice partita Errato!");
        }
    }


    public void indietro(MouseEvent mouseEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("PartitaSelector.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }


    public void portaATutorial(MouseEvent mouseEvent) throws IOException {
        AudioManager.erroreSuono();

      /*  Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Recupera passowrd");
        alert.setContentText("Stai per essere reinderizzato su Telegram");
        Optional<ButtonType> result = alert.showAndWait();

       */

        AlertController.showConfirm("Stai per essere reinderizzato al Tutorial");

        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("Tutorial.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();

    }


}








