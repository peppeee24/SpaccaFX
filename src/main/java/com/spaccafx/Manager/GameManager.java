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

    //region # GAME

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

    //endregion

    //region #GAME SETUP

    private void LancioDadiIniziale()
    {
        for(int c=0; c<giocatori.size() ;c++)
        {
            Giocatore editGiocatore = giocatori.get(c); // prendo il giocatore con le sue informazioni
            int valoreDado = LancioDadoSingolo();

            System.out.println("\nIl giocatore " + editGiocatore.getNome() + " ha lanciato un dado ed e uscito: " + valoreDado);
            editGiocatore.setDado(valoreDado);

            giocatori.set(c, editGiocatore);
        }
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


        while (perdenti.size() > 1) // Fino a quando ci sono piu perdenti
        {
            System.out.println("\n** PIU PERSONE HANNO IL DADO CON LO STESSO VALORE ** \n");

            for(Giocatore perdente : perdenti)
            {
                perdente.setDado(LancioDadoSingolo()); // faccio ritirare i dadi ai giocatori perdenti
            }

            MostraDadiGicatoriAttuali(perdenti); // faccio una sorta di debug per vedere i dadi che sono usciti
            int spareggioValorePiuAlto = TrovaValoreDadoAlto(perdenti); // controllo quale dei perdenti che ha rilanciato ha il valore piu alto
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
    }

    //endregion

    //region #METHODS

    private int LancioDadoSingolo()
    {
        return (int)(1 + Math.random() * (2)); // un valore a caso da 1 a 6
    }

    public void aggiungiGiocatore(Giocatore giocatore){this.giocatori.add(giocatore);}

    private int TrovaValoreDadoAlto(ArrayList<Giocatore> lista)
    {
        int numeroPiuAlto = 1; // metto il valore piu piccolo del dado possibile (da noi il piu piccolo e' il piu alto)

        for(int c=0; c<lista.size() ;c++)
        {
            if (lista.get(c).getValoreDado() >= numeroPiuAlto)
            {
                numeroPiuAlto = lista.get(c).getValoreDado();
            }
        }

        return numeroPiuAlto;
    }

    private void DistrubuisciCarte() // # MODIFICARE!! parte sempre dallo 0 e mai DALLA POSIZIONE DEL MAZZIERE
    {
        for(int c=0; c<giocatori.size() ;c++)
        {
            Giocatore giocatoreEdit = giocatori.get(c); // prendo il player attuale con tutte le sue informazioni e lo metto in un oggetto di tipo giocatore
            giocatoreEdit.setCarta(mazzo.PescaCarta()); // gli assegno una carta randomica dal mazzo

            giocatori.set(c, giocatoreEdit); // reimposto la cella di quel giocatore con il valore della carta presa dal mazzo;
        }
    }

    private boolean IsGameRunning() // # MODIFICARE!! Se muoiono tutti i playere e ne rimangono 0??
    {
        if(giocatori.size() == 1) return false; // se ce solo un giocatore la partita finisce in automatico
        else return true; // vuol dire che ci sono piu giocatori vivi
    }

    private int getCurrentRound(){return this.currentRound;}

    private void MostraIstruzioni(Giocatore giocatore)
    {
        System.out.println("\nGiocatore: " + giocatore.getNome() + " tocca a te fare una mossa! ");
        System.out.println("Carta: " + giocatore.getCarta().toString() + ", Vite: " + giocatore.getVita() + ", Ruolo: " + giocatore.getRuolo());

        System.out.println("1 - SCAMBIA LA CARTA CON QUELLO SUCCESSIVO/ CON IL MAZZO");
        System.out.println("2 - PASSA IL TURNO");
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

    private void ScambiaCarta(Giocatore currentGiocatore)
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
            Giocatore nextPlayer;
            int nextIndexPlayer = currentIndexPlayer + 1;

            // ERRORE QUI DA QUALCHE PARTE

            if(nextIndexPlayer > giocatori.size()) // se sono oltre il limite
                nextPlayer = giocatori.get(0); // prendo il primo giocatore
            else // altrimenti
                nextPlayer = giocatori.get(nextIndexPlayer); // prendo il prossimo giocatore

            Carta cartaNextPlayer = nextPlayer.getCarta();
            nextPlayer.setCarta(cartaPlayerAttuale);
            giocatori.get(currentIndexPlayer).setCarta(cartaNextPlayer);
            System.out.println("Ho scambiato la carta con il player successivo!");

        }
    }

    private void PassaTurno()
    {
        System.out.println("Ho passato il turno");
    }

    //endregion

    //region #DEBUG

    private void MostraDadiGicatoriAttuali(ArrayList<Giocatore> lista)
    {
        for (int c=0; c<lista.size();c++)
        {
            System.out.println("Giocatore: " + lista.get(c).getNome() + " Dado: " + lista.get(c).getValoreDado());
        }
    }

    private void StampaInfoGiocatori()
    {
        for(int c=0; c<giocatori.size() ;c++)
        {
            Giocatore currentGiocatore = giocatori.get(c);

            System.out.println("\n> Giocatore: " + currentGiocatore.getNome() +
                    ", Carta: " + currentGiocatore.getCarta().toString() +
                    ", Vite: " + currentGiocatore.getVita() +
                    ", Ruolo: " + currentGiocatore.getRuolo());
        }
    }


    //endregion







    private void ControllaRisultati() // con questo metodo capiamo a chi togliere la vita dei player
    {

    }

    private void RunRound()
    {
        Scanner s = new Scanner(System.in);

        // devo partire dalla posizione del mazziere in poi
        boolean flag = true; // fino a quando non incontriamo il mazziere
        int c = posMazziere; // deve partire dal mazziere
        int mossa = 0;

        while(flag)
        {
            if(c+1 > giocatori.size())
                c = 0;
            else
                c+=1;

            System.out.println("Posizione attuale: " + c);
            Giocatore currentGiocatore = giocatori.get(c); // ERRORE QUI DA QUALCHE PARTE

            if(currentGiocatore.getRuolo() == RuoloGiocatore.MAZZIERE)
            {
                System.out.println("Tocca a un MAZZIERE");
                MostraIstruzioni(currentGiocatore); // mostra le istruzioni che puo fare il MAZZIERE attualmente
                mossa = s.nextInt();
                Scelta(mossa, currentGiocatore);

                flag = false; // abbiamo trovato il mazziere e quindi e apposto
            }
            else
            {
                System.out.println("Tocca a un GIOCATORE");
                MostraIstruzioni(currentGiocatore); // mostra le istruzioni che puo fare il GIOCATORE attualmente
                mossa = s.nextInt();
                Scelta(mossa, currentGiocatore);
            }
        }

        System.out.println("Round finito.. CONTROLLO I RISULTATI...");
    }
}
