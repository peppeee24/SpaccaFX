package com.spaccafx.Manager;

import com.spaccafx.Interface.IGiocatore;

import java.util.ArrayList;

public class Torneo
{
    String codiceTorneo;
    ArrayList<Partita> partite;
    ArrayList<IGiocatore> giocatori;

    private int currentMatch;
    private int nPartite;

    public Torneo(int nPartite)
    {
        partite = new ArrayList<Partita>(nPartite);
        giocatori = new ArrayList<IGiocatore>();

        this.nPartite = nPartite;
        this.currentMatch = 0;
        this.codiceTorneo = generaCodiceTorneo();


        System.out.println("Generato codice torneo: " + codiceTorneo);
        System.out.println("NPartite totali: " + nPartite);
    }

    public void StartTorneo()
    {
        System.out.println("\n Inizio torneo! ");

        while(ControllaFineTorneo(currentMatch))
        {
            System.out.println("Partita attuale: " + currentMatch);
            partite.add(new Partita(4)); // inizializziamo una classe partita con 4 giocatori

            partite.get(currentMatch).aggiungiListaGiocatori(giocatori); // prendo i player e li metto nella partita attuale
            partite.get(currentMatch).StartGame();

            System.out.println("\nPartita " + currentMatch + " finita! Andiamo alla prossima..");

            currentMatch++;
        }
    }

    private void nextMatch()
    {

    }

    private String generaCodiceTorneo() // # MODIFICARE - Prevedere il caso venga generato un codice uguale a una partita gia esistente!
    {
        return "TORNEO_" + (int)(1 + (Math.random() * 1000));
    }

    private boolean ControllaFineTorneo(int c)
    {
        boolean b = c < nPartite;
        System.out.println("Ci sono altre partite: " + b);
        return b;
    }

    public void aggiungiGiocatoreTorneo(IGiocatore giocatore){this.giocatori.add(giocatore);}

}
