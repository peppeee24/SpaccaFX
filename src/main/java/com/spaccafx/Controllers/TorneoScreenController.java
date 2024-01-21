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

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TorneoScreenController
{
    private int passwordTorneo, codiceTorneo, currentMatch;

    @FXML
    PasswordField passwordField;

    public TorneoScreenController() { // costruttore

        //this.partitaClassicaController = ShareData.getInstance().getPartitaClassicaController();
        //this.pwd=partitaClassicaController.P.getPasswordPartita();
    }

    public void setInfoTorneo(int codiceTorneo, int passwordTorneo, int currentMatch)
    {
        this.passwordTorneo = passwordTorneo;
        this.codiceTorneo = codiceTorneo;
        this.currentMatch = currentMatch;

        System.out.println("Sto prendendo il TORNEO con ID: " + codiceTorneo);
        System.out.println("Il torneo ha la Password: " + passwordTorneo);
        System.out.println("Riprendo dal match: " + currentMatch);
    }

    public void loginAction(ActionEvent actionEvent) throws IOException // bottone inizia
    {
        AudioManager.bottoneSuono();
try{
        int PasswordField = Integer.parseInt(passwordField.getText());


        System.out.println("Codice per accedere TORNEO: " + this.passwordTorneo);
        System.out.println("Codice INSERITO:" + PasswordField);


        if (passwordTorneo == PasswordField)
        {

            boolean isOkPressed = AlertController.showConfirm("Conferma: Accesso Eseguito, stai per entrare nel gioco");

            if (isOkPressed) {
                FXMLLoader loaderTavolo = new FXMLLoader(Spacca.class.getResource("Tavolo2.fxml"));
                //FXMLLoader loaderTavolo = new FXMLLoader(Spacca.class.getResource("PreGame.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Parent root = loaderTavolo.load();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();


                TavoloController tavoloController = loaderTavolo.getController();
                tavoloController.inizializzaClasseTorneo(this.codiceTorneo, this.currentMatch);

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
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("TorneoSelector.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }


    public void portaATutorial(MouseEvent mouseEvent) throws IOException {
        AudioManager.erroreSuono();



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








