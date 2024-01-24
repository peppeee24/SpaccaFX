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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MatchItemController
{
    @FXML
    private Label partitaId;

    @FXML
    private Button playButtonId;
    @FXML
    ImageView deleteMatchButton;

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

        System.out.println("[MatchItemController] ti sto reindirizzando al pregame screen");

        try
        {
            AudioManager.bottoneSuono();
            FXMLLoader preGameScreen = new FXMLLoader(Spacca.class.getResource("PreGameMatch.fxml"));

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Parent root = preGameScreen.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            PreGameController preGameController = preGameScreen.getController();
            preGameController.setInfoPartita2(codicePartita, passwordPartita);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void leaderboardPartita(javafx.event.ActionEvent actionEvent)
    {
        try
        {
            AudioManager.bottoneSuono();
            FXMLLoader loaderLeaderboard = new FXMLLoader(Spacca.class.getResource("LeaderboardScreen.fxml"));
            Parent root = loaderLeaderboard.load();

            LeaderboardScreenController leaderboardController = loaderLeaderboard.getController();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Bisogna prendere i dati della partita dalla quale clicco sulla leaderboard.
            Partita partita = FileManager.leggiPartitaDaFile(codicePartita);

            ArrayList<IGiocatore> giocatoriLeaderboard = new ArrayList<>(partita.giocatori.size());
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


                leaderboardController.getGridPaneScoreboard().add(anchorPane, 0, riga);

                GridPane.setMargin(anchorPane, new Insets(10));

                riga++;
                posizione++;
            }

            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


        // una volta che clicco su un determinato pulsante play, mi deve aprire la schermata di un pre partita, dove carica
        // delle determinate informazioni di tale partita (giocatori, tipo, round, se sta ancora andando etc..)
        // prendo le info dal json

        public void deleteMatch(MouseEvent mouseEvent) throws IOException {
        AudioManager.bottoneSuono();


            FXMLLoader loaderDeleteGame = new FXMLLoader(Spacca.class.getResource("LoginDelete.fxml"));
            Parent root = loaderDeleteGame.load();

            LoginDeleteController loginDeleteController = loaderDeleteGame.getController();
            loginDeleteController.setEliminazione(this.codicePartita, 0);

            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        System.out.println("[MatchItemController] Devo cancellare una determinata partita");

    }


}
