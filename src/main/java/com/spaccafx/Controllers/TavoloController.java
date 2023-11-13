package com.spaccafx.Controllers;

import com.spaccafx.Cards.Carta;
import com.spaccafx.Cards.CartaImprevisto;
import com.spaccafx.Enums.GameStatus;
import com.spaccafx.Files.AudioManager;
import com.spaccafx.Files.FileManager;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.Bot;
import com.spaccafx.Spacca;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class TavoloController
{
    // region #VARIABLES
    @FXML
    Label nomePlayer1, nomePlayer2, nomePlayer3, nomePlayer4; // nome giocatori

    @FXML
    Label partitaIdLabel, roundIdLabel; // general UI

    @FXML
    Label  popUpTitleLabel, popUpTextLabel; // banner pop up panel

    @FXML
    Pane popUpPane;

    @FXML
    ImageView cartaPlayer1, cartaPlayer2, cartaPlayer3, cartaPlayer4, cartaCentrale; // carte giocatori

    @FXML
    ImageView life1Pl2, life2Pl2, life3Pl2, life1Pl3, life2Pl3, life3Pl3, life1Pl4, life2Pl4, life3Pl4, life1Pl1, life2Pl1, life3Pl1, lifeGoldPl2, lifeGoldPl3, lifeGoldPl4, lifeGoldPl1;

    @FXML
    ImageView mazzierePlayer1Icon, mazzierePlayer2Icon, mazzierePlayer3Icon, mazzierePlayer4Icon; // icone del mazziere

    @FXML
    ImageView dicePl1, dicePl2, dicePl3, dicePl4, exitGame; // dadi

    @FXML
    Button bottoneStart, bottoneEffetto, bottoneScambia, bottonePassa; // bottoni partita


    //private PartitaClassicaController2 PC;

    private Partita partita;
    private int codicePartita;

    //endregion

    public void initialize(){inizializzazioneTavolo(); }// tolgo tutto

    // costruttore
    public TavoloController()
    {
        ShareData.getInstance().setTavoloController(this);
    }

    // prima METODO che viene eseguita dopo che fai l accesso
    public void inizializzaClassePartita(int codicePartita)
    {
        // gli devo passare il codice che mando quando clicco sul bottone
        setCodicePartita(codicePartita);
        System.out.println("Codice della partita attuale: " + codicePartita);

        this.partita = FileManager.leggiPartitaDaFile(codicePartita); // prendiamo la partita (codice, passw, giocatori, stato)
        this.partita.impostaTavoloController();
        inizializzaNomiPlayer(); // aggiorno UI e inizializzo nomi player VIVI E MORTI

        //TODO RIMUOVERE DAL MAZZO CHE SI CREA LE CARTE CHE HANNO I GIOCATORI, ALTRIMENTI RISCHIO DUPLICAZIONE!!

        System.out.println("Codice della partita: " + partita.getCodicePartita());

        preInizializzazioneTavolo(this.partita.getPartitaStatus()); // inizializzo il tavolo in base alle mie esigenze
    }

    private void setCodicePartita(int codicePartita){this.codicePartita=codicePartita;}

    public void preInizializzazioneTavolo(GameStatus gameStatus)
    {
        switch (gameStatus)
        {
            case STARTED:
                            System.out.println("Il gioco sta per iniziare ora!");
                            bottoneStart.setVisible(true);
                            break;

            // mettiamo alert dicendo che il gioco sta per essere ripreso
            case STOPPED:   AudioManager.erroreSuono();
                            bottoneStart.setVisible(false);
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("RIPRESA MATCH!");
                            alert.setContentText("Vuoi veramente riprendere il gioco?");
                            Optional<ButtonType> result = alert.showAndWait();

                            if(result.isPresent() && result.get() == ButtonType.OK) // se accetta di riprendere
                            {
                                System.out.println("Stai per riprendere il gioco");
                                System.out.println("Current Giocatore: " + partita.getCurrentGiocatore().getNome()
                                        + " in posizione: " + partita.getCurrentGiocatorePos());

                                partita.StampaInfoGiocatori();

                                riprendiGioco(this.partita);
                            }
                            else
                            {
                                // lo riporto fuori e non carico nulla
                            }
                            break;

            case PLAYING:
            case ENDED:
                            break;

            default:
        }
    }

    private void inizializzazioneTavolo()
    {
        nascondiCorone();
        nascondiBannerAttesa();
        gestisciPulsanti(false, false ,false);
        nascondiDadi();
    }

    private void inizializzaNomiPlayer()
    {
        // imposto nome giocatori da prendere dal file
        // todo attenzione nel caso i player siano morti, fare il controllo non sempre da riempire con 4 nomi
        nomePlayer1.setText(partita.giocatori.get(0).getNome()); // prendo il giocatore
        nomePlayer2.setText(partita.giocatori.get(1).getNome());
        nomePlayer3.setText(partita.giocatori.get(2).getNome());
        nomePlayer4.setText(partita.giocatori.get(3).getNome());

        aggiornaInfoUI();
    }



    // region #ACTION EVENT METHODS
    public void start(ActionEvent actionEvent)
    {
        AudioManager.bottoneSuono();
        // attendi lancio i dadi..
        gestisciPulsanti(false, true, true);
        partita.preStartGame();
        bottoneStart.setVisible(false);
    }

    public void scambiaCarta(ActionEvent actionEvent) {
        AudioManager.bottoneSuono();
        partita.ScambiaCartaUI();
    } // all interno della partita faccio la mossa del giocatore attuale

    public void passaTurno(ActionEvent actionEvent) {
        AudioManager.bottoneSuono();
        partita.passaTurnoUI();
        //disableDice();
    } // passo nella partita il turno del player


    public void scambiaConMazzo(ActionEvent actionEvent)
    {
        AudioManager.bottoneSuono();
        partita.getCurrentGiocatore().setCarta(partita.mazzo.PescaCartaSenzaEffetto());
        updateCarteUI();
        pulsanteScambiaMazzo(false);

    }

    // per uscire dalla partita
    public void exitGame(MouseEvent mouseEvent) throws IOException
    {
        partita.SavePartita(mouseEvent);
    }
    //endregion

    // region #METHODS

    public void riprendiGioco(Partita partita)
    {
        this.updateVitaUI(); // aggiorna tutte le vite dei player
        this.updateCarteUI(); // aggiorna tutte le carte dei giocatori e scopre quella del giocatore a cui tocca
        this.updateCartaCentraleMazzoUI(); // imposta la carta centrale
        this.impostaCoroneMazziereUI(); // imposta chi e il mazziere

        partita.setPartitaStatus(GameStatus.PLAYING); // reimposto il gioco allo stato playing
        partita.riprendiPartita(partita.getCurrentGiocatorePos());
    }

    public void caricaMenuUI(MouseEvent mouseEvent) throws IOException
    {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("SelectionMenuGiocatore.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }

    public void impostaCoroneMazziereUI()
    {
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
        String currentPlayerName = partita.giocatori.get(partita.getCurrentGiocatorePos()).getNome(); // giocatore a cui tocca
        System.out.println("[UpdateUI] - tocca la giocatore: " + currentPlayerName);
        Image back = new Image(Objects.requireNonNull(getClass().getResource("/Assets/Cards/back.png")).toString()); // carta back
        String playerName;

        for(IGiocatore giocatore : partita.giocatori)
        {
            playerName = giocatore.getNome();

            if(playerName.equalsIgnoreCase(nomePlayer1.getText())) // se il nome equivale al primo
            {
                if(playerName.equalsIgnoreCase(currentPlayerName)) // se tocca a lui
                    cartaPlayer1.setImage(giocatore.getCarta().getImmagineCarta());
                else // se non tocca a lui gli copro la carta
                    cartaPlayer1.setImage(back);
            }
            else if(playerName.equalsIgnoreCase(nomePlayer2.getText())) // se il nome equivale al secondo
            {
                if(playerName.equalsIgnoreCase(currentPlayerName)) // se tocca a lui
                    cartaPlayer2.setImage(giocatore.getCarta().getImmagineCarta());
                else // se non tocca a lui gli copro la carta
                    cartaPlayer2.setImage(back);
            }
            else if(playerName.equalsIgnoreCase(nomePlayer3.getText())) // se il nome equivale al terzo
            {
                if(playerName.equalsIgnoreCase(currentPlayerName)) // se tocca a lui
                    cartaPlayer3.setImage(giocatore.getCarta().getImmagineCarta());
                else // se non tocca a lui gli copro la carta
                    cartaPlayer3.setImage(back);
            }
            else if(playerName.equalsIgnoreCase(nomePlayer4.getText())) // se il nome equivale al quarto
            {
                if(playerName.equalsIgnoreCase(currentPlayerName)) // se tocca a lui
                    cartaPlayer4.setImage(giocatore.getCarta().getImmagineCarta());
                else // se non tocca a lui gli copro la carta
                    cartaPlayer4.setImage(back);
            }
        }
    }

    public void aggiornaInfoUI()
    {
        partitaIdLabel.setText("ID_PARTITA: " + partita.getCodicePartita());
        roundIdLabel.setText("ROUND " + partita.getCurrentRound());
    }

    public void mostraTutteCarteUI()
    {
        AudioManager.giraCarteSuono();
        String playerName;
        for(IGiocatore giocatore : partita.giocatori)
        {
            playerName = giocatore.getNome();

            if(playerName.equalsIgnoreCase(nomePlayer1.getText())) // se il nome equivale al primo
                cartaPlayer1.setImage(giocatore.getCarta().getImmagineCarta());
            else if(playerName.equalsIgnoreCase(nomePlayer2.getText())) // se il nome equivale al secondo
                cartaPlayer2.setImage(giocatore.getCarta().getImmagineCarta());
            else if(playerName.equalsIgnoreCase(nomePlayer3.getText())) // se il nome equivale al terzo
                cartaPlayer3.setImage(giocatore.getCarta().getImmagineCarta());
            else // se il nome equivale al quarto
                cartaPlayer4.setImage(giocatore.getCarta().getImmagineCarta());
        }
    }


    //endregion


    public void impostaDadiUI()
    {
        AudioManager.dadoSuono();
        int valoreDado = 0;

        for(IGiocatore giocatore : partita.giocatori)
        {
            if (giocatore.getNome().equalsIgnoreCase(nomePlayer1.getText()))
            {
                valoreDado = giocatore.getValoreDado();
                Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                dicePl1.setVisible(true);
                dicePl1.setImage(myImage);
            }
            else if (giocatore.getNome().equalsIgnoreCase(nomePlayer2.getText()))
            {
                valoreDado = giocatore.getValoreDado();
                Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                dicePl2.setVisible(true);
                dicePl2.setImage(myImage);
            }
            else if (giocatore.getNome().equalsIgnoreCase(nomePlayer3.getText()))
            {
                valoreDado = giocatore.getValoreDado();
                Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
                dicePl3.setVisible(true);
                dicePl3.setImage(myImage);
            }
            else if (giocatore.getNome().equalsIgnoreCase(nomePlayer4.getText()))
            {
                valoreDado = giocatore.getValoreDado();
                Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".png").toString());
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




    public void mostraBannerAttesa(String titolo, String effetto)
    {
        popUpPane.setVisible(true);
        popUpPane.setDisable(false);

        popUpTitleLabel.setVisible(true);
        popUpTitleLabel.setText(titolo);
        popUpTextLabel.setVisible(true);
        popUpTextLabel.setText(effetto);
    }




    public void updateCartaCentraleMazzoUI()
    {
        Image back = new Image(Objects.requireNonNull(getClass().getResource("/Assets/Cards/back.PNG")).toString());
        cartaCentrale.setImage(back);
    }

    public void mostraMazzoCentrale(Carta c)
    {
        cartaCentrale.setImage(c.getImmagineCarta());
    }

    public void updateVitaUI()
    {
        for (int i = 0; i < partita.giocatori.size(); i++)
        {
            String nome = partita.giocatori.get(i).getNome();
            int vita = partita.giocatori.get(i).getVita();
            boolean vitaExtra = partita.giocatori.get(i).hasVitaExtra();

            if (nome.equalsIgnoreCase(nomePlayer1.getText()))
            {
                lifeGoldPl1.setVisible(false);

                if (vita == 3)
                {
                    life1Pl1.setVisible(true);
                    life2Pl1.setVisible(true);
                    life3Pl1.setVisible(true);

                }
                else if (vita == 2)
                {
                    life1Pl1.setVisible(true);
                    life2Pl1.setVisible(true);
                    life3Pl1.setVisible(false);
                }
                else if (vita == 1)
                {
                    life1Pl1.setVisible(true);
                    life2Pl1.setVisible(false);
                    life3Pl1.setVisible(false);
                }

                if(vitaExtra) // fa vedere vita extra
                {
                    lifeGoldPl1.setVisible(true);
                }
            }

            if (nome.equalsIgnoreCase(nomePlayer2.getText()))
            {
                lifeGoldPl2.setVisible(false);

                if (vita == 3)
                {
                    life1Pl2.setVisible(true);
                    life2Pl2.setVisible(true);
                    life3Pl2.setVisible(true);
                }
                else if (vita == 2)
                {
                    life1Pl2.setVisible(true);
                    life2Pl2.setVisible(true);
                    life3Pl2.setVisible(false);
                }
                else if (vita == 1)
                {
                    life1Pl2.setVisible(true);
                    life2Pl2.setVisible(false);
                    life3Pl2.setVisible(false);
                }

                if(vitaExtra) // fa vedere vita extra
                {
                    lifeGoldPl2.setVisible(true);
                }
            }

            if (nome.equalsIgnoreCase(nomePlayer3.getText()))
            {
                lifeGoldPl3.setVisible(false);

                if (vita == 3)
                {
                    life1Pl3.setVisible(true);
                    life2Pl3.setVisible(true);
                    life3Pl3.setVisible(true);
                }
                else if (vita == 2)
                {
                    life1Pl3.setVisible(true);
                    life2Pl3.setVisible(true);
                    life3Pl3.setVisible(false);
                }
                else if (vita == 1)
                {
                    life1Pl3.setVisible(true);
                    life2Pl3.setVisible(false);
                    life3Pl3.setVisible(false);
                }

                if(vitaExtra) // fa vedere vita extra
                {
                    lifeGoldPl3.setVisible(true);
                }

            }

            if (nome.equalsIgnoreCase(nomePlayer4.getText()))
            {
                lifeGoldPl4.setVisible(false);

                if (vita == 3)
                {
                    life1Pl4.setVisible(true);
                    life2Pl4.setVisible(true);
                    life3Pl4.setVisible(true);
                }
                else if (vita == 2)
                {
                    life1Pl4.setVisible(true);
                    life2Pl4.setVisible(true);
                    life3Pl4.setVisible(false);
                }
                else if (vita == 1)
                {
                    life1Pl4.setVisible(true);
                    life2Pl4.setVisible(false);
                    life3Pl4.setVisible(false);
                }

                if(vitaExtra) // fa vedere vita extra
                {
                    lifeGoldPl4.setVisible(true);
                }
            }


        }
    }

    public void HidePlayerUI(String player)
    {
        // TODO DEVO NASCONDERE TUTTI I GIOCATORI CHE HANNO IL RUOLO MORTO!

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
            Image back = new Image(getClass().getResource("/Assets/Cards/morte.png").toString());
            cartaPlayer1.setImage(back);
        }

        if (nomePlayer2.getText().equalsIgnoreCase(player)) {
            System.out.println("Giocatore 1 morto!");
            life1Pl2.setVisible(false);
            life2Pl2.setVisible(false);
            life3Pl3.setVisible(false);
            lifeGoldPl2.setVisible(false);
            dicePl2.setVisible(false);
            Image back = new Image(getClass().getResource("/Assets/Cards/morte.png").toString());
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
            Image back = new Image(getClass().getResource("/Assets/Cards/morte.png").toString());
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
            Image back = new Image(getClass().getResource("/Assets/Cards/morte.png").toString());
            cartaPlayer4.setImage(back);
            String nomeMorto = nomePlayer4.getText();
            nomePlayer4.setText(nomeMorto + " è morto");
        }
    }

    public void EndGameUI()
    {
        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() ->
                {
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
                });

                Thread.sleep(3000);

                Platform.runLater(() ->
                {
                    popUpPane.setVisible(true);
                    popUpPane.setDisable(false);

                    popUpTitleLabel.setVisible(true);
                    popUpTitleLabel.setText("LOBBY...");
                    popUpTitleLabel.setTextFill(Color.YELLOW);

                    popUpTextLabel.setVisible(true);
                    popUpTextLabel.setText("Stai per ritornare al menu principale...");
                });

                Thread.sleep(1500);

                /*Platform.runLater(() ->
                {
                    FXMLLoader menu = new FXMLLoader(Spacca.class.getResource("SelectionMenuGiocatore.fxml"));
                    try {
                        Scene scene = new Scene(menu.load());
                        stage.setScene(scene);
                        stage.setTitle("Alpha Build SpaccaFX");
                        stage.setResizable(false);
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                 */

                // TODO METTERE CHE DOPO UN ATTESA TI RIPORTA AL MENU PRINCIPALE!!

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();

    }

    //region # OTHER METHODS

    public void gestisciPulsanti(boolean sMazzo, boolean sNormale, boolean passa)
    {
        bottoneEffetto.setVisible(sMazzo);
        bottoneScambia.setVisible(sNormale);
        bottonePassa.setVisible(passa);
    }

   public void gestisciPulsanteScambio(boolean sNormale){
       bottoneScambia.setVisible(sNormale);
   }
    public void mostraCorone()
    {
        mazzierePlayer1Icon.setVisible(true);
        mazzierePlayer2Icon.setVisible(true);
        mazzierePlayer3Icon.setVisible(true);
        mazzierePlayer4Icon.setVisible(true);

    }

    public void nascondiCorone()
    {
        mazzierePlayer1Icon.setVisible(false);
        mazzierePlayer2Icon.setVisible(false);
        mazzierePlayer3Icon.setVisible(false);
        mazzierePlayer4Icon.setVisible(false);
    }

    public void nascondiBannerAttesa()
    {
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

    public void mostraDadi()
    {
        dicePl1.setVisible(true);
        dicePl2.setVisible(true);
        dicePl3.setVisible(true);
        dicePl4.setVisible(true);
    }

    public void pulsanteScambiaMazzo(boolean isVisibile){this.bottoneEffetto.setVisible(isVisibile);}
    //endregion



    public void startGameUI()
    {
        impostaCoroneMazziereUI();
        updateVitaUI();
        updateCartaCentraleMazzoUI();

        partita.iniziaNuovoRoundUI();
    }

    public void mostraCarta(int pos)
    {
        IGiocatore currentGiocatore = partita.giocatori.get(pos);
        String playerName = currentGiocatore.getNome();

        if (playerName.equalsIgnoreCase(nomePlayer1.getText()))
        {
            cartaPlayer1.setImage(currentGiocatore.getCarta().getImmagineCarta()); // player1

        } else if (playerName.equalsIgnoreCase(nomePlayer2.getText())) {
            cartaPlayer2.setImage(currentGiocatore.getCarta().getImmagineCarta());

        } else if (playerName.equalsIgnoreCase(nomePlayer3.getText())) {
            cartaPlayer3.setImage(currentGiocatore.getCarta().getImmagineCarta());
        } else if (playerName.equalsIgnoreCase(nomePlayer4.getText())) {
            cartaPlayer4.setImage(currentGiocatore.getCarta().getImmagineCarta());
        }
    }







    public void rollLite(int valoreDado, int posPlayer)
    {
        AudioManager.dadoSuono();
        String currentPlayer = partita.giocatori.get(posPlayer).getNome();

        if (nomePlayer1.getText().equalsIgnoreCase(currentPlayer))
        {
            Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".PNG").toString());
            dicePl1.setImage(myImage);
            dicePl1.setVisible(true);
            dicePl2.setVisible(false);
            dicePl3.setVisible(false);
            dicePl4.setVisible(false);
        }

        if (nomePlayer2.getText().equalsIgnoreCase(currentPlayer))
        {
            Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".PNG").toString());
            dicePl2.setImage(myImage);
            dicePl1.setVisible(false);
            dicePl2.setVisible(true);
            dicePl3.setVisible(false);
            dicePl4.setVisible(false);
        }

        if (nomePlayer3.getText().equalsIgnoreCase(currentPlayer))
        {
            Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".PNG").toString());
            dicePl3.setImage(myImage);
            dicePl1.setVisible(false);
            dicePl2.setVisible(false);
            dicePl3.setVisible(true);
            dicePl4.setVisible(false);
        }

        if (nomePlayer4.getText().equalsIgnoreCase(currentPlayer))
        {
            Image myImage = new Image(getClass().getResource("/Assets/Game/Environment/dice/dice" + valoreDado + ".PNG").toString());
            dicePl4.setImage(myImage);
            dicePl1.setVisible(false);
            dicePl2.setVisible(false);
            dicePl3.setVisible(false);
            dicePl4.setVisible(true);
        }

    }
}

