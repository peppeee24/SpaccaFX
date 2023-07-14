package com.spaccafx.Manager;

import com.spaccafx.Enums.RuoloGiocatore;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.Giocatore;
import com.spaccafx.Cards.*;

import java.util.*;

public class GameManager
{
    Mazzo mazzo = new Mazzo(); // creo il mazzo con tutte le carte

    ArrayList<IGiocatore> giocatori;
    ArrayList<IGiocatore> giocatoriMorti;

    private int currentRound = 1;
    private int posMazziere = 0;

    public GameManager(int size)
    {
        giocatori = new  ArrayList<IGiocatore>(size);
        giocatoriMorti = new ArrayList<IGiocatore>();
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
            System.out.println("\n\tNUOVO ROUND: " + getCurrentRound());
            DistrubuisciCarte(); // inzia distribuendo le carte

            StampaInfoGiocatori(); // per debug iniziale

            RunRound();
            StampaInfoGiocatori(); // per debug finale!

            System.out.println("\n\n\t ****** IL ROUND E FINITO!! ******");
            currentRound++;

        }
        System.out.println("Il gioco e finito perche c'e solo 1 player rimasto vivo!");

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
            if(c+1 > giocatori.size() - 1)
                c = 0;
            else
                c++;

            System.out.println("Posizione attuale: " + c);
            IGiocatore currentGiocatore = giocatori.get(c); // ERRORE QUI DA QUALCHE PARTE

