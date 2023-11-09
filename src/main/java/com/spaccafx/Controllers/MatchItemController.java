package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;
import com.spaccafx.Spacca;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
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

    public void setData(MatchData matchData)
    {
        this.partitaId.setText("ID: " + matchData.getCodice());

        switch (matchData.getStatus())
        {
            case STARTED:   stateId.setText("State: " + matchData.getStatus());
                            stateId.setTextFill(Color.GREEN); // crea il giocatore in base all istanza
                            playButtonId.setVisible(true); // lo rendiamo disponibile
                            playButtonId.setDisable(false);
                            break;

            case STOPPED:   stateId.setText("State: " + matchData.getStatus());
                            stateId.setTextFill(Color.ORANGE);
                            playButtonId.setVisible(true);
                            playButtonId.setDisable(false);
                            break;

            case PLAYING:   stateId.setText("State: " + matchData.getStatus());
                            stateId.setTextFill(Color.BLUE);
                            playButtonId.setVisible(false);
                            playButtonId.setDisable(true);
                            break;

            case ENDED:     stateId.setText("State: " + matchData.getStatus());
                            stateId.setTextFill(Color.RED);
                            playButtonId.setVisible(true);
                            playButtonId.setDisable(true); // lo disabilitamo perche il gioco e finito
                            break;

            default:        stateId.setText("State: ERRROR");
                            stateId.setTextFill(Color.RED);
                            playButtonId.setVisible(false);
                            playButtonId.setDisable(true);
        }

        this.passwordPartita = matchData.getPassword();
        this.codicePartita = matchData.getCodice();
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
