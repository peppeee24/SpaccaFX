package com.spaccafx.Controllers;

import com.spaccafx.Cards.Carta;
import com.spaccafx.Enums.RuoloGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;
import com.spaccafx.Spacca;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TavoloController {


    @FXML
    Label nomeGiocatoreLabel, nomeBot1Label, nomeBot2Label, nomeBot3Label, cartaSpecialeLabel, effettoLabel, partitaNLabel, roundNLabel;

    @FXML
    ImageView bot1Space, bot2Space, bot3Space, humanPlayerSpace, centerSpace;

    @FXML
    ImageView life1BT1, life2BT1, life3BT1, life1BT2, life2BT2, life3BT2, life1BT3, life2BT3, life3BT3, life1PL, life2PL, life3PL, lifeGoldBT1, lifeGoldBT2, lifeGoldBT3, lifeGoldPL;

    @FXML
    ImageView mazziereBT1, mazziereBT2, mazziereBT3, mazzierePL;

    @FXML
    ImageView diceBot1, diceBot2, diceBot3, dicePL, settingGame;

    @FXML
    Button bottoneLancio, bottoneBlue, bottoneRosso, bottonePassa;


    private PartitaClassicaController2 PC;

    private Partita partita;

    // inizializzo
    public void initialize() {
        setLableTable();
        partitaNLabel.setText("Partita n: " + partita.getCodicePartita());
        roundNLabel.setText("Round n: " + partita.getCurrentRound());


    }

    // costruttore
    public TavoloController() { // todo da rivedere
        ShareData sharedData = ShareData.getInstance();
        ShareData.getInstance().setTavoloController(this);
        this.PC = sharedData.getPartitaClassicaController();
        this.partita = sharedData.getPartita();
        this.partita.impostaTavoloController();
    }


    public void setLableTable()
    {
        nascondiCartaSpeciale();
        hideScambiaBlu();
        hideLifeGold();
        disableCrown();
        disableDice();
        //partita.StampaInfoGiocatori();

        nomeGiocatoreLabel.setText(PC.P.giocatori.get(0).getNome());
        nomeBot1Label.setText(PC.P.giocatori.get(1).getNome());
        nomeBot2Label.setText(PC.P.giocatori.get(2).getNome());
        nomeBot3Label.setText(PC.P.giocatori.get(3).getNome());


    }

    public void lancia(ActionEvent actionEvent) {
        partita.preStartGame();

        bottoneLancio.setVisible(false);
        //impostaDadi();

    }

    public void showScambiaRosso() {
        bottoneRosso.setVisible(true);
    }

    public void showScambiaBlu(boolean sMazzo, boolean sNormale, boolean passa)
    {
        bottoneBlue.setVisible(sMazzo);
        bottoneRosso.setVisible(sNormale);
        bottonePassa.setVisible(passa);

    }

    public void hideLifeGold(){
        lifeGoldPL.setVisible(false);
        lifeGoldBT1.setVisible(false);
        lifeGoldBT2.setVisible(false);
        lifeGoldBT3.setVisible(false);
    }

    public void hideScambiaRosso() {
        bottoneRosso.setVisible(false);
    }

    public void hideScambiaBlu() {bottoneBlue.setVisible(false);}

    public void scambiaConMazzo(ActionEvent actionEvent)
    {
       partita.giocatori.get(partita.getCurrentGiocatorePos()).setCarta(partita.mazzo.PescaCarta());
       setCartaTavolo();
       showScambiaBlu(false, true, true);
    }




    public void impostaDadi() {
        int valoreDado = 0;
        showDice();

        //ImageView ninni = new ImageView("../../Assets/Game/Environment/dice/dice" + valoreDado + ".PNG");
        // dicePL.setImage(ninni.getImage());
        for (int i = 0; i < partita.giocatori.size(); i++) {

            if (partita.giocatori.get(i).getNome().equalsIgnoreCase(nomeGiocatoreLabel.getText())) {
                valoreDado = partita.giocatori.get(i).getValoreDado();
                //  System.out.println("Valore del dado"+valoreDado);
                Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                // Image myImage = new Image(getClass().getResourceAsStream("dice/dice" + valoreDado + ".png"));
                dicePL.setImage(myImage);
                //   roll1(valoreDado);


            } else if (partita.giocatori.get(i).getNome().equalsIgnoreCase(nomeBot1Label.getText())) {
                valoreDado = partita.giocatori.get(i).getValoreDado();
                Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                diceBot1.setImage(myImage);
                // roll2(valoreDado);
            } else if (partita.giocatori.get(i).getNome().equalsIgnoreCase(nomeBot2Label.getText())) {
                valoreDado = partita.giocatori.get(i).getValoreDado();
                Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                diceBot2.setImage(myImage);
                //  roll3(valoreDado);
            } else if (partita.giocatori.get(i).getNome().equalsIgnoreCase(nomeBot3Label.getText())) {
                valoreDado = partita.giocatori.get(i).getValoreDado();
                Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                diceBot3.setImage(myImage);
                //  roll4(valoreDado);
            }
        }
        //gestisciMazziere();

    }


    // ../../Assets/Game/Environment/dice/dice1.png
    public void getCartaSpeciale() {
        cartaSpecialeLabel.setVisible(true);
        effettoLabel.setVisible(true);


    }

    public void nascondiCartaSpeciale() {
        cartaSpecialeLabel.setVisible(false);
        effettoLabel.setVisible(false);

    }

    public void mostraCartaSpeciale(String titolo, String effetto)
    {
        cartaSpecialeLabel.setVisible(true);
        cartaSpecialeLabel.setText(titolo);
        effettoLabel.setVisible(true);
        effettoLabel.setText(effetto);

        // Creare un thread che eseguirà i metodi dopo un ritardo
        Thread thread = new Thread(() -> {
            try {
                // Dormire per un certo numero di millisecondi (ad esempio, 2000 millisecondi o 2 secondi)
                Thread.sleep(4000);

                // Eseguire i metodi successivi all'interno di Platform.runLater
                // Altri metodi da eseguire dopo il ritardo
                Platform.runLater(this::nascondiCartaSpeciale);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Avviare il thread
        thread.start(); // TODO SISTEMARE QUESTO CHE DUPLICA MESSAGGI

    }


    public void disableDice() {
        dicePL.setVisible(false);
        diceBot1.setVisible(false);
        diceBot2.setVisible(false);
        diceBot3.setVisible(false);
    }

    public void showDice() {
        dicePL.setVisible(true);
        diceBot1.setVisible(true);
        diceBot2.setVisible(true);
        diceBot3.setVisible(true);
    }


    public void disableCrown() {
        mazzierePL.setVisible(false);
        mazziereBT1.setVisible(false);
        mazziereBT2.setVisible(false);
        mazziereBT3.setVisible(false);
    }


    public void scambiaCarta(ActionEvent actionEvent) {
        partita.ScambiaCartaUI(partita.getCurrentGiocatorePos());
    } // all interno della partita faccio la mossa del giocatore attuale

    public void passaTurno(ActionEvent actionEvent) {
        partita.passaTurnoUI();
    } // passo nella partita il turno del player

    public void impostazioneInizialeCarte() {
        Image back = new Image(getClass().getResource("/Assets/Cards/back.PNG").toString());
        centerSpace.setImage(back);

        // TODO continuare implemntazione
        //    setCartaTavolo();
/*

       bot1Space.setImage(back); // Cambia il percorso all'immagine desiderata
        bot2Space.setImage(back); // Cambia il percorso all'immagine desiderata
        bot3Space.setImage(back); // Cambia il percorso all'immagine desiderata


 */
    }

    public void gestisciVite() {

        for (int i = 0; i < partita.giocatori.size(); i++)
        {
            String nome = partita.giocatori.get(i).getNome();
            int vita = partita.giocatori.get(i).getVita();
            //System.out.println(vita);
            if (nome.equalsIgnoreCase(nomeGiocatoreLabel.getText())) {
                if (vita == 3) {

                    life1PL.setVisible(true);
                    life2PL.setVisible(true);
                    life3PL.setVisible(true);
                } else if (vita == 2) {

                    life1PL.setVisible(true);
                    life2PL.setVisible(true);
                    life3PL.setVisible(false);
                } else if (vita == 1) {

                    life1PL.setVisible(true);
                    life2PL.setVisible(false);
                    life3PL.setVisible(false);
                }
            }


            if (nome.equalsIgnoreCase(nomeBot1Label.getText())) {
                if (vita == 3) {
                    life1BT1.setVisible(true);
                    life2BT1.setVisible(true);
                    life3BT1.setVisible(true);
                } else if (vita == 2) {
                    life1BT1.setVisible(true);
                    life2BT1.setVisible(true);
                    life3BT1.setVisible(false);
                } else if (vita == 1) {
                    life1BT1.setVisible(true);
                    life2BT1.setVisible(false);
                    life3BT1.setVisible(false);
                }
            }

            if (nome.equalsIgnoreCase(nomeBot2Label.getText())) {
                if (vita == 3) {
                    life1BT2.setVisible(true);
                    life2BT2.setVisible(true);
                    life3BT2.setVisible(true);
                } else if (vita == 2) {
                    life1BT2.setVisible(true);
                    life2BT2.setVisible(true);
                    life3BT2.setVisible(false);
                } else if (vita == 1) {
                    life1BT2.setVisible(true);
                    life2BT2.setVisible(false);
                    life3BT2.setVisible(false);
                }
            }

            if (nome.equalsIgnoreCase(nomeBot3Label.getText())) {
                if (vita == 3) {
                    life1BT3.setVisible(true);
                    life2BT3.setVisible(true);
                    life3BT3.setVisible(true);
                } else if (vita == 2) {
                    life1BT3.setVisible(true);
                    life2BT3.setVisible(true);
                    life3BT3.setVisible(false);
                } else if (vita == 1) {
                    life1BT3.setVisible(true);
                    life2BT3.setVisible(false);
                    life3BT3.setVisible(false);
                }
            }


        }
    }

    public void HidePlayerUI(String player)
    {
        System.out.println("[UI] elimino: " + player);
        System.out.flush();
        if (nomeGiocatoreLabel.getText().equalsIgnoreCase(player))
        {
            System.out.println("Giocatore 0 morto!");
            // TODO mettere carta gameover
            life1PL.setVisible(false);
            life2PL.setVisible(false);
            life3PL.setVisible(false);
            dicePL.setVisible(false);
            // humanPlayerSpace.setVisible(false);
            String nomeMorto = nomeGiocatoreLabel.getText();
            nomeGiocatoreLabel.setText(nomeMorto + " è morto");
            Image back = new Image(getClass().getResource("/Assets/Cards/morte.png").toString());
            humanPlayerSpace.setImage(back);
        }

        if(nomeBot1Label.getText().equalsIgnoreCase(player))
        {
            // TODO mettere carta gameover
            System.out.println("Giocatore 1 morto!");
            life1BT1.setVisible(false);
            life2BT1.setVisible(false);
            life3BT1.setVisible(false);
            // bot1Space.setVisible(false);
            diceBot1.setVisible(false);
            Image back = new Image(getClass().getResource("/Assets/Cards/morte.png").toString());
            bot1Space.setImage(back);
            String nomeMorto = nomeBot1Label.getText();
            nomeBot1Label.setText(nomeMorto + " è morto");
        }

        if(nomeBot2Label.getText().equalsIgnoreCase(player))
        {
            // TODO leggere l'array dei morti
            System.out.println("Giocatore 2 morto!");
            life1BT2.setVisible(false);
            life2BT2.setVisible(false);
            life3BT2.setVisible(false);
            // bot2Space.setVisible(false);
            diceBot2.setVisible(false);
            Image back = new Image(getClass().getResource("/Assets/Cards/morte.png").toString());
            bot2Space.setImage(back);
            String nomeMorto = nomeBot2Label.getText();
            nomeBot2Label.setText(nomeMorto + " è morto");
        }

        if(nomeBot3Label.getText().equalsIgnoreCase(player))
        {
            // TODO leggere l'array dei morti
            System.out.println("Giocatore 3 morto!");
            life1BT3.setVisible(false);
            life2BT3.setVisible(false);
            life3BT3.setVisible(false);
            //  bot3Space.setVisible(false);
            diceBot3.setVisible(false);
            Image back = new Image(getClass().getResource("/Assets/Cards/morte.png").toString());
            bot3Space.setImage(back);
            String nomeMorto = nomeBot3Label.getText();
            nomeBot3Label.setText(nomeMorto + " è morto");
        }
    }

    public void EndGameUI()
    {
        bottonePassa.setVisible(false);
        bottoneRosso.setVisible(false);


        cartaSpecialeLabel.setVisible(true);
        cartaSpecialeLabel.setText("Partita finita");
        effettoLabel.setVisible(true);
        effettoLabel.setText("Vincitore: " + partita.getVincitore().getNome());

    }


    public void getCrown() {
        mazzierePL.setVisible(true);
        mazziereBT1.setVisible(true);
        mazziereBT2.setVisible(true);
        mazziereBT3.setVisible(true);
    }


    public void gestisciMazziere() {
        getCrown();
        String mazziere = partita.getMazziereNome();
        if (nomeGiocatoreLabel.getText().equalsIgnoreCase(mazziere)) {
            mazziereBT1.setVisible(false);
            mazziereBT2.setVisible(false);
            mazziereBT3.setVisible(false);
        } else if (nomeBot1Label.getText().equalsIgnoreCase(mazziere)) {
            mazzierePL.setVisible(false);
            mazziereBT2.setVisible(false);
            mazziereBT3.setVisible(false);
        } else if (nomeBot2Label.getText().equalsIgnoreCase(mazziere)) {
            mazzierePL.setVisible(false);
            mazziereBT1.setVisible(false);
            mazziereBT3.setVisible(false);
        } else {
            mazzierePL.setVisible(false);
            mazziereBT1.setVisible(false);
            mazziereBT2.setVisible(false);
        }
        this.gestisciVite();

        partita.distribuisciCarte();
        partita.runRoundUI();


        impostazioneInizialeCarte();
        setCartaTavolo();
    }


    public void setCartaTavolo() {
        // Image back = new Image(getClass().getResource("/Assets/Cards/back.PNG").toString());
        //   centerSpace.setImage(back);
        //Carta c;
        for (int i = 0; i < partita.giocatori.size(); i++) {
            if (nomeGiocatoreLabel.getText().equalsIgnoreCase(partita.giocatori.get(i).getNome())) {
                //partita.giocatori.get(i).getCarta().getImmagineCarta();
                humanPlayerSpace.setImage(partita.giocatori.get(i).getCarta().getImmagineCarta());

            } else if (nomeBot1Label.getText().equalsIgnoreCase(partita.giocatori.get(i).getNome())) {

                bot1Space.setImage(partita.giocatori.get(i).getCarta().getImmagineCarta());

            } else if (nomeBot2Label.getText().equalsIgnoreCase(partita.giocatori.get(i).getNome())) {
                bot2Space.setImage(partita.giocatori.get(i).getCarta().getImmagineCarta());

            } else {
                bot3Space.setImage(partita.giocatori.get(i).getCarta().getImmagineCarta());

            }
            //this.gestisciVite();
            impostazioneInizialeCarte();
        }
    }

    public void settingGame(MouseEvent mouseEvent) throws IOException {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("PlayerScreen.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }


    public void roll1(int valoreDado) {
//showDice();
        Thread thread = new Thread() {
            public void run() {
                System.out.println("Thread Running");
                try {
                    for (int i = 0; i < 15; i++) {
                        // numeroDado=valoreDado;
                        System.out.println("Valore dato in roll:" + valoreDado);
                        Image uno = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                        dicePL.setImage(uno);

                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


    }

    public void roll2(int valoreDado) {
//showDice();
        Thread thread = new Thread() {
            public void run() {
                System.out.println("Thread Running");
                try {
                    for (int i = 0; i < 15; i++) {
                        // numeroDado=valoreDado;
                        System.out.println("Valore dato in roll:" + valoreDado);

                        Image due = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                        diceBot1.setImage(due);

                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


    }

    public void roll3(int valoreDado) {
//showDice();
        Thread thread = new Thread() {
            public void run() {
                System.out.println("Thread Running");
                try {
                    for (int i = 0; i < 15; i++) {
                        // numeroDado=valoreDado;
                        System.out.println("Valore dato in roll:" + valoreDado);

                        Image tre = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                        diceBot2.setImage(tre);

                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


    }

    public void roll4(int valoreDado) {
//showDice();
        Thread thread = new Thread() {
            public void run() {
                System.out.println("Thread Running");
                try {
                    for (int i = 0; i < 15; i++) {
                        // numeroDado=valoreDado;
                        System.out.println("Valore dato in roll:" + valoreDado);

                        Image quattro = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                        diceBot3.setImage(quattro);
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


    }

    public void roll(int valoreDado) {
//showDice();
        Thread thread = new Thread() {
            public void run() {
                System.out.println("Thread Running");
                try {
                    for (int i = 0; i < 15; i++) {
                        // numeroDado=valoreDado;
                        System.out.println("Valore dato in roll:" + valoreDado);

                        Image quattro = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                        diceBot3.setImage(quattro);
                        Image tre = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                        diceBot2.setImage(tre);
                        Image due = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                        diceBot1.setImage(due);
                        Image uno = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                        dicePL.setImage(uno);
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


    }
}

