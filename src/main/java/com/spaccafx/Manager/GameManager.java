package com.spaccafx.Manager;

import com.spaccafx.Enums.RuoloGiocatore;
import com.spaccafx.Player.Giocatore;
import com.spaccafx.Cards.*;

import java.util.*;

public class GameManager
{
    Mazzo mazzo = new Mazzo(); // creo il mazzo con tutte le carte


    ArrayList<Giocatore> giocatori;

    private int currentRound = 1;
    private int posMazziere = 0;

    public GameManager(int size)
    {
        giocatori = new  ArrayList<Giocatore>(size);
    }

    public void StartGame()
    {
        PreStartGame(); // la fase di PreGame, va messa sempre all inizio!!

        while(IsGameRunning()) // fino a quando ci sono giocatori (piu di 1, altrimenti termina in automatico!!)
        {
            System.out.println("\tNUOVO ROUND: " + getCurrentRound());
            DistrubuisciCarte(); // inzia distribuendo le carte

            StampaInfoGiocatori(); // per debug iniziale

            RunRound();

            ControllaRisultati(); // controlla i risultati finali
            StampaInfoGiocatori(); // per debug finale!

            currentRound++;


        }
        System.out.println("Il gioco e finito perche c'e solo 1 player rimasto vivo!");

    }

    private void PreStartGame()
    {
        System.out.println("\tIL GIOCO SPACCAFX E INIZIATO!!!");
        System.out.println("\t **** FASE PREPARATIVA DI GIOCO ****");
        System.out.println("\n* REGOLE DEL DADO *");
        System.out.println("1) Il giocatore con il punteggio del dado piu ALTO perde. In caso di parita si continua fino a uno spareggio!");
        System.out.println("2) In caso di parita si continua fino a uno spareggio!");
        System.out.println("3) Chi perde diventa un MAZZIERE, il restante sono GIOCATORI NORMALI!");

        LancioDadiIniziale(); // i giocatori effettuano il lancio dei dadi

        StabilisciMazziere(); // viene decretato chi e il mazziere
    }

    public void LancioDadiIniziale()
    {

        for(int c=0; c<giocatori.size() ;c++)
        {
            Giocatore editGiocatore = giocatori.get(c); // prendo il giocatore con le sue informazioni

            System.out.println("\n IL GIOCATORE " + editGiocatore.getNome() + " HA LANCIATO UN DADO!");
            int valoreDado = LancioDadoSingolo();
            System.out.println("E USCITO IL NUMERO: " + valoreDado);
            editGiocatore.setDado(valoreDado);

            giocatori.set(c, editGiocatore);
        }
    }

    private int LancioDadoSingolo()
    {
        return (int)(1 + Math.random() * (2)); // un valore a caso da 1 a 6
    }

    private void StabilisciMazziere()
    {
        ArrayList<Giocatore> perdenti = new ArrayList<Giocatore>();
        int valorePiuAlto = TrovaValoreDadoAlto(giocatori);

        for(int c=0; c<giocatori.size() ;c++)
        {
            if(giocatori.get(c).getValoreDado() >= valorePiuAlto) // per sicurezza metto maggiore uguale
            {
                perdenti.add(giocatori.get(c)); // metto il perdente dentro la lista e non gli assegno ancora il ruolo
            }
            else
            {
                giocatori.get(c).setRuolo(RuoloGiocatore.GIOCATORE); // se ha un valore piu basso, allora e per forza di cose un player
            }
        }

        // VERIFICO SE CE LA PRESENZA DI SPAREGGIO DA EFFETTUARE


        /*
            MI DA I PROBLEMI PORCODIO
            DA SISTEMARE ASSOLUTAMENTE
            DA ERRORI E VA IN LOOP ALCUNE VOLTE PENSARE ALLA STRUTTURA NUOVAMENTE!!
            SE CE SOLO 1 GIOCATORE PERDENTE FUNZIONA (IN TEORIA!)
         */

        while (perdenti.size() > 1)
        {
            System.out.println("\n\t** CI SONO PIU PERSONE CHE SONO PERDENTI!! **");
            int spareggioValorePiuAlto = TrovaValoreDadoAlto(perdenti);

            for(Giocatore perdente : perdenti)
            {
                perdente.setDado(LancioDadoSingolo());
            }

            for(Giocatore perdente : perdenti)
            {
                //MostraCurrentPlayerInfo(perdente);
                if(perdente.getValoreDado() < spareggioValorePiuAlto) // per sicurezza metto maggiore uguale
                {
                    System.out.println("OK 1 GRANDEZZA PERDENTI: " + perdenti.size());
                    perdente.setRuolo(RuoloGiocatore.GIOCATORE); // se ha un valore piu basso, allora e per forza di cose un player
                    System.out.println("OK 2");
                    perdenti.remove(perdente);
                    System.out.println("OK 3 GRANDEZZA PERDENTI: " + perdenti.size());
                }
            }

        }

        int currentPosMazziere = 0; // IL GIOCATORE NON CAMBIANO LA POSIZIONE CON IL MAZZIERE, MA SOLAMENTE IL RUOLO. BASTA CHE CAMBIA IL RUOLO DI ESSI ALL INTERNO DEL GIOCO

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

        Giocatore appoggio = giocatori.get(0);
        Giocatore mazziere = perdenti.get(0);

        System.out.println("\n\t** NUOVO MAZZIERE IN CIRCOLAZIONE ("+ giocatori.get(posMazziere).getNome() + ") **");

        System.out.println("\n\t** Lo metto nella posizione 0 dell arraylist di giocatori!");
        giocatori.set(0, mazziere);
        giocatori.set(0, mazziere);
    }

