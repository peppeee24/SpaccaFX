package com.spaccafx.Manager;

import com.spaccafx.Interface.IGiocatore;

import java.util.ArrayList;

import java.util.Objects;

public class Torneo
{
    String codiceTorneo;
    ArrayList<Partita> partite;
    ArrayList<IGiocatore> giocatori;

    IGiocatore winner; //finchè è null vuol dire che non c'è ancora un vincitore del torneo

    private int currentMatch;

    private int nGiocatoriTot;

    private int nGiocatoriMatch;

    private int partenza;

    public Torneo(int nGiocatoriTot)
    {
        partite = new ArrayList<Partita>();
        giocatori = new ArrayList<IGiocatore>();

        this.nGiocatoriTot = nGiocatoriTot;
        this.currentMatch = 0;
        this.codiceTorneo = generaCodiceTorneo();
        this.winner = null;
        this.nGiocatoriMatch = trovaNGiocatori();
        this.partenza = 0;


        System.out.println("Generato codice torneo: " + codiceTorneo);
        System.out.println("NGiocatori totali: " + nGiocatoriTot);
        System.out.println("NTavoli: " + nGiocatoriTot/4);
    }

    public void StartTorneo()
    {
        System.out.println("\n Inizio torneo! ");

        while(ControllaFineTorneo())
        {
            System.out.println("Partita attuale: " + currentMatch);
            partite.add(new Partita(nGiocatoriMatch)); // inizializziamo una classe partita con n giocatori, perchè potrebbe essere da 2 o 4 giocatori

            partite.get(currentMatch).aggiungiListaGiocatori(getMatchPlayersToArrayList(partenza)); // prendo i player e li metto nella partita attuale
            partite.get(currentMatch).StartGame();
            //prevedere un modo per
            System.out.println("\nPartita " + currentMatch + " finita! Andiamo alla prossima..");
            aggiornaPartenza();

            currentMatch++;
        }
    }

    /*private void nextMatch()
    {

    }*/

    private String generaCodiceTorneo() // # MODIFICARE - Prevedere il caso venga generato un codice uguale a una partita gia esistente!
    {
        return "TORNEO_" + (int)(1 + (Math.random() * 1000));
    }

    private boolean ControllaFineTorneo()
    {
        boolean flag = true;
        if(winner==null){ //metodo alternativo al metodo isNull
            return flag;
        }
        else{
            flag=false;
            return flag;
        }

    }

    private void aggiungiGiocatoreTorneo(IGiocatore giocatore){this.giocatori.add(giocatore);}

    public int trovaNGiocatori(){
        if(giocatori.size()==2){
            return 2;
        }
        else return 4;
    }

    private ArrayList<IGiocatore> getMatchPlayersToArrayList(int partenza){ //implementare il fatto che i giocatori che hanno vinto rimangono dentro l'arrayList giocatori
        ArrayList<IGiocatore> matchPlayers = new ArrayList<IGiocatore>();
        for(int i = 0; i < trovaNGiocatori(); i++){
            if(i+partenza < giocatori.size()){
                matchPlayers.add(giocatori.get(i+partenza));
            }
            else{
             // capire cosa metterci
            }
        }
    return matchPlayers;
    }

    private int aggiornaPartenza(){
        return partenza++;
    }

}
