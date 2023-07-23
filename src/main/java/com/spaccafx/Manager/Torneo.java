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
            System.out.println("Creo partita con " + nGiocatoriMatch + " giocatori");
            System.out.println("Dimensione attuale arrayList player " + giocatori.size());
            nGiocatoriMatch = trovaNGiocatori(); //aggiorno prima di creare la partita i giocatori che ci sono nela partita
            partite.add(new Partita(nGiocatoriMatch)); // inizializziamo una classe partita con n giocatori, perchè potrebbe essere da 2 o 4 giocatori

            partite.get(currentMatch).aggiungiListaGiocatori(getMatchPlayersToArrayList(partenza)); // prendo i player e li metto nella partita attuale
            partite.get(currentMatch).StartGame();

            System.out.println("\nPartita " + currentMatch + " finita! Il vincitore è " + partite.get(currentMatch).getWinner().getNome() + " Andiamo alla prossima.."); //TODO metodo per ritornare il nome del vincitore e non il suo indirizzo
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
        System.out.println("SONO NEL METODO E DIMENSIONE = " + giocatori.size());
        if(giocatori.size()<4 && giocatori.size()>0){
            System.out.println("RETURN 2");
            return 2;
        }
        else return 4;
    }

    private ArrayList<IGiocatore> getMatchPlayersToArrayList(int partenza){
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
            System.out.println("Hai rimosso " + partite.get(currentMatch).giocatoriMorti.get(i).getNome());
        }
    }
    /*
    TODO NOTES:
    1) metodo per restituire il nome del vincitore e non il suo indirizzo(vedi riga 59) DONE
    2) sistemare un errore di exit da un'arrayList, errore in stabilisciMazziere nell'AL perdenti, da errore quando in finale si deve stabilire il mazziere, solo nel torneo (probabile errore di indici o di dimensione array)
    3) implementare una visualizzazione del metodo removeDeadPLayerFromArrayList così riusciamo a capire sempre quali player vengono tolti DONE
    4) ...
    */

}