    private int TrovaValoreDadoAlto(ArrayList<Giocatore> lista)
    {
        int numeroPiuAlto = 1; // metto il valore piu piccolo del dado possibile

        for(int c=0; c<lista.size() ;c++)
        {
            if (lista.get(c).getValoreDado() > numeroPiuAlto)
            {
                numeroPiuAlto = lista.get(c).getValoreDado();
            }
        }

        return numeroPiuAlto;
    }


    public void aggiungiGiocatore(Giocatore giocatore){this.giocatori.add(giocatore);}

    public void DistrubuisciCarte()
    {
        for(int c=0; c<giocatori.size() ;c++)
        {
            Giocatore giocatoreEdit = giocatori.get(c); // prendo il player attuale con tutte le sue informazioni e lo metto in un oggetto di tipo giocatore
            giocatoreEdit.setCarta(mazzo.PescaCarta()); // gli assegno una carta randomica dal mazzo

            giocatori.set(c, giocatoreEdit); // reimposto la cella di quel giocatore con il valore della carta presa dal mazzo;
        }
    }

    public void StampaInfoGiocatori()
    {
        for(int c=0; c<giocatori.size() ;c++)
        {
            System.out.println("\nINFORMAZIONI PLAYER: " + giocatori.get(c).getNome());
            System.out.println("* CARTA ATTUALE: " + giocatori.get(c).getCarta().toString());
            System.out.println("<3 VITE: " + giocatori.get(c).getVita());
        }
    }

    public boolean IsGameRunning()
    {
        if(giocatori.size() == 1) return false; // se ce solo un giocatore la partita finisce in automatico
        else return true; // vuol dire che ci sono piu giocatori vivi
    }

    private void Scelta(int scelta, Giocatore currentGiocatore)
    {
        switch (scelta)
        {
            case 1: ScambiaCarta(currentGiocatore); break;
            case 2: PassaTurno(); break;
            default: System.out.println("Scelta NON ACCETTABILE!!"); break;
        }
    }

    public int getCurrentRound(){return this.currentRound;}
    private void MostraIstruzioni(Giocatore giocatore)
    {
        System.out.println("\n" + giocatore.getNome() + " TOCCA A TE FARE UNA MOSSA!");
        System.out.println("1 - SCAMBIA LA CARTA CON QUELLO SUCCESSIVO/ CON IL MAZZO");
        System.out.println("2 - PASSA IL TURNO");
    }

    private void MostraCurrentPlayerInfo(Giocatore giocatore)
    {
        System.out.println("\nGIOCATORE ATTUALE: " + giocatore.getNome());
        System.out.println("CARTA IN POSSESSO: " + giocatore.getCarta().toString());
        System.out.println("RUOLO GIOCATORE: " + giocatore.getRuolo());
    }

    private void ScambiaCarta(Giocatore currentGiocatore)
    {
        int currentIndexPlayer = giocatori.indexOf(currentGiocatore);
        Carta cartaPlayerAttuale = currentGiocatore.getCarta();

        if(currentGiocatore.getRuolo() == RuoloGiocatore.MAZZIERE)
        {
            giocatori.get(currentIndexPlayer).setCarta(mazzo.PescaCarta());
            System.out.println("Pesco dal mazzo la prossima carta perche sono un mazziere!");
        }
        else
        {
            Giocatore nextPlayer;

            if(currentIndexPlayer++ > giocatori.size())
                nextPlayer = giocatori.get(0); // prendo il primo giocatore
            else
                nextPlayer = giocatori.get(currentIndexPlayer + 1); // prendo il prossimo giocatore

            Carta cartaNextPlayer = nextPlayer.getCarta();
            nextPlayer.setCarta(cartaPlayerAttuale);
            giocatori.get(currentIndexPlayer).setCarta(cartaNextPlayer);
            System.out.println("Ho scambiato la carta con il player successivo!");

        }
    }

    private void PassaTurno()
    {

    }

    private void ControllaRisultati() // con questo metodo capiamo a chi togliere la vita dei player
    {

    }

    private void RunRound()
    {
        Scanner s = new Scanner(System.in);

        // devo partire dalla posizione del mazziere in poi

        boolean flag = true; // fino a quando non incontriamo il mazziere
        int c = 0;
        int mossa = 0;

        while(flag)
        {
            if(flag && c > giocatori.size())
                c = 0;

            if(giocatori.get(c).getRuolo() != RuoloGiocatore.MAZZIERE)
            {
                System.out.println("Tocca a un MAZZIERE");
                MostraCurrentPlayerInfo(giocatori.get(c));
                MostraIstruzioni(giocatori.get(c)); // mostra le istruzioni che puo fare il giocatore attualmente
                // allora il mazziere ha una scelta diversa per cambiare le carte con il mazzo
                mossa = s.nextInt();
                Scelta(mossa, giocatori.get(c));

                flag = true; // abbiamo trovato il mazziere e quindi e apposto
            }
            else
            {
                System.out.println("Tocca a un PLAYER");
                MostraCurrentPlayerInfo(giocatori.get(c));
                MostraIstruzioni(giocatori.get(c)); // mostra le istruzioni che puo fare il giocatore attualmente
                mossa = s.nextInt();

                Scelta(mossa, giocatori.get(c));
            }

            c++;
        }
    }

}
