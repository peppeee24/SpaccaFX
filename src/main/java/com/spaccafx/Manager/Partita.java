package com.spaccafx.Manager;

import com.spaccafx.Controllers.ShareData;
import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.*;
import com.spaccafx.ExternalApps.*;
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

    public void preStartGame()
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

    private void avanzaManoUI()
    {
        Thread thread = new Thread(() -> {
            try {

                Platform.runLater(() ->
                {
                    if(cDistaccoMazziere+1 > giocatori.size() - 1)
                        cDistaccoMazziere = 0;
                    else
                        cDistaccoMazziere++;


                    this.currentGiocatorePos = cDistaccoMazziere;
                    TC.updateCarteUI(); // aggiorna le carte dei player graficamente

                    String mossa = "Tocca al giocatore " + getCurrentGiocatore().getNome().toUpperCase() + " ruolo " + getCurrentGiocatore().getRuolo();
                    TC.mostraBannerAttesa("MOSSA", mossa);
                });

                Thread.sleep(3000);

                Platform.runLater(() -> {
                    TC.nascondiBannerAttesa();

                    IGiocatore currentGiocatore = getCurrentGiocatore();

                    if(currentGiocatore instanceof Bot)
                    {
                        TC.gestisciPulsanti(false, false, false); // facciamo scomparire i bottoni ai bot
                        System.out.println("[GAME] Tocca al BOT: " + currentGiocatore.getNome() + " in posizione: " + currentGiocatorePos);
                        controlloMano(currentGiocatorePos);

                        System.out.println("[GAME] Info: " + currentGiocatore.getCarta().toString());


                        if(!(currentGiocatore.getCarta() instanceof CartaImprevisto))
                            ((Bot) currentGiocatore).SceltaBotUI(this, TC);
                    }
                    else // e un player
                    {
                        TC.gestisciPulsanti(false, true, true);
                        System.out.println("[GAME] Tocca al giocatore: " + currentGiocatore.getNome() + " in posizione: " + currentGiocatorePos);
                        controlloMano(currentGiocatorePos);
                        System.out.println("[GAME] Info: " + currentGiocatore.getCarta().toString());
                    }

                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();

    }

    //TODO CONTROLLARE LE MOSSE DEI BOT CON PROBABILITA

    private boolean controlloMano(int currentGiocatore) // restituisce TRUE se ha carta probabilita con effetto NON attivo, e FALSE con le carte NORMALI, Imprevisto e carte speciali con effetti GIA ATTIVATI
    {
        System.out.println("[DEBUG] SONO IN CONTROLLA MANO");

        // controlliamo che carta ha e cosa attivare o meno graficamente
        Carta currentMano = giocatori.get(currentGiocatore).getCarta();

        if(currentMano instanceof  CartaProbabilita)
        {
            System.out.println("[MANO] Ho una carta speciale PROBABILITA");

            if(!((CartaProbabilita) currentMano).isAttivato()) // caso in cui l'effetto della carta non sia stato attivato
            {
                // lancio dadi aumento vita
                // scambio con mazzo

                ((CartaProbabilita)currentMano).Effetto(this, giocatori.get(currentGiocatore), TC);
                System.out.println("[MANO] Attivato effetto carta: "+currentMano.toString() );
                return true; // da true perche posso fare eventualmente altre mosse
            }
            else
                System.out.println("[MANO] Impossibile attivare effetto! GIA ATTIVATO!");

        }
        else if(currentMano instanceof  CartaImprevisto)
        {
            System.out.println("[MANO] Ho una carta speciale IMPREVISTO");
            // passa Obbligatorio
            // Scambio obbligatorio

            if(!((CartaImprevisto) currentMano).isAttivato()) // caso in cui l'effetto della carta non sia stato attivato
            {
                ((CartaImprevisto)currentMano).Effetto(this, giocatori.get(currentGiocatore), TC);
                System.out.println("[MANO] Attivato effetto carta: "+currentMano.toString() );
                return false; // non posso fare altre mosse anche se attivo l effetto
            }
            else
                System.out.println("[MANO] Impossibile attivare effetto! GIA ATTIVATO!");
        }
        else // debug
        {
            System.out.println("[MANO] Ho una carta NORMALE");
        }

        return false;

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


                Thread.sleep(3000);

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
    }

    private void gestisciScambioCarta()
    {
        Carta cartaPlayerAttuale = getCurrentGiocatore().getCarta(); // prendo la sua carta

        if(getCurrentGiocatore().getRuolo() == RuoloGiocatore.MAZZIERE) // se e il mazziere la scambio con il mazzo
        {
            getCurrentGiocatore().setCarta(mazzo.PescaCartaSenzaEffetto());
            TC.updateCarteUI();

            System.out.println("[GAME] Sono MAZZIERE e pesco dal mazzo la prossima carta perche sono un mazziere! Ho preso: " + getCurrentGiocatore().getCarta().toString());
            // se pesco una carta speciale, l effetto non viene attivato e vale come carta normale, quindi non ce bisogno del controlloMano() per verificare se attivare o meno
            // l'effetto.


            System.out.println("[GAME] Round finito!! Passo al controllo dei risultati");
            controllaRisultatiUI(); // alla fine della mossa del mazziere, controllo i risultati

        }
        else // sono un giocatore normale NON MAZZIERE
        {
            if(!cartaGiaScambiata) // la carta non e stata ancora scambiata
            {
                System.out.println("[GAME] Non ho ancora scambiato, posso prendere la carta del player dopo");
                //TC.mostraBannerAttesa("SCAMBIO", "Carta scambiata con il giocatore successivo!");
                eseguiScambioPlayerSuccessivo(cartaPlayerAttuale);
            }

            // la carta, non puo attivare l'effetto perche il metodo prosegue avanti e richiama avanzaManoUI;

            if(controlloMano(currentGiocatorePos) && !cartaGiaScambiata) // se dopo lo scambio ha una carta speciale e ho scambiato gia la carta, attesa della risposta
            {
                System.out.println("[GAME] Scambiando ho preso una carta effetto!");

                if(getCurrentGiocatore() instanceof  Bot) // deve riscegliere la mossa
                {
                    cartaGiaScambiata = true; // dico che e stata gia scambiata la carta
                    System.out.println("[BOT] Carta gia scambiata: " + cartaGiaScambiata);
                    // todo controllare
                    passaTurnoUI();
                    //((Bot) getCurrentGiocatore()).SceltaBotUI(this, TC); // metodo bot

                }
                else // sceglie il player
                {
                    cartaGiaScambiata = true;
                    System.out.println("[PLAYER] Carta gia scambiata: " + cartaGiaScambiata);
                    TC.gestisciPulsanteScambio(false);
                }
            }
            else
            {
                // TODO QUANDO SI ATTIVA EFFETTO IMPREVISTO E PASSA IL TURNO, ESSENDO CHE DEVE FINIRE QUESTO METODO RIPASSA
                System.out.println("[DEBUG-SCAMBIO] Non ho preso nessuna carta effetto scambiando oppure  ho una carta effetto gia attivata!");
                System.out.println("[GAME] Passo al prossimo giocatore!");
                reimpostaGrafica(); // reimposta la grafica di default
                cartaGiaScambiata = false;


                avanzaManoUI();

                /*if(!(cartaPlayerAttuale instanceof CartaImprevisto))
                    avanzaManoUI();
                else
                {
                    if(!((CartaImprevisto) cartaPlayerAttuale).isAttivatoSpecial())
                        avanzaManoUI();
                }

                 */

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
                    cartaGiaScambiata = false;

                    if (getCurrentGiocatore().getRuolo() == RuoloGiocatore.MAZZIERE)
                    {
                        // finisce il round e controlla i risultati
                        System.out.println("[GAME] Round finito, controllo i risultati..");

                        // attesa..
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
                        }
                        else
                        {
                            System.out.println("[CHECK-GAME] " + giocatoreDebole.getNome() + " tolta vita NORMALE del giocatore");
                            giocatoreDebole.setVita(giocatoreDebole.getVita() - 1); // tolgo 1 vita
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
                    else
                        risultato = "Il giocatore " + giocatoreDebole.getNome().toUpperCase() + " ha perso una vita!";


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
        lancioDadiIniziale();
        mazzo.MescolaMazzo();

        this.currentRound ++;
        TC.aggiornaInfoUI();

        iniziaNuovoRoundUI();
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

    private boolean isGameRunning() { return giocatori.size() > 1; } // Restituisce true se ci sono piu di 1 giocatore vivi

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

    //endregion



}