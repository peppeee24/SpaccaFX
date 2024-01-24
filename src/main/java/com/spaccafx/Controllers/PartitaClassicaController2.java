package com.spaccafx.Controllers;


import com.spaccafx.Cards.Mazzo;
import com.spaccafx.Enums.GameStatus;
import com.spaccafx.Enums.GameType;
import com.spaccafx.Files.AudioManager;
import com.spaccafx.Files.FileManager;
import com.spaccafx.Files.NameGenerator;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;

import javafx.scene.control.TextField;


import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PartitaClassicaController2 {
    private int numeroGiocatori, numeroBotMenu, numeroCarteNormali, numeroVite, numeroCarteSpeciali; // sono i dati della partita
    private String difficolta; // sono i dati della partita

    @FXML
    Tab playerTab, botTab, creaTab, impostazioniTab;

    @FXML
    Button generaCodiceButton, inviaButton, setBotButton, salvaNomiPlayerButton;

    @FXML
    ChoiceBox<Integer> numeroGiocatoriMenu, viteMenu, carteNormaliMenu, carteSpecialiMenu;

    @FXML
    ChoiceBox<String> difficoltaBotMenu;

    @FXML
    TextField playerName1, playerName2, playerName3, playerName4;

    @FXML
    Label codicePartita, numeroBotLabel, difficoltaBotLabel, labelBot1, labelBot2, labelBot3, labelBot4, botCounter, passwordPartita;

    @FXML
    ImageView oneLabel, twoLabel, treeLabel, fourLabel, hardBot1, hardBot2, hardBot3, hardBot4, easyBot1, easyBot2, easyBot3, easyBot4;


    ArrayList<IGiocatore> giocatoriPartita, giocatoriBot;

    int codiceP, passwordP;


    @FXML
    public void initialize() // si attiva da SelectionMenuController
    {
        setNumeroGiocatori();
        setDifficolta();

        setNumeroVite();
        setNumeroCarteSpeciali();
        setNumeroCarteNormali();
        botTab.setDisable(true);
        creaTab.setDisable(true);
        impostazioniTab.setDisable(true);

        this.codiceP = 0;
        this.passwordP = 0;
        this.giocatoriPartita = new ArrayList<IGiocatore>();
        this.giocatoriBot = new ArrayList<IGiocatore>();

        inviaButton.setVisible(false);
        generaCodiceButton.setVisible(true);
        playerName1.setVisible(false);
        playerName2.setVisible(false);
        playerName3.setVisible(false);
        playerName4.setVisible(false);
        oneLabel.setVisible(false);
        twoLabel.setVisible(false);
        treeLabel.setVisible(false);
        fourLabel.setVisible(false);
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

        if (numeroVite != 0 && numeroCarteSpeciali != 0 && numeroCarteNormali != 0) {
            AudioManager.bottoneSuono();
            System.out.println("Salvo il numero di vite" + getNumeroVite());
            this.setNumeroVite();
            System.out.println("Salvo il numero di carte speciali" + getNumeroCarteSpeciali());
            this.setNumeroCarteSpeciali();
            System.out.println("Salvo il numero di carte normali" + getNumeroCarteNormali());
            this.setNumeroCarteNormali();

            creaTab.setDisable(false);
        } else {
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

        impostazioniTab.setDisable(false);
    }

    private void mostraIconaBot(Label labelNomeBot, String nome, ImageView hardBot, boolean flag1, ImageView easyBot, boolean flag2) {
        labelNomeBot.setText(nome);
        labelNomeBot.setVisible(true);
        hardBot.setVisible(flag1);
        easyBot.setVisible(flag2);
    }


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
                System.out.println("Giocatori Bot: ");
                NameGenerator.stampaNomi(giocatoriBot);
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

            System.out.println("Giocatori iniziali normali: ");
            NameGenerator.stampaNomi(giocatoriPartita);
            this.salvaNomiPlayerButton.setVisible(false);
            this.numeroGiocatoriMenu.setVisible(false);

        } else {
            AudioManager.erroreSuono();
            AlertController.showErrore("Non ci possono essere giocatori con lo stesso nome o senza, RIPROVA ");
            this.giocatoriPartita.clear();
        }


    }

    private void enableBotTab() {
        if (getNumeroGiocatori() == 4) {

            impostazioniTab.setDisable(false);
        } else {
            botTab.setDisable(false);

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
    public void generaCodice(ActionEvent actionEvent)
    {
        try
        {
            AudioManager.bottoneSuono();
            AlertController.showWarning("Attenzione: Attendi Caricamento");


            int somma = getNumeroGiocatori() + getNumeroBot();

            if (somma > 1 && somma < 5) {
                this.codiceP = FileManager.creaCodicePartitaUnico();
                this.passwordP = FileManager.creaPasswordPartitaUnica();


                System.out.println("Codice Generato: " + codiceP);
                System.out.println("Password Generata: " + passwordP);
                passwordPartita.setText("Password: " + passwordP);
                codicePartita.setText("Codice: " + codiceP);


                AlertController.showWarning("Codice partita generato!,Comunica il codice ai giocatori che dovranno inserirlo successivamente");
                generaCodiceButton.setVisible(false);
                inviaButton.setVisible(true);


            } else { //teoricamente non entra mai siccome il numero dei bot va di pari passo a quello dei giocatori, lo teniamo solo per avere una sicurezza maggiore
                AudioManager.erroreSuono();

                AlertController.showErrore("Errore: Player/BOT non impostati correttamente");

            }
        }
        catch (Exception e)
        {
            Alert test = new Alert(Alert.AlertType.ERROR);
            test.setContentText("Errore di caricamento: " + e.getMessage());
            test.show();
        }
    }

    // Pulsante crea partita
    public void impostaGioco(ActionEvent actionEvent) throws IOException {
        AudioManager.bottoneSuono();
        // adesso aggiungiamo ai giocatori i bot
        if(giocatoriBot != null || !giocatoriBot.isEmpty() || giocatoriBot.size() != 0)
        {
            for (IGiocatore giocatoreBot : giocatoriBot)
                giocatoriPartita.add(giocatoreBot);
        }

        System.out.println("Giocatori Partita Totali : " );
        NameGenerator.stampaNomi(giocatoriPartita);

        FileManager.creaPartitaSuFile(this.codiceP, this.passwordP, this.giocatoriPartita, numeroCarteNormali, numeroCarteSpeciali, numeroVite); // salviamo le informazioni dati


        // ti riporta al menu principale
        FXMLLoader impostaGioco = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(impostaGioco.load());
        stage.setScene(scene);
        stage.show();

    }

}