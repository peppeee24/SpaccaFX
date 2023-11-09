package com.spaccafx.Manager;

import com.spaccafx.Controllers.ShareData;
import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.*;
import com.spaccafx.ExternalApps.*;
import com.spaccafx.Files.AudioManager;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Player.*;
import com.spaccafx.Cards.*;
import javafx.application.Platform;

import java.util.*;

public class Partita
{
    //region #VARIABLES

    //region #PRIVATE VARIABLES
    int currentRound;
    int posMazziere; // mazziere
    int currentGiocatorePos; // viene dopo il mazziere
    int codicePartita;
    int passwordPartita;
    boolean isGameRunning;
    GameStatus partitaStatus;
    SpaccaTGBot telegramBot;
    TavoloController TC;

    //endregion

    //region #PUBLIC VARIABLES
    public Mazzo mazzo; // creo il mazzo con tutte le carte
    public ArrayList<IGiocatore> giocatori;
    public ArrayList<IGiocatore> giocatoriMorti;

    int cDistaccoMazziere;
    //endregion

    //endregion

    public Partita(int size) // size è il numero di giocatori a partita
    {
        giocatoriMorti = new ArrayList<IGiocatore>();
        giocatori = new  ArrayList<IGiocatore>(size);

        this.codicePartita = 0;
        this.passwordPartita = 0;
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
        mazzo = new Mazzo();
        lancioDadiIniziale(); // Lancio dadi iniziale + stabilimento INIZIALE mazziere
        TC.startGameUI();
    }

    public void iniziaNuovoRoundUI()
    {
        distribuisciCarte();
        startNewGame();
    }

    public void startNewGame()
    {
        // devo partire dalla posizione del mazziere in poi
        cDistaccoMazziere = posMazziere; // deve partire dal mazziere
        avanzaManoUI(); // ci avanza di giocatore in giocatore
    }

