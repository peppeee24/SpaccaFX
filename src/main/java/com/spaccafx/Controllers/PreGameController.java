package com.spaccafx.Controllers;

import com.spaccafx.Enums.GameStatus;
import com.spaccafx.Files.AudioManager;
import com.spaccafx.Files.FileManager;
import com.spaccafx.Files.ResourceLoader;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;
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
import javafx.scene.image.Image;
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
    Label numerovite1, numerovite2,numerovite3,numerovite4,giocatore1,giocatore2,giocatore3,giocatore4, currentPlayer, currentRound;

    @FXML
    ImageView user1, user2, user3, user4, cuore1, cuore2, cuore3, cuore4;

    private int codicePartita, passwordPartita;



    public void setInfoPartita2(int codicePartita, int passwordPartita) {
        this.codicePartita = codicePartita;
        this.passwordPartita = passwordPartita;

        // Carichiamo i dati dal file
        //AlertController.showErrore("Sto caricando i dati della partita: " + codicePartita);
        Partita p = FileManager.leggiPartitaDaFile(codicePartita);
        ArrayList<IGiocatore> giocatori = p.giocatori;

        // Imposta i dettagli per ogni giocatore
        mostraDatiSingolaPartita(giocatori.get(0), user1, giocatore1, numerovite1);
        mostraDatiSingolaPartita(giocatori.get(1), user2, giocatore2, numerovite2);
        mostraDatiSingolaPartita(giocatori.get(2), user3, giocatore3, numerovite3);
        mostraDatiSingolaPartita(giocatori.get(3), user4, giocatore4, numerovite4);

        // Imposta altre informazioni sulla partita
        if(p.getPartitaStatus() != GameStatus.STARTED && p.getPartitaStatus() != GameStatus.ENDED)
        {
            currentPlayer.setVisible(true);
            currentRound.setVisible(true);

            currentRound.setText("La partita riprenderà dal Round: " + p.getCurrentRound());
            currentPlayer.setText("Toccherà al giocatore: " + p.getCurrentGiocatore().getNome());
        }
        else
        {
            currentPlayer.setVisible(false);
            currentRound.setVisible(false);
        }

    }

    private void mostraDatiSingolaPartita(IGiocatore giocatore, ImageView imageView, Label nomeLabel, Label viteLabel)
    {
        // Imposta nome e vite
        if (nomeLabel != null && viteLabel != null) {
            nomeLabel.setText(giocatore.getNome());
            viteLabel.setText(Integer.toString(giocatore.getVita()));
        }

        // Assegna l'immagine corrispondente in base al tipo di giocatore
        if (imageView != null) {
            if (giocatore instanceof EasyBot) {
                imageView.setImage(ResourceLoader.loadImage("/Assets/Game/Environment/easyBot.PNG"));
            } else if (giocatore instanceof AdvancedBot) {
                imageView.setImage(ResourceLoader.loadImage("/Assets/Game/Environment/hardBot.PNG"));
            } else if (giocatore instanceof Giocatore) {
                imageView.setImage(ResourceLoader.loadImage("/Assets/Game/Environment/userIcons.png"));
            }
        }
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
