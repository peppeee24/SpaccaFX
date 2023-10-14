package com.spaccafx.Cards;

import com.spaccafx.Enums.RuoloGiocatore;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;

import java.util.Scanner;

public class CartaImprevisto extends Carta
{
    boolean attivato = false;

    public CartaImprevisto(int valore)
    {
        super(valore);
    }

    @Override
    public String toString(){return this.getValore() + " di IMPREVISTO";}

    @Override
    public void Effetto(Partita partita, IGiocatore currentGiocatore)
    {
        if(attivato==true)
            return;

        //partita.mazzo.StampaMazzo(); // DA CANCELLARE PER DEBUG!, se commentato non da più il problema del cambio di effetto

        /*this.partitaAttuale = partita;
        this.giocatoreAttuale=currentGiocatore;*/
        //System.out.println("Ho in mano una carta effetto e non posso fare altre scelte!");

        int scelta = (int)((1 + Math.random() * 2)); //genero o 1 o 2 che sono i "codici" delle scelte;

        switch(scelta)
        {
            case 1:  PerdiVitaConDado(partita, currentGiocatore);break;
            case 2:  ObbligoScambioConMazzo(partita, currentGiocatore); break;
            //case 3: ScopriTuaCarta(); break;
            default: break;
        }
        attivato=true;
    }


    private void ScopriTuaCarta(Partita partita, IGiocatore giocatore){
        //TODO in codice non è utile, va codificato poi con la parte graFICA
    }

    private void ObbligoScambioConMazzo(Partita partita, IGiocatore currentGiocatore){
        System.out.println("IMPREVISTO! - Sei obbligato a scambiare la tua carta con una dal mazzo");
        Carta newCarta = partita.mazzo.PescaCarta();
        partita.giocatori.get(partita.giocatori.indexOf(currentGiocatore)).setCarta(newCarta);
        System.out.println("La carta che hai pescato è: " + currentGiocatore.getCarta().getValore()); //TODO nuovo metodo per fare in modo che ti dica il valore e di che tipo è la carta ad esempio: 2 di cane
        System.out.println("Ora fai la tua mossa");
    }

    private void PerdiVitaConDado(Partita partita, IGiocatore currentGiocatore){
        System.out.println("PROBABILITA - Tira il dado, se il numero che ti esce è uguale al valore della tua carta perdi una vita!");
        System.out.println(currentGiocatore.getNome() + " TIRA IL DADO!");
        int valoreDadoNew = partita.lancioDadoSingolo();
        System.out.println("Valore dado: " + valoreDadoNew);
        //capire se settare o no il nuovo valore del dado con setDado(). Non dovrebbe servire
        if(valoreDadoNew==currentGiocatore.getCarta().getValore()){
            System.out.println("Che sfortuna " + currentGiocatore.getNome() + " hai perso una vita");
            if(currentGiocatore.getVita()==1){ //TODO decidere se eliminare o no. Al momento implementata l'opzione della NON eliminazione
                //caso eliminazione, gestire eliminazione
                /*System.out.println("Avevi solo una vita rimasta " + currentGiocatore.getNome() + " sei stato ELIMINATO!");
                currentGiocatore.setVita(currentGiocatore.getVita() - 1);*/
                //caso NON eliminazione
                System.out.println("Siccome hai solo una vita rimanente non vieni eliminato");
                System.out.println("Il gioco prosegue");
            }

            currentGiocatore.setVita(currentGiocatore.getVita() - 1);
            System.out.println("Ora possiedi " + currentGiocatore.getVita() + " vite");
        }
        else{
            System.out.println("Sei stato fortunato, niente vita persa per te. Gioca il tuo turno");
        }
    }











    //commento nel caso volessimo tornare indietro oppure usare pezzi di codice, TOGLIERE quando facciamo la pulizia prima delle gui
    /*private void DiminuisciEScambia(Partita partita, IGiocatore giocatore){
        System.out.println("IMPREVISTO pt1 - Scambia la tua carta con una del mazzo");
        Carta newCarta = partita.mazzo.PescaCarta();
        System.out.println("IMPREVISTO pt2 - Vieni obbligato a scambiare la carta con il giocatore successiivo");


        int indexCG = partita.giocatori.indexOf(giocatore); // salvo induice del giocaotore attuale

        if(giocatore.getRuolo() == RuoloGiocatore.MAZZIERE)
        //TODO da rivedere
        {//se il giocaotre che pesca la carta è mazziere deve scambiare con la prima carta del mazzo
            System.out.println("Hai ricevuto " + newCarta.toString());
            if(!(newCarta.getValore()>9)){
                newCarta.setValore(newCarta.getValore()+1);
            }
            System.out.println("Aumento di uno il suo valore: " + newCarta.toString());
            giocatore.setCarta(newCarta);
        }
        else //se sono un giocatore normale
        {
            if(indexCG>=partita.giocatori.size()-1){ //caso in cui l'ultimo giocvatore non è mazziere e deve scambiare cojn il primo giocatore
                Carta nextPlayerCarta = partita.giocatori.get(0).getCarta();
                partita.giocatori.get(0).setCarta(newCarta);
                System.out.println("Hai ricevuto " + nextPlayerCarta.toString());
                if(!(nextPlayerCarta.getValore()>9)){
                    nextPlayerCarta.setValore(nextPlayerCarta.getValore()+1);
                }
                System.out.println("Aumento di uno il suo valore: " + nextPlayerCarta.toString());
                giocatore.setCarta(nextPlayerCarta);
            }
            else{ //caso player centrali
                Carta nextPlayerCarta = partita.giocatori.get(indexCG+1).getCarta();
                partita.giocatori.get(indexCG+1).setCarta(newCarta);
                System.out.println("Hai ricevuto " + nextPlayerCarta.toString());
                if(!(nextPlayerCarta.getValore()>9)){
                    nextPlayerCarta.setValore(nextPlayerCarta.getValore()+1);
                }
                System.out.println("Aumento di uno il suo valore: " + nextPlayerCarta.toString());
                giocatore.setCarta(nextPlayerCarta);
            }

        }


    }


    //commento nel caso volessimo tornare indietro oppure usare pezzi di codice, TOGLIERE quando facciamo la pulizia prima delle gui
    public void AumentaValoreCarta(){
        System.out.println("IMPREVISTO - Aumento il valore della tua carta!");
        System.out.println("Valore prima: " + getValore());
        this.setValore(this.getValore() + 1);
        System.out.println("Valore dopo: " + getValore());
    }*/





}
