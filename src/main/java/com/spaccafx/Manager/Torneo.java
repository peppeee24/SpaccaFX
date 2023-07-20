package com.spaccafx.Manager;

import com.spaccafx.Interface.IGiocatore;

import java.util.ArrayList;

import java.util.Objects;

public class Torneo
{
    String codiceTorneo;
    ArrayList<Partita> partite;
    ArrayList<IGiocatore> giocatori;

    //ArrayList<IGiocatore> vincitori;

    IGiocatore winner; //finchè è null vuol dire che non c'è ancora un vincitore del torneo

    private int currentMatch;

    private int nGiocatoriTot;

    private int nGiocatoriMatch;


    private int partenza;

    public Torneo(int nGiocatoriTot)
    {
        partite = new ArrayList<Partita>(nGiocatoriMatch);
        giocatori = new ArrayList<IGiocatore>(nGiocatoriTot);

        this.nGiocatoriTot = nGiocatoriTot;
        this.currentMatch = 0;
        this.codiceTorneo = generaCodiceTorneo();
        this.winner = null;
        this.nGiocatoriMatch = trovaNGiocatori();
        this.partenza = 0;


        System.out.println("Generato codice torneo: " + codiceTorneo);
        System.out.println("NGiocatori totali: " + nGiocatoriTot);
        System.out.println("NTavoli: " + nGiocatoriTot/4);
        System.out.println("NGiocatori per tavolo "+ nGiocatoriMatch);
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

            System.out.println("\nPartita " + currentMatch + " finita! Il vincitore è " + partite.get(currentMatch).getWinner() + " Andiamo alla prossima..");
            aggiornaPartenza();
            removeDeadPLayerFromArrayList();
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

    public void aggiungiGiocatoreTorneo(IGiocatore giocatore){this.giocatori.add(giocatore);}

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
        }
    return matchPlayers;
    }

    private int aggiornaPartenza(){
        return partenza++;
    }

    private void removeDeadPLayerFromArrayList(){
        for(int i = 0; i < partite.get(currentMatch).giocatoriMorti.size(); i++){
            giocatori.remove(partite.get(currentMatch).giocatoriMorti.get(i));
        }
    }

}
