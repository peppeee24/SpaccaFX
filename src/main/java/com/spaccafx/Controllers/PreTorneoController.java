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
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class PreTorneoController {

    @FXML
    Button giocaButton;

    @FXML
    Label numerovite1, numerovite2, numerovite3, numerovite4, numerovite11, numerovite21, numerovite31, numerovite41, numerovite12, numerovite22, numerovite32, numerovite42, numerovite13, numerovite23, numerovite33, numerovite43, numerovite14, numerovite24, numerovite34, numerovite44;
    @FXML
    Label giocatore1, giocatore2, giocatore3, giocatore4, giocatore11, giocatore21, giocatore31, giocatore41, giocatore12, giocatore22, giocatore32, giocatore42, giocatore13, giocatore23, giocatore33, giocatore43, giocatore14, giocatore24, giocatore34, giocatore44;
    @FXML
    Label currentPlayer, currentRound, currentPlayer1, currentRound1, currentPlayer2, currentRound2, currentPlayer3, currentRound3;

    @FXML
    Tab partitaFinaleTab;


    @FXML
    ImageView user1, user2, user3, user4, user11, user21, user31, user41, user12, user22, user32, user42, user13, user23, user33, user43, user14, user24, user34, user44;

    private int codiceTorneo, passwordTorneo, currentMatch;


    public void setInfoTorneo(int codiceTorneo, int passwordTorneo) {
        partitaFinaleTab.setDisable(true);
        giocaButton.setVisible(true);


        this.codiceTorneo = codiceTorneo;
        this.passwordTorneo = passwordTorneo;
        this.currentMatch = FileManager.getCurrentMatchTorneo(codiceTorneo);


        // Carichiamo i dati dal file
        ArrayList<Partita> p = FileManager.leggiTorneoDaFile(codiceTorneo);


        // Imposta i dettagli per ogni giocatore
        for (int i = 0; i < 4; i++) { //for gira partite // TODO aggiungere round finale
            for (int j = 0; j < 4; j++) { // for gira partita singola

                IGiocatore giocatore = p.get(i).giocatori.get(j);
                ImageView imageView = null;
                Label nomeLabel = null;
                Label viteLabel = null;

                // Assegna le ImageView e le Label in base all'indice del giocatore
                if (i == 0) {
                    switch (j) {
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
                } else if (i == 1) {
                    switch (j) {
                        case 0:
                            imageView = user11;
                            nomeLabel = giocatore11;
                            viteLabel = numerovite11;
                            break;
                        case 1:
                            imageView = user21;
                            nomeLabel = giocatore21;
                            viteLabel = numerovite21;
                            break;
                        case 2:
                            imageView = user31;
                            nomeLabel = giocatore31;
                            viteLabel = numerovite31;
                            break;
                        case 3:
                            imageView = user41;
                            nomeLabel = giocatore41;
                            viteLabel = numerovite41;
                            break;
                    }

                } else if (i == 2) {
                    switch (j) {
                        case 0:
                            imageView = user12;
                            nomeLabel = giocatore12;
                            viteLabel = numerovite12;
                            break;
                        case 1:
                            imageView = user22;
                            nomeLabel = giocatore22;
                            viteLabel = numerovite22;
                            break;
                        case 2:
                            imageView = user32;
                            nomeLabel = giocatore32;
                            viteLabel = numerovite32;
                            break;
                        case 3:
                            imageView = user42;
                            nomeLabel = giocatore42;
                            viteLabel = numerovite42;
                            break;
                    }
                } else if (i == 3) {
                    switch (j) {
                        case 0:
                            imageView = user13;
                            nomeLabel = giocatore13;
                            viteLabel = numerovite13;
                            break;
                        case 1:
                            imageView = user23;
                            nomeLabel = giocatore23;
                            viteLabel = numerovite23;
                            break;
                        case 2:
                            imageView = user33;
                            nomeLabel = giocatore33;
                            viteLabel = numerovite33;
                            break;
                        case 3:
                            imageView = user43;
                            nomeLabel = giocatore43;
                            viteLabel = numerovite43;
                            break;
                    }
                } else if (i == 4 && i == currentMatch) {
                    partitaFinaleTab.setDisable(false);

                    switch (j) {
                        case 0:
                            imageView = user14;
                            nomeLabel = giocatore14;
                            viteLabel = numerovite14;
                            break;
                        case 1:
                            imageView = user24;
                            nomeLabel = giocatore24;
                            viteLabel = numerovite24;
                            break;
                        case 2:
                            imageView = user34;
                            nomeLabel = giocatore34;
                            viteLabel = numerovite34;
                            break;
                        case 3:
                            imageView = user44;
                            nomeLabel = giocatore44;
                            viteLabel = numerovite44;
                            break;
                    }

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

                // TODO MOSTRARE SOLAMENTE SE IL GIOCO E IN CORSO, ALTRIMENTI NO
                // Imposta altre informazioni sulla partita
                currentRound.setText("La partita riprenderà dal Round: " + p.get(0).getCurrentRound());
                currentPlayer.setText("Toccherà al giocatore: " + p.get(0).getCurrentGiocatore().getNome());
                currentRound1.setText("La partita riprenderà dal Round: " + p.get(1).getCurrentRound());
                currentPlayer1.setText("Toccherà al giocatore: " + p.get(1).getCurrentGiocatore().getNome());
                currentRound2.setText("La partita riprenderà dal Round: " + p.get(2).getCurrentRound());
                currentPlayer2.setText("Toccherà al giocatore: " + p.get(2).getCurrentGiocatore().getNome());
                currentRound3.setText("La partita riprenderà dal Round: " + p.get(3).getCurrentRound());
                currentPlayer3.setText("Toccherà al giocatore: " + p.get(3).getCurrentGiocatore().getNome());
                //currentRound4.setText("La partita riprenderà dal Round: " + p.get(0).getCurrentRound());
                //currentPlayer4.setText("Toccherà al giocatore: " + p.get(0).getCurrentGiocatore().getNome());
            }
        }
    }


    public void apriTavolo(ActionEvent actionEvent) throws IOException // bottone inizia
    {

        System.out.println("Ho cliccato sul bottone");
        try {
            AudioManager.bottoneSuono();
            FXMLLoader torneoScreen = new FXMLLoader(Spacca.class.getResource("TorneoScreen.fxml"));

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Parent root = torneoScreen.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            TorneoScreenController torneoScreenController = torneoScreen.getController();
            torneoScreenController.setInfoTorneo(codiceTorneo, passwordTorneo, currentMatch);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void indietro(MouseEvent mouseEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("SelectionMenuGiocatore.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }

}
