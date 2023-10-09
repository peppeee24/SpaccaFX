package com.spaccafx.Cards;

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
            case 1:  NonPuoiScambiareCarta(partita, currentGiocatore); break;
            case 2:  AumentaValoreCarta(); break;
            //case 3: ScopriTuaCarta(); break;
            default: break;
        }
        attivato=true;
    }

    public void NonPuoiScambiareCarta(Partita partita, IGiocatore giocatore){
        System.out.println("IMPREVISTO - Non puoi scambiare la tua carta");
        Scanner s = new Scanner(System.in);
        boolean flg = false;
        partita.sceltaNew(s,giocatore);
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
