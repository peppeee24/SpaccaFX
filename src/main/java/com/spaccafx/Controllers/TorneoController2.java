package com.spaccafx.Controllers;


import com.spaccafx.Enums.GameStatus;
import com.spaccafx.Enums.GameType;
import com.spaccafx.Files.AudioManager;
import com.spaccafx.Files.FileManager;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Manager.Torneo;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class TorneoController2 {

    private int numeroGiocatori, numeroBotMenu, numeroCarteNormali, numeroVite, numeroCarteSpeciali, contaPartite=0; // sono i dati della partita
    private String difficolta, nomeGiocatore1, nomeGiocatore2, nomeGiocatore3, nomeGiocatore4, E1, E2, E3,E4, A1, A2, A3,A4; // sono i dati della partita


    Partita P; // // TODO guardare dichiaro classe partita
    Torneo T;


    @FXML
    Tab playerTab, botTbb, creaTab,impostazioniPreliminariTab;

    @FXML
    ChoiceBox<Integer> numeroGiocatoriMenu, viteMenu, carteNormaliMenu,carteSpecialiMenu;

    @FXML
    ChoiceBox<String> difficoltaBotMenu;

    @FXML
    TextField playerName1, playerName2, playerName3, playerName4;

    @FXML
    Label codicePartita,codiceTorneo, numeroBotLabel, difficoltaBotLabel, labelBot1, labelBot2, labelBot3, labelBot4, botCounter, passwordPartita,passwordTorneo;

    @FXML
    ImageView oneLabel,twoLabel, treeLabel, fourLabel, hardBot1, hardBot2, hardBot3,hardBot4, easyBot1, easyBot2, easyBot3, easyBot4;
    Button inviaButton, generaCodiceTorneoButton,generaCodiceButton;

    // creo bot per settare nomi/ difficolta
    EasyBot E = new EasyBot(); // TODO NON VA BENE
    AdvancedBot A = new AdvancedBot();


    @FXML
    public void initialize() // si attiva da SelectionMenuController
    {
        setNumeroGiocatori();
    }

    // legge i dati dal menu tendina dei giocatori
    public void setNumeroGiocatori() {
        numeroGiocatoriMenu.setOnAction(this::nG);
    }

    // legge i dati dal menu tendina dei giocatori
    public void nG(ActionEvent event) {
        numeroGiocatori = numeroGiocatoriMenu.getValue();
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
        numeroCarteNormali = carteNormaliMenu.getValue();
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
        numeroCarteSpeciali = carteSpecialiMenu.getValue();
        setNumeroCarteSpeciali();
        System.out.println("Numero carte Speciali : " + numeroCarteSpeciali);
    }

    public int getNumeroCarteSpeciali() {
        return numeroCarteSpeciali;
    }



    public void setNumeroVite() {viteMenu.setOnAction(this::nV);}

    // legge i dati dal menu tendina dei giocatori
    public void nV(ActionEvent event) {
        numeroVite = viteMenu.getValue();
        setNumeroVite();
        System.out.println("Numero vite : " + numeroVite);
    }

    public int getNumeroVite() {
        return numeroVite;
    }


    public void saveNormali(ActionEvent actionEvent) throws IOException  // pulsante
    {
        AudioManager.bottoneSuono();
        System.out.println("Salvo il numero di carte normali");



    }

    public void saveSpeciali(ActionEvent actionEvent) throws IOException  // pulsante
    {
        AudioManager.bottoneSuono();
        System.out.println("Salvo il numero di carte speciali");
        this.setNumeroCarteSpeciali();

    }

    public void saveVite(ActionEvent actionEvent) throws IOException  // pulsante
    {
        AudioManager.bottoneSuono();
        System.out.println("Salvo il numero di vite");
        this.setNumeroVite();

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
        System.out.println("Salvo i nomi");
        switch (getNumeroGiocatori()) {
            case 1:
                setNomeGiocatore1();
                break;
            case 2:
                setNomeGiocatore1();
                setNomeGiocatore2();
                break;
            case 3:
                setNomeGiocatore1();
                setNomeGiocatore2();
                setNomeGiocatore3();
                break;
            case 4:
                setNomeGiocatore1();
                setNomeGiocatore2();
                setNomeGiocatore3();
                setNomeGiocatore4();
                break;
            default: break;


        }
        this.nascondiBot();


    }


    public void setNomeGiocatore1() {
        nomeGiocatore1 = playerName1.getText();

        /*
        if(nomeGiocatore1.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campo vuoto");
            alert.setContentText("Inserisci un nome per continuare a giocare");
            Optional<ButtonType> result = alert.showAndWait();
        }

         */

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

    /*    Alert alert3 = new Alert(Alert.AlertType.CONFIRMATION);
        alert3.setTitle("Piccolo LAG!");
        alert3.setContentText("Sto caricando il codice, attendi....");
        Optional<ButtonType> result3 = alert3.showAndWait();

     */
        AlertController.showWarning("Attenzione: Attendi Caricamento");


        int somma = getNumeroGiocatori() + getNumeroBot();

        if (somma > 1 && somma < 5) {
            this.P = new Partita(somma);
            P.generaCodicePartita();
            P.generaPasswordPartita();
            System.out.println("Codice Generato: " + P.getCodicePartita());
            System.out.println("Password Generata: " + P.getPasswordPartita());
            passwordPartita.setText("Password: " + P.getPasswordPartita());
            codicePartita.setText("Codice: " + P.getCodicePartita());
            //   codicePartita.wrapTextProperty().set(true);
            // codicePartita.getStyleClass().add("copiable-label");
            // TODO non riesco a rendere selezionabile la label

           /* Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Codice partita generato");
            alert.setContentText("Comunica il codice ai giocatori che dovranno inserirlo successivamente");
            Optional<ButtonType> result = alert.showAndWait();

            */
            AlertController.showWarning("Codice partita generato!,Comunica il codice ai giocatori che dovranno inserirlo successivamente");


        } else { //teoricamente non entra mai siccome il numero dei bot va di pari passo a quello dei giocatori, lo teniamo solo per avere una sicurezza maggiore
            AudioManager.erroreSuono();
          /*  Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Parametri errati");
            alert.setContentText("Si puà giocare tra 2 e 4 giocatori, compresi bot, rivedi le impostazioni");
            Optional<ButtonType> result = alert.showAndWait();

           */
            AlertController.showErrore("Errore: Player/BOT non impostati correttamente");

        }

        contaPartite++;
        if (contaPartite == 4) {

            inviaButton.setText("Iniza Tornero");
            generaCodiceTorneoButton.setVisible(true);
            codiceTorneo.setText("Codice Torneo: " + T.getCodiceTorneo());
            System.out.println("Codice torneo:"+T.getCodiceTorneo());
            passwordTorneo.setText("Password Torneo" + T.getPasswordTorneo());
            System.out.println("Password Torneo:"+T.getPasswordTorneo());
            generaCodiceButton.setVisible(false);
            codicePartita.setVisible(false);
            passwordPartita.setVisible(false);

        } else if (contaPartite > 0) {
            impostazioniPreliminariTab.setDisable(true);
        } else {
            generaCodiceTorneoButton.setVisible(false);
            codiceTorneo.setVisible(false);
            passwordTorneo.setVisible(false);
        }
    }



    // Pulsante crea partita
    public void impostaGioco(ActionEvent actionEvent) throws IOException {

        AudioManager.bottoneSuono();

        if (contaPartite == 4) {

            FXMLLoader impostaGioco = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(impostaGioco.load());
            stage.setScene(scene);
            stage.show();

        } else {


            // TODO CONTROLLARE STO CODICE

            ArrayList<IGiocatore> GiocatoriPartita = new ArrayList<>();

            if (getNumeroGiocatori() == 1) {
                System.out.println("Nome giocatore 1: " + getNomeGiocatore1());
                GiocatoriPartita.add(new Giocatore(getNomeGiocatore1()));
                T.aggiungiGiocatoreTorneo(new Giocatore(getNomeGiocatore1()));
            } else if (getNumeroGiocatori() == 2) {
                T.aggiungiGiocatoreTorneo(new Giocatore(getNomeGiocatore1()));
                T.aggiungiGiocatoreTorneo(new Giocatore(getNomeGiocatore2()));
                GiocatoriPartita.add(new Giocatore(getNomeGiocatore1()));
                GiocatoriPartita.add(new Giocatore(getNomeGiocatore2()));
            } else if (getNumeroGiocatori() == 3) {
                GiocatoriPartita.add(new Giocatore(getNomeGiocatore1()));
                GiocatoriPartita.add(new Giocatore(getNomeGiocatore2()));
                GiocatoriPartita.add(new Giocatore(getNomeGiocatore3()));
            } else if (getNumeroGiocatori() == 4) {
                GiocatoriPartita.add(new Giocatore(getNomeGiocatore1()));
                GiocatoriPartita.add(new Giocatore(getNomeGiocatore2()));
                GiocatoriPartita.add(new Giocatore(getNomeGiocatore3()));
                GiocatoriPartita.add(new Giocatore(getNomeGiocatore4()));
            } else {

            }


            switch (getNumeroBot()) {

                case 3:

                    if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                        GiocatoriPartita.add(new AdvancedBot(getA1()));
                        GiocatoriPartita.add(new AdvancedBot(getA2()));
                        GiocatoriPartita.add(new AdvancedBot(getA3()));
                    } else {
                        GiocatoriPartita.add(new EasyBot(getE1()));
                        GiocatoriPartita.add(new EasyBot(getE2()));
                        GiocatoriPartita.add(new EasyBot(getE3()));

                    }
                    break;
                case 4:

                    if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                        GiocatoriPartita.add(new AdvancedBot(getA1()));
                        GiocatoriPartita.add(new AdvancedBot(getA2()));
                        GiocatoriPartita.add(new AdvancedBot(getA3()));
                        GiocatoriPartita.add(new AdvancedBot(getA4()));
                    } else {
                        GiocatoriPartita.add(new EasyBot(getE1()));
                        GiocatoriPartita.add(new EasyBot(getE2()));
                        GiocatoriPartita.add(new EasyBot(getE3()));
                        GiocatoriPartita.add(new EasyBot(getE4()));

                    }
                    break;
                case 2:

                    if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                        GiocatoriPartita.add(new AdvancedBot(getA1()));
                        GiocatoriPartita.add(new AdvancedBot(getA2()));
                    } else {
                        GiocatoriPartita.add(new EasyBot(getE1()));
                        GiocatoriPartita.add(new EasyBot(getE2()));
                    }
                    break;
                case 1:
                    if (getDifficolta().equals("Difficile") && !getDifficolta().isEmpty()) {
                        GiocatoriPartita.add(new AdvancedBot(getA1()));
                    } else {
                        GiocatoriPartita.add(new EasyBot(getE1()));
                    }
                    break;
                default:
                    break;
            }

            //TODO IMPOSTARE DECISIONE DELLE VITE DEI PLAYER
            P.aggiungiListaGiocatori(GiocatoriPartita);
        //    T.aggiungiGiocatoreTorneo();
            ArrayList<Partita> Partite =new ArrayList<>();
            Partite.add(P);

            // creiamo il salvataggio della nuova partita
            FileManager.creaPartitaSuFile(P.getCodicePartita(), P.getPasswordPartita(), GiocatoriPartita, GameType.PARTITA, GameStatus.STARTED); // salviamo le informazioni dati


            // ti riporta al menu principale
            FXMLLoader impostaGioco = new FXMLLoader(Spacca.class.getResource("Torneo2.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(impostaGioco.load());
            stage.setScene(scene);
            stage.show();

            // Mi permette di creare un "oggetto del controller", in questo modo riesco a passare tutto al controller senza creare un nuovo oggetto della classe che provocherebbe due istanze aperte
            //   MainMenuController mmc = impostaGioco.getController();
            //  mmc.setPartitaClassicaController(this);
            //ShareData.getInstance().setTorneoController(this); // gli passo classe partitacontroller
            ShareData.getInstance().setPartita(this.P);
            //  ShareData.getInstance().setPassword(this.P); // non serve
        }


    }

}