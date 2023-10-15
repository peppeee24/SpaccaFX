package com.spaccafx.Manager;

import com.spaccafx.Controllers.ShareData;
import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.*;
import com.spaccafx.ExternalApps.*;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Player.*;
import com.spaccafx.Cards.*;

import java.util.*;

public class Partita
{
    //region #VARIABLES

    //region #PRIVATE VARIABLES
    int currentRound;
    int posMazziere;
    int codicePartita;
    boolean isGameRunning;
    SpaccaTGBot telegramBot;

    TavoloController TC;

    //endregion

    //region #PUBLIC VARIABLES
    public Mazzo mazzo = new Mazzo(); // creo il mazzo con tutte le carte
    public ArrayList<IGiocatore> giocatori;
    public ArrayList<IGiocatore> giocatoriMorti;
    //endregion

    //endregion

    public Partita(int size) // size è il numero di giocatori a partita
    {
        giocatori = new  ArrayList<IGiocatore>(size);
        giocatoriMorti = new ArrayList<IGiocatore>();


        this.posMazziere = 0;
        this.currentRound = 1; // quando si crea la partita parte gia dal round 1
        this.isGameRunning = true;

        this.telegramBot = new SpaccaTGBot(); // TODO CAMBIARE INIZILIAZZAZIONE BOT, DA METTERE SOLO QUANDO SI AVVIA IL PROGRAMMA E NON OGNI VOLTA CHE SI CREA UNA PARTITA


    }

    //region # GAME

    public void startGame()
    {

        preStartGame(); // Fase iniziale del gioco

        while(isGameRunning()) // fino a quando ci sono giocatori (piu di 1, altrimenti termina in automatico!!)
        {
            System.out.println("\n\tNUOVO ROUND: " + getCurrentRound());
            distrubuisciCarte(); // Distribuzione delle carte

            StampaInfoGiocatori(); // per debug iniziale

            runRound();

            StampaInfoGiocatori(); // per debug finale!

            System.out.println("\n\n\t ****** IL ROUND E FINITO!! ******");
            this.currentRound++;
        }

        System.out.println("[END GAME] GIOCO FINITO, 1 PLAYER RIMASTO VIVO. INVIO DATI SU TELEGRAM...");
        telegramBot.messaggioVincitorePartita(codicePartita, giocatori.get(0).getNome());
        this.isGameRunning = false; // gioco finito e metto dunque false
    }

    private void preStartGame()
    {
        lancioDadiIniziale(); // I giocatori effettuano il lancio dei dadi
        stabilisciMazziere(); // Viene decretato chi e il mazziere
    }

    private void runRound()
    {
        Scanner s = new Scanner(System.in);

        // devo partire dalla posizione del mazziere in poi
        boolean flag = true; // fino a quando non incontriamo il mazziere
        int c = posMazziere; // deve partire dal mazziere

        while(flag)
        {
            if(c+1 > giocatori.size() - 1)
                c = 0;
            else
                c++;

            System.out.println("Posizione attuale: " + c);

            IGiocatore currentGiocatore = giocatori.get(c);

            if(currentGiocatore.getRuolo() == RuoloGiocatore.MAZZIERE)
            {
                System.out.println("[MOSSA] Tocca a un (MAZZIERE)");

                if(currentGiocatore instanceof Bot) // se e un bot
                {
                    //SceltaNew(((Bot) currentGiocatore).Scelta(), currentGiocatore);
                    //TODO RISOLVERE SCELTA BOT1
                }
                else // se e un player normale
                    sceltaNew(s, currentGiocatore);

                flag = false; // MAZZIERE TROVATO! Dopo questa mossa finisce il ciclo
            }
            else // se il giocatore ha il ruolo GIOCATORE
            {
                System.out.println("[MOSSA] Tocca a un (GIOCATORE)");

                if(currentGiocatore instanceof Bot) // se e un bot
                {
                    //SceltaNew(((Bot) currentGiocatore).Scelta(), currentGiocatore);
                    //TODO RISOLVERE SCELTA BOT2
                }
                else // se e un player normale
                    sceltaNew(s, currentGiocatore);
            }
        }

        System.out.println("[GAME] Round FINITO!! CONTROLLO I RISULTATI...");

        controllaRisultati(); // prendo tutti i dati e li metto in un mapset
        ruotaMazziere();
        mazzo.MescolaMazzo();
    }

    //endregion

    //region #GAME SETUP

    public void lancioDadiIniziale()
    {
        //ShareData sharedData = ShareData.getInstance();

        //this.TC = sharedData.getTavoloController();

        for(int c=0; c<giocatori.size() ;c++)
        {
            IGiocatore editGiocatore = giocatori.get(c); // prendo il giocatore con le sue informazioni
            int valoreDado = lancioDadoSingolo(); // prendo valore a caso del dado


            //System.out.println(valoreDado);
            //TC.disableDice();

            System.out.println("\nIl giocatore " + editGiocatore.getNome() + " ha lanciato un dado ed e uscito: " + valoreDado);
            editGiocatore.setDado(valoreDado);

            giocatori.set(c, editGiocatore);
        }

        //TC.roll(4);
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
//TC.gestisciMazziere();
    }



    //endregion

    //region #METHODS

    public int lancioDadoSingolo() { return (int)(1 + Math.random() * (2));} // TODO CAMBIARE NUMERO FACCE DADO DA 1 A 6 NEL GIOCO FINALE

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

    private void distrubuisciCarte() // TODO LA DISTRIBUZIONE DELLE CARTE PARTE SEMPRE DALLO 0 E NON DALLA POSIZIONE DEL MAZZIERE IN POI
    {
      //  TC.impostazioneInizialeCarte();
        for(int c=0; c<giocatori.size() ;c++) // cambiare il ciclo for in un enhanced for
        {
            // NON SERVE CREARE ALTRI OGGETTI, BASTA CAMBIARE I DATI DI QUELL OGGETTO CON I SET

            IGiocatore giocatoreEdit = giocatori.get(c); // prendo il player attuale con tutte le sue informazioni e lo metto in un oggetto di tipo giocatore
            giocatoreEdit.setCarta(mazzo.PescaCarta()); // gli assegno una carta randomica dal mazzo

            giocatori.set(c, giocatoreEdit); // reimposto la cella di quel giocatore con il valore della carta presa dal mazzo;
        }
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

    private int getCurrentRound(){return this.currentRound;}

    private void mostraIstruzioni(IGiocatore giocatore)
    {
        System.out.println("\nGiocatore: " + giocatore.getNome() + " tocca a te fare una mossa! ");
        System.out.println("Carta: " + giocatore.getCarta().toString() + ", Vite: " + giocatore.getVita() + ", Ruolo: " + giocatore.getRuolo());
        System.out.println("1 - SCAMBIA LA CARTA CON QUELLO SUCCESSIVO / CON IL MAZZO (Se sei il mazziere)");
        System.out.println("2 - PASSA IL TURNO");
    }


    private void sceltaNew(Scanner s, IGiocatore currentGiocatore)
    {
        if(currentGiocatore.getCarta() instanceof CartaImprevisto)
        {
            ((CartaImprevisto)(currentGiocatore.getCarta())).Effetto(this,currentGiocatore); // gli mando questa classe
        }
        else if(currentGiocatore.getCarta() instanceof CartaProbabilita)
        {
            ((CartaProbabilita)(currentGiocatore.getCarta())).Effetto(this,currentGiocatore); // gli mando questa classe
        }

        mostraIstruzioni(currentGiocatore);

        switch (s.nextInt())
        {
            case 1: scambiaCarta(currentGiocatore); break;
            case 2: passaTurno(); break;
            default: System.out.println("Scelta NON ACCETTABILE!!"); break;
        }
    }


    public void scambiaCarta(IGiocatore currentGiocatore)
    {
        int currentIndexPlayer = giocatori.indexOf(currentGiocatore); // prendo il giocatore attuale
        Carta cartaPlayerAttuale = currentGiocatore.getCarta(); // prendo la sua carta

        if(currentGiocatore.getRuolo() == RuoloGiocatore.MAZZIERE) // se e il mazziere la scambio con il mazzo
        {
            giocatori.get(currentIndexPlayer).setCarta(mazzo.PescaCarta());
            System.out.println("Pesco dal mazzo la prossima carta perche sono un mazziere!");
        }
        else // sono un giocatore normale
        {
            IGiocatore nextPlayer;
            int nextIndexPlayer = currentIndexPlayer + 1;

            if(nextIndexPlayer >= giocatori.size()) // se sono oltre il limite
            {
                nextPlayer = giocatori.get(0); // prendo il primo giocatore
                nextIndexPlayer = 0;
            }
            else // altrimenti
                nextPlayer = giocatori.get(nextIndexPlayer); // prendo il prossimo giocatore

            System.out.println("Indirizzo PLAYER successivo: " + nextIndexPlayer);

            Carta cartaNextPlayer = nextPlayer.getCarta();
            nextPlayer.setCarta(cartaPlayerAttuale);
            giocatori.get(currentIndexPlayer).setCarta(cartaNextPlayer);
            System.out.println("Ho scambiato la carta con il player successivo!");
        }
    }

    public void passaTurno()
    {
        System.out.println("Ho passato il turno");
    }

    private void ruotaMazziere()
    {
        if(posMazziere+1 > giocatori.size() - 1)
        {
            if(posMazziere < giocatori.size())
            {
                giocatori.get(posMazziere).setRuolo(RuoloGiocatore.GIOCATORE);
                posMazziere = 0;
                giocatori.get(posMazziere).setRuolo(RuoloGiocatore.MAZZIERE);
            }
            else
            {
                posMazziere = posMazziere - 1;
                giocatori.get(posMazziere).setRuolo(RuoloGiocatore.GIOCATORE);
                posMazziere = 0;
                giocatori.get(posMazziere).setRuolo(RuoloGiocatore.MAZZIERE);
            }

        }
        else
        {
            giocatori.get(posMazziere).setRuolo(RuoloGiocatore.GIOCATORE);
            posMazziere++;
            giocatori.get(posMazziere).setRuolo(RuoloGiocatore.MAZZIERE);
        }
    }

    private void controllaRisultati() // con questo metodo capiamo a chi togliere la vita dei player
    {
        Map<Integer, ArrayList<IGiocatore>> mapSet = new HashMap<>();

        int maxValue = trovaValoreCartaAlta(giocatori);
        System.out.println("[GAME] La carta con il valore piu alto e: " + maxValue);


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

        if(giocatoriConValoreMassimo.size() <= 1) // se abbiamo solo un perdente, NO spareggio
        {
            System.out.println("[CHECK-GAME] * 1 SOLO Giocatore perdente!");
            giocatoriConValoreMassimo.get(0).setVita(giocatoriConValoreMassimo.get(0).getVita() - 1); // tolgo 1 vita
            System.out.println("(!) " + giocatoriConValoreMassimo.get(0).getNome() + " HA PERSO 1 VITA!!");

            if(giocatoriConValoreMassimo.get(0).getVita() <= 0) // se il giocatore in questione ha 0 o meno vite, viene ELIMINATO dalla partita
            {
                giocatoriMorti.add(giocatoriConValoreMassimo.get(0)); // viene messo nella lista degli eliminati
                giocatori.remove(giocatoriConValoreMassimo.get(0));
                System.out.println("\n\t[CHECK-GAME] ** (ELIMINATO) " + giocatoriConValoreMassimo.get(0).getNome() + " **");
            }
        }
        else // vuol dire che ci sono piu giocatori (spareggio)
        {
            System.out.println("[CHECK-GAME] * " +  giocatoriConValoreMassimo.size() + " MOLTEPLICI Giocatori perdenti!");

            // Controllo Seme
            IGiocatore giocatoreDebole = null;

            for(IGiocatore giocatore : giocatoriConValoreMassimo)
            {
                if(giocatoreDebole == null || ((((CartaNormale)giocatore.getCarta()).getSeme().compareTo(((CartaNormale)giocatoreDebole.getCarta()).getSeme())) > 0)) // TODO IMPORTANTE!! - FARE CONTROLLO / CONFRONTO ANCHE CON CARTE SPECIALI
                {
                    giocatoreDebole = giocatore;
                    System.out.println("Giocatore perdente attuale: " + giocatoreDebole.getNome());
                }
            }

            if(giocatoreDebole != null)
            {
                giocatoreDebole.setVita(giocatoreDebole.getVita() - 1); // tolgo 1 vita
                System.out.println("(!) " + giocatoreDebole.getNome() + " HA PERSO 1 VITA!!");

                if(giocatoreDebole.getVita() <= 0) // se il giocatore in questione ha 0 o meno vite, viene ELIMINATO dalla partita
                {
                    giocatoriMorti.add(giocatoreDebole); // viene messo nella lista degli eliminati
                    giocatori.remove(giocatoreDebole);
                    System.out.println("\n\t[CHECK-GAME] ** (ELIMINATO) " + giocatoreDebole.getNome() + " **");
                }

            }
        }
    }

    public void generaCodicePartita()  { this.codicePartita =  (int)(1 + (Math.random() * 1000)); } // TODO PREVEDERE CASO IN CUI VENGA GENERATO CODICE ESISTENTE

    public int getCodicePartita() { return this.codicePartita; }

    // public boolean isGameRunning(){return this.isGameRunning;}

    public void aggiungiListaGiocatori(ArrayList<IGiocatore> giocatori) {this.giocatori = giocatori;} // # MODIFICARE - Controllare che il num dei player sia inferiore al max

    public IGiocatore getVincitore()
    {
        IGiocatore vincitore = null;
        if(!isGameRunning())
            vincitore = giocatori.get(0); //se è rimasto solo un giocatore

        return vincitore;
    }

    //endregion

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
                //    ", Carta: " + currentGiocatore.getCarta().toString() +
                    ", Vite: " + currentGiocatore.getVita() +
                    ", Ruolo: " + currentGiocatore.getRuolo());
        }
    }

    //endregion


    public String getMazziereNome(){
        String nomeMazziere="";

        for(int i=0;i<giocatori.size();i++){

            if(giocatori.get(i).getRuolo()==RuoloGiocatore.MAZZIERE)
                nomeMazziere=giocatori.get(i).getNome();
        }
        return nomeMazziere;
    }




}