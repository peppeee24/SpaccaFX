package com.spaccafx.Cards;

import com.spaccafx.Enums.SemeCarta;
import com.spaccafx.Manager.Partita;

public class CartaNormale extends Carta
{
    private SemeCarta seme;


    public CartaNormale(int valore, SemeCarta seme)
    {
        super(valore);
        this.seme = seme;
    }

    public void setSeme(SemeCarta seme){this.seme = seme;}


    public SemeCarta getSeme(){return this.seme;}


    @Override
    public String toString(){return this.getValore() + " di " + this.seme;}

    @Override
    public void Effetto(Partita partita)
    {
        return; // non fa nulla
    }
}
