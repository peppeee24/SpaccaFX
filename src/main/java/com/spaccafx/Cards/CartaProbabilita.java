package com.spaccafx.Cards;

import com.spaccafx.Manager.Partita;

public class CartaProbabilita extends Carta
{
    Partita partitaAttuale;

    public CartaProbabilita(int valore)
    {
        super(valore);
        this.partitaAttuale = null;
    }

    @Override
    public String toString(){return this.getValore() + " di PROBABILITA";}

    @Override
    public void Effetto(Partita partita)
    {
        this.partitaAttuale = partita;
        System.out.println("Ho in mano una carta effetto e non posso fare altre scelte!");
    }

    private void NoCartaScambiabile()
    {
        // ESEGUI IL METODO CHE FA LA SCELTA DI PASSARE IL TURNO
    }
}
