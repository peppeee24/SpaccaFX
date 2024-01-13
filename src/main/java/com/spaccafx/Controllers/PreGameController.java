package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Files.FileManager;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class PreGameController
{

    @FXML
    Button giocaButton;

    @FXML
    Label numerovite1, numerovite2,numerovite3,numerovite4,giocatore1,giocatore2,giocatore3,giocatore4;

    @FXML
    Text currentPlayer, currentRound;

    @FXML
    ImageView user1, user2, user3, user4, cuore1, cuore2, cuore3, cuore4;

    private int codicePartita, passwordPartita;

    public void setInfoPartita(int codicePartita, int passwordPartita)
    {
        this.codicePartita = codicePartita;
        this.passwordPartita = passwordPartita;

        // carichiamo i dati dal file

        Partita p = FileManager.leggiPartitaDaFile(codicePartita);
        ArrayList<IGiocatore> giocatori = p.giocatori;

        giocatore1.setText(giocatori.get(0).getNome());
        numerovite1.setText(Integer.toString(giocatori.get(0).getVita()));

        giocatore2.setText(giocatori.get(1).getNome());
        numerovite2.setText(Integer.toString(giocatori.get(1).getVita()));

        giocatore3.setText(giocatori.get(2).getNome());
        numerovite3.setText(Integer.toString(giocatori.get(2).getVita()));

        giocatore4.setText(giocatori.get(3).getNome());
        numerovite4.setText(Integer.toString(giocatori.get(3).getVita()));

        currentRound.setText(Integer.toString(p.getCurrentRound()));
        currentPlayer.setText(p.getCurrentGiocatore().getNome());

    }

    public void apriTavolo(ActionEvent actionEvent) throws IOException // bottone inizia
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


    public void indietro(MouseEvent mouseEvent) throws IOException
    {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("PartitaSelector.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }





}








