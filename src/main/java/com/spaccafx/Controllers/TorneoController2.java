package com.spaccafx.Controllers;


import com.spaccafx.Enums.GameStatus;
import com.spaccafx.Enums.GameType;
import com.spaccafx.Files.AudioManager;
import com.spaccafx.Files.FileManager;
import com.spaccafx.Files.NameGenerator;
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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.telegram.telegrambots.meta.api.objects.Audio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TorneoController2 {

    private int numeroGiocatori, numeroBotMenu, numeroCarteNormali, numeroVite, numeroCarteSpeciali, contatorePartita; // sono i dati della partita
    private String difficolta; // sono i dati della partita

    @FXML
    Tab playerTab, botTab, creaTab, impostazioniPreliminariTab;

    @FXML
    ChoiceBox<Integer> numeroGiocatoriMenu, viteMenu, carteNormaliMenu, carteSpecialiMenu;

    @FXML
    ChoiceBox<String> difficoltaBotMenu;

    @FXML
    TextField playerName1, playerName2, playerName3, playerName4;

    @FXML
    Label codiceTorneo, numeroBotLabel, difficoltaBotLabel, labelBot1, labelBot2, labelBot3, labelBot4, botCounter, passwordTorneo;

    @FXML
    ImageView oneLabel, twoLabel, treeLabel, fourLabel, hardBot1, hardBot2, hardBot3, hardBot4, easyBot1, easyBot2, easyBot3, easyBot4;

    @FXML
    Button inviaButton, generaCodiceTorneoButton, setBotButton, salvaNomiPlayerButton, impostaGiocoButton;

    ArrayList<Partita> partiteXTorneo;

    ArrayList<IGiocatore> giocatoriPartita, giocatoriBot;

    int codiceT, passwordT;


    public TorneoController2() {
        partiteXTorneo = new ArrayList<Partita>();
    }

    @FXML
    public void initialize() // si attiva da SelectionMenuController
    {
        setNumeroGiocatori();
        setDifficolta();

        setNumeroVite();
        setNumeroCarteSpeciali();
        setNumeroCarteNormali();
        playerTab.setDisable(true);
        botTab.setDisable(true);
        creaTab.setDisable(true);

        this.passwordT = 0;
        this.codiceT = 0;
        this.giocatoriPartita = new ArrayList<IGiocatore>();
        this.giocatoriBot = new ArrayList<IGiocatore>();

        inviaButton.setVisible(false);
        generaCodiceTorneoButton.setVisible(true);

        playerName1.setVisible(false);
        playerName2.setVisible(false);
        playerName3.setVisible(false);
        playerName4.setVisible(false);
        oneLabel.setVisible(false);
        twoLabel.setVisible(false);
        treeLabel.setVisible(false);
        fourLabel.setVisible(false);
        salvaNomiPlayerButton.setVisible(false);
        impostaGiocoButton.setVisible(false);

    }


    // legge i dati dal menu tendina dei giocatori
    public void setNumeroGiocatori() {
        numeroGiocatoriMenu.setOnAction(this::nG);
    }

    // legge i dati dal menu tendina dei giocatori
    public void nG(ActionEvent event) {
        numeroGiocatori = Integer.parseInt(String.valueOf(numeroGiocatoriMenu.getValue()));
        setNumeroGiocatori();
        this.controlloGiocatori();
        this.setNumeroBot();
        System.out.println("Numero giocatori : " + numeroGiocatori);
        salvaNomiPlayerButton.setVisible(true);
    }

    public int getNumeroGiocatori() {
        return numeroGiocatori;
    }


    public void setDifficolta() {
        difficoltaBotMenu.setOnAction(this::dB);

    }


    public void dB(ActionEvent event) {

        difficolta = difficoltaBotMenu.getValue();

    }


    public String getDifficolta() {

        return difficolta;
    }


    public void setNumeroBot() {

        numeroBotMenu = 4 - getNumeroGiocatori();
        botCounter.setText(Integer.toString(numeroBotMenu));


    }

    public int getNumeroBot() {
        return numeroBotMenu;
    }


    public void setNumeroCarteNormali() {

        carteNormaliMenu.setOnAction(this::nCN);

    }

    // legge i dati dal menu tendina dei giocatori
    public void nCN(ActionEvent event) {
        numeroCarteNormali = Integer.parseInt(String.valueOf(carteNormaliMenu.getValue()));
        setNumeroCarteNormali();
        System.out.println("Numero carte Normali : " + numeroCarteNormali);
    }

    public int getNumeroCarteNormali() {
        return numeroCarteNormali;
    }


    public void setNumeroCarteSpeciali() {

        carteSpecialiMenu.setOnAction(this::nCS);

    }

    // legge i dati dal menu tendina dei giocatori
    public void nCS(ActionEvent event) {
        numeroCarteSpeciali = Integer.parseInt(String.valueOf(carteSpecialiMenu.getValue()));
        setNumeroCarteSpeciali();
        System.out.println("Numero carte Speciali : " + numeroCarteSpeciali);
    }

    public int getNumeroCarteSpeciali() {
        return numeroCarteSpeciali;
    }


    public void setNumeroVite() {
        viteMenu.setOnAction(this::nV);
    }

    // legge i dati dal menu tendina dei giocatori
    public void nV(ActionEvent event) {
        numeroVite = Integer.parseInt(String.valueOf(viteMenu.getValue()));
        setNumeroVite();
        System.out.println("Numero vite : " + numeroVite);
    }

    public int getNumeroVite() {
        return numeroVite;
    }


    public void salvaImpostazioni(ActionEvent actionEvent) throws IOException { // PLAY

        AudioManager.bottoneSuono();
        if (numeroVite != 0 && numeroCarteSpeciali != 0 && numeroCarteNormali != 0) {
           // System.out.println("Salvo il numero di vite " + getNumeroVite());
            this.setNumeroVite();
           // System.out.println("Salvo il numero di carte speciali " + getNumeroCarteSpeciali());
            this.setNumeroCarteSpeciali();
          //  System.out.println("Salvo il numero di carte normali " + getNumeroCarteNormali());
            this.setNumeroCarteNormali();
            impostazioniPreliminariTab.setDisable(true);
            playerTab.setDisable(false);
            System.out.println("Dati salvati");
            //botTab.setDisable(false);
        } else {
            AudioManager.erroreSuono();
            AlertController.showErrore("Inserisci tutti i valori correttamente");
        }
    }

    public void nascondiBot() { // viene attivato quando clicchi sul bottone salva
        labelBot1.setVisible(false);
        labelBot2.setVisible(false);
        labelBot3.setVisible(false);
        labelBot4.setVisible(false);
        hardBot1.setVisible(false);
        hardBot2.setVisible(false);
        hardBot3.setVisible(false);
        hardBot4.setVisible(false);
        easyBot1.setVisible(false);
        easyBot2.setVisible(false);
        easyBot3.setVisible(false);
        easyBot4.setVisible(false);
    }


    public void salvaDifficolta(ActionEvent actionEvent) throws IOException {
        AudioManager.bottoneSuono();
        this.setDifficolta();
        System.out.println(difficolta);
        System.out.println("Imposto difficolta" + getDifficolta());
        this.impostaDifficolta();
        impostaGiocoButton.setVisible(true);
    }

    private void mostraIconaBot(Label labelNomeBot, String nome, ImageView hardBot, boolean flag1, ImageView easyBot, boolean flag2) {
        labelNomeBot.setText(nome);
        labelNomeBot.setVisible(true);
        hardBot.setVisible(flag1);
        easyBot.setVisible(flag2);
    }


    // per i bot
    public void impostaDifficolta() {

        // arraylist giocatori =---> 1 - 4 //// 0
        if (difficolta != null && !difficolta.isEmpty()) {
            if (getDifficolta().equalsIgnoreCase("Difficile")) {
                for (int c = 0; c < getNumeroBot(); c++) {
                    giocatoriBot.add(new AdvancedBot(NameGenerator.generaNomeBotAdvanced()));
                }
            } else {
                for (int c = 0; c < getNumeroBot(); c++) {
                    giocatoriBot.add(new EasyBot(NameGenerator.generaNomeBotEasy()));
                }
            }

            if (NameGenerator.controllaNomiDiversi(giocatoriBot))
            {
                caricaGraficaTuttiBot(giocatoriBot);
                //System.out.println("Giocatori Bot: ");
               // NameGenerator.stampaNomi(giocatoriBot);
                this.setBotButton.setVisible(false);
                this.difficoltaBotMenu.setVisible(false);
            } else {
                System.out.println("Giocatori Bot DUPLICATI: ");
                NameGenerator.stampaNomi(giocatoriBot);
                AlertController.showErrore("Ti chiediamo scusa, il sistema ha generato nomi di bot uguali. Riprova..");
                giocatoriBot.clear();
            }
        } else {
            AlertController.showErrore("Devi inserire una difficolta per i bot");
            giocatoriBot.clear();
        }
    }

    private void caricaGraficaTuttiBot(ArrayList<IGiocatore> giocatoriBot) {
        switch (this.getNumeroBot())  // getnumero() bot viene generato quando salvo i dati dei player
        {
            case 1:
                if (getDifficolta().equalsIgnoreCase("Difficile")) {
                    mostraIconaBot(labelBot1, giocatoriBot.get(0).getNome(), hardBot1, true, easyBot1, false);

                } else {
                    mostraIconaBot(labelBot1, giocatoriBot.get(0).getNome(), hardBot1, false, easyBot1, true);
                }
                break;
            case 2:
                if (getDifficolta().equalsIgnoreCase("Difficile")) {

                    mostraIconaBot(labelBot1, giocatoriBot.get(0).getNome(), hardBot1, true, easyBot1, false);
                    mostraIconaBot(labelBot2, giocatoriBot.get(1).getNome(), hardBot2, true, easyBot2, false);
                } else {
                    mostraIconaBot(labelBot1, giocatoriBot.get(0).getNome(), hardBot1, false, easyBot1, true);
                    mostraIconaBot(labelBot2, giocatoriBot.get(1).getNome(), hardBot2, false, easyBot2, true);
                }
                break;
            case 3:
                if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {

                    mostraIconaBot(labelBot1, giocatoriBot.get(0).getNome(), hardBot1, true, easyBot1, false);
                    mostraIconaBot(labelBot2, giocatoriBot.get(1).getNome(), hardBot2, true, easyBot2, false);
                    mostraIconaBot(labelBot3, giocatoriBot.get(2).getNome(), hardBot3, true, easyBot3, false);
                } else {
                    mostraIconaBot(labelBot1, giocatoriBot.get(0).getNome(), hardBot1, false, easyBot1, true);
                    mostraIconaBot(labelBot2, giocatoriBot.get(1).getNome(), hardBot2, false, easyBot2, true);
                    mostraIconaBot(labelBot3, giocatoriBot.get(2).getNome(), hardBot3, false, easyBot3, true);

                }
                break;
            case 4:
                if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                    mostraIconaBot(labelBot1, giocatoriBot.get(0).getNome(), hardBot1, true, easyBot1, false);
                    mostraIconaBot(labelBot2, giocatoriBot.get(1).getNome(), hardBot2, true, easyBot2, false);
                    mostraIconaBot(labelBot3, giocatoriBot.get(2).getNome(), hardBot3, true, easyBot3, false);
                    mostraIconaBot(labelBot4, giocatoriBot.get(3).getNome(), hardBot4, true, easyBot4, false);
                } else {
                    mostraIconaBot(labelBot1, giocatoriBot.get(0).getNome(), hardBot1, false, easyBot1, true);
                    mostraIconaBot(labelBot2, giocatoriBot.get(1).getNome(), hardBot2, false, easyBot2, true);
                    mostraIconaBot(labelBot3, giocatoriBot.get(2).getNome(), hardBot3, false, easyBot3, true);
                    mostraIconaBot(labelBot4, giocatoriBot.get(3).getNome(), hardBot4, false, easyBot4, true);
                }
                break;
            default:
                break;

        }
    }

    private void controlloGiocatori() {
        // Assumiamo che tutti i campi e le immagini siano inizialmente invisibili
        playerName1.setVisible(false);
        playerName2.setVisible(false);
        playerName3.setVisible(false);
        playerName4.setVisible(false);

        oneLabel.setVisible(false);
        twoLabel.setVisible(false);
        treeLabel.setVisible(false);
        fourLabel.setVisible(false);



        // Rendi visibili i campi e le immagini in base al numero di giocatori
        if (numeroGiocatori >= 1) {
            playerName1.setVisible(true);
            oneLabel.setVisible(true);
            playerName1.setVisible(true);
        }
        if (numeroGiocatori >= 2) {
            playerName2.setVisible(true);
            twoLabel.setVisible(true);
            playerName1.setVisible(true);
            playerName2.setVisible(true);
        }
        if (numeroGiocatori >= 3) {
            playerName3.setVisible(true);
            treeLabel.setVisible(true);
            playerName1.setVisible(true);
            playerName2.setVisible(true);
            playerName3.setVisible(true);
        }
        if (numeroGiocatori >= 4) {
            playerName4.setVisible(true);
            fourLabel.setVisible(true);
            playerName1.setVisible(true);
            playerName2.setVisible(true);
            playerName3.setVisible(true);
            playerName4.setVisible(true);
        }
    }



    public void salvaNomi(ActionEvent actionEvent) throws IOException  // pulsante salva giocatori
    {
        AudioManager.bottoneSuono();

        // prende nome player
        switch (getNumeroGiocatori()) { //non è un granchè ma risolve il problema dei giocatori quando sono null, cossì posSIAKMO FARE LED CONDIZIONE PROSSIME
            case 1:
                giocatoriPartita.add(new Giocatore(playerName1.getText()));
                break;
            case 2:
                giocatoriPartita.add(new Giocatore(playerName1.getText()));
                giocatoriPartita.add(new Giocatore(playerName2.getText()));
                break;
            case 3:
                giocatoriPartita.add(new Giocatore(playerName1.getText()));
                giocatoriPartita.add(new Giocatore(playerName2.getText()));
                giocatoriPartita.add(new Giocatore(playerName3.getText()));
                break;
            case 4:
                giocatoriPartita.add(new Giocatore(playerName1.getText()));
                giocatoriPartita.add(new Giocatore(playerName2.getText()));
                giocatoriPartita.add(new Giocatore(playerName3.getText()));
                giocatoriPartita.add(new Giocatore(playerName4.getText()));
                impostaGiocoButton.setVisible(true);
                break;
            case 0:
                break;
            default:
                break;
        }

        // guarda che siano diversi i nomi DEI PLAYER tra loro prima di salvare
        if (NameGenerator.controllaNomiDiversi(giocatoriPartita) || giocatoriPartita.size() == 0) {
            // appuriamo che tutti i nomi dei player sono diversi
            AlertController.showWarning("Impostati giocatori normali!");
            enableBotTab();
            this.nascondiBot(); // nasconde icone bot fino a che non imposta difficolta

         //   System.out.println("Giocatori iniziali normali: ");
          //  NameGenerator.stampaNomi(giocatoriPartita);
            this.salvaNomiPlayerButton.setVisible(false);
            this.numeroGiocatoriMenu.setVisible(false);

        } else {
            AudioManager.erroreSuono();
            AlertController.showErrore("Non ci possono essere giocatori con lo stesso nome o senza, RIPROVA ");
            this.giocatoriPartita.clear();
        }


    }

    private void enableBotTab() {
        if (getNumeroGiocatori() != 4 || getNumeroGiocatori() == 0) {
            botTab.setDisable(false);
        } else {
            botTab.setDisable(false);
            setBotButton.setVisible(false);
            difficoltaBotMenu.setVisible(false);
            difficoltaBotLabel.setVisible(false);

        }

    }


    public boolean controlloNomiTotaleTorneo()
    {

        try
        {
            // Utilizza un set per verificare l'unicità dei nomi dei giocatori nella partita
            Set<String> nomiUnici = new HashSet<>();

            for (Partita partita : partiteXTorneo)
            {
                ArrayList<IGiocatore> giocatoriPartita = partita.giocatori;

                for (IGiocatore giocatore : giocatoriPartita)
                {
                    String nomeGiocatore = giocatore.getNome();

                    // Verifica se il nome è già presente nel set
                    if (nomiUnici.contains(nomeGiocatore))
                    {
                        // Stampa i nomi dei giocatori nel set
                        System.out.println("Nomi dei giocatori NON UNICI nel Torneo:");
                        for (String nomiGiocatore : nomiUnici) {
                            System.out.println(nomiGiocatore);
                        }

                        // Se il nome è già presente, restituisci false
                        return false;
                    } else {
                        // Aggiungi il nome al set
                        nomiUnici.add(nomeGiocatore);
                    }
                }
            }

            // Stampa i nomi dei giocatori nel set
            System.out.println("Nomi dei giocatori UNICI nel Torneo:");
            for (String nomeGiocatore : nomiUnici) {
                System.out.println(nomeGiocatore);
            }

            // Se siamo arrivati a questo punto, tutti i nomi sono unici in ogni partita
            return true;
        }
        catch (Exception e)
        {
            AlertController.showErrore("C'e stato un errore durante la creazione del torneo, ricrealo!");
            return false;
        }
    }


    public void indietro(MouseEvent mouseEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("SelectionMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }


    // Utilizziamo classe partita
    public void generaCodice(ActionEvent actionEvent) throws IOException {
        //int currentIdPartita = FileManager.creaCodicePartitaUnico();

        AudioManager.bottoneSuono();

        AlertController.showWarning("Attenzione: Attendi Caricamento");

        this.codiceT = FileManager.creaCodiceTorneoUnico();
        this.passwordT = FileManager.creaPasswordTorneoUnico();

        System.out.println("Codice Generato: " + codiceT);
        System.out.println("Password Generata: " + passwordT);
        passwordTorneo.setText("Password: " + passwordT);
        codiceTorneo.setText("Codice: " + codiceT);

        AlertController.showWarning("Codice torneo generato!,Comunica il codice ai giocatori che dovranno inserirlo successivamente");

        generaCodiceTorneoButton.setVisible(false);
        inviaButton.setVisible(true);
    }

    public void impostaTorneo(ActionEvent actionEvent) throws IOException
    {
        AudioManager.bottoneSuono();
        FileManager.creaTorneoSuFile(this.codiceT, this.passwordT, this.partiteXTorneo, GameType.TORNEO, GameStatus.STARTED, this.numeroCarteNormali, this.numeroCarteSpeciali, this.numeroVite);

        if (controlloNomiTotaleTorneo()) // se e true i nomi nel torneo sono unici e possiamo crearlo
        {
            FXMLLoader playerScreen = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent root = playerScreen.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else
        {// nomi non univoci
            FileManager.eliminaTorneo(this.codiceT);
            AudioManager.erroreSuono();
            AlertController.showErrore("Nel torneo sono presenti nomi uguali!");

            FXMLLoader playerScreen = new FXMLLoader(Spacca.class.getResource("Torneo2.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent root = playerScreen.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }


    // Pulsante crea partita
    public void impostaGioco(ActionEvent actionEvent) throws IOException {

        AudioManager.bottoneSuono();
        partiteXTorneo.add(new Partita(4));
        Partita p = partiteXTorneo.get(contatorePartita);

        // adesso aggiungiamo ai giocatori i bot
        if(giocatoriBot != null || !giocatoriBot.isEmpty() || giocatoriBot.size() != 0)
        {
            for (IGiocatore giocatoreBot : giocatoriBot)
                giocatoriPartita.add(giocatoreBot);
        }


        System.out.println("Giocatori Partita Totali : " );
        NameGenerator.stampaNomi(giocatoriPartita);

        p.aggiungiListaGiocatori(giocatoriPartita);



        //System.out.println("La mia partita contiene " + p.giocatori.size() + " giocatori");
        System.out.println("La mia partita contiene " + partiteXTorneo.get(contatorePartita).giocatori.size() + " giocatori");

        this.contatorePartita++;

        if (contatorePartita < 4)
        {
            FXMLLoader impostaTorneo = new FXMLLoader(Spacca.class.getResource("Torneo2.fxml"));
            Parent root = impostaTorneo.load();

            TorneoController2 torneoController = impostaTorneo.getController();
            torneoController.salvaDati(this.partiteXTorneo, this.numeroCarteNormali, this.numeroVite, this.numeroCarteSpeciali, this.contatorePartita);
            torneoController.impostazioniPreliminariTab.setDisable(true);
            torneoController.creaTab.setDisable(true);
            torneoController.playerTab.setDisable(false);
            torneoController.botTab.setDisable(false);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            impostazioniPreliminariTab.setDisable(true);
            playerTab.setDisable(true);
            botTab.setDisable(true);
            creaTab.setDisable(false);
        }

    }


    private void salvaDati(ArrayList<Partita> partiteXTorneo, int numeroCarteNormali, int numeroVite, int numeroCarteSpeciali, int contatorePartita) {
        this.partiteXTorneo = partiteXTorneo;
        this.numeroCarteNormali = numeroCarteNormali;
        this.numeroVite = numeroVite;
        this.numeroCarteSpeciali = numeroCarteSpeciali;
        this.contatorePartita = contatorePartita;
    }

}

