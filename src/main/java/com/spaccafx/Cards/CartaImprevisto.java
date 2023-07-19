package com.spaccafx.Cards;

import com.spaccafx.Manager.Partita;

public class CartaImprevisto extends Carta
{
    Partita partitaAttuale;

    public CartaImprevisto(int valore)
    {
        super(valore);
        this.partitaAttuale = null;
    }

    @Override
    public String toString(){return this.getValore() + " di IMPREVISTO";}

    @Override
    public void Effetto(Partita partita)
    {
        this.partitaAttuale = partita;

        return; // non fa nulla
    }
}
