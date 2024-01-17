package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class TorneoScreenController
{
    private int passwordTorneo, codiceTorneo;

    @FXML
    PasswordField passwordField;

    public TorneoScreenController() { // costruttore

        //this.partitaClassicaController = ShareData.getInstance().getPartitaClassicaController();
        //this.pwd=partitaClassicaController.P.getPasswordPartita();
    }

    public void setInfoTorneo(int codiceTorneo, int passwordTorneo)
    {
        this.passwordTorneo = passwordTorneo;
        this.codiceTorneo = codiceTorneo;
        System.out.println("Sto prendendo la PARTITA con ID: " + codiceTorneo);
        System.out.println("Il match ha la Password: " + passwordTorneo);
    }

    public void loginAction(ActionEvent actionEvent) throws IOException // bottone inizia
    {
        AudioManager.bottoneSuono();
        // TODO CREARE UNA PASSWORD PER OGNI PARTITA != DAL ID PARTITA

        int PasswordField = Integer.parseInt(passwordField.getText());
        // TODO SISTEMARE ERRORE DI CONVERSIONE NEL CASO INSERISCA UNA STRINGA

        System.out.println("Codice per accedere PARTITA: " + this.passwordTorneo);
        System.out.println("Codice INSERITO:" + PasswordField);


        if (passwordTorneo == PasswordField)
        {


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
            tavoloController.inizializzaClassePartita(this.codiceTorneo);

        }
        else // se sbagliato
        {
            AudioManager.erroreSuono();

            AlertController.showErrore("Errore: Codice partita Errato!");
        }
    }


    public void indietro(MouseEvent mouseEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("TorneoSelector.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }


    public void portaATutorial(MouseEvent mouseEvent) throws IOException {
        AudioManager.erroreSuono();


        AlertController.showConfirm("Stai per essere reinderizzato al Tutorial");

        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("Tutorial.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();

    }


}








