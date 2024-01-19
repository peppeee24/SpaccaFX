package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Files.FileManager;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;
import com.spaccafx.Spacca;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TorneoItemController
{
    @FXML
    private Label torneoId;

    @FXML
    private Button playButtonId, leaderboardButtonId;

    @FXML
    private Label stateId;


    private int codiceTorneo, passwordTorneo;

    public void setData(MatchData matchData)
    {
        this.torneoId.setText("ID: " + matchData.getCodice());

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

        this.passwordTorneo = matchData.getPassword();
        this.codiceTorneo = matchData.getCodice();
    }


    // TODO FARE ACCEDI TORNEO
    public void accediTorneo(javafx.event.ActionEvent actionEvent)
    {
        // una volta che clicco su un determinato pulsante play, mi deve aprire la schermata di un pre torneo, dove carica
        // delle determinate informazioni di tale partita (giocatori, tipo, round, se sta ancora andando etc..)
        // prendo le info dal json

        System.out.println("[MatchItemController] ti sto reindirizzando al pre-torneo screen");

        try
        {
            AudioManager.bottoneSuono();
            //AlertController.showWarning("Caricamento torneo in corso");
            FXMLLoader preTorneoScreen = new FXMLLoader(Spacca.class.getResource("PreTorneo.fxml"));

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Parent root = preTorneoScreen.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            PreTorneoController preTorneoController = preTorneoScreen.getController();
            preTorneoController.setInfoTorneo(codiceTorneo, passwordTorneo);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // TODO FARE LEADERBOARD TORNEO
    public void leaderboardPartita(javafx.event.ActionEvent actionEvent)
    {
        try
        {
            AudioManager.bottoneSuono();
            FXMLLoader loaderLeaderboardTorneo = new FXMLLoader(Spacca.class.getResource("LeaderboardScreenTorneo.fxml"));
            Parent root = loaderLeaderboardTorneo.load();

            LeaderboardScreenTorneoController leaderboardTorneoController = loaderLeaderboardTorneo.getController();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Bisogna prendere i dati delle partite dalla quale clicco sulla leaderboard.
            ArrayList<Partita> partiteNormaliTorneo = FileManager.leggiTorneoDaFile(this.codiceTorneo);

            for(int c=0; c<5;c++)
            {
                // prendo i dati delle mie 4 partite
                if(c!= 4)
                {
                    // prendo i miei giocatori della partita
                    ArrayList<IGiocatore> giocatoriLeaderboard = new ArrayList<>(4);
                    loadSingleMatchLeaderboard(giocatoriLeaderboard, partiteNormaliTorneo.get(c), leaderboardTorneoController.getGridPaneScoreboard(c));
                }
                else
                {
                    // prendo i dati della finale
                    Partita partitaFinale = FileManager.getFinalePartitaTorneo(this.codiceTorneo);
                    ArrayList<IGiocatore> giocatoriLeaderboard = new ArrayList<>(4);
                    loadSingleMatchLeaderboard(giocatoriLeaderboard, partitaFinale, leaderboardTorneoController.getGridPaneScoreboard(c));
                    // TODO MOSTRARLO SOLO SE LA PARTITA FINALE E PRESENTE E NON CI SONO GIOCATORI NULL, ALTRIMENTI ERRORE
                }



            }

            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void loadSingleMatchLeaderboard(ArrayList<IGiocatore> giocatoriLeaderboard, Partita partita, GridPane singleMatchPane) throws IOException {


        for (IGiocatore giocatore : partita.giocatori)
        {
            // Crea una copia del giocatore e aggiungila alla nuova lista
            IGiocatore copiaGiocatore;

            if(giocatore instanceof Bot)
            {
                if(giocatore instanceof EasyBot)
                    copiaGiocatore = new EasyBot(giocatore.getNome(), giocatore.getPlayerRounds(), giocatore.getVita(), giocatore.getVitaExtra());
                else
                    copiaGiocatore = new AdvancedBot(giocatore.getNome(), giocatore.getPlayerRounds(), giocatore.getVita(), giocatore.getVitaExtra());
            }
            else
            {
                copiaGiocatore = new Giocatore(giocatore.getNome(), giocatore.getPlayerRounds(), giocatore.getVita(), giocatore.getVitaExtra());
            }

            giocatoriLeaderboard.add(copiaGiocatore);
        }

        // Ordina l'ArrayList in base alla vita e ai rounds dei giocatori utilizzando un comparatore
        Collections.sort(giocatoriLeaderboard, new Comparator<IGiocatore>() {
            @Override
            public int compare(IGiocatore giocatore1, IGiocatore giocatore2) {
                // Confronta i giocatori in base alla vita extra
                boolean vitaExtra1 = giocatore1.hasVitaExtra();
                boolean vitaExtra2 = giocatore2.hasVitaExtra();

                // Primo criterio: giocatori con vita extra prima
                if (vitaExtra1 && !vitaExtra2) {
                    return -1;
                } else if (!vitaExtra1 && vitaExtra2) {
                    return 1;
                }

                // Secondo criterio: confronta i giocatori in base alla vita
                int compareVita = Integer.compare(giocatore2.getVita(), giocatore1.getVita());

                // Terzo criterio: se i giocatori hanno la stessa vita, confronta in base ai round
                if (compareVita == 0) {
                    return Integer.compare(giocatore2.getPlayerRounds(), giocatore1.getPlayerRounds());
                } else {
                    return compareVita; // Ordina prima per vita
                }
            }
        });

        // adesso devo associare ad ogni partita il relativo pannello

        int riga = 1;
        int posizione = 1;
        // PARTE NUOVA
        for (IGiocatore giocatore : giocatoriLeaderboard)
        {
            FXMLLoader fxmlLoader = new FXMLLoader(Spacca.class.getResource("SinglePlayerScoreboard.fxml"));

            AnchorPane anchorPane = fxmlLoader.load();

            SinglePlayerScoreboardController singlePlayerScoreboardController = fxmlLoader.getController();
            System.out.println("Viota extra: " + giocatore.getVitaExtra());
            singlePlayerScoreboardController.setData(posizione, giocatore.getNome(), giocatore.getPlayerRounds(), giocatore.getVita(), giocatore.getVitaExtra()); // mettere vite extra

            // Ottenere l'AnchorPane dal tuo controller
            //AnchorPane singleAnchorPane = singlePlayerScoreboardController.getAnchorPane();

            // Imposta il colore di sfondo dell'AnchorPane in base al player se vivo o morto
            if(giocatore.getVita() == 0)
            {
                BackgroundFill backgroundFill = new BackgroundFill(Color.ORANGERED, null, null); // Cambia il colore a tuo piacimento
                Background background = new Background(backgroundFill);
                anchorPane.setBackground(background);
            }
            else
            {
                BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTGREEN, null, null); // Cambia il colore a tuo piacimento
                Background background = new Background(backgroundFill);
                anchorPane.setBackground(background);
            }


            singleMatchPane.add(anchorPane, 0, riga);

            GridPane.setMargin(anchorPane, new Insets(10));

            riga++;
            posizione++;
        }
    }
}
