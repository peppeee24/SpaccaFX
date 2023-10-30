package com.spaccafx.Controllers;

import com.spaccafx.Cards.Carta;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Spacca;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Objects;

public class TavoloController {


    @FXML
    Label nomeGiocatoreLabel, nomeBot1Label, nomeBot2Label, nomeBot3Label, cartaSpecialeLabel, effettoLabel, partitaNLabel, roundNLabel;

    @FXML
    ImageView bot1Space, bot2Space, bot3Space, humanPlayerSpace, centerSpace;

    @FXML
    ImageView life1BT1, life2BT1, life3BT1, life1BT2, life2BT2, life3BT2, life1BT3, life2BT3, life3BT3, life1PL, life2PL, life3PL, lifeGoldBT1, lifeGoldBT2, lifeGoldBT3, lifeGoldPL;

    @FXML
    ImageView mazziereBT1, mazziereBT2, mazziereBT3, mazzierePL; // corone

    @FXML
    ImageView diceBot1, diceBot2, diceBot3, dicePL, settingGame;

    @FXML
    Button bottoneLancio, bottoneBlue, bottoneRosso, bottonePassa;


    private PartitaClassicaController2 PC;

    private Partita partita;

    // inizializzo
    public void initialize()
    {
        inizializzaTavolo();
    }

    // costruttore
    public TavoloController() { // todo da rivedere
        ShareData sharedData = ShareData.getInstance();
        ShareData.getInstance().setTavoloController(this);
        this.PC = sharedData.getPartitaClassicaController();
        this.partita = sharedData.getPartita();
        this.partita.impostaTavoloController();
    }


    public void inizializzaTavolo()
    {
        nascondiCorone();
        nascondiBannerSpeciale();
        gestisciPulsanti(false, true ,true);
        nascondiDadi();

        // imposto nome giocatori
        nomeGiocatoreLabel.setText(PC.P.giocatori.get(0).getNome());
        nomeBot1Label.setText(PC.P.giocatori.get(1).getNome());
        nomeBot2Label.setText(PC.P.giocatori.get(2).getNome());
        nomeBot3Label.setText(PC.P.giocatori.get(3).getNome());

        aggiornaInfoUI();
    }



    // region #ACTION EVENT METHODS
    public void lancia(ActionEvent actionEvent)
    {
        partita.preStartGame();
        bottoneLancio.setVisible(false);
    }

    public void scambiaCarta(ActionEvent actionEvent) {
        partita.ScambiaCartaUI();
    } // all interno della partita faccio la mossa del giocatore attuale

    public void passaTurno(ActionEvent actionEvent) {
        partita.passaTurnoUI();
        //disableDice();
    } // passo nella partita il turno del player


    public void scambiaConMazzo(ActionEvent actionEvent)
    {
        partita.getCurrentGiocatore().setCarta(partita.mazzo.PescaCartaSenzaEffetto());
        updateCarteUI();
        pulsanteScambiaMazzo(false);

    }
    //endregion

    // region #METHODS
    public void impostaCoroneMazziereUI()
    {
        nascondiCorone();

        String mazziere = partita.getMazziereNome();

        if (nomeGiocatoreLabel.getText().equalsIgnoreCase(mazziere))
            mazzierePL.setVisible(true);
        else if (nomeBot1Label.getText().equalsIgnoreCase(mazziere))
            mazziereBT1.setVisible(true);
        else if (nomeBot2Label.getText().equalsIgnoreCase(mazziere))
            mazziereBT2.setVisible(true);
        else
            mazziereBT3.setVisible(true);
    }

    public void updateCarteUI() // new Method
    {
        String currentPlayerName = partita.giocatori.get(partita.getCurrentGiocatorePos()).getNome(); // giocatore a cui tocca
        Image back = new Image(Objects.requireNonNull(getClass().getResource("/Assets/Cards/back.png")).toString()); // carta back
        String playerName;

        for(IGiocatore giocatore : partita.giocatori)
        {
            playerName = giocatore.getNome();

            if(playerName.equalsIgnoreCase(nomeGiocatoreLabel.getText())) // se il nome equivale al primo
            {
                if(playerName.equalsIgnoreCase(currentPlayerName)) // se tocca a lui
                    humanPlayerSpace.setImage(giocatore.getCarta().getImmagineCarta());
                else // se non tocca a lui gli copro la carta
                    humanPlayerSpace.setImage(back);
            }
            else if(playerName.equalsIgnoreCase(nomeBot1Label.getText())) // se il nome equivale al secondo
            {
                if(playerName.equalsIgnoreCase(currentPlayerName)) // se tocca a lui
                    bot1Space.setImage(giocatore.getCarta().getImmagineCarta());
                else // se non tocca a lui gli copro la carta
                    bot1Space.setImage(back);
            }
            else if(playerName.equalsIgnoreCase(nomeBot2Label.getText())) // se il nome equivale al terzo
            {
                if(playerName.equalsIgnoreCase(currentPlayerName)) // se tocca a lui
                    bot2Space.setImage(giocatore.getCarta().getImmagineCarta());
                else // se non tocca a lui gli copro la carta
                    bot2Space.setImage(back);
            }
            else if(playerName.equalsIgnoreCase(nomeBot3Label.getText())) // se il nome equivale al quarto
            {
                if(playerName.equalsIgnoreCase(currentPlayerName)) // se tocca a lui
                    bot3Space.setImage(giocatore.getCarta().getImmagineCarta());
                else // se non tocca a lui gli copro la carta
                    bot3Space.setImage(back);
            }
        }
    }

    public void aggiornaInfoUI()
    {
        partitaNLabel.setText("ID_Partita: " + partita.getCodicePartita());
        roundNLabel.setText("Round: " + partita.getCurrentRound());
    }

    //endregion


    public void impostaDadiUI()
    {
        int valoreDado = 0;
        mostraDadi();

        for(IGiocatore giocatore : partita.giocatori)
        {
            if (giocatore.getNome().equalsIgnoreCase(nomeGiocatoreLabel.getText()))
            {
                valoreDado = giocatore.getValoreDado();
                Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                dicePL.setImage(myImage);
            }
            else if (giocatore.getNome().equalsIgnoreCase(nomeBot1Label.getText()))
            {
                valoreDado = giocatore.getValoreDado();
                Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                diceBot1.setImage(myImage);
            }
            else if (giocatore.getNome().equalsIgnoreCase(nomeBot2Label.getText()))
            {
                valoreDado = giocatore.getValoreDado();
                Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                diceBot2.setImage(myImage);
            }
            else if (giocatore.getNome().equalsIgnoreCase(nomeBot3Label.getText()))
            {
                valoreDado = giocatore.getValoreDado();
                Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                diceBot3.setImage(myImage);
            }
        }
    }


    // ../../Assets/Game/Environment/dice/dice1.png
    public void getCartaSpeciale() {
        cartaSpecialeLabel.setVisible(true);
        effettoLabel.setVisible(true);


    }




    public void mostraCartaSpeciale(String titolo, String effetto) {

        cartaSpecialeLabel.setVisible(true);
        cartaSpecialeLabel.setText(titolo);
        effettoLabel.setVisible(true);
        effettoLabel.setText(effetto);

        // Creare un thread che eseguirà i metodi dopo un ritardo
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(4000); // TODO CAPIRE COME FUNZIONANO I THREAD

                // Dormire per un certo numero di millisecondi (ad esempio, 2000 millisecondi o 2 secondi)


                // Eseguire i metodi successivi all'interno di Platform.runLater
                // Altri metodi da eseguire dopo il ritardo
                Platform.runLater(this::nascondiBannerSpeciale);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Avviare il thread
        thread.start(); // TODO SISTEMARE QUESTO CHE DUPLICA MESSAGGI

    }










    public void updateCartaCentraleMazzoUI()
    {
        Image back = new Image(Objects.requireNonNull(getClass().getResource("/Assets/Cards/back.PNG")).toString());
        centerSpace.setImage(back);
    }

    public void mostraMazzoCentrale(Carta c) {

        centerSpace.setImage(c.getImmagineCarta());


    }

    public void updateVitaUI()
    {
        for (int i = 0; i < partita.giocatori.size(); i++)
        {
            String nome = partita.giocatori.get(i).getNome();
            int vita = partita.giocatori.get(i).getVita();
            boolean vitaExtra = partita.giocatori.get(i).hasVitaExtra();

            if (nome.equalsIgnoreCase(nomeGiocatoreLabel.getText()))
            {
                lifeGoldPL.setVisible(false);

                if (vita == 3)
                {
                    life1PL.setVisible(true);
                    life2PL.setVisible(true);
                    life3PL.setVisible(true);

                }
                else if (vita == 2)
                {
                    life1PL.setVisible(true);
                    life2PL.setVisible(true);
                    life3PL.setVisible(false);
                }
                else if (vita == 1)
                {
                    life1PL.setVisible(true);
                    life2PL.setVisible(false);
                    life3PL.setVisible(false);
                }

                if(vitaExtra) // fa vedere vita extra
                {
                    lifeGoldPL.setVisible(true);
                }
            }

            if (nome.equalsIgnoreCase(nomeBot1Label.getText()))
            {
                lifeGoldBT1.setVisible(false);

                if (vita == 3)
                {
                    life1BT1.setVisible(true);
                    life2BT1.setVisible(true);
                    life3BT1.setVisible(true);
                }
                else if (vita == 2)
                {
                    life1BT1.setVisible(true);
                    life2BT1.setVisible(true);
                    life3BT1.setVisible(false);
                }
                else if (vita == 1)
                {
                    life1BT1.setVisible(true);
                    life2BT1.setVisible(false);
                    life3BT1.setVisible(false);
                }

                if(vitaExtra) // fa vedere vita extra
                {
                    lifeGoldBT1.setVisible(true);
                }
            }

            if (nome.equalsIgnoreCase(nomeBot2Label.getText()))
            {
                lifeGoldBT2.setVisible(false);

                if (vita == 3)
                {
                    life1BT2.setVisible(true);
                    life2BT2.setVisible(true);
                    life3BT2.setVisible(true);
                }
                else if (vita == 2)
                {
                    life1BT2.setVisible(true);
                    life2BT2.setVisible(true);
                    life3BT2.setVisible(false);
                }
                else if (vita == 1)
                {
                    life1BT2.setVisible(true);
                    life2BT2.setVisible(false);
                    life3BT2.setVisible(false);
                }

                if(vitaExtra) // fa vedere vita extra
                {
                    lifeGoldBT2.setVisible(true);
                }

            }

            if (nome.equalsIgnoreCase(nomeBot3Label.getText()))
            {
                lifeGoldBT3.setVisible(false);

                if (vita == 3)
                {
                    life1BT3.setVisible(true);
                    life2BT3.setVisible(true);
                    life3BT3.setVisible(true);
                }
                else if (vita == 2)
                {
                    life1BT3.setVisible(true);
                    life2BT3.setVisible(true);
                    life3BT3.setVisible(false);
                }
                else if (vita == 1)
                {
                    life1BT3.setVisible(true);
                    life2BT3.setVisible(false);
                    life3BT3.setVisible(false);
                }

                if(vitaExtra) // fa vedere vita extra
                {
                    lifeGoldBT3.setVisible(true);
                }
            }


        }
    }

    // TODO RIVEDERE
    public void HidePlayerUI(String player) {
        System.out.println("[UI] elimino: " + player);
        System.out.flush();
        if (nomeGiocatoreLabel.getText().equalsIgnoreCase(player)) {
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

        if (nomeBot1Label.getText().equalsIgnoreCase(player)) {
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

        if (nomeBot2Label.getText().equalsIgnoreCase(player)) {
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

        if (nomeBot3Label.getText().equalsIgnoreCase(player)) {
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

    public void EndGameUI() {
        bottonePassa.setVisible(false);
        bottoneRosso.setVisible(false);


        cartaSpecialeLabel.setVisible(true);
        cartaSpecialeLabel.setText("Partita finita");
        effettoLabel.setVisible(true);
        effettoLabel.setText("Vincitore: " + partita.getVincitore().getNome());
        nascondiCorone();
    }

    //region # OTHER METHODS

    public void gestisciPulsanti(boolean sMazzo, boolean sNormale, boolean passa)
    {
        bottoneBlue.setVisible(sMazzo);
        bottoneRosso.setVisible(sNormale);
        bottonePassa.setVisible(passa);
    }

    public void mostraCorone()
    {
        mazzierePL.setVisible(true);
        mazziereBT1.setVisible(true);
        mazziereBT2.setVisible(true);
        mazziereBT3.setVisible(true);
    }

    public void nascondiCorone()
    {
        mazzierePL.setVisible(false);
        mazziereBT1.setVisible(false);
        mazziereBT2.setVisible(false);
        mazziereBT3.setVisible(false);
    }

    public void nascondiBannerSpeciale()
    {
        cartaSpecialeLabel.setVisible(false);
        effettoLabel.setVisible(false);
    }

    public void nascondiDadi() {
        dicePL.setVisible(false);
        diceBot1.setVisible(false);
        diceBot2.setVisible(false);
        diceBot3.setVisible(false);
    }

    public void mostraDadi()
    {
        dicePL.setVisible(true);
        diceBot1.setVisible(true);
        diceBot2.setVisible(true);
        diceBot3.setVisible(true);
    }

    public void pulsanteScambiaMazzo(boolean isVisibile){this.bottoneBlue.setVisible(isVisibile);}
    //endregion



    public void startGameUI()
    {
        impostaCoroneMazziereUI();
        updateVitaUI();
        updateCartaCentraleMazzoUI();

        partita.iniziaNuovoRoundUI();
    }

    // TODO RIVEDERE DI BRUTTO ASSOLUTAMENTE E QUALUNQUEMENTE CHIU PILU P'TUTT
    public void updateTurnoUI()
    {

    }

    public void mostraCarta(int pos)
    {
        IGiocatore currentGiocatore = partita.giocatori.get(pos);
        String playerName = currentGiocatore.getNome();

        /*boolean isCurrentPlayer = playerName.equalsIgnoreCase(nomeGiocatoreLabel.getText()) && playerName.equalsIgnoreCase(partita.giocatori.get(partita.getCurrentGiocatorePos()).getNome());
        boolean isCurrentBot1 = playerName.equalsIgnoreCase(nomeBot1Label.getText()) && playerName.equalsIgnoreCase(partita.giocatori.get(partita.getCurrentGiocatorePos()).getNome());
        boolean isCurrentBot2 = playerName.equalsIgnoreCase(nomeBot2Label.getText()) && playerName.equalsIgnoreCase(partita.giocatori.get(partita.getCurrentGiocatorePos()).getNome());
        boolean isCurrentBot3 = playerName.equalsIgnoreCase(nomeBot3Label.getText()) && playerName.equalsIgnoreCase(partita.giocatori.get(partita.getCurrentGiocatorePos()).getNome());
         */

        if (playerName.equalsIgnoreCase(nomeGiocatoreLabel.getText()))
        {
            humanPlayerSpace.setImage(currentGiocatore.getCarta().getImmagineCarta()); // player1

        } else if (playerName.equalsIgnoreCase(nomeBot1Label.getText())) {
            bot1Space.setImage(currentGiocatore.getCarta().getImmagineCarta());

        } else if (playerName.equalsIgnoreCase(nomeBot2Label.getText())) {
            bot2Space.setImage(currentGiocatore.getCarta().getImmagineCarta());
        } else if (playerName.equalsIgnoreCase(nomeBot3Label.getText())) {
            bot3Space.setImage(currentGiocatore.getCarta().getImmagineCarta());
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

    public void roll(int valoreDado, int pos) {
//showDice();
        Thread thread = new Thread() {
            public void run() {
                System.out.println("Thread Running");
                try {
                    for (int i = 0; i < 15; i++) {
                        // numeroDado=valoreDado;
                        System.out.println("Valore dato in roll:" + valoreDado);

                        Image nuovoLancio = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());

                        if (nomeGiocatoreLabel.getText().equalsIgnoreCase(partita.giocatori.get(pos).getNome())) {
                            //partita.giocatori.get(i).getCarta().getImmagineCarta();
                            dicePL.setImage(nuovoLancio);

                        } else if (nomeBot1Label.getText().equalsIgnoreCase(partita.giocatori.get(pos).getNome())) {

                            diceBot1.setImage(nuovoLancio);

                        } else if (nomeBot2Label.getText().equalsIgnoreCase(partita.giocatori.get(pos).getNome())) {
                            diceBot2.setImage(nuovoLancio);

                        } else {
                            diceBot3.setImage(nuovoLancio);

                        }


                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


    }

    public void rollLite(int valoreDado, int posPlayer)
    {
        String currentPlayer = partita.giocatori.get(posPlayer).getNome();

        if (nomeGiocatoreLabel.getText().equalsIgnoreCase(currentPlayer))
        {
            Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".PNG").toString());
            dicePL.setImage(myImage);
            dicePL.setVisible(true);
            diceBot1.setVisible(false);
            diceBot2.setVisible(false);
            diceBot3.setVisible(false);
        }

        if (nomeBot1Label.getText().equalsIgnoreCase(currentPlayer))
        {
            Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".PNG").toString());
            diceBot1.setImage(myImage);
            dicePL.setVisible(false);
            diceBot1.setVisible(true);
            diceBot2.setVisible(false);
            diceBot3.setVisible(false);
        }

        if (nomeBot2Label.getText().equalsIgnoreCase(currentPlayer))
        {
            Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".PNG").toString());
            diceBot2.setImage(myImage);
            dicePL.setVisible(false);
            diceBot1.setVisible(false);
            diceBot2.setVisible(true);
            diceBot3.setVisible(false);
        }

        if (nomeBot3Label.getText().equalsIgnoreCase(currentPlayer))
        {
            Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".PNG").toString());
            diceBot3.setImage(myImage);
            dicePL.setVisible(false);
            diceBot1.setVisible(false);
            diceBot2.setVisible(false);
            diceBot3.setVisible(true);
        }

    }

    public void rollTotal(int valoreDado) {
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

    // TODO aggiungere piccolo terminale il basso per alcune informazioni
}

