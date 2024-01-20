package com.spaccafx.Controllers;


import com.spaccafx.Enums.GameStatus;
import com.spaccafx.Enums.GameType;
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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TorneoController2 {

    private int numeroGiocatori, numeroBotMenu, numeroCarteNormali, numeroVite, numeroCarteSpeciali, contatorePartita; // sono i dati della partita
    private String difficolta, nomeGiocatore1, nomeGiocatore2, nomeGiocatore3, nomeGiocatore4, E1, E2, E3, E4, A1, A2, A3, A4; // sono i dati della partita

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
    Button inviaButton, generaCodiceTorneoButton, setBotButton;

    // creo bot per settare nomi/ difficolta
    EasyBot E = new EasyBot(); // TODO NON VA BENE
    AdvancedBot A = new AdvancedBot();
    ArrayList<Partita> partiteXTorneo;


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

        inviaButton.setVisible(false);
        generaCodiceTorneoButton.setVisible(true);
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
        setNumeroBot();
        System.out.println("Numero giocatori : " + numeroGiocatori);
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
            System.out.println("Salvo il numero di vite " + getNumeroVite());
            this.setNumeroVite();
            System.out.println("Salvo il numero di carte speciali " + getNumeroCarteSpeciali());
            this.setNumeroCarteSpeciali();
            System.out.println("Salvo il numero di carte normali " + getNumeroCarteNormali());
            this.setNumeroCarteNormali();
            impostazioniPreliminariTab.setDisable(true);
            playerTab.setDisable(false);
            //botTab.setDisable(false);
        } else {
            AudioManager.erroreSuono();
            AlertController.showErrore("Inserisci tutti i valori correttamente");
        }
    }

    public void getEasyBot1() {
        E1 = E.generaNomeBot();
    }

    public String getE1() {
        return E1;
    }

    public void getEasyBot2() {
        E2 = E.generaNomeBot();
    }

    public String getE2() {
        return E2;
    }

    public void getEasyBot4() {
        E4 = E.generaNomeBot();

    }

    public String getE4() {

        return E4;
    }

    public void getEasyBot3() {
        E3 = E.generaNomeBot();

    }

    public String getE3() {

        return E3;
    }


    public void getAdvBot1() {
        A1 = A.generaNomeBot();
    }

    public String getA1() {
        return A1;
    }

    public void getAdvBot2() {
        A2 = A.generaNomeBot();
    }

    public String getA2() {
        return A2;
    }

    public void getAdvBot3() {
        A3 = A.generaNomeBot();

    }

    public String getA3() {
        return A3;
    }

    public void getAdvBot4() {
        A4 = A.generaNomeBot();

    }

    public String getA4() {
        return A4;
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
        getAdvBot1();
        getAdvBot2();
        getAdvBot3();
        getAdvBot4();
        getEasyBot1();
        getEasyBot2();
        getEasyBot3();
        getEasyBot4();
    }


    public void salvaDifficolta(ActionEvent actionEvent) throws IOException {
        AudioManager.bottoneSuono();
        this.setDifficolta();
        System.out.println(difficolta);
        System.out.println("Imposto difficolta" + getDifficolta());
        this.impostaDifficolta();
    }


    // per i bot
    public void impostaDifficolta() {

        if (difficolta != null) {
            switch (getNumeroBot())  // getnumero() bot viene generato quando salvo i dati dei player
            {
                case 0:
                    difficoltaBotMenu.setVisible(false); // guardare sta merda (demolire tutto)
                    difficoltaBotLabel.setVisible(false);
                    numeroBotLabel.setText("Non ci sono bot");
                    break;
                case 1:
                    if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                        labelBot1.setText(getA1());
                        labelBot1.setVisible(true);
                        hardBot1.setVisible(true);
                    } else {
                        labelBot1.setText(getE1());
                        labelBot1.setVisible(true);
                        easyBot1.setVisible(true);
                    }
                    break;
                case 2:
                    if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                        labelBot1.setText(getA1());
                        labelBot1.setVisible(true);
                        hardBot1.setVisible(true);
                        labelBot2.setText(getA2());
                        labelBot2.setVisible(true);
                        hardBot2.setVisible(true);
                    } else {
                        labelBot1.setText(getE1());
                        labelBot1.setVisible(true);
                        easyBot1.setVisible(true);
                        labelBot2.setText(getE2());
                        labelBot2.setVisible(true);
                        easyBot2.setVisible(true);
                    }
                    break;
                case 3:
                    if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                        labelBot1.setText(getA1());
                        labelBot1.setVisible(true);
                        hardBot1.setVisible(true);
                        labelBot2.setText(getA2());
                        labelBot2.setVisible(true);
                        hardBot2.setVisible(true);
                        labelBot3.setText(getA3());
                        labelBot3.setVisible(true);
                        hardBot3.setVisible(true);
                    } else {
                        labelBot1.setText(getE1());
                        labelBot1.setVisible(true);
                        easyBot1.setVisible(true);
                        labelBot2.setText(getE2());
                        labelBot2.setVisible(true);
                        easyBot2.setVisible(true);
                        labelBot3.setText(getE3());
                        labelBot3.setVisible(true);
                        easyBot3.setVisible(true);
                    }
                    break;
                case 4:
                    if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                        labelBot1.setText(getA1());
                        labelBot1.setVisible(true);
                        hardBot1.setVisible(true);
                        labelBot2.setText(getA2());
                        labelBot2.setVisible(true);
                        hardBot2.setVisible(true);
                        labelBot3.setText(getA3());
                        labelBot3.setVisible(true);
                        hardBot3.setVisible(true);
                        labelBot4.setText(getA4());
                        labelBot4.setVisible(true);
                        hardBot4.setVisible(true);
                    } else {
                        labelBot1.setText(getE1());
                        labelBot1.setVisible(true);
                        easyBot1.setVisible(true);
                        labelBot2.setText(getE2());
                        labelBot2.setVisible(true);
                        easyBot2.setVisible(true);
                        labelBot3.setText(getE3());
                        labelBot3.setVisible(true);
                        easyBot3.setVisible(true);
                        labelBot4.setText(getE4());
                        labelBot4.setVisible(true);
                        easyBot4.setVisible(true);
                    }
                    break;
                default:

            }
        } else {
            System.out.println("Difficoltà è null");
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
        }
        if (numeroGiocatori >= 2) {
            playerName2.setVisible(true);
            twoLabel.setVisible(true);
        }
        if (numeroGiocatori >= 3) {
            playerName3.setVisible(true);
            treeLabel.setVisible(true);
        }
        if (numeroGiocatori >= 4) {
            playerName4.setVisible(true);
            fourLabel.setVisible(true);
        }
    }


    public void salvaNomi(ActionEvent actionEvent) throws IOException  // pulsante salva giocatori
    {

        AudioManager.bottoneSuono();

        String pn1 = "";
        String pn2 = "";
        String pn3 = "";
        String pn4 = "";

        switch (getNumeroGiocatori()) { //non è un granchè ma risolve il problema dei giocatori quando sono null, cossì posSIAKMO FARE LED CONDIZIONE PROSSIME
            case 1:
                pn1 = playerName1.getText();
                pn2 = "verijhvnerthunbv";
                pn3 = "gbwoktermbojqet";
                pn4 = "rtbkmitrbmirt";
                break;
            case 2:
                pn1 = playerName1.getText();
                pn2 = playerName2.getText();
                pn3 = "dfbijmq9obm";
                pn4 = "eobtjmvortejmgb9uipw";
                break;
            case 3:
                pn1 = playerName1.getText();
                pn2 = playerName2.getText();
                pn3 = playerName3.getText();
                pn4 = "rgbkeito0qbkjio9wjg";
                break;
            case 4:
                pn1 = playerName1.getText();
                pn2 = playerName2.getText();
                pn3 = playerName3.getText();
                pn4 = playerName4.getText();
                break;
            case 0:
                pn1 = "fjhrkspvrjhhhfkwpvf";
                pn2 = "verijhvnerthunbv";
                pn3 = "gbwoktermbojqet";
                pn4 = "rtbkmitrbmirt";
                break;
            default:
                break;
        }

        if (!(pn1.equalsIgnoreCase(pn2) || pn1.equalsIgnoreCase(pn3) || pn1.equalsIgnoreCase(pn4) || pn2.equalsIgnoreCase(pn3) || pn2.equalsIgnoreCase(pn4) || pn3.equalsIgnoreCase(pn4))) {
            System.out.println(playerName1.getText() + playerName2.getText() + playerName3.getText() + playerName4.getText());

            switch (getNumeroGiocatori()) {
                case 1:
                    if (!(playerName1.getText().equals(""))) {

                        setNomeGiocatore1();
                        AudioManager.leaderboardSuono();
                        AlertController.showWarning("I nomi sono stati salvati con successo!");
                        enableBotTab();


                    } else {
                        AudioManager.erroreSuono();
                        AlertController.showErrore("Devi impostare il nome del giocatore");
                    }
                    break;
                case 2:
                    if (!(playerName1.getText().equals("") || playerName2.getText().equals(""))) {

                        setNomeGiocatore1();
                        setNomeGiocatore2();
                        AudioManager.leaderboardSuono();
                        AlertController.showWarning("I nomi sono stati salvati con successo!");
                        enableBotTab();


                    } else {
                        AudioManager.erroreSuono();
                        AlertController.showErrore("Devi impostare il nome del giocatore");
                    }
                    break;
                case 3:
                    if (!(playerName1.getText().equals("") || playerName2.getText().equals("") || playerName3.getText().equals(""))) {

                        setNomeGiocatore1();
                        setNomeGiocatore2();
                        setNomeGiocatore3();
                        AudioManager.leaderboardSuono();
                        AlertController.showWarning("I nomi sono stati salvati con successo!");
                        enableBotTab();


                    } else {
                        AudioManager.erroreSuono();
                        AlertController.showErrore("Devi impostare il nome del giocatore");
                    }
                    break;
                case 4:
                    if (!(playerName1.getText().equals("") || playerName2.getText().equals("") || playerName3.getText().equals("") || playerName4.getText().equals(""))) {

                        setNomeGiocatore1();
                        setNomeGiocatore2();
                        setNomeGiocatore3();
                        setNomeGiocatore4();
                        AudioManager.leaderboardSuono();
                        AlertController.showWarning("I nomi sono stati salvati con successo!");

                        enableBotTab();
                       // botTab.setDisable(true);
                    } else {
                        AudioManager.erroreSuono();
                        AlertController.showErrore("Devi impostare il nome del giocatore");
                    }
                    break;
                case 0:
                    AlertController.showWarning("Non hai impostato giocatori, la partita sarà governata da solo bot");
                    enableBotTab();
                    break;
                default:
                    break;


            }
            this.nascondiBot();

        } else {
            AudioManager.erroreSuono();
            AlertController.showErrore("Non ci possono essere giocatori con lo stesso o senza, RIPROVA ");
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


    public boolean controlloNomiTotale2() {

        boolean esito = true; // Imposto esito a true di default

        ArrayList<Partita> p = FileManager.leggiTorneoDaFile(codiceT);

        Set<String> nomiIncontrati = new HashSet<>(); // Utilizzo un set per tenere traccia dei nomi già incontrati

        for (int i = 0; i < 4; i++) { // For per le partite
            for (int j = 0; j < 4; j++) { // For per i giocatori in una partita singola

                IGiocatore giocatore = p.get(i).giocatori.get(j);

                String nomeGiocatore = giocatore.getNome();

                // Controllo se il nome è già stato incontrato
                if (nomiIncontrati.contains(nomeGiocatore)) {
                    AudioManager.erroreSuono();
                    AlertController.showErrore("Esiste già un giocatore in questo torneo chiamato: " + nomeGiocatore + ", reimposta la partita");
                    esito = false; // Imposto esito a false se trovo un nome duplicato
                    break; // Esco dal ciclo in quanto ho già trovato un nome duplicato
                } else {
                    // Aggiungo il nome al set perché l'ho incontrato ora
                    nomiIncontrati.add(nomeGiocatore);
                }
            }
            if (!esito) {
                break; // Esco anche dal ciclo esterno se ho trovato un nome duplicato
            }
        }

        return esito;
    }

    public void setNomeGiocatore1() {
        nomeGiocatore1 = playerName1.getText();

    }

    public void setNomeGiocatore2() {
        nomeGiocatore2 = playerName2.getText();

    }

    public void setNomeGiocatore3() {
        nomeGiocatore3 = playerName3.getText();

    }

    public void setNomeGiocatore4() {
        nomeGiocatore4 = playerName4.getText();

    }

    public String getNomeGiocatore1() {
        return nomeGiocatore1;
    }

    public String getNomeGiocatore2() {
        return nomeGiocatore2;
    }

    public String getNomeGiocatore3() {
        return nomeGiocatore3;
    }

    public String getNomeGiocatore4() {
        return nomeGiocatore4;
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

    public void impostaTorneo(ActionEvent actionEvent) throws IOException {
        AudioManager.bottoneSuono();
        FileManager.creaTorneoSuFile(this.codiceT, this.passwordT, this.partiteXTorneo, GameType.TORNEO, GameStatus.STARTED, this.numeroCarteNormali, this.numeroCarteSpeciali, this.numeroVite);

        if (controlloNomiTotale2()) {
            FXMLLoader playerScreen = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent root = playerScreen.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            FileManager.sovrascriviStatoTorneo(codiceT, GameStatus.ENDED); // TODO cancellare dal file la partita creata in caso di doppio giocatore
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

        if (getNumeroGiocatori() == 1) {
            System.out.println("Nome giocatore 1: " + getNomeGiocatore1());
            p.aggiungiGiocatore(new Giocatore(getNomeGiocatore1()));

        } else if (getNumeroGiocatori() == 2) {

            p.aggiungiGiocatore(new Giocatore(getNomeGiocatore1()));
            p.aggiungiGiocatore(new Giocatore(getNomeGiocatore2()));
        } else if (getNumeroGiocatori() == 3) {
            p.aggiungiGiocatore(new Giocatore(getNomeGiocatore1()));
            p.aggiungiGiocatore(new Giocatore(getNomeGiocatore2()));
            p.aggiungiGiocatore(new Giocatore(getNomeGiocatore3()));
        } else if (getNumeroGiocatori() == 4) {
            p.aggiungiGiocatore(new Giocatore(getNomeGiocatore1()));
            p.aggiungiGiocatore(new Giocatore(getNomeGiocatore2()));
            p.aggiungiGiocatore(new Giocatore(getNomeGiocatore3()));
            p.aggiungiGiocatore(new Giocatore(getNomeGiocatore4()));
        } else {

        }


        switch (getNumeroBot()) {

            case 3:

                if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                    p.aggiungiGiocatore(new AdvancedBot(getA1()));
                    p.aggiungiGiocatore(new AdvancedBot(getA2()));
                    p.aggiungiGiocatore(new AdvancedBot(getA3()));
                } else {
                    p.aggiungiGiocatore(new EasyBot(getE1()));
                    p.aggiungiGiocatore(new EasyBot(getE2()));
                    p.aggiungiGiocatore(new EasyBot(getE3()));

                }
                break;
            case 4:

                if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                    p.aggiungiGiocatore(new AdvancedBot(getA1()));
                    p.aggiungiGiocatore(new AdvancedBot(getA2()));
                    p.aggiungiGiocatore(new AdvancedBot(getA3()));
                    p.aggiungiGiocatore(new AdvancedBot(getA4()));
                } else {
                    p.aggiungiGiocatore(new EasyBot(getE1()));
                    p.aggiungiGiocatore(new EasyBot(getE2()));
                    p.aggiungiGiocatore(new EasyBot(getE3()));
                    p.aggiungiGiocatore(new EasyBot(getE4()));

                }
                break;
            case 2:

                if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                    p.aggiungiGiocatore(new AdvancedBot(getA1()));
                    p.aggiungiGiocatore(new AdvancedBot(getA2()));
                } else {
                    p.aggiungiGiocatore(new EasyBot(getE1()));
                    p.aggiungiGiocatore(new EasyBot(getE2()));
                }
                break;
            case 1:
                if (getDifficolta().equals("Difficile") && !getDifficolta().isEmpty()) {
                    p.aggiungiGiocatore(new AdvancedBot(getA1()));
                } else {
                    p.aggiungiGiocatore(new EasyBot(getE1()));
                }
                break;
            default:
                break;
        }


        System.out.println("La mia partita contiene " + p.giocatori.size() + " giocatori");
        System.out.println("La mia partita contiene " + partiteXTorneo.get(contatorePartita).giocatori.size() + " giocatori");

        this.contatorePartita++;

        if (contatorePartita < 4) {
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

