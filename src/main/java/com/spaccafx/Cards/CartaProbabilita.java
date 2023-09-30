package com.spaccafx.Cards;

import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.Giocatore;

public class CartaProbabilita extends Carta
{
    Partita partitaAttuale;
    IGiocatore giocatoreAttuale;
    IGiocatore nextGiocatore;

    public CartaProbabilita(int valore)
    {
        super(valore);
        this.partitaAttuale = null;
        this.giocatoreAttuale=null;
    }

    @Override
    public String toString(){return this.getValore() + " di PROBABILITA";}

    @Override
    public void Effetto(Partita partita, IGiocatore currentGiocatore)
    {
        this.partitaAttuale = partita;
        this.giocatoreAttuale=currentGiocatore;
        System.out.println("Ho in mano una carta effetto e non posso fare altre scelte!");

        int scelta = (int)((1 + Math.random() * 3)); //genero o 1 o 2 che sono i "codici" delle scelte;

        switch(scelta)
        {
            case 1: DiminuisciValoreCarta(); break;
            case 2: ScambiaCartaConMazzo(); break;
            case 3: ScopriCartaGiocatoreSuccessivo(); break;
            default: break;
        }
    }
    //TODO pensare se tenere o no carta random

    private void DiminuisciValoreCarta()
    {
        System.out.println("PROBABILITA - Diminuisco il valore della tua carta!");
        System.out.println("Valore prima: " + getValore());
        this.setValore(this.getValore() - 1);
        System.out.println("Valore dopo: " + getValore());
    }

    private void ScambiaCartaConMazzo() //TODO discutere, perchè ha senso solo se le carte prob. possono avere qualsiasi valore sennò io non ho alcun interesse a scambiare una carta bassa con una dal mazzo, rischieri di perdere e basta
    {
        System.out.println("PROBABILITA - Posso scambiare la carta con il mazzo!");
        partitaAttuale.ScambioCartaMazzoWithProb(giocatoreAttuale);
    }

    private void ScopriCartaGiocatoreSuccessivo() //TODO non prende mai il nextGiocatore siccome non lo inizializzo mai, perciò essendo null non può avere una posizione
    {
        System.out.println("Mostro carta giocatore successivo!");
        nextGiocatore.setPos(giocatoreAttuale.getPos()+1); //TODO se sono mazziere non posso vedere la carta del giocatore successivo èerchè non esiste
        System.out.println("Ecco il valore della carta del giocatore successivo" + nextGiocatore.getCarta().getValore());
    }
}
//TODO GENERALE errore quando il mazziere ha una carta probabilità e viene cambiato con l'altro giocatore e credo quando dal mazzo come ultima carta viene pescata una probabilità