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
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.telegram.telegrambots.meta.api.objects.passport.dataerror.PassportElementErrorSelfie;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;

public class PreTorneoController
{

    @FXML
    Button giocaButton;

    @FXML
    Label numerovite1, numerovite2, numerovite3, numerovite4, numerovite11, numerovite21, numerovite31, numerovite41, numerovite12, numerovite22, numerovite32, numerovite42, numerovite13, numerovite23, numerovite33, numerovite43, numerovite14, numerovite24, numerovite34, numerovite44;
    @FXML
    Label giocatore1, giocatore2, giocatore3, giocatore4, giocatore11, giocatore21, giocatore31, giocatore41, giocatore12, giocatore22, giocatore32, giocatore42, giocatore13, giocatore23, giocatore33, giocatore43, giocatore14, giocatore24, giocatore34, giocatore44;
    @FXML
    Label currentPlayer, currentRound, currentPlayer1, currentRound1, currentPlayer2, currentRound2, currentPlayer3, currentRound3, currentPlayer4, currentRound4, currentMatchLabel;

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
        this.currentMatchLabel.setText("MATCH: " + (this.currentMatch+1) + "/5");


        // Carichiamo i dati dal file
        ArrayList<Partita> p = FileManager.leggiTorneoDaFile(codiceTorneo);


        // Imposta i dettagli per ogni giocatore
        //for gira partite
        for (int i = 0; i < 4; i++)
        {
            Partita partitaSingola = p.get(i);

            for (int j = 0; j < 4; j++) { // for gira partita singola

                IGiocatore giocatore = p.get(i).giocatori.get(j);



                // Assegna le ImageView e le Label in base all'indice del giocatore
                if (i == 0) // partita 0
                {
                    switch (j)
                    {
                        case 0:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user1, giocatore1, numerovite1);
                            break;
                        case 1:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user2, giocatore2, numerovite2);
                            break;
                        case 2:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user3, giocatore3, numerovite3);
                            break;
                        case 3:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user4, giocatore4, numerovite4);
                            break;
                    }
                }
                else if (i == 1)
                {
                    switch (j)
                    {
                        case 0:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user11, giocatore11, numerovite11);
                            break;
                        case 1:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user21, giocatore21, numerovite21);
                            break;
                        case 2:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user31, giocatore31, numerovite31);
                            break;
                        case 3:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user41, giocatore41, numerovite41);
                            break;
                    }

                }
                else if (i == 2)
                {
                    switch (j)
                    {
                        case 0:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user12, giocatore12, numerovite12);
                            break;
                        case 1:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user22, giocatore22, numerovite22);
                            break;
                        case 2:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user32, giocatore32, numerovite32);
                            break;
                        case 3:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user42, giocatore42, numerovite42);
                            break;
                    }
                }
                else if (i == 3)
                {
                    switch (j)
                    {
                        case 0:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user13, giocatore13, numerovite13);
                            break;
                        case 1:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user23, giocatore23, numerovite23);
                            break;
                        case 2:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user33, giocatore33, numerovite33);
                            break;
                        case 3:
                            mostraUITavoloPreTorneoGiocatori(partitaSingola, giocatore, user43, giocatore43, numerovite43);
                            break;
                    }
                }

            }
        }

        mostraInfoPartiteTorneo(p);

        if (currentMatch == 4)
            mostraUITavolopreTorneoFinale();


    }

    private void mostraUITavoloPreTorneoGiocatori(Partita p, IGiocatore giocatore, ImageView imageView, Label nomeLabel, Label viteLabel)
    {

        // Imposta nome e vite
        if (nomeLabel != null && viteLabel != null)
        {
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


    private void mostraUITavolopreTorneoFinale()
    {
        partitaFinaleTab.setDisable(false);
        // abilito partita finale
        Partita partitaFinale = FileManager.getFinalePartitaTorneo(this.codiceTorneo);

        System.out.println("E presente la partita finale e devo caricare i dati!!");
        mostraUITavoloPreTorneoGiocatori(partitaFinale, partitaFinale.giocatori.get(0), user14, giocatore14, numerovite14);
        mostraUITavoloPreTorneoGiocatori(partitaFinale, partitaFinale.giocatori.get(1), user24, giocatore24, numerovite24);
        mostraUITavoloPreTorneoGiocatori(partitaFinale, partitaFinale.giocatori.get(2), user34, giocatore34, numerovite34);
        mostraUITavoloPreTorneoGiocatori(partitaFinale, partitaFinale.giocatori.get(3), user44, giocatore44, numerovite44);

        if(partitaFinale.getPartitaStatus() != GameStatus.STARTED && partitaFinale.getPartitaStatus() != GameStatus.ENDED)
        {
            currentRound4.setVisible(true);
            currentPlayer4.setVisible(true);

            currentRound4.setText("La partita riprenderà dal Round: " + partitaFinale.getCurrentRound());
            currentPlayer4.setText("Toccherà al giocatore: " + partitaFinale.getCurrentGiocatore().getNome());
        }
        else
        {
            currentRound4.setVisible(false);
            currentPlayer4.setVisible(false);
        }
    }

    private void mostraInfoPartiteTorneo(ArrayList<Partita> p)
    {
        // Imposta altre informazioni sulla partita

        if(p.get(0).getPartitaStatus() != GameStatus.STARTED && p.get(0).getPartitaStatus() != GameStatus.ENDED)
        {
            currentRound.setVisible(true);
            currentPlayer.setVisible(true);

            currentRound.setText("La partita riprenderà dal Round: " + p.get(0).getCurrentRound());
            currentPlayer.setText("Toccherà al giocatore: " + p.get(0).getCurrentGiocatore().getNome());
        }
        else
        {
            currentRound.setVisible(false);
            currentPlayer.setVisible(false);
        }


        if(p.get(1).getPartitaStatus() != GameStatus.STARTED && p.get(1).getPartitaStatus() != GameStatus.ENDED)
        {
            currentRound1.setVisible(true);
            currentPlayer1.setVisible(true);

            currentRound1.setText("La partita riprenderà dal Round: " + p.get(1).getCurrentRound());
            currentPlayer1.setText("Toccherà al giocatore: " + p.get(1).getCurrentGiocatore().getNome());
        }
        else
        {
            currentRound1.setVisible(false);
            currentPlayer1.setVisible(false);
        }

        if(p.get(2).getPartitaStatus() != GameStatus.STARTED && p.get(2).getPartitaStatus() != GameStatus.ENDED)
        {
            currentRound2.setVisible(true);
            currentPlayer2.setVisible(true);

            currentRound2.setText("La partita riprenderà dal Round: " + p.get(2).getCurrentRound());
            currentPlayer2.setText("Toccherà al giocatore: " + p.get(2).getCurrentGiocatore().getNome());
        }
        else
        {
            currentRound2.setVisible(false);
            currentPlayer2.setVisible(false);
        }

        if(p.get(3).getPartitaStatus() != GameStatus.STARTED && p.get(3).getPartitaStatus() != GameStatus.ENDED)
        {
            currentRound3.setVisible(true);
            currentPlayer3.setVisible(true);

            currentRound3.setText("La partita riprenderà dal Round: " + p.get(3).getCurrentRound());
            currentPlayer3.setText("Toccherà al giocatore: " + p.get(3).getCurrentGiocatore().getNome());
        }
        else
        {
            currentRound3.setVisible(false);
            currentPlayer3.setVisible(false);
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
