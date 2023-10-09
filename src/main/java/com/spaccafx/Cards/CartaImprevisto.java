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
            case 1:  DiminuisciEScambia( partita, currentGiocatore); break;
            case 2:  AumentaValoreCarta(); break;
            //case 3: ScopriTuaCarta(); break;
            default: break;
        }
        attivato=true;
    }
//TODO FONDAMENTALE RISOLVERE IL PROBLEMA DI PIÙ CARTE UGUALI DENTRO AL MAZZO
    public void DiminuisciEScambia(Partita partita, IGiocatore giocatore){
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

    public void AumentaValoreCarta(){
        System.out.println("IMPREVISTO - Aumento il valore della tua carta!");
        System.out.println("Valore prima: " + getValore());
        this.setValore(this.getValore() + 1);
        System.out.println("Valore dopo: " + getValore());
    }

    public void ScopriTuaCarta(Partita partita, IGiocatore giocatore){
        //TODO in codice non è utile, va codificato poi con la parte graFICA
    }


}