    private void avanzaManoUI() // ogni volta che tocca a un giocatore/bot
    {

        Thread thread = new Thread(() -> {
            try {

                Platform.runLater(() ->
                {
                    this.cartaGiaScambiata = false;

                    if(cDistaccoMazziere+1 > giocatori.size() - 1)
                        cDistaccoMazziere = 0;
                    else
                        cDistaccoMazziere++;


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
                        TC.gestisciPulsanti(false, false, false); // facciamo scomparire i bottoni ai bot inizialmente
                        System.out.println("[GAME] Tocca al BOT: " + currentGiocatore.getNome() + " in posizione: " + currentGiocatorePos);
                        controllaManoIniziale(currentGiocatorePos);


                        // all'inizio del suo turno se NON ha una carta imprevisto in mano,
                        // fa una mossa, altrimenti passera obbligatoriamente!
                        if(!(currentGiocatore.getCarta() instanceof CartaImprevisto))
                            ((Bot) currentGiocatore).SceltaBotUI(this, TC);
                    }
                    else // e un player
                    {
                        TC.gestisciPulsanti(false, true, true); // fa la mossa con i pulsanti
                        System.out.println("[GAME] Tocca al giocatore: " + currentGiocatore.getNome() + " in posizione: " + currentGiocatorePos);
                        controllaManoIniziale(currentGiocatorePos);
                    }

                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
    }


    private void controlloManoScambio(int currentGiocatorePos) {
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
        System.out.println("[CONTROLLO-MANO-SCAMBIO] Ho una carta speciale PROBABILITA");

        if (!((CartaProbabilita) currentMano).isAttivato()) {
            ((CartaProbabilita) currentMano).Effetto(this, currentGiocatoreScambio, TC);
        }

        gestisciTurno(currentGiocatoreScambio);
    }

    private void gestisciCartaImprevisto(Carta currentMano, IGiocatore currentGiocatoreScambio) {
        System.out.println("[CONTROLLO-MANO-SCAMBIO] Ho una carta speciale IMPREVISTO");

        if (!((CartaImprevisto) currentMano).isAttivato()) {
            ((CartaImprevisto) currentMano).Effetto(this, currentGiocatoreScambio, TC);
        } else {
            System.out.println("ERRORE MAI DEVE USCIRE QUI");
            gestisciTurno(currentGiocatoreScambio);
        }
    }

    private void gestisciCartaNormale(IGiocatore currentGiocatoreScambio) {
        System.out.println("[MANO] Ho una carta NORMALE");
        gestisciTurno(currentGiocatoreScambio);
    }

    private void gestisciTurno(IGiocatore giocatore) {
        if (giocatore instanceof Bot) {
            passaTurnoUI();
        } else {
            TC.gestisciPulsanti(false, false, true);
        }
    }











    /*private void controlloManoScambio(int currentGiocatorePos)
    {
        // controlliamo che carta ha e cosa attivare o meno graficamente
        IGiocatore currentGiocatoreScambio = giocatori.get(currentGiocatorePos);
        Carta currentMano = currentGiocatoreScambio.getCarta();
        System.out.println("[CONTROLLA-MANO-SCAMBIO] Il giocatore possiede: " + currentMano.toString());

        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() ->
                {
                    TC.mostraBannerAttesa("CONTROLLO-SCAMBIO", "Controlliamo la carta che hai ricevuto");
                });

                Thread.sleep(4000);

                Platform.runLater(() ->
                {
                    TC.nascondiBannerAttesa();

                    if(currentMano instanceof  CartaProbabilita)
                    {
                        System.out.println("[CONTROLLO-MANO-SCAMBIO] Ho una carta speciale PROBABILITA");

                        if(!((CartaProbabilita) currentMano).isAttivato()) // caso in cui l'effetto della carta non sia stato attivato
                        {
                            // lancio dadi aumento vita
                            // scambio con mazzo

                            ((CartaProbabilita)currentMano).Effetto(this, currentGiocatoreScambio, TC); // attivo effetto su quel giocatore
                        }

                        if(currentGiocatoreScambio instanceof  Bot) // il bot passa il turno
                            passaTurnoUI(); // passa il turno se prende o meno una carta prob attiva o da attivare
                        else
                            TC.gestisciPulsanti(false, false, true);

                    }
                    else if(currentMano instanceof  CartaImprevisto)
                    {
                        System.out.println("[CONTROLLO-MANO-SCAMBIO] Ho una carta speciale IMPREVISTO");

                        if(!((CartaImprevisto) currentMano).isAttivato()) // caso in cui l'effetto della carta non sia stato attivato
                        {
                            // passa Obbligatorio
                            // Scambio obbligatorio

                            ((CartaImprevisto)currentMano).Effetto(this, currentGiocatoreScambio, TC); // passa sempre
                        }
                        else // se e stato gia attivato
                        {
                            System.out.println("ERRORE MAI DEVE USCIRE QUI");
                            if(currentGiocatoreScambio instanceof  Bot) // il bot passa il turno
                                passaTurnoUI(); // passa il turno se prende o meno una carta prob attiva o da attivare
                            else
                                TC.gestisciPulsanti(false, false, true);
                        }

                    }
                    else // debug
                    {
                        System.out.println("[MANO] Ho una carta NORMALE");

                        if(currentGiocatoreScambio instanceof  Bot) // il bot passa il turno
                            passaTurnoUI(); // passa il turno se prende o meno una carta prob attiva o da attivare
                        else
                            TC.gestisciPulsanti(false, false, true);

                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();

    }

     */

    private void controllaManoIniziale(int currentGiocatore) // metodo che viene richiamato appena si avanza di mano
    {
        // controlliamo che carta ha e cosa attivare o meno graficamente
        Carta currentMano = giocatori.get(currentGiocatore).getCarta();
        System.out.println("[CONTROLLA-MANO-INIZIALE] Il giocatore possiede: " + currentMano.toString());

        if(currentMano instanceof  CartaProbabilita)
        {
            System.out.println("[CONTROLLA-MANO-INIZIALE] Possiedo carta PROBABILITA!");
            if(!((CartaProbabilita) currentMano).isAttivato()) // caso in cui l'effetto della carta non sia stato attivato
            {
                // lancio dadi aumento vita
                // scambio con mazzo
                ((CartaProbabilita)currentMano).Effetto(this, giocatori.get(currentGiocatore), TC);
            }
            else
            {
                System.out.println("[CONTROLLA-MANO-INIZIALE] Effetto gia attivato!");
            }
        }
        else if(currentMano instanceof  CartaImprevisto)
        {
            System.out.println("[CONTROLLA-MANO-INIZIALE] Possiedo carta IMPREVISTO!");

            if(!((CartaImprevisto) currentMano).isAttivato()) // caso in cui l'effetto della carta non sia stato attivato
            {
                // passa Obbligatorio
                // Scambio obbligatorio
                ((CartaImprevisto)currentMano).Effetto(this, giocatori.get(currentGiocatore), TC);
            }
            else
            {
                System.out.println("[CONTROLLA-MANO-INIZIALE] Effetto gia attivato!");
            }
        }
        else // debug
        {
            System.out.println("[CONTROLLA-MANO-INIZIALE] Possiedo carta NORMALE!");
        }
    }


    boolean cartaGiaScambiata = false;
    public void ScambiaCartaUI()
    {
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
        IGiocatore nextPlayer;
        int nextIndexPlayer = currentGiocatorePos + 1;

        if(nextIndexPlayer >= giocatori.size()) // se sono oltre il limite
            nextIndexPlayer = 0; // prendo il primo giocatore

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

    private void gestisciScambioCarta() // TODO PROBLEMA NELLO SCAMBIO CON LE CARTE IMPREVISTO, CASO IN CUI IL GIOCATORE DOPO HA IMPREVISTO E LO PASSA A QUELLO PRIMA. FA UNA DESIONE CHE NON DEVE, SIA BOT CHE PLAYER
    {
        Carta cartaPlayerAttuale = getCurrentGiocatore().getCarta(); // prendo la sua carta

        if(getCurrentGiocatore().getRuolo() == RuoloGiocatore.MAZZIERE) // se e il mazziere la scambio con il mazzo
        {
            this.cartaGiaScambiata = false;
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
    }


    // TODO RICONTROLLARE TUTTO IL FLUSSO DI QUESTO METODO
    private void controllaRisultatiUI() // con questo metodo capiamo a chi togliere la vita dei player
    {

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
                        if (giocatore.getCarta().getValore() == maxValue)
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

                            // todo vedere se togliere per i file
                            giocatoriMorti.add(giocatoreDebole); // viene messo nella lista degli eliminati
                            giocatori.remove(giocatoreDebole);

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

                                giocatoriMorti.add(giocatoreDebole); // viene messo nella lista degli eliminati
                                giocatori.remove(giocatoreDebole);

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

                    if(!isGameRunning()) // se NON sta ancora andando
                    {
                        System.out.println("[END] Gioco concluso con un vincitore!");
                        TC.EndGameUI(); // da rivedere
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
        lancioDadiIniziale(); // vengono rilanciati i dadi
        mazzo.MescolaMazzo(); // messe le carte, tolti gli effetti e rimescolato

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

    public void stabilisciMazziere() // todo controlla
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

    public int lancioDadoSingolo() { return (int)(1 + Math.random() * (6));}

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
        AudioManager.distribuisciCarteSuono();
        AudioManager.distribuisciCarteSuono();
        AudioManager.distribuisciCarteSuono();
        int primoGiocatore = posMazziere+1; // parto dalla posizione del giocatore dopo al mazziere

        if(primoGiocatore >= giocatori.size()) // se il mazziere e alla fine e devo partire dal primo giocatore
        {
            System.out.println("Distribuisco le carte partendo dal PRIMO giocatore");
            for(IGiocatore giocatore : giocatori)
            {
                giocatore.setCarta(mazzo.PescaCarta());
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

                giocatori.get(currentIndex).setCarta(mazzo.PescaCarta());

                currentIndex++;
                carteDistribuite++;
                System.out.println("Carte distribuite: " + carteDistribuite);
            }while(carteDistribuite < giocatori.size()); // fino a quando non do tot carte tanti quanti sono i player continuo a darle
        }

        System.out.println("Esco dalla distribuzione e do le info...");
        StampaInfoGiocatori();
    }

    private int trovaValoreCartaAlta(ArrayList<IGiocatore> lista)
    {
        int numeroPiuAlto = 1; // metto il valore piu piccolo della carta possibile (da noi il piu piccolo e' il piu alto)

        // NEW METHOD
        for (IGiocatore iGiocatore : lista) {
            if (iGiocatore.getCarta().getValore() >= numeroPiuAlto) {
                numeroPiuAlto = iGiocatore.getCarta().getValore(); // ho trovato una carta che supera quella che era piu piccola prima
            }
        }

        return numeroPiuAlto;
    }

    public boolean isGameRunning() { return giocatori.size() > 1; } // Restituisce true se ci sono piu di 1 giocatore vivi

    public int getCurrentRound(){return this.currentRound;}

    public void generaCodicePartita()  { this.codicePartita =  (int)(1 + (Math.random() * 1000)); } // TODO PREVEDERE CASO IN CUI VENGA GENERATO CODICE ESISTENTE

    public void generaPasswordPartita()  { this.passwordPartita =  (int)(1 + (Math.random() * 1000)); }


    public int getCodicePartita() { return this.codicePartita; }

    public int getPasswordPartita() { return this.passwordPartita; }

    // public boolean isGameRunning(){return this.isGameRunning;}

    public void aggiungiListaGiocatori(ArrayList<IGiocatore> giocatori) {this.giocatori = giocatori;} // # MODIFICARE - Controllare che il num dei player sia inferiore al max

    public IGiocatore getVincitore()
    {
        IGiocatore vincitore = null;
        if(!isGameRunning())
            vincitore = giocatori.get(0); //se è rimasto solo un giocatore

        return vincitore;
    }

    public int getCurrentGiocatorePos(){return this.currentGiocatorePos;}
    public IGiocatore getCurrentGiocatore(){return giocatori.get(currentGiocatorePos);}

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
            System.out.println("\n> Giocatore: " + currentGiocatore.getNome() +
                    ", Carta: " + currentGiocatore.getCarta().toString() +
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

    //endregion



}