package com.spaccafx.Cards;

import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;

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

        partita.mazzo.StampaMazzo(); // DA CANCELLARE PER DEBUG!

        /*this.partitaAttuale = partita;
        this.giocatoreAttuale=currentGiocatore;*/
        //System.out.println("Ho in mano una carta effetto e non posso fare altre scelte!");

        int scelta = (int)((1 + Math.random() * 2)); //genero o 1 o 2 che sono i "codici" delle scelte;

        switch(scelta)
        {
            case 1: //DiminuisciValoreCarta(); break;
            case 2:  break;
            //case 3: DiminuisciValoreCarta(); break;
            default: break;
        }
        attivato=true;
    }
}
