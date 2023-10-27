package com.spaccafx.Cards;

import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.SemeCarta;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;

public class CartaNormale extends Carta
{

    public CartaNormale(int valore, SemeCarta seme, TavoloController TC)
    {
        super(valore, seme, TC);
    }

    @Override
    public String toString(){return this.getValore() + " di " + this.seme;}

    @Override
    public void Effetto(Partita partita, IGiocatore currentGiocatore, TavoloController TC)
    {
        return; // non fa nulla
    }
}
