package com.spaccafx.Manager;

import com.spaccafx.Controllers.AlertController;
import com.spaccafx.Controllers.ShareData;
import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.*;
import com.spaccafx.ExternalApps.*;
import com.spaccafx.Files.AudioManager;
import com.spaccafx.Files.FileManager;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Player.*;
import com.spaccafx.Cards.*;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Partita
{
    //region #VARIABLES

    //region #PRIVATE VARIABLES
    int currentRound;
    int posMazziere; // mazziere
    int currentGiocatorePos; // viene dopo il mazziere
    int codicePartita;
    int passwordPartita;
    int cDistaccoMazziere;
    int maxCarteNormali, maxCarteSpeciali;
    int numeroPlayerVite;
    boolean isGameRunning;
    boolean cartaGiaScambiata;
    GameStatus partitaStatus;
    SpaccaTGBot telegramBot;
    TavoloController TC;

    //endregion

    //region #PUBLIC VARIABLES
    public Mazzo mazzo; // creo il mazzo con tutte le carte
    public ArrayList<IGiocatore> giocatori;
    //endregion

    //endregion

    public Partita(int size) // size è il numero di giocatori a partita
    {
        giocatori = new  ArrayList<IGiocatore>(size);

        this.codicePartita = 0;
        this.passwordPartita = 0;
        this.cartaGiaScambiata = false;
        this.posMazziere = 0;
        this.currentGiocatorePos = 0;
        this.currentRound = 1; // quando si crea la partita parte gia dal round 1
        this.isGameRunning = false;
        this.cDistaccoMazziere = 0;

        this.telegramBot = new SpaccaTGBot(); // TODO CAMBIARE INIZILIAZZAZIONE BOT, DA METTERE SOLO QUANDO SI AVVIA IL PROGRAMMA E NON OGNI VOLTA CHE SI CREA UNA PARTITA

    }


    public void impostaTavoloController()
    {
        ShareData sharedData = ShareData.getInstance();
        this.TC = sharedData.getTavoloController();
    }

    //region # GAME

    public void preStartGame() // quando si clicca sul bottone start all'inizio
    {
        this.isGameRunning = true;
        mazzo = new Mazzo(true, maxCarteNormali, maxCarteSpeciali);
        lancioDadiIniziale(); // Lancio dadi iniziale + stabilimento INIZIALE mazziere
        TC.startGameUI();
    }

    public void iniziaNuovoRoundUI()
    {
        distribuisciCarte(); // vado a ricreare le carte togliendo tutti effetti
        startNewGame();
    }

    public void startNewGame()
    {
        // devo partire dalla posizione del mazziere in poi
        this.cDistaccoMazziere = posMazziere; // deve partire dal mazziere
        avanzaManoUI(); // ci avanza di giocatore in giocatore
    }


    public void riprendiPartita(int giocatoreRipresaPos)
    {
        if(isGameStopped())
            return;

        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    TC.mostraBannerAttesa("CONTROLLO-RIPRESA", "Controlliamo la carta");
                    ricaricaMazzo(FileManager.getPlayerCarte(this.codicePartita));
                });

                Thread.sleep(4000);

                Platform.runLater(() -> {
                    TC.nascondiBannerAttesa();

                    controlloManoRipresa(giocatoreRipresaPos);
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
    }


    private void avanzaManoUI() // ogni volta che tocca a un giocatore/bot
    {
        if(isGameStopped())
            return;

        TC.setExitGame(true);
        TC.gestisciPulsanti(false, false, false);
        TC.gestisciPulsanteRiprendiBot(false);

        Thread thread = new Thread(() -> {
            try {

                Platform.runLater(() ->
                {
                    this.cartaGiaScambiata = false;

                    do
                    {
                        this.cDistaccoMazziere = (cDistaccoMazziere + 1) % giocatori.size();
                        System.out.println("[AVANZA-MANO] Avanzo di mano e passo al giocatore: " + cDistaccoMazziere);
                    } while (!isGiocatoreValido(cDistaccoMazziere));



                    this.currentGiocatorePos = cDistaccoMazziere;
                    TC.updateCarteUI(); // aggiorna le carte dei player graficamente
                    AudioManager.giraCarteSuono();

                    String mossa = "Tocca al giocatore " + getCurrentGiocatore().getNome().toUpperCase() + " ruolo " + getCurrentGiocatore().getRuolo();
                    TC.mostraBannerAttesa("MOSSA", mossa);
                });

                Thread.sleep(4000);

                Platform.runLater(() -> {
                    TC.nascondiBannerAttesa();

                    IGiocatore currentGiocatore = getCurrentGiocatore();

                    if(currentGiocatore instanceof Bot)
                    {
                        System.out.println("[GAME] Tocca al BOT: " + currentGiocatore.getNome() + " in posizione: " + currentGiocatorePos);
                        controllaManoIniziale(currentGiocatorePos);
                    }
                    else // e un player
                    {
                        TC.gestisciPulsanti(false, true, true); // fa la mossa con i pulsanti
                        System.out.println("[GAME] Tocca al giocatore: " + currentGiocatore.getNome() + " in posizione: " + currentGiocatorePos);
                        controllaManoIniziale(currentGiocatorePos);
                    }

                });
            } catch (InterruptedException e) {
                // Qui puoi gestire l'interruzione in modo più appropriato
                System.out.println("Thread interrotto: " + e.getMessage());
                // Potresti anche ripristinare l'interrupt flag:
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                // Gestione di altre eccezioni
                System.out.println("Si è verificato un errore: " + e.getMessage());
            }
        });

        thread.start();
    }

    private boolean isGiocatoreValido(int pos)
    {
        return isRuoloValido(giocatori.get(pos).getRuolo()) && !(giocatori.get(pos).getVita()<=0);
    }

    // Metodo per verificare se il ruolo è valido
    private boolean isRuoloValido(RuoloGiocatore ruolo)
    {
        return ruolo.equals(RuoloGiocatore.GIOCATORE) || ruolo.equals(RuoloGiocatore.MAZZIERE);
    }

    private void controlloManoRipresa(int currentGiocatorePos)
    {
        if (isGameStopped())
            return;

        IGiocatore currentGiocatoreRipresa = giocatori.get(currentGiocatorePos);
        Carta currentMano = currentGiocatoreRipresa.getCarta();
        System.out.println("[CONTROLLA-MANO-RIPRESA] Il giocatore possiede: " + currentMano);

        gestisciCartaRipresa(currentMano, currentGiocatoreRipresa);
    }

    private void gestisciCartaRipresa(Carta carta, IGiocatore giocatore) {
        if (carta instanceof CartaProbabilita) {
            gestisciCartaProbabilitaRipresa(carta, giocatore);
        } else if (carta instanceof CartaImprevisto) {
            gestisciCartaImprevistoRipresa(carta, giocatore);
        } else {
            gestisciCartaNormaleRipresa(giocatore);
        }
    }

    private void gestisciCartaProbabilitaRipresa(Carta currentMano, IGiocatore currentGiocatoreRipresa) {
        if (isGameStopped())
            return;

        System.out.println("[CONTROLLO-MANO-RIPRESA] Ho una carta speciale PROBABILITA");

        if (!((CartaProbabilita) currentMano).getCartaEffettoAttivato())
        {
           ((CartaProbabilita) currentMano).Effetto(this, currentGiocatoreRipresa, TC);
        }


        gestisciTurnoRipresa(currentGiocatoreRipresa);
    }

    private void gestisciCartaImprevistoRipresa(Carta currentMano, IGiocatore currentGiocatoreRipresa) {
        if(isGameStopped())
            return;

        System.out.println("[CONTROLLO-MANO-RIPRESA] Ho una carta speciale IMPREVISTO");

        if (!((CartaImprevisto) currentMano).getCartaEffettoAttivato()) {
            ((CartaImprevisto) currentMano).Effetto(this, currentGiocatoreRipresa, TC);
        }
        else
        {
            System.out.println("[CONTROLLO-MANO-RIPRESA] Imprevisto gia attivo!");
            gestisciTurnoRipresa(currentGiocatoreRipresa);
        }
    }

    private void gestisciCartaNormaleRipresa(IGiocatore currentGiocatoreRipresa) {
        if(isGameStopped())
            return;

        System.out.println("[MANO] Ho una carta NORMALE");
        gestisciTurnoRipresa(currentGiocatoreRipresa);
    }

    private void gestisciTurnoRipresa(IGiocatore currentGiocatoreRipresa) {
        if(isGameStopped())
            return;

        if (currentGiocatoreRipresa instanceof Bot)
        {
            TC.gestisciPulsanteRiprendiBot(true);
        }
        else
        {
            TC.gestisciPulsanteRiprendiBot(false);

            if(this.cartaGiaScambiata)
                TC.gestisciPulsanteScambio(false);
            else
                TC.gestisciPulsanteScambio(true);

            TC.gestisciPulsantePassa(true);
        }
    }


    private void controlloManoScambio(int currentGiocatorePos)
    {
        if(isGameStopped())
            return;

        IGiocatore currentGiocatoreScambio = giocatori.get(currentGiocatorePos);
        Carta currentMano = currentGiocatoreScambio.getCarta();
        System.out.println("[CONTROLLA-MANO-SCAMBIO] Il giocatore possiede: " + currentMano.toString());

        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    TC.mostraBannerAttesa("CONTROLLO-SCAMBIO", "Controlliamo la carta che hai ricevuto");
                });

                Thread.sleep(4000);

                Platform.runLater(() -> {
                    TC.nascondiBannerAttesa();

                    if (currentMano instanceof CartaProbabilita) {
                        gestisciCartaProbabilita(currentMano, currentGiocatoreScambio);
                    } else if (currentMano instanceof CartaImprevisto) {
                        gestisciCartaImprevisto(currentMano, currentGiocatoreScambio);
                    } else {
                        gestisciCartaNormale(currentGiocatoreScambio);
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
    }

    private void gestisciCartaProbabilita(Carta currentMano, IGiocatore currentGiocatoreScambio) {
        if(isGameStopped())
            return;

        System.out.println("[CONTROLLO-MANO-SCAMBIO] Ho una carta speciale PROBABILITA");

        if (!((CartaProbabilita) currentMano).getCartaEffettoAttivato()) {
            ((CartaProbabilita) currentMano).Effetto(this, currentGiocatoreScambio, TC);
        }

        gestisciTurno(currentGiocatoreScambio);
    }

    private void gestisciCartaImprevisto(Carta currentMano, IGiocatore currentGiocatoreScambio) {
        if(isGameStopped())
            return;

        System.out.println("[CONTROLLO-MANO-SCAMBIO] Ho una carta speciale IMPREVISTO");

        if (!((CartaImprevisto) currentMano).getCartaEffettoAttivato()) {
            ((CartaImprevisto) currentMano).Effetto(this, currentGiocatoreScambio, TC);
        } else {
            System.out.println("ERRORE MAI DEVE USCIRE QUI");
            gestisciTurno(currentGiocatoreScambio);
        }
    }

    private void gestisciCartaNormale(IGiocatore currentGiocatoreScambio) {
        if(isGameStopped())
            return;

        System.out.println("[MANO] Ho una carta NORMALE");
        gestisciTurno(currentGiocatoreScambio);
    }

    private void gestisciTurno(IGiocatore giocatore) {
        if(isGameStopped())
            return;

        if (giocatore instanceof Bot)
        {
            TC.gestisciPulsanteRiprendiBot(true);
            //passaTurnoUI();
        }
        else
        {
            TC.gestisciPulsanti(false, false, true);
        }
    }

    private void controllaManoIniziale(int currentGiocatore) // metodo che viene richiamato appena si avanza di mano
    {
        if(isGameStopped())
            return;

        // controlliamo che carta ha e cosa attivare o meno graficamente
        Carta currentMano = giocatori.get(currentGiocatore).getCarta();
        IGiocatore currentGiocatoreObj = giocatori.get(currentGiocatore);
        System.out.println("[CONTROLLA-MANO-INIZIALE] Il giocatore possiede: " + currentMano.toString());

        if(currentMano instanceof  CartaProbabilita)
        {
            System.out.println("[CONTROLLA-MANO-INIZIALE] Possiedo carta PROBABILITA!");
            if(!((CartaProbabilita) currentMano).getCartaEffettoAttivato()) // caso in cui l'effetto della carta non sia stato attivato
            {
                // lancio dadi aumento vita
                // scambio con mazzo
                ((CartaProbabilita)currentMano).Effetto(this, giocatori.get(currentGiocatore), TC);
            }
            else
            {
                System.out.println("[CONTROLLA-MANO-INIZIALE] Effetto gia attivato!");
            }

            if(currentGiocatoreObj instanceof Bot)
            {
                TC.gestisciPulsanteRiprendiBot(true);
            }
        }
        else if(currentMano instanceof  CartaImprevisto)
        {
            System.out.println("[CONTROLLA-MANO-INIZIALE] Possiedo carta IMPREVISTO!");

            if(!((CartaImprevisto) currentMano).getCartaEffettoAttivato()) // caso in cui l'effetto della carta non sia stato attivato
            {
                // passa Obbligatorio
                // Scambio obbligatorio
                ((CartaImprevisto)currentMano).Effetto(this, giocatori.get(currentGiocatore), TC);
            }
            else
            {
                if(currentGiocatoreObj instanceof Bot)
                {
                    TC.gestisciPulsanteRiprendiBot(true);
                }
            }
        }
        else // debug
        {
            System.out.println("[CONTROLLA-MANO-INIZIALE] Possiedo carta NORMALE!");
            if(currentGiocatoreObj instanceof Bot)
            {
                TC.gestisciPulsanteRiprendiBot(true);
            }
        }
    }

    public void ScambiaCartaUI()
    {
        if(isGameStopped())
            return;

        TC.gestisciPulsanteScambio(false);

        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() ->
                {
                    System.out.println("[SCELTA] Ho deciso di scambiare la carta");
                    TC.mostraBannerAttesa("SCAMBIO", "Ho deciso di scambiare la carta");
                });


                Thread.sleep(4000);

                Platform.runLater(() -> {
                    TC.nascondiBannerAttesa();
                    gestisciScambioCarta();
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
    }

    private void eseguiScambioPlayerSuccessivo(Carta currentCarta)
    {
        if(isGameStopped())
            return;

        IGiocatore nextPlayer;
        int nextIndexPlayer = this.currentGiocatorePos;

        do
        {
            nextIndexPlayer = (nextIndexPlayer + 1) % giocatori.size(); // andiamo ad incrementare senza uscire dalla grandezza dei giocatori
        }while(!isRuoloValido(giocatori.get(nextIndexPlayer).getRuolo()));

        System.out.println("[DEBUG] Indirizzo PLAYER successivo: " + nextIndexPlayer);

        nextPlayer = giocatori.get(nextIndexPlayer); // prendo il prossimo giocatore
        Carta cartaNextPlayer = nextPlayer.getCarta();
        nextPlayer.setCarta(currentCarta);
        getCurrentGiocatore().setCarta(cartaNextPlayer);

        System.out.println("[GAME] Ho scambiato la carta con il player successivo, adesso "
                + getCurrentGiocatore().getNome() +  " ha la carta: " + getCurrentGiocatore().getCarta());

        TC.updateCarteUI(); // riaggiorno la grafica
        // TODO aggiungere suono scambio
        AudioManager.distribuisciCarteSuono();

    }

    private void gestisciScambioCarta()
    {

        if(isGameStopped())
            return;

        Carta cartaPlayerAttuale = getCurrentGiocatore().getCarta(); // prendo la sua carta

        if(getCurrentGiocatore().getRuolo() == RuoloGiocatore.MAZZIERE) // se e il mazziere la scambio con il mazzo
        {
            this.cartaGiaScambiata = true;
            System.out.println("carta gia scambiata: " + cartaGiaScambiata);
            // se pesco una carta speciale, l effetto non viene attivato e vale come carta normale, quindi non ce bisogno del controlloMano() per verificare se attivare o meno
            // l'effetto.

            Thread thread = new Thread(() -> {
                try {
                    Platform.runLater(() ->
                    {
                        getCurrentGiocatore().setCarta(mazzo.PescaCartaSenzaEffetto());
                        TC.updateCarteUI();
                        AudioManager.giraCarteSuono();

                        System.out.println("[GAME] Sono MAZZIERE e pesco dal mazzo la prossima carta perche sono un mazziere! Ho preso: " + getCurrentGiocatore().getCarta().toString());
                        TC.mostraBannerAttesa("SCAMBIO", "Sono mazziere e ho pescato dal mazzo!");
                    });

                    Thread.sleep(4000);


                    Platform.runLater(() -> {
                        TC.nascondiBannerAttesa();


                        System.out.println("[GAME] Round finito!! Passo al controllo dei risultati");
                        controllaRisultatiUI(); // alla fine della mossa del mazziere, controllo i risultati
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            thread.start();

        }
        else // sono un giocatore normale NON MAZZIERE
        {
            if(!cartaGiaScambiata) // la carta non e stata ancora scambiata, faccio lo scambio!
            {
                System.out.println("[GAME] Non ho mai scambiato, posso scambiare!");
                eseguiScambioPlayerSuccessivo(cartaPlayerAttuale); // scambio con il player dopo
                this.cartaGiaScambiata = true; // metto a true lo scambio
                TC.gestisciPulsanteScambio(false); // nascondo il pulsante per poter ricambiare

                // se sono un bot posso rifare una scelta solo se ho una carta normale/ probabilita
                controlloManoScambio(currentGiocatorePos); // gli controllo la mano dopo lo scambio
            }
            else // se HA GIA scambiato la carta
            {
                System.out.println("[GAME] Ho gia scambiato, NON posso rifarlo!");
                System.out.println("[GAME] Passo al prossimo giocatore!");
                reimpostaGrafica(); // reimposta la grafica di default

                avanzaManoUI();
            }
        }
    }


    public void passaTurnoUI()
    {
        if(isGameStopped())
            return;

        TC.gestisciPulsanti(false, false, false);

        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    System.out.println("[SCELTA] Ho deciso di passare il turno");
                    TC.mostraBannerAttesa("PASSO", "Il giocatore ha deciso di passare il turno");
                });

                Thread.sleep(4000);

                Platform.runLater(() ->
                {
                    TC.nascondiBannerAttesa();

                    reimpostaGrafica();
                    this.cartaGiaScambiata = false;

                    if (getCurrentGiocatore().getRuolo() == RuoloGiocatore.MAZZIERE)
                    {
                        // finisce il round e controlla i risultati
                        System.out.println("[GAME] Round finito, controllo i risultati..");

                        controllaRisultatiUI();
                    }
                    else
                        avanzaManoUI(); // vado avanti
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    private void reimpostaGrafica()
    {
        TC.gestisciPulsanti(false, true, true); // rimetti i pulsanti normali
        TC.gestisciPulsanteRiprendiBot(false);
    }


    private void controllaRisultatiUI() // con questo metodo capiamo a chi togliere la vita dei player
    {
        if(isGameStopped())
            return;

        TC.setExitGame(false);
        TC.gestisciPulsanti(false,false,false);

        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() ->
                {
                    TC.mostraBannerAttesa("CONTROLLO", "Scopriamo le carte per controllare i risultati");
                    TC.mostraTutteCarteUI();
                });

                Thread.sleep(4000);

                Platform.runLater(() ->
                {
                    TC.nascondiBannerAttesa();

                    Map<Integer, ArrayList<IGiocatore>> mapSet = new HashMap<>();

                    int maxValue = trovaValoreCartaAlta(giocatori);
                    System.out.println("[GAME] La carta con il valore piu alto e: " + maxValue);

                    IGiocatore giocatoreDebole = null;

                    for (IGiocatore giocatore : giocatori)
                    {
                        if ((giocatore.getCarta().getValore() == maxValue) && giocatore.getRuolo() != RuoloGiocatore.MORTO) // Prendo il valore massimo delle carte dei player vivi
                        {
                            ArrayList<IGiocatore> giocatoriConValoreMassimo = mapSet.getOrDefault(maxValue, new ArrayList<>());
                            giocatoriConValoreMassimo.add(giocatore);
                            mapSet.put(maxValue, giocatoriConValoreMassimo);
                        }
                    }

                    ArrayList<IGiocatore> giocatoriConValoreMassimo = mapSet.get(maxValue);

                    if(giocatoriConValoreMassimo.size() <= 1) // se abbiamo solo un perdente
                    {
                        System.out.println("[CHECK-GAME] * 1 SOLO Giocatore perdente!");
                        giocatoreDebole = giocatoriConValoreMassimo.get(0);

                        if(giocatoreDebole.hasVitaExtra())
                        {
                            System.out.println("[CHECK-GAME] " + giocatoreDebole.getNome() + " tolta vita EXTRA del giocatore");
                            giocatoreDebole.removeVitaExtra();
                            //AudioManager.vitaDownSuono();
                        }
                        else
                        {
                            System.out.println("[CHECK-GAME] " + giocatoreDebole.getNome() + " tolta vita NORMALE del giocatore");
                            giocatoreDebole.setVita(giocatoreDebole.getVita() - 1); // tolgo 1 vita
                           // AudioManager.vitaDownSuono();
                        }

                        if(giocatoreDebole.getVita() <= 0 && !giocatoreDebole.hasVitaExtra()) // se il giocatore in questione ha 0 o meno vite, viene ELIMINATO dalla partita
                        {
                            TC.HidePlayerUI(giocatoreDebole.getNome()); // rivedere

                            giocatoreDebole.setRuolo(RuoloGiocatore.MORTO); // lo imposto come morto

                            System.out.println("\n\t[CHECK-GAME] ** (ELIMINATO) " + giocatoreDebole.getNome() + " **");
                        }
                    }
                    else // vuol dire che ci sono piu giocatori (spareggio)
                    {
                        System.out.println("[CHECK-GAME] * " +  giocatoriConValoreMassimo.size() + " MOLTEPLICI Giocatori perdenti!");

                        // Controllo Seme

                        for(IGiocatore giocatore : giocatoriConValoreMassimo)
                        {
                            if(giocatoreDebole == null || ((((Carta)giocatore.getCarta()).getSeme().compareTo(((Carta)giocatoreDebole.getCarta()).getSeme())) > 0))
                            {
                                giocatoreDebole = giocatore;
                                System.out.println("Giocatore perdente attuale: " + giocatoreDebole.getNome());
                            }
                        }

                        if(giocatoreDebole != null)
                        {
                            if(giocatoreDebole.hasVitaExtra())
                            {
                                System.out.println("[CHECK-GAME] " + giocatoreDebole.getNome() + " tolta vita EXTRA del giocatore");
                                giocatoreDebole.removeVitaExtra();
                            }
                            else
                            {
                                System.out.println("[CHECK-GAME] " + giocatoreDebole.getNome() + " tolta vita NORMALE del giocatore");
                                giocatoreDebole.setVita(giocatoreDebole.getVita() - 1); // tolgo 1 vita
                            }


                            if(giocatoreDebole.getVita() <= 0 && !giocatoreDebole.hasVitaExtra()) // se il giocatore in questione ha 0 o meno vite, viene ELIMINATO dalla partita
                            {
                                TC.HidePlayerUI(giocatoreDebole.getNome());

                                //giocatoriMorti.add(giocatoreDebole); // viene messo nella lista degli eliminati
                                giocatoreDebole.setRuolo(RuoloGiocatore.MORTO); // lo imposto come morto

                                System.out.println("\n\t[CHECK-GAME] ** (ELIMINATO) " + giocatoreDebole.getNome() + " **");
                            }
                        }

                    }

                    String risultato;

                    if(!giocatori.contains(giocatoreDebole))
                        risultato = "Il giocatore " + giocatoreDebole.getNome().toUpperCase() + " MORTO!";
                    else {
                        risultato = "Il giocatore " + giocatoreDebole.getNome().toUpperCase() + " ha perso una vita!";
                        AudioManager.vitaDownSuono();
                    }


                    TC.mostraBannerAttesa("RISULTATI", risultato);
                });

                Thread.sleep(4000);

                Platform.runLater(() ->
                {
                    TC.nascondiBannerAttesa();
                    TC.updateVitaUI();

                    if(!isGameEnded()) // se NON sta ancora andando
                    {
                        System.out.println("[END] Gioco concluso con un vincitore!");
                        setPartitaStatus(GameStatus.ENDED);
                        TC.EndGameUI(); // TODO DA COMPLETARLO
                    }
                    else // se sta andando il gioco, giocatori vivi
                    {
                        System.out.println("[GAME] Passo al prossimo round!");
                        avanzaRoundUI();
                    }
                });


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void avanzaRoundUI()
    {
        aumentaPlayerRounds();
        TC.setExitGame(true);

        RuotaMazziereUI(); // ruota il mazziere
        this.mazzo.CreoCarte();

        this.currentRound ++; // aumenta il round
        TC.aggiornaInfoUI(); // aggiorna la grafica

        iniziaNuovoRoundUI(); // si reinizia la mano
    }

    //endregion

    //region #GAME SETUP

    public void lancioDadiIniziale()
    {

        for(int c=0; c<giocatori.size() ;c++)
        {
            IGiocatore editGiocatore = giocatori.get(c); // prendo il giocatore con le sue informazioni
            int valoreDado = lancioDadoSingolo(); // prendo valore a caso del dado

            System.out.println("\nIl giocatore " + editGiocatore.getNome() + " ha lanciato un dado ed e uscito: " + valoreDado);
            editGiocatore.setDado(valoreDado);

            giocatori.set(c, editGiocatore);
        }

        TC.impostaDadiUI();
        // dimostro i dadi lanciati e faccio attesa.......

        this.stabilisciMazziere();
    }

    public void stabilisciMazziere()
    {
        ArrayList<IGiocatore> perdenti = new ArrayList<IGiocatore>(); // lista giocatori che hanno lo stesso valore
        int valorePiuAlto = trovaValoreDadoAlto(giocatori); // valore dei dadi piu alto tra tutti i player

        System.out.println("Il valore piu alto : " + valorePiuAlto);

        // NEW METHOD
        for (IGiocatore iGiocatore : giocatori)
        {
            if (iGiocatore.getValoreDado() >= valorePiuAlto)
                perdenti.add(iGiocatore); // metto il giocatore con il valore perdente dentro la lista e non gli assegno ancora il ruolo..
            else
                iGiocatore.setRuolo(RuoloGiocatore.GIOCATORE); // se ha un valore piu basso, allora e per forza di cose un player perche ha vinto
        }

        // VERIFICO SE CE LA PRESENZA DI SPAREGGIO DA EFFETTUARE


        // dimostro i dadi lanciati e faccio attesa.......
        while (perdenti.size() > 1) // Fino a quando ci sono piu perdenti
        {
            System.out.println("\n** PIU PERSONE HANNO IL DADO CON LO STESSO VALORE ** \n");

            for(IGiocatore perdente : perdenti)
                perdente.setDado(lancioDadoSingolo()); // faccio ritirare i dadi ai giocatori perdenti

            MostraDadiGicatoriAttuali(perdenti); // faccio una sorta di debug per vedere i dadi che sono usciti

            int spareggioValorePiuAlto = trovaValoreDadoAlto(perdenti); // controllo quale dei perdenti che ha rilanciato ha il valore piu alto
            System.out.println("Il valore dei dati tra i perdenti piu alto è " + spareggioValorePiuAlto);

            for(int c = 0; c < perdenti.size(); c++)
            {
                if(perdenti.get(c).getValoreDado() < spareggioValorePiuAlto) // per sicurezza metto maggiore uguale
                {
                    perdenti.get(c).setRuolo(RuoloGiocatore.GIOCATORE); // se ha un valore piu basso, allora e per forza di cose un player
                    System.out.println("Il giocatore: " + perdenti.get(c).getNome() + " non e un perdente (e un PLAYER)");
                    perdenti.remove(perdenti.get(c));
                    c = 0;
                }
            }
        }


        if(giocatori.contains(perdenti.get(0)))
        {
            posMazziere = giocatori.indexOf(perdenti.get(0));
            giocatori.get(posMazziere).setRuolo(RuoloGiocatore.MAZZIERE); // prendo il giocatore perdente e gli assegno il ruolo mazziere. Il giocatore e PER FORZA presente
            // metto il mazziere per primo nella lista!
        }
        else
        {
            System.out.println("ERRORE MADORNALE DA SISTEMARE ASSOLUTAMENTE. NON DEVE MAI COMPARIRE!");
            System.exit(1); //FINISCO IL PROGRAMMA PERCHE NON PUO CONTINUARE!
        }

        System.out.println("\n\t** NUOVO MAZZIERE IN CIRCOLAZIONE ("+ giocatori.get(posMazziere).getNome() + ") **" + " Pos mazziere: " + posMazziere);
        TC.impostaCoroneMazziereUI();
    }


    //endregion

    //region #METHODS

    public synchronized int lancioDadoSingolo() { return (int)(1 + Math.random() * (6));}

    public void aggiungiGiocatore(IGiocatore giocatore){this.giocatori.add(giocatore);}

    private int trovaValoreDadoAlto(ArrayList<IGiocatore> lista)
    {
        int numeroPiuAlto = 1; // metto il valore piu piccolo del dado possibile (da noi il piu piccolo e' il piu alto)

        // NEW METHOD
        for (IGiocatore giocatore : lista)
        {
            if (giocatore.getValoreDado() >= numeroPiuAlto)
                numeroPiuAlto = giocatore.getValoreDado();
        }

        return numeroPiuAlto;
    }

    public void distribuisciCarte()
    {
        AudioManager.distribuisciCarteSuono();

        int primoGiocatore = posMazziere+1; // parto dalla posizione del giocatore dopo al mazziere

        if(primoGiocatore >= giocatori.size()) // se il mazziere e alla fine e devo partire dal primo giocatore
        {
            System.out.println("Distribuisco le carte partendo dal PRIMO giocatore");
            for(IGiocatore giocatore : giocatori)
            {
                if(giocatore.getRuolo() != RuoloGiocatore.MORTO) // distribuisce la carta solo se il player no ne morto
                {
                    giocatore.setCarta(mazzo.PescaCarta());
                    System.out.println("Ho dato la carta al giocatore: " + giocatore.getNome());
                }
            }
        }
        else // altrimenti devo vedere fino a quando non arriva all ultimo giocatore presente nell array
        {
            int carteDistribuite = 0;
            int currentIndex = primoGiocatore;
            System.out.println("Distribuisco dal giocatore n.: " + currentIndex);

            System.out.println("Distribuisco le carte partendo nel MEZZO tra un giocatore");
            do
            {
                if(currentIndex >= giocatori.size()) // se sono l ultimo giocatore
                {
                    System.out.println("Riparto dall'array dei giocatori");
                    currentIndex = 0;
                }

                if(giocatori.get(currentIndex).getRuolo() != RuoloGiocatore.MORTO) // se e un player vivo gli posso cambiare la carta
                {
                    giocatori.get(currentIndex).setCarta(mazzo.PescaCarta());
                    System.out.println("Ho dato la carta al giocatore: " + giocatori.get(currentIndex).getNome());
                    carteDistribuite++;
                }

                currentIndex++;
                System.out.println("Carte distribuite: " + carteDistribuite);

            }while(carteDistribuite < getCountGiocatoriVivi()); // fino a quando non do tot carte tanti quanti sono i player VIVI continuo a darle
        }

        System.out.println("Esco dalla distribuzione e do le info...");
        StampaInfoGiocatori();
    }

    private int trovaValoreCartaAlta(ArrayList<IGiocatore> lista)
    {
        int numeroPiuAlto = 1; // metto il valore piu piccolo della carta possibile (da noi il piu piccolo e' il piu alto)

        // NEW METHOD
        for (IGiocatore giocatore : lista)
        {
            if(giocatore.getRuolo() != RuoloGiocatore.MORTO)
            {
                if (giocatore.getCarta().getValore() >= numeroPiuAlto)
                {
                    numeroPiuAlto = giocatore.getCarta().getValore(); // ho trovato una carta che supera quella che era piu piccola prima
                }
            }

        }

        return numeroPiuAlto;
    }

    private void RuotaMazziereUI()
    {
        for (IGiocatore giocatore : giocatori)
        {
            if(!(giocatore.getRuolo() == RuoloGiocatore.MORTO))
            {
                giocatore.setRuolo(RuoloGiocatore.GIOCATORE);
            }
        }

        // Trova la prossima posizione con il ruolo GIOCATORE, saltando quelli con il ruolo MORTO
        int numeroGiocatori = giocatori.size();
        int nuovaPosizione = (posMazziere + 1) % numeroGiocatori; //avanziamo di 1 la pos mazziere attuale rimanendo nell array

        while (giocatori.get(nuovaPosizione).getRuolo() == RuoloGiocatore.MORTO) // fino a quando il player e un morto, avanzo
        {
            nuovaPosizione = (nuovaPosizione + 1) % numeroGiocatori;
        }

        // Assegna il ruolo di MAZZIERE al giocatore alla nuova posizione
        giocatori.get(nuovaPosizione).setRuolo(RuoloGiocatore.MAZZIERE);
        this.posMazziere = nuovaPosizione;
        TC.impostaCoroneMazziereUI();

        System.out.println("[ROTAZIONE-MAZZIERE] Il mazziere è stato ruotato correttamente!");
    }

    public boolean isGameEnded()
    {
        int countAlivePlayers = 0;

        for (IGiocatore giocatore : giocatori) {
            if ((giocatore.getRuolo() == RuoloGiocatore.GIOCATORE) || giocatore.getRuolo() == RuoloGiocatore.MAZZIERE)
            {
                countAlivePlayers++;
            }
        }

        // Restituisce true se ci sono più di 1 giocatore vivi, altrimenti false
        return countAlivePlayers > 1;

    } // Restituisce true se ci sono piu di 1 giocatore vivi

    public int getCurrentRound(){return this.currentRound;}
    public boolean getIsGameRunning(){return this.isGameRunning;}
    public int getPosMazziere(){return this.posMazziere;}

    public void generaCodicePartita()  { this.codicePartita =  (int)(1 + (Math.random() * 1000)); } // TODO PREVEDERE CASO IN CUI VENGA GENERATO CODICE ESISTENTE

    public void generaPasswordPartita()  { this.passwordPartita =  (int)(1 + (Math.random() * 1000)); }


    public int getCodicePartita() { return this.codicePartita; }

    public int getPasswordPartita() { return this.passwordPartita; }

    // public boolean isGameRunning(){return this.isGameRunning;}

    public void aggiungiListaGiocatori(ArrayList<IGiocatore> giocatori) {this.giocatori = giocatori;} // # MODIFICARE - Controllare che il num dei player sia inferiore al max

    public IGiocatore getVincitore()
    {
        IGiocatore vincitore = null;

        if(!isGameEnded())
        {
            for (IGiocatore giocatore : giocatori)
            {
                if (giocatore.getVita() > 0)
                {
                    vincitore = giocatore;
                    break;
                }
            }
        }

        return vincitore;
    }

    public int getCurrentGiocatorePos(){return this.currentGiocatorePos;}
    public IGiocatore getCurrentGiocatore(){return giocatori.get(currentGiocatorePos);}

    private void aumentaPlayerRounds()
    {
        for(IGiocatore giocatore : giocatori)
        {
            if(giocatore.getRuolo() != RuoloGiocatore.MORTO)
                giocatore.setPlayerRounds(giocatore.getPlayerRounds()+1);
        }
    }

    // endregion

    //region #DEBUG

    private void MostraDadiGicatoriAttuali(ArrayList<IGiocatore> lista)
    {
        for (IGiocatore giocatore : lista) {
            System.out.println("Giocatore: " + giocatore.getNome() + " Dado: " + giocatore.getValoreDado());
        }
    }

    public void StampaInfoGiocatori()
    {
        for (IGiocatore currentGiocatore : giocatori)
        {
            Carta cartaGiocatore = currentGiocatore.getCarta();

            System.out.println("\n> Giocatore: " + currentGiocatore.getNome() +
                    ", Carta: " + cartaGiocatore.toString() +
                    ", Vite: " + currentGiocatore.getVita() +
                    ", Vita Extra: " + currentGiocatore.hasVitaExtra() +
                    ", Ruolo: " + currentGiocatore.getRuolo());
        }
    }

    public String getMazziereNome() {return giocatori.get(posMazziere).getNome();}
    public void setCodicePartita(int codicePartita){this.codicePartita=codicePartita;}
    public void setPasswordPartita(int passwordPartita){this.passwordPartita = passwordPartita;}
    public void setIsGameRunning(boolean isGameRunning){this.isGameRunning = isGameRunning;}
    public void setPartitaStatus(GameStatus partitaStatus){this.partitaStatus = partitaStatus;}
    public GameStatus getPartitaStatus(){return this.partitaStatus;}
    public int getNumeroPlayerVite(){return  this.numeroPlayerVite;}
    public void setNumeroPlayerVite(int numeroPlayerVite){this.numeroPlayerVite = numeroPlayerVite;}

    public void SavePartita(MouseEvent mouseEvent) throws IOException
    {
      /*  Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Vuoi uscire dalla partita");
        alert.setContentText("Se confermi i tuoi dati saranno salvati");

       */
        AlertController.showConfirm("Salvo i dati del gioco");

      //  Optional<ButtonType> result = alert.showAndWait();
      //  if (result.isPresent() && result.get() == ButtonType.OK)
       // {
            if(getIsGameRunning()) // se sta andando salvo, altrimenti non sovrascrivo nulla....
            {
                System.out.println("isGamerunning: " + this.isGameRunning);
                setPartitaStatus(GameStatus.STOPPED);
                System.out.println("Il gioco e stato messo in pausa correttamente!");

                FileManager.sovrascriviSalvataggiPartita(this); // salvo tutti i dati di questa partita
            }

            TC.caricaMenuUI(mouseEvent); // ritorno indietro al menu a prescindere

       // } else {
            // TODO impostare che se si clicca su annulla non succede nulla e si chiude il popup
            System.out.println("Continua il gioco");
      //  }

    }

    public void setCurrentGiocatorePos(int currentGiocatorePos) {this.currentGiocatorePos = currentGiocatorePos;}

    public void setPosMazziere(int posMazziere){this.posMazziere = posMazziere;}
    public int getDistaccoMazziere(){return this.cDistaccoMazziere;}
    public void setDistaccoMazziere(int cDistaccoMazziere){this.cDistaccoMazziere = cDistaccoMazziere;}

    public void ricaricaMazzo(ArrayList<Carta> carteDaEliminare)
    {
        this.mazzo = new Mazzo(false, maxCarteNormali, maxCarteSpeciali); // se messo su false non mi rigenera le carte nel costruttore
        this.mazzo.CreoCarte(); // ricreo il mazzo
        this.mazzo.EliminaCarte(carteDaEliminare); // dal mazzo che genero cancello le carte che gli passo per parametro
    }

    public boolean getCartaGiaScambiata(){return this.cartaGiaScambiata;}
    public void setCartaGiaScambiata(boolean cartaGiaScambiata){this.cartaGiaScambiata = cartaGiaScambiata;}

    public int getMaxCarteNormali(){return this.maxCarteNormali;}
    public int getMaxCarteSpeciali(){return this.maxCarteSpeciali;}

    public void setMaxCarteNormali(int maxCarteNormali){this.maxCarteNormali = maxCarteNormali;}
    public void setMaxCarteSpeciali(int maxCarteSpeciali){this.maxCarteSpeciali = maxCarteSpeciali;}

    public boolean isGameStopped()
    {
        if(this.partitaStatus == GameStatus.STOPPED)
        {
            System.out.println("Il gioco e stato stoppato e non devo fare alcuna operazione");
            return true;
        }

        return false;
    }

    private int getCountGiocatoriVivi()
    {
        int count = 0;

        for (IGiocatore giocatore: giocatori)
        {
            if(giocatore.getRuolo() != RuoloGiocatore.MORTO)
                count++;
        }

        return count; // restituisco il numero dei giocatori vivi
    }

    public void setCurrentRound(int currentRound){this.currentRound = currentRound;}
    //endregion



}