package com.spaccafx.Controllers;

import com.spaccafx.Cards.Carta;
import com.spaccafx.Cards.CartaImprevisto;
import com.spaccafx.Enums.GameStatus;
import com.spaccafx.Enums.GameType;
import com.spaccafx.Enums.RuoloGiocatore;
import com.spaccafx.Files.AudioManager;
import com.spaccafx.Files.FileManager;
import com.spaccafx.Files.ResourceLoader;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;
import com.spaccafx.Spacca;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.scene.image.ImageView;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class TavoloController {
    // region #VARIABLES
    @FXML
    Label nomePlayer1, nomePlayer2, nomePlayer3, nomePlayer4; // nome giocatori

    @FXML
    Label partitaIdLabel, roundIdLabel; // general UI

    @FXML
    Label popUpTitleLabel, popUpTextLabel; // banner pop up panel

    @FXML
    Pane popUpPane;

    @FXML
    ImageView cartaPlayer1, cartaPlayer2, cartaPlayer3, cartaPlayer4, cartaCentrale; // carte giocatori

    @FXML
    ImageView life1Pl2, life2Pl2, life3Pl2, life1Pl3, life2Pl3, life3Pl3, life1Pl4, life2Pl4, life3Pl4, life1Pl1, life2Pl1, life3Pl1, lifeGoldPl2, lifeGoldPl3, lifeGoldPl4, lifeGoldPl1;

    @FXML
    ImageView mazzierePlayer1Icon, mazzierePlayer2Icon, mazzierePlayer3Icon, mazzierePlayer4Icon; // icone del mazziere

    @FXML
    ImageView dicePl1, dicePl2, dicePl3, dicePl4, exitGame, leaderboard; // dadi

    @FXML
    Button bottoneStart, bottoneEffetto, bottoneScambia, bottonePassa, bottoneRiprendiBot; // bottoni partita


    //private PartitaClassicaController2 PC;

    private Partita partita;
    private int currentMatch, codiceTorneo;
    private Stage leaderboardStage; // Variabile di stato per tenere traccia della finestra della classifica

    //endregion

    public void initialize() {
        inizializzazioneTavolo();
    }// tolgo tutto

    // costruttore
    public TavoloController() {
        ShareData.getInstance().setTavoloController(this);
    }

    // prima METODO che viene eseguita dopo che fai l accesso
    public void inizializzaClassePartita(int codicePartita)
    {
        try
        {
            // gli devo passare il codice che mando quando clicco sul bottone
            System.out.println("Codice della partita attuale: " + codicePartita);
            this.partitaIdLabel.setText("ID_PARTITA: " + codicePartita);

            this.partita = FileManager.leggiPartitaDaFile(codicePartita); // prendiamo la partita (codice, passw, giocatori, stato)
            this.partita.impostaTavoloController();
            inizializzaNomiPlayer(); // aggiorno UI e inizializzo nomi player VIVI E MORTI
            this.updateVitaUI();

            System.out.println("Codice della partita: " + partita.getCodicePartita());

            preInizializzazioneTavolo(this.partita.getPartitaStatus()); // inizializzo il tavolo in base alle mie esigenze
        }
        catch (Exception e)
        {
            Alert test = new Alert(Alert.AlertType.ERROR);
            test.setContentText("Errore insolito: " + e.getMessage());
            test.show();
            System.exit(0);
        }

    }

    public void inizializzaClasseTorneo(int codiceTorneo, int currentMatch)
    {
        try
        {
            // gli devo passare il codice che mando quando clicco sul bottone
            System.out.println("Codice del torneo attuale: " + codiceTorneo);
            this.setCurrentMatch(currentMatch);
            this.setCodiceTorneo(codiceTorneo);

            this.partitaIdLabel.setText("ID_TORNEO: " + codiceTorneo);

            // se diverso da 4, carico una partita normale
            if (this.currentMatch != 4)
                this.partita = FileManager.getCurrentPartitaTorneo(this.codiceTorneo, this.currentMatch); // prendiamo la partita (codice, passw, giocatori, stato)
            else
                this.partita = FileManager.getFinalePartitaTorneo(this.codiceTorneo); // prendo i dati della partita finale

            this.partita.impostaTavoloController();
            inizializzaNomiPlayer(); // aggiorno UI e inizializzo nomi player VIVI E MORTI
            this.updateVitaUI();

            preInizializzazioneTavolo(this.partita.getPartitaStatus()); // inizializzo il tavolo in base alle mie esigenze
        }
        catch (Exception e)
        {
            Alert test = new Alert(Alert.AlertType.ERROR);
            test.setContentText("Errore insolito: " + e.getMessage());
            test.show();
            System.exit(0);
        }
    }

    private void setCodiceTorneo(int codiceTorneo) {
        this.codiceTorneo = codiceTorneo;
    }

    public int getCodiceTorneo() {
        return this.codiceTorneo;
    }

    private void setCurrentMatch(int currentMatch) {
        this.currentMatch = currentMatch;
    }

    public int getCurrentMatch() {
        return this.currentMatch;
    }


    public void riprendiBot()
    {
        try
        {
            if (partita.getCurrentGiocatore() instanceof Bot) {
                gestisciPulsanteRiprendiBot(false);

                if (partita.getCartaGiaScambiata())
                    partita.passaTurnoUI();
                else
                    ((Bot) partita.getCurrentGiocatore()).SceltaBotUI(this.partita, this);

            }
        }
        catch (Exception e)
        {
            Alert test = new Alert(Alert.AlertType.ERROR);
            test.setContentText("Errore insolito: " + e.getMessage());
            test.show();
            System.exit(0);
        }
        AudioManager.bottoneSuono();
    }


    public void preInizializzazioneTavolo(GameStatus gameStatus) {
        switch (gameStatus) {
            case STARTED:
                System.out.println("Il gioco sta per iniziare ora!");
                bottoneStart.setVisible(true);
                break;

            case STOPPED:

                bottoneStart.setVisible(false);


                boolean isOkPressed = AlertController.showConfirm("Vuoi riprendere la partita?");

                if (isOkPressed) {

                    System.out.println("Stai per riprendere il gioco");
                    System.out.println("Current Giocatore: " + partita.getCurrentGiocatore().getNome()
                            + " in posizione: " + partita.getCurrentGiocatorePos());

                    partita.StampaInfoGiocatori();

                    riprendiGioco(this.partita);

                    // lo riporto fuori e non carico nulla

                    break;
                } else {
                    try {
                        Stage currentStage = (Stage) popUpPane.getScene().getWindow();
                        FXMLLoader playerScreen = new FXMLLoader(Spacca.class.getResource("PartitaSelector.fxml"));
                        Stage stage = new Stage();
                        Parent root = playerScreen.load();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();

                        currentStage.close();


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }


            case PLAYING:
            case ENDED:
                break;

            default:
        }
    }

    public void openLeaderboard()
    {
        if (leaderboardStage == null) {
            AudioManager.leaderboardSuono();
            try {
                // Carica il file FXML per la finestra della classifica
                FXMLLoader loaderLeaderboard = new FXMLLoader(Spacca.class.getResource("leaderboard.fxml"));
                Parent root = loaderLeaderboard.load();

                LeaderboardController leaderboardController = loaderLeaderboard.getController();

                // Crea una nuova scena
                Scene scene = new Scene(root);

                // Crea una nuova finestra per la classifica
                leaderboardStage = new Stage();
                leaderboardStage.setTitle("Classifica - SpaccaFX");
                leaderboardStage.setScene(scene);
                leaderboardStage.setResizable(false);

                ArrayList<IGiocatore> giocatoriLeaderboard = new ArrayList<>(partita.giocatori.size());
                for (IGiocatore giocatore : partita.giocatori) {
                    // Crea una copia del giocatore e aggiungila alla nuova lista
                    IGiocatore copiaGiocatore;

                    if (giocatore instanceof Bot) {
                        if (giocatore instanceof EasyBot)
                            copiaGiocatore = new EasyBot(giocatore.getNome(), giocatore.getPlayerRounds(), giocatore.getVita(), giocatore.getVitaExtra());
                        else
                            copiaGiocatore = new AdvancedBot(giocatore.getNome(), giocatore.getPlayerRounds(), giocatore.getVita(), giocatore.getVitaExtra());
                    } else {
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
                for (IGiocatore giocatore : giocatoriLeaderboard) {
                    FXMLLoader fxmlLoader = new FXMLLoader(Spacca.class.getResource("SinglePlayerScoreboard.fxml"));

                    AnchorPane anchorPane = fxmlLoader.load();

                    SinglePlayerScoreboardController singlePlayerScoreboardController = fxmlLoader.getController();
                    System.out.println("Vita extra: " + giocatore.getVitaExtra());
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

                    leaderboardController.getGridPane().add(anchorPane, 0, riga);

                    GridPane.setMargin(anchorPane, new Insets(10));

                    riga++;
                    posizione++;
                }


                // Gestisce l'evento di chiusura della finestra della classifica
                leaderboardStage.setOnCloseRequest(event -> {
                    leaderboardStage = null; // Ripristina la variabile quando la finestra viene chiusa
                });

                // Mostra la finestra della classifica
                leaderboardStage.show();
            } catch (Exception e) {
                e.printStackTrace();
                // Gestire eventuali eccezioni nel caricamento del file FXML
            }
        } else {
            // Se la finestra è già stata creata, riportala in primo piano
            leaderboardStage.toFront();
        }
    }

    public void closeLeaderboard() {
        if (leaderboardStage != null) {
            leaderboardStage.close();
            leaderboardStage = null; // Imposta a null dopo la chiusura
        }
    }

    private void inizializzazioneTavolo() {
        nascondiCorone();
        nascondiBannerAttesa();
        gestisciPulsanti(false, false, false);
        gestisciPulsanteRiprendiBot(false);
        nascondiDadi();
    }

    private void inizializzaNomiPlayer() {
        // imposto nome giocatori da prendere dal file
        nomePlayer1.setText(partita.giocatori.get(0).getNome()); // prendo il giocatore
        nomePlayer2.setText(partita.giocatori.get(1).getNome());
        nomePlayer3.setText(partita.giocatori.get(2).getNome());
        nomePlayer4.setText(partita.giocatori.get(3).getNome());

        aggiornaInfoUI();
    }


    // region #ACTION EVENT METHODS
    public void start(ActionEvent actionEvent) {
        try
        {
            AudioManager.bottoneSuono();
            // attendi lancio i dadi..
            gestisciPulsanti(false, true, true);
            partita.preStartGame();
            bottoneStart.setVisible(false);
        }
        catch (Exception e)
        {
            Alert test = new Alert(Alert.AlertType.ERROR);
            test.setContentText("Errore di caricamento: " + e.getMessage());
            test.show();
        }

    }

    public void scambiaCarta(ActionEvent actionEvent)
    {
        try
        {
            AudioManager.bottoneSuono();
            partita.ScambiaCartaUI();
            this.setExitGame(false);
        }
        catch (Exception e)
        {
            Alert test = new Alert(Alert.AlertType.ERROR);
            test.setContentText("Errore di caricamento: " + e.getMessage());
            test.show();
        }

    }

    public void passaTurno(ActionEvent actionEvent)
    {
        try
        {
            AudioManager.bottoneSuono();
            partita.passaTurnoUI();
            this.setExitGame(false);
        }
        catch (Exception e)
        {
            Alert test = new Alert(Alert.AlertType.ERROR);
            test.setContentText("Errore di caricamento: " + e.getMessage());
            test.show();
        }
    } // passo nella partita il turno del player


    public void scambiaConMazzo(ActionEvent actionEvent)
    {
        try
        {
            AudioManager.bottoneSuono();
            partita.getCurrentGiocatore().setCarta(partita.mazzo.PescaCartaSenzaEffetto());
            updateCarteUI();
            pulsanteScambiaMazzo(false);
            this.setExitGame(true);
        }
        catch (Exception e)
        {
            Alert test = new Alert(Alert.AlertType.ERROR);
            test.setContentText("Errore di caricamento: " + e.getMessage());
            test.show();
        }

    }

    // per uscire dalla partita
    public void exitGame(MouseEvent mouseEvent) throws IOException
    {
        try
        {
            partita.SavePartita(mouseEvent);
        }
        catch (Exception e)
        {
            Alert test = new Alert(Alert.AlertType.ERROR);
            test.setContentText("Errore di caricamento: " + e.getMessage());
            test.show();
        }
    }
    //endregion

    // region #METHODS

    public void riprendiGioco(Partita partita)
    {
        try
        {
            this.updateVitaUI(); // aggiorna tutte le vite dei player
            this.updateCarteUI(); // aggiorna tutte le carte dei giocatori e scopre quella del giocatore a cui tocca
            this.updateCarteMortiUI(); // aggiorno la grafica dei player morti
            this.updateCartaCentraleMazzoUI(); // imposta la carta centrale
            this.impostaCoroneMazziereUI(); // imposta chi e il mazziere

            partita.setPartitaStatus(GameStatus.PLAYING); // reimposto il gioco allo stato playing
            partita.riprendiPartita(partita.getCurrentGiocatorePos());
        }
        catch (Exception e)
        {
            Alert test = new Alert(Alert.AlertType.ERROR);
            test.setContentText("Errore di caricamento: " + e.getMessage());
            test.show();
        }
    }

    public void caricaMenuUI(MouseEvent mouseEvent) throws IOException
    {
        closeLeaderboard();

        if(this.partita.getGameType() == GameType.PARTITA)
            ritornaMenuPartiteSingole();
        else
            ritornaMenuTornei();
    }

    public void impostaCoroneMazziereUI() {
        nascondiCorone();

        String mazziere = partita.getMazziereNome();

        if (nomePlayer1.getText().equalsIgnoreCase(mazziere))
            mazzierePlayer1Icon.setVisible(true);
        else if (nomePlayer2.getText().equalsIgnoreCase(mazziere))
            mazzierePlayer2Icon.setVisible(true);
        else if (nomePlayer3.getText().equalsIgnoreCase(mazziere))
            mazzierePlayer3Icon.setVisible(true);
        else
            mazzierePlayer4Icon.setVisible(true);
    }

    public void updateCarteUI() // new Method
    {
        updateCarteViviUI();
    }

    private void updateCarteMortiUI() {
        Image back = ResourceLoader.loadImage("/Assets/Cards/morte.png"); // carta back
        String playerName;

        for (IGiocatore giocatore : partita.giocatori) {
            if (giocatore.getRuolo() == RuoloGiocatore.MORTO) // aggiorno il back dei player morti
            {
                playerName = giocatore.getNome();
                System.out.println("[Morte-UI] Aggiorno UI giocatore morto: " + playerName);

                if (playerName.equalsIgnoreCase(nomePlayer1.getText())) // se il nome equivale al primo
                {
                    life1Pl1.setVisible(false);
                    life2Pl1.setVisible(false);
                    life3Pl1.setVisible(false);
                    lifeGoldPl1.setVisible(false);
                    dicePl1.setVisible(false);
                    // humanPlayerSpace.setVisible(false);
                    String nomeMorto = nomePlayer1.getText();
                    nomePlayer1.setText(nomeMorto + " è morto");
                    cartaPlayer1.setImage(back);
                } else if (playerName.equalsIgnoreCase(nomePlayer2.getText())) // se il nome equivale al secondo
                {
                    life1Pl2.setVisible(false);
                    life2Pl2.setVisible(false);
                    life3Pl2.setVisible(false);
                    lifeGoldPl2.setVisible(false);
                    dicePl2.setVisible(false);
                    // humanPlayerSpace.setVisible(false);
                    String nomeMorto = nomePlayer2.getText();
                    nomePlayer2.setText(nomeMorto + " è morto");
                    cartaPlayer2.setImage(back);
                } else if (playerName.equalsIgnoreCase(nomePlayer3.getText())) // se il nome equivale al terzo
                {
                    life1Pl3.setVisible(false);
                    life2Pl3.setVisible(false);
                    life3Pl3.setVisible(false);
                    lifeGoldPl3.setVisible(false);
                    dicePl3.setVisible(false);
                    // humanPlayerSpace.setVisible(false);
                    String nomeMorto = nomePlayer3.getText();
                    nomePlayer3.setText(nomeMorto + " è morto");
                    cartaPlayer3.setImage(back);
                } else // se il nome equivale al quarto
                {
                    life1Pl4.setVisible(false);
                    life2Pl4.setVisible(false);
                    life3Pl4.setVisible(false);
                    lifeGoldPl4.setVisible(false);
                    dicePl4.setVisible(false);
                    // humanPlayerSpace.setVisible(false);
                    String nomeMorto = nomePlayer4.getText();
                    nomePlayer4.setText(nomeMorto + " è morto");
                    cartaPlayer4.setImage(back);
                }
            }

        }
    }

    private void updateCarteViviUI() {
        String currentPlayerName = partita.giocatori.get(partita.getCurrentGiocatorePos()).getNome(); // giocatore a cui tocca
        System.out.println("[UpdateUI] - tocca la giocatore: " + currentPlayerName);
        Image back = ResourceLoader.loadImage("/Assets/Cards/back.png"); // carta back
        String playerName;

        for (IGiocatore giocatore : partita.giocatori) {
            if (giocatore.getRuolo() != RuoloGiocatore.MORTO) // aggiorno soltanto le carte dei giocatori vivi
            {
                playerName = giocatore.getNome();

                if (playerName.equalsIgnoreCase(nomePlayer1.getText())) // se il nome equivale al primo
                {
                    if (playerName.equalsIgnoreCase(currentPlayerName)) // se tocca a lui
                        cartaPlayer1.setImage(giocatore.getCarta().getImmagineCarta());
                    else // se non tocca a lui gli copro la carta
                        cartaPlayer1.setImage(back);
                } else if (playerName.equalsIgnoreCase(nomePlayer2.getText())) // se il nome equivale al secondo
                {
                    if (playerName.equalsIgnoreCase(currentPlayerName)) // se tocca a lui
                        cartaPlayer2.setImage(giocatore.getCarta().getImmagineCarta());
                    else // se non tocca a lui gli copro la carta
                        cartaPlayer2.setImage(back);
                } else if (playerName.equalsIgnoreCase(nomePlayer3.getText())) // se il nome equivale al terzo
                {
                    if (playerName.equalsIgnoreCase(currentPlayerName)) // se tocca a lui
                        cartaPlayer3.setImage(giocatore.getCarta().getImmagineCarta());
                    else // se non tocca a lui gli copro la carta
                        cartaPlayer3.setImage(back);
                } else if (playerName.equalsIgnoreCase(nomePlayer4.getText())) // se il nome equivale al quarto
                {
                    if (playerName.equalsIgnoreCase(currentPlayerName)) // se tocca a lui
                        cartaPlayer4.setImage(giocatore.getCarta().getImmagineCarta());
                    else // se non tocca a lui gli copro la carta
                        cartaPlayer4.setImage(back);
                }
            }

        }
    }

    public void aggiornaInfoUI() {
        roundIdLabel.setText("ROUND " + partita.getCurrentRound());
    }

    public void mostraTutteCarteUI() {
        AudioManager.giraCarteSuono();
        String playerName;
        for (IGiocatore giocatore : partita.giocatori) {
            if (giocatore.getRuolo() != RuoloGiocatore.MORTO) // mostro tutte le carte solo dei giocatori vivi
            {
                playerName = giocatore.getNome();

                if (playerName.equalsIgnoreCase(nomePlayer1.getText())) // se il nome equivale al primo
                    cartaPlayer1.setImage(giocatore.getCarta().getImmagineCarta());
                else if (playerName.equalsIgnoreCase(nomePlayer2.getText())) // se il nome equivale al secondo
                    cartaPlayer2.setImage(giocatore.getCarta().getImmagineCarta());
                else if (playerName.equalsIgnoreCase(nomePlayer3.getText())) // se il nome equivale al terzo
                    cartaPlayer3.setImage(giocatore.getCarta().getImmagineCarta());
                else // se il nome equivale al quarto
                    cartaPlayer4.setImage(giocatore.getCarta().getImmagineCarta());
            }
        }
    }


    //endregion


    public void impostaDadiUI() {
        AudioManager.dadoSuono();
        int valoreDado = 0;

        for (IGiocatore giocatore : partita.giocatori) {
            if (giocatore.getNome().equalsIgnoreCase(nomePlayer1.getText())) {
                valoreDado = giocatore.getValoreDado();
                Image myImage = ResourceLoader.loadImage("/Assets/Game/Environment/dice/dice" + valoreDado + ".png");
                dicePl1.setVisible(true);
                dicePl1.setImage(myImage);
            } else if (giocatore.getNome().equalsIgnoreCase(nomePlayer2.getText())) {
                valoreDado = giocatore.getValoreDado();
                Image myImage = ResourceLoader.loadImage("/Assets/Game/Environment/dice/dice" + valoreDado + ".png");
                dicePl2.setVisible(true);
                dicePl2.setImage(myImage);
            } else if (giocatore.getNome().equalsIgnoreCase(nomePlayer3.getText())) {
                valoreDado = giocatore.getValoreDado();
                Image myImage = ResourceLoader.loadImage("/Assets/Game/Environment/dice/dice" + valoreDado + ".png");
                dicePl3.setVisible(true);
                dicePl3.setImage(myImage);
            } else if (giocatore.getNome().equalsIgnoreCase(nomePlayer4.getText())) {
                valoreDado = giocatore.getValoreDado();
                Image myImage = ResourceLoader.loadImage("/Assets/Game/Environment/dice/dice" + valoreDado + ".png");
                dicePl4.setVisible(true);
                dicePl4.setImage(myImage);
            }
        }
    }


    // ../../Assets/Game/Environment/dice/dice1.png
    public void getCartaSpeciale() {
        popUpTitleLabel.setVisible(true);
        popUpTextLabel.setVisible(true);
    }


    public synchronized void mostraBannerAttesa(String titolo, String effetto) {
        popUpPane.setVisible(true);
        popUpPane.setDisable(false);

        popUpTitleLabel.setVisible(true);
        popUpTitleLabel.setText(titolo);
        popUpTextLabel.setVisible(true);
        popUpTextLabel.setText(effetto);
    }


    public void updateCartaCentraleMazzoUI() {
        Image back = ResourceLoader.loadImage("/Assets/Cards/back.png");
        cartaCentrale.setImage(back);
    }

    public void mostraMazzoCentrale(Carta c) {
        cartaCentrale.setImage(c.getImmagineCarta());
    }

    public void updateVitaUI() {
        for (int i = 0; i < partita.giocatori.size(); i++) {
            String nome = partita.giocatori.get(i).getNome();
            int vita = partita.giocatori.get(i).getVita();
            boolean vitaExtra = partita.giocatori.get(i).hasVitaExtra();

            if (nome.equalsIgnoreCase(nomePlayer1.getText())) {
                lifeGoldPl1.setVisible(false);

                if (vita == 3) {
                    life1Pl1.setVisible(true);
                    life2Pl1.setVisible(true);
                    life3Pl1.setVisible(true);

                } else if (vita == 2) {
                    life1Pl1.setVisible(true);
                    life2Pl1.setVisible(true);
                    life3Pl1.setVisible(false);
                } else if (vita == 1) {
                    life1Pl1.setVisible(true);
                    life2Pl1.setVisible(false);
                    life3Pl1.setVisible(false);
                }

                if (vitaExtra) // fa vedere vita extra
                {
                    lifeGoldPl1.setVisible(true);
                }
            }

            if (nome.equalsIgnoreCase(nomePlayer2.getText())) {
                lifeGoldPl2.setVisible(false);

                if (vita == 3) {
                    life1Pl2.setVisible(true);
                    life2Pl2.setVisible(true);
                    life3Pl2.setVisible(true);
                } else if (vita == 2) {
                    life1Pl2.setVisible(true);
                    life2Pl2.setVisible(true);
                    life3Pl2.setVisible(false);
                } else if (vita == 1) {
                    life1Pl2.setVisible(true);
                    life2Pl2.setVisible(false);
                    life3Pl2.setVisible(false);
                }

                if (vitaExtra) // fa vedere vita extra
                {
                    lifeGoldPl2.setVisible(true);
                }
            }

            if (nome.equalsIgnoreCase(nomePlayer3.getText())) {
                lifeGoldPl3.setVisible(false);

                if (vita == 3) {
                    life1Pl3.setVisible(true);
                    life2Pl3.setVisible(true);
                    life3Pl3.setVisible(true);
                } else if (vita == 2) {
                    life1Pl3.setVisible(true);
                    life2Pl3.setVisible(true);
                    life3Pl3.setVisible(false);
                } else if (vita == 1) {
                    life1Pl3.setVisible(true);
                    life2Pl3.setVisible(false);
                    life3Pl3.setVisible(false);
                }

                if (vitaExtra) // fa vedere vita extra
                {
                    lifeGoldPl3.setVisible(true);
                }

            }

            if (nome.equalsIgnoreCase(nomePlayer4.getText())) {
                lifeGoldPl4.setVisible(false);

                if (vita == 3) {
                    life1Pl4.setVisible(true);
                    life2Pl4.setVisible(true);
                    life3Pl4.setVisible(true);
                } else if (vita == 2) {
                    life1Pl4.setVisible(true);
                    life2Pl4.setVisible(true);
                    life3Pl4.setVisible(false);
                } else if (vita == 1) {
                    life1Pl4.setVisible(true);
                    life2Pl4.setVisible(false);
                    life3Pl4.setVisible(false);
                }

                if (vitaExtra) // fa vedere vita extra
                {
                    lifeGoldPl4.setVisible(true);
                }
            }
        }
    }

    public void HidePlayerUI(String player) {

        AudioManager.perdenteSuono();
        System.out.println("[UI] elimino: " + player);
        System.out.flush();
        if (nomePlayer1.getText().equalsIgnoreCase(player)) {
            System.out.println("Giocatore 0 morto!");
            life1Pl1.setVisible(false);
            life2Pl1.setVisible(false);
            life3Pl1.setVisible(false);
            lifeGoldPl1.setVisible(false);
            dicePl1.setVisible(false);
            // humanPlayerSpace.setVisible(false);
            String nomeMorto = nomePlayer1.getText();
            nomePlayer1.setText(nomeMorto + " è morto");
            Image back = ResourceLoader.loadImage("/Assets/Cards/morte.png");
            cartaPlayer1.setImage(back);
        }

        if (nomePlayer2.getText().equalsIgnoreCase(player)) {
            System.out.println("Giocatore 1 morto!");
            life1Pl2.setVisible(false);
            life2Pl2.setVisible(false);
            life3Pl3.setVisible(false);
            lifeGoldPl2.setVisible(false);
            dicePl2.setVisible(false);
            Image back = ResourceLoader.loadImage("/Assets/Cards/morte.png");
            cartaPlayer2.setImage(back);
            String nomeMorto = nomePlayer2.getText();
            nomePlayer2.setText(nomeMorto + " è morto");
        }

        if (nomePlayer3.getText().equalsIgnoreCase(player)) {
            System.out.println("Giocatore 2 morto!");
            life1Pl3.setVisible(false);
            life2Pl3.setVisible(false);
            life3Pl3.setVisible(false);
            lifeGoldPl3.setVisible(false);
            dicePl3.setVisible(false);
            Image back = ResourceLoader.loadImage("/Assets/Cards/morte.png");
            cartaPlayer3.setImage(back);
            String nomeMorto = nomePlayer3.getText();
            nomePlayer3.setText(nomeMorto + " è morto");
        }

        if (nomePlayer4.getText().equalsIgnoreCase(player)) {
            System.out.println("Giocatore 3 morto!");
            life1Pl4.setVisible(false);
            life2Pl4.setVisible(false);
            life3Pl4.setVisible(false);
            lifeGoldPl4.setVisible(false);
            dicePl4.setVisible(false);
            Image back = ResourceLoader.loadImage("/Assets/Cards/morte.png");
            cartaPlayer4.setImage(back);
            String nomeMorto = nomePlayer4.getText();
            nomePlayer4.setText(nomeMorto + " è morto");
        }
    }

    public void EndGameUI() {
        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() ->
                {
                    this.closeLeaderboard();
                    AudioManager.vittoriaSuono();
                    gestisciPulsanti(false, false, false);

                    popUpPane.setVisible(true);
                    popUpPane.setDisable(false);

                    popUpTitleLabel.setVisible(true);
                    popUpTitleLabel.setText("VITTORIA");
                    popUpTitleLabel.setTextFill(Color.YELLOW);

                    popUpTextLabel.setVisible(true);
                    popUpTextLabel.setText("Congratulazioni al vincitore: " + partita.getVincitore().getNome().toUpperCase());
                    nascondiCorone();
                    this.openLeaderboard();
                });

                Thread.sleep(7000);

                Platform.runLater(() ->
                {
                    // chiudo la leaderboard
                    this.closeLeaderboard();

                    popUpPane.setVisible(true);
                    popUpPane.setDisable(false);

                    popUpTitleLabel.setVisible(true);
                    popUpTitleLabel.setText("RITORNO LOBBY");
                    popUpTitleLabel.setTextFill(Color.YELLOW);

                    popUpTextLabel.setVisible(true);
                    popUpTextLabel.setText("Stai per ritornare al menu principale...");
                });

                Thread.sleep(4000);

                Platform.runLater(() ->
                {

                    if (this.partita.getGameType() == GameType.PARTITA) {
                        // ritorna semplicemente alla lobby delle partite
                        // devo salvare tutti i miei dati della partita finita

                        FileManager.sovrascriviSalvataggiPartita(this.partita);

                        // mando messaggio vincitore partita
                        this.partita.getTelegramBot().messaggioVincitorePartita(this.partita.getCodicePartita(), this.partita.getVincitore().getNome());
                        ritornaMenuPartiteSingole();

                    } else {
                        // siamo in un torneo
                        // aumento il currentMatch e salvo su file e ritorno al match dei tornei

                        if (this.currentMatch != 4) // non puo superarlo perche le partite nel torneo sono al MASSIMO 5 (da 0 a 4)
                        {
                            // Ogni volta che finisce una partita, prendo il vincitore e lo metto gia nella partita finale!
                            FileManager.sovrascriviSalvataggiPartitaTorneo(this.partita, this.codiceTorneo, this.currentMatch); // salvo i dati della mia partita finita
                            this.partita.getTelegramBot().messaggioVincitorePartitaTorneo(this.codiceTorneo, this.currentMatch+1, this.partita.getVincitore().getNome());
                            this.setCurrentMatch(this.currentMatch + 1);
                            FileManager.aumentaCurrentMatchTorneo(this.codiceTorneo, this.currentMatch);
                            FileManager.popolaPartitaFinaleTorneo(this.codiceTorneo, this.partita);
                        } else {
                            this.partita.getTelegramBot().messaggioVincitoreTorneo(this.codiceTorneo, this.partita.getVincitore().getNome());
                            FileManager.sovrascriviSalvataggiPartitaFinaleTorneo(this.partita, this.codiceTorneo); // salvo i dati della mia partita finale finita
                            FileManager.sovrascriviStatoTorneo(this.codiceTorneo, GameStatus.ENDED);
                        }

                        ritornaMenuTornei();
                    }
                });


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();

    }

    private void ritornaMenuTornei() {
        try {
            Stage currentStage = (Stage) popUpPane.getScene().getWindow();
            FXMLLoader playerScreen = new FXMLLoader(Spacca.class.getResource("TorneoSelector.fxml"));
            Stage stage = new Stage();
            Parent root = playerScreen.load();
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.setTitle("SpaccaFX - Game");
            stage.getIcons().add(ResourceLoader.gameIcons());
            stage.setOnCloseRequest(event -> {
                // Aggiungi qui la logica di chiusura dell'applicazione
                System.out.println("L'utente sta chiudendo l'applicazione");
                // Chiudi l'applicazione
                System.exit(0);
            });

            stage.setScene(scene);
            stage.show();

            currentStage.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void ritornaMenuPartiteSingole() {
        try {
            Stage currentStage = (Stage) popUpPane.getScene().getWindow();
            FXMLLoader playerScreen = new FXMLLoader(Spacca.class.getResource("PartitaSelector.fxml"));
            Stage stage = new Stage();
            Parent root = playerScreen.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getIcons().add(ResourceLoader.gameIcons());
            stage.setTitle("SpaccaFX - Game");
            stage.setOnCloseRequest(event -> {
                // Aggiungi qui la logica di chiusura dell'applicazione
                System.out.println("L'utente sta chiudendo l'applicazione");
                // Chiudi l'applicazione
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();

            currentStage.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //region # OTHER METHODS

    public void gestisciPulsanti(boolean sMazzo, boolean sNormale, boolean passa) {
        bottoneEffetto.setVisible(sMazzo);
        bottoneScambia.setVisible(sNormale);
        bottonePassa.setVisible(passa);
    }

    public void gestisciPulsanteScambio(boolean sNormale) {
        bottoneScambia.setVisible(sNormale);
    }

    public void gestisciPulsanteRiprendiBot(boolean flag) {
        bottoneRiprendiBot.setVisible(flag);
    }

    public void gestisciPulsantePassa(boolean passa) {
        bottonePassa.setVisible(passa);
    }

    public void mostraCorone() {
        mazzierePlayer1Icon.setVisible(true);
        mazzierePlayer2Icon.setVisible(true);
        mazzierePlayer3Icon.setVisible(true);
        mazzierePlayer4Icon.setVisible(true);

    }

    public void nascondiCorone() {
        mazzierePlayer1Icon.setVisible(false);
        mazzierePlayer2Icon.setVisible(false);
        mazzierePlayer3Icon.setVisible(false);
        mazzierePlayer4Icon.setVisible(false);
    }

    public void nascondiBannerAttesa() {
        popUpPane.setVisible(false);
        popUpPane.setDisable(true);
        popUpTitleLabel.setVisible(false);
        popUpTextLabel.setVisible(false);
    }

    public void nascondiDadi() {
        dicePl1.setVisible(false);
        dicePl2.setVisible(false);
        dicePl3.setVisible(false);
        dicePl4.setVisible(false);
    }

    public void mostraDadi() {
        dicePl1.setVisible(true);
        dicePl2.setVisible(true);
        dicePl3.setVisible(true);
        dicePl4.setVisible(true);
    }

    public void pulsanteScambiaMazzo(boolean isVisibile) {
        this.bottoneEffetto.setVisible(isVisibile);
    }
    //endregion


    public void startGameUI() {
        impostaCoroneMazziereUI();
        updateVitaUI();
        updateCartaCentraleMazzoUI();

        partita.iniziaNuovoRoundUI();
    }

    public void mostraCarta(int pos) {
        IGiocatore currentGiocatore = partita.giocatori.get(pos);
        String playerName = currentGiocatore.getNome();

        if (playerName.equalsIgnoreCase(nomePlayer1.getText())) {
            cartaPlayer1.setImage(currentGiocatore.getCarta().getImmagineCarta()); // player1

        } else if (playerName.equalsIgnoreCase(nomePlayer2.getText())) {
            cartaPlayer2.setImage(currentGiocatore.getCarta().getImmagineCarta());

        } else if (playerName.equalsIgnoreCase(nomePlayer3.getText())) {
            cartaPlayer3.setImage(currentGiocatore.getCarta().getImmagineCarta());
        } else if (playerName.equalsIgnoreCase(nomePlayer4.getText())) {
            cartaPlayer4.setImage(currentGiocatore.getCarta().getImmagineCarta());
        }
    }


    public void rollLite(int valoreDado, int posPlayer) {
        AudioManager.dadoSuono();
        String currentPlayer = partita.giocatori.get(posPlayer).getNome();

        if (nomePlayer1.getText().equalsIgnoreCase(currentPlayer)) {
            Image myImage = ResourceLoader.loadImage("/Assets/Game/Environment/dice/dice" + valoreDado + ".png");
            dicePl1.setImage(myImage);
            dicePl1.setVisible(true);
            dicePl2.setVisible(false);
            dicePl3.setVisible(false);
            dicePl4.setVisible(false);
        }

        if (nomePlayer2.getText().equalsIgnoreCase(currentPlayer)) {
            Image myImage = ResourceLoader.loadImage("/Assets/Game/Environment/dice/dice" + valoreDado + ".png");
            dicePl2.setImage(myImage);
            dicePl1.setVisible(false);
            dicePl2.setVisible(true);
            dicePl3.setVisible(false);
            dicePl4.setVisible(false);
        }

        if (nomePlayer3.getText().equalsIgnoreCase(currentPlayer))
        {
            Image myImage = ResourceLoader.loadImage("/Assets/Game/Environment/dice/dice" + valoreDado + ".png");
            dicePl3.setImage(myImage);
            dicePl1.setVisible(false);
            dicePl2.setVisible(false);
            dicePl3.setVisible(true);
            dicePl4.setVisible(false);
        }

        if (nomePlayer4.getText().equalsIgnoreCase(currentPlayer))
        {
            Image myImage = ResourceLoader.loadImage("/Assets/Game/Environment/dice/dice" + valoreDado + ".png");
            dicePl4.setImage(myImage);
            dicePl1.setVisible(false);
            dicePl2.setVisible(false);
            dicePl3.setVisible(false);
            dicePl4.setVisible(true);
        }
    }

    public void setExitGame(boolean state) {
        this.exitGame.setVisible(state);
    }



}

