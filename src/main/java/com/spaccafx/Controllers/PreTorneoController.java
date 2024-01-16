package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Files.FileManager;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class PreTorneoController
{

    @FXML
    Button giocaButton;

    @FXML
    Label numerovite1, numerovite2,numerovite3,numerovite4,numerovite11, numerovite21,numerovite31,numerovite41,numerovite12, numerovite22,numerovite32,numerovite42, numerovite13, numerovite23,numerovite33,numerovite43, numerovite14, numerovite24,numerovite34,numerovite44;
    Label giocatore1,giocatore2,giocatore3,giocatore4, giocatore11,giocatore21,giocatore31,giocatore41,giocatore12,giocatore22,giocatore32,giocatore42,giocatore13,giocatore23,giocatore33,giocatore43,giocatore14,giocatore24,giocatore34,giocatore44;
            Label currentPlayer, currentRound;



    @FXML
    ImageView user1, user2, user3, user4, user11, user21, user31, user41, user12, user22, user32, user42,user13, user23, user33, user43, user14, user24, user34, user44  ;

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

        currentRound.setText("La partita riprenderà dal Round: " +(Integer.toString(p.getCurrentRound())));
        currentPlayer.setText("Toccherà al giocatore: " +p.getCurrentGiocatore().getNome());

    }


    public void setInfoPartita2(int codicePartita, int passwordPartita) {
        this.codicePartita = codicePartita;
        this.passwordPartita = passwordPartita;

        // Carichiamo i dati dal file
        Partita p = FileManager.leggiPartitaDaFile(codicePartita);
        ArrayList<IGiocatore> giocatori = p.giocatori;

        // Imposta i dettagli per ogni giocatore
        for (int i = 0; i < giocatori.size(); i++) {
            IGiocatore giocatore = giocatori.get(i);
            ImageView imageView = null;
            Label nomeLabel = null;
            Label viteLabel = null;

            // Assegna le ImageView e le Label in base all'indice del giocatore
            switch (i) {
                case 0:
                    imageView = user1;
                    nomeLabel = giocatore1;
                    viteLabel = numerovite1;
                    break;
                case 1:
                    imageView = user2;
                    nomeLabel = giocatore2;
                    viteLabel = numerovite2;
                    break;
                case 2:
                    imageView = user3;
                    nomeLabel = giocatore3;
                    viteLabel = numerovite3;
                    break;
                case 3:
                    imageView = user4;
                    nomeLabel = giocatore4;
                    viteLabel = numerovite4;
                    break;
            }

            // Imposta nome e vite
            if (nomeLabel != null && viteLabel != null) {
                nomeLabel.setText(giocatore.getNome());
                viteLabel.setText(Integer.toString(giocatore.getVita()));
            }

            // Assegna l'immagine corrispondente in base al tipo di giocatore
            if (imageView != null) {
                if (giocatore instanceof EasyBot) {
                    imageView.setImage(new Image(getClass().getResourceAsStream("/Assets/Game/Environment/easyBot.PNG")));
                } else if (giocatore instanceof AdvancedBot) {
                    imageView.setImage(new Image(getClass().getResourceAsStream("/Assets/Game/Environment/hardBot.PNG")));
                } else if (giocatore instanceof Giocatore) {
                    imageView.setImage(new Image(getClass().getResourceAsStream("/Assets/Game/Environment/userIcons.png")));
                }
            }
        }

        // Imposta altre informazioni sulla partita
        currentRound.setText("La partita riprenderà dal Round: " + p.getCurrentRound());
        currentPlayer.setText("Toccherà al giocatore: " + p.getCurrentGiocatore().getNome());
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
