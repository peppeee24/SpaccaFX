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
        try {
        int PasswordField = Integer.parseInt(passwordField.getText());

        System.out.println("Codice per accedere PARTITA: " + this.passwordPartita);
        System.out.println("Codice INSERITO:" + PasswordField);


        if (passwordPartita == PasswordField)
        {

            boolean isOkPressed = AlertController.showConfirm("Conferma: Accesso Eseguito, stai per entrare nel gioco");

            if (isOkPressed) {

                AlertController.showWarning("Attendi caricamento tavolo");
                FXMLLoader loaderTavolo = new FXMLLoader(Spacca.class.getResource("Tavolo2.fxml"));
                //FXMLLoader loaderTavolo = new FXMLLoader(Spacca.class.getResource("PreGame.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Parent root = loaderTavolo.load();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();


                TavoloController tavoloController = loaderTavolo.getController();
                tavoloController.inizializzaClassePartita(this.codicePartita);
            }





        }
        else // se sbagliato
        {
            AudioManager.erroreSuono();

            AlertController.showErrore("Errore: Codice partita Errato!");
        }
        } catch (NumberFormatException e) {
            // Gestione dell'eccezione nel caso in cui il testo non sia un numero
            AudioManager.erroreSuono();
            AlertController.showErrore("Errore: Inserisci un numero valido per la password!");
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
        AudioManager.leaderboardSuono();


        boolean isOkPressed = AlertController.showConfirm("Stai per essere reinderizzato al Tutorial");

        if (isOkPressed) {

            FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("Tutorial.fxml"));
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(Indietro.load());
            stage.setScene(scene);
            stage.show();
        }



    }


}