            if(currentGiocatore.getRuolo() == RuoloGiocatore.MAZZIERE)
            {
                System.out.println("Tocca a un MAZZIERE");
                MostraIstruzioni(currentGiocatore); // mostra le istruzioni che puo fare il GIOCATORE attualmente
                if(currentGiocatore instanceof Bot){
                    Scelta(((Bot) currentGiocatore).Scelta(), currentGiocatore);
                }
                else{
                    mossa = s.nextInt();
                    Scelta(mossa, currentGiocatore);
                }

                flag = false; // abbiamo trovato il mazziere e quindi e apposto
            }
            else
            {
                System.out.println("Tocca a un GIOCATORE");
                MostraIstruzioni(currentGiocatore); // mostra le istruzioni che puo fare il GIOCATORE attualmente
                if(currentGiocatore instanceof Bot){
                    Scelta(((Bot) currentGiocatore).Scelta(), currentGiocatore);
                }
                else{
                    mossa = s.nextInt();
                    Scelta(mossa, currentGiocatore);
                }

            }
        }

        System.out.println("Round finito.. CONTROLLO I RISULTATI...");

        ControllaRisultati(); // prendo tutti i dati e li metto in un mapset
        RuotaMazziere();
    }

    //endregion

    //region #GAME SETUP

    private void LancioDadiIniziale() //verigicare se avere il metodo setDado basta solo nella classe padre
    {
        for(int c=0; c<giocatori.size() ;c++)
        {
            IGiocatore editGiocatore = giocatori.get(c); // prendo il giocatore con le sue informazioni
            int valoreDado = LancioDadoSingolo();

            System.out.println("\nIl giocatore " + editGiocatore.getNome() + " ha lanciato un dado ed e uscito: " + valoreDado);
            editGiocatore.setDado(valoreDado);

            giocatori.set(c, editGiocatore);
        }
    }

    private void StabilisciMazziere()
    {
        ArrayList<IGiocatore> perdenti = new ArrayList<IGiocatore>();
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

            for(IGiocatore perdente : perdenti)
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

    public void aggiungiGiocatore(IGiocatore giocatore){this.giocatori.add(giocatore);}

    private int TrovaValoreDadoAlto(ArrayList<IGiocatore> lista)
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

    private int TrovaValoreCartaAlta(ArrayList<IGiocatore> lista)
    {
        int numeroPiuAlto = 1; // metto il valore piu piccolo della carta possibile (da noi il piu piccolo e' il piu alto)

        for(int c=0; c<lista.size() ;c++)
        {
            if (lista.get(c).getCarta().getValore() >= numeroPiuAlto)
            {
                numeroPiuAlto = lista.get(c).getCarta().getValore(); // ho trovato una carta che supera quella che era piu piccola prima
            }
        }

        return numeroPiuAlto;
    }

    private void DistrubuisciCarte() // # MODIFICARE!! parte sempre dallo 0 e mai DALLA POSIZIONE DEL MAZZIERE
    {
        for(int c=0; c<giocatori.size() ;c++)
        {
            IGiocatore giocatoreEdit = giocatori.get(c); // prendo il player attuale con tutte le sue informazioni e lo metto in un oggetto di tipo giocatore
            giocatoreEdit.setCarta(mazzo.PescaCarta()); // gli assegno una carta randomica dal mazzo

            giocatori.set(c, giocatoreEdit); // reimposto la cella di quel giocatore con il valore della carta presa dal mazzo;
        }
    }

    private boolean IsGameRunning() // # MODIFICARE!! Se muoiono tutti i playere e ne rimangono 0??
    {
        if(giocatori.size() == 1){
            return false; // se ce solo un giocatore la partita finisce in automatico
        }
        else
        {
            if(giocatori.size()==0){
                System.out.println("Dimensione uguale a 0");
                return  false;
            }
            return true; // vuol dire che ci sono piu giocatori vivi
        }
    }

    private int getCurrentRound(){return this.currentRound;}

    private void MostraIstruzioni(IGiocatore giocatore)
    {
        System.out.println("\nGiocatore: " + giocatore.getNome() + " tocca a te fare una mossa! ");
        System.out.println("Carta: " + giocatore.getCarta().toString() + ", Vite: " + giocatore.getVita() + ", Ruolo: " + giocatore.getRuolo());

        System.out.println("1 - SCAMBIA LA CARTA CON QUELLO SUCCESSIVO/ CON IL MAZZO");
        System.out.println("2 - PASSA IL TURNO");
    }

    private void Scelta(int scelta, IGiocatore currentGiocatore)
    {
        switch (scelta)
        {
            case 1: ScambiaCarta(currentGiocatore); break;
            case 2: PassaTurno(); break;
            default: System.out.println("Scelta NON ACCETTABILE!!"); break;
        }
    }

    private void ScambiaCarta(IGiocatore currentGiocatore)
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

            // ERRORE QUI DA QUALCHE PARTE

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

    private void PassaTurno()
    {
        System.out.println("Ho passato il turno");
    }

    private void RuotaMazziere() // # SISTEMARE. Una volta che e finito il gioco e rimane solo 1 player, si cerca di assegnare un nuovo mazziere e da errore!
    {
        if(posMazziere+1 > giocatori.size() - 1)
        {
            if(posMazziere < giocatori.size()){ //POSSIBILE ERRORE in certi casi si cerca il mazziere ma non si trova la posizione, controllare questo if
                giocatori.get(posMazziere).setRuolo(RuoloGiocatore.GIOCATORE);
                posMazziere = 0;
                giocatori.get(posMazziere).setRuolo(RuoloGiocatore.MAZZIERE);
            }
            else{
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

    private void ControllaRisultati() // con questo metodo capiamo a chi togliere la vita dei player
    {
        Map<Integer, ArrayList<IGiocatore>> mapSet = new HashMap<>();

        int maxValue = TrovaValoreCartaAlta(giocatori);
        System.out.println("La carta con il valore piu alto e: " + maxValue);


        for (IGiocatore giocatore : giocatori)
        {
            if (giocatore.getCarta().getValore() == maxValue)
            {
                ArrayList<IGiocatore> giocatoriConValoreMassimo = mapSet.getOrDefault(maxValue, new ArrayList<>());
                giocatoriConValoreMassimo.add(giocatore);
                mapSet.put(maxValue, giocatoriConValoreMassimo);
                System.out.println("(!) " + giocatore.getNome() + " HA PERSO 1 VITA!!");
            }
        }

        ArrayList<IGiocatore> giocatoriConValoreMassimo = mapSet.get(maxValue);
        for (IGiocatore giocatore : giocatoriConValoreMassimo)
        {
            giocatore.setVita(giocatore.getVita() - 1); // Tolgo 1 vita al giocatore che ha perso dalle vite che aveva prima

            if(giocatore.getVita() <= 0) // se il giocatore in questione ha 0 o meno vite, viene eliminato dalla partita
            {
                giocatoriMorti.add(giocatore); // viene messo nella lista degli eliminati
                giocatori.remove(giocatore);
                System.out.println("\n\t** (ELIMINATO) " + giocatore.getNome() + " **");
            }

        }

    }

    //endregion

    //region #DEBUG

    private void MostraDadiGicatoriAttuali(ArrayList<IGiocatore> lista)
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
            IGiocatore currentGiocatore = giocatori.get(c);

            System.out.println("\n> Giocatore: " + currentGiocatore.getNome() +
                    ", Carta: " + currentGiocatore.getCarta().toString() +
                    ", Vite: " + currentGiocatore.getVita() +
                    ", Ruolo: " + currentGiocatore.getRuolo());
        }
    }


    //endregion

}
