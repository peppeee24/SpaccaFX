package com.spaccafx.Cards;

import com.spaccafx.Enums.SemeCarta;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;

public class CartaNormale extends Carta
{

    public CartaNormale(int valore, SemeCarta seme)
    {
        super(valore, seme);
    }

    public void setSeme(SemeCarta seme){this.seme = seme;}


    public SemeCarta getSeme(){return this.seme;}

    @Override
    public String toString(){return this.getValore() + " di " + this.seme;}

    @Override
    public void Effetto(Partita partita, IGiocatore currentGiocatore)
    {
        return; // non fa nulla
    }
}
