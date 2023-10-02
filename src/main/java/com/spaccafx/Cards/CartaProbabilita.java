package com.spaccafx.Cards;

import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.Giocatore;
import com.spaccafx.Enums.*;

public class CartaProbabilita extends Carta
{
    /*Partita partitaAttuale;
    IGiocatore giocatoreAttuale;
    IGiocatore nextGiocatore;*/

    boolean attivato = false;

    public CartaProbabilita(int valore)
    {
        super(valore);
        /*this.partitaAttuale = null;
        this.giocatoreAttuale=null;*/
    }

    @Override
    public String toString(){return this.getValore() + " di PROBABILITA";}

    @Override
    public void Effetto(Partita partita, IGiocatore currentGiocatore)
    {
        if(attivato==true)
            return;

        partita.mazzo.StampaMazzo(); // DA CANCELLARE PER DEBUG!

        /*this.partitaAttuale = partita;
        this.giocatoreAttuale=currentGiocatore;*/
        //System.out.println("Ho in mano una carta effetto e non posso fare altre scelte!");

        int scelta = (int)((1 + Math.random() * 2)); //genero o 1 o 2 che sono i "codici" delle scelte;

        switch(scelta)
        {
            case 1: //DiminuisciValoreCarta(); break;
            case 2: ScopriCartaGiocatoreSuccessivo(partita, currentGiocatore); break;
            //case 3: DiminuisciValoreCarta(); break;
            default: break;
        }
        attivato=true;
    }

    private void DiminuisciValoreCarta()
    {
        System.out.println("PROBABILITA - Diminuisco il valore della tua carta!");
        System.out.println("Valore prima: " + getValore());
        this.setValore(this.getValore() - 1);
        System.out.println("Valore dopo: " + getValore());
    }

    private void ScopriCartaGiocatoreSuccessivo(Partita partita, IGiocatore currentGiocatore)
    {
        System.out.println("\nEFFETTO ATTIVATO: SCOPRO CARTA GIOCATORE SUCCESSIVO\n");
        int indexCG = partita.giocatori.indexOf(currentGiocatore);
        int indexNG=0;
        if(currentGiocatore.getRuolo() == RuoloGiocatore.MAZZIERE)
        {//se il giocaotre che pesca la carta è mazziere deve vedere la prima carta del mazzo

            System.out.println("SEI MAZZIERE E GUARDI CARTA DAL MAZZO CON VALORE: " + partita.mazzo.mazzoCarte.get(partita.mazzo.mazzoCarte.size()-1).toString());
            //TODO caso mazziere
        }
        else
        {
            if(!(indexCG >= partita.giocatori.size()-1))//se è l'ultimo giocatore del tavolo MA non è il mazziere allora vede la carta del primo giocatore
                indexNG = indexCG+1;


            /*if(indexCG >= partita.giocatori.size()-1)//se è l'ultimo giocatore del tavolo MA non è il mazziere allora vede la carta del primo giocatore
                indexNG=0;
            else //altrimenti prendo il giocatore successivo nell'arrayList dei giocatroie
                indexNG = indexCG+1; */

            System.out.println("SEI GIOCATORE E GUARDI CARTA DAL GIOCATORE NEXT CON VAL: " + partita.giocatori.get(indexNG).getCarta().toString());
        }
    }
}
//TODO GENERALE errore quando il mazziere ha una carta probabilità e viene cambiato con l'altro giocatore e credo quando dal mazzo come ultima carta viene pescata una probabilità