package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Spacca;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class MatchItemController
{
    @FXML
    private Label partitaId;

    @FXML
    private Button playButtonId;

    @FXML
    private Label stateId;

    private int codicePartita, passwordPartita;
    MatchData matchData;


    public void setData(MatchData matchData)
    {
        this.partitaId.setText(matchData.getIdMatch());
        playButtonId.setVisible(true);
        stateId.setText("State: INIZIA");
        this.passwordPartita = matchData.getPassword();
        this.codicePartita = matchData.codice;
    }

    public void accediPartita(javafx.event.ActionEvent actionEvent)
    {
        // una volta che clicco su un determinato pulsante play, mi deve aprire la schermata di un pre partita, dove carica
        // delle determinate informazioni di tale partita (giocatori, tipo, round, se sta ancora andando etc..)
        // prendo le info dal json

        System.out.println("Ho cliccato sul bottone");
        try
        {
            AudioManager.bottoneSuono();
            FXMLLoader playerScreen = new FXMLLoader(Spacca.class.getResource("PlayerScreen.fxml"));

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Parent root = playerScreen.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            PlayerScreenController playerController = playerScreen.getController();
            playerController.setInfoPartita(codicePartita, passwordPartita);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
