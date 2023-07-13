package com.spaccafx.Cards;

import com.spaccafx.Enums.SemeCarta;

import java.util.ArrayList;

public class Mazzo
{
    ArrayList<Carta> carte;

    public Mazzo()
    {
        carte = new ArrayList<Carta>();
        RiempiMazzo(); // per ora facciamo che riempiamo il mazzo da 40 carte
    }

    public void RiempiMazzo() // riempo il mazzo di 20 carte (per ora)
    {
        for (SemeCarta seme : SemeCarta.values())
        {
            for (int c = 1; c < 11; c++)
            {
                carte.add(new Carta(c, seme));
            }
        }
    }

    public Carta PescaCarta() // carta randomica
    {
        int cartaRandomInt = (int)(0 + Math.random() * (39)); // ORA LE CARTE SONO DA 0 A 19!!
        Carta carta = carte.get(cartaRandomInt); // do una carta a caso dalla 0 alla 40
        // una volta che viene pescata una carta, la devo eliminare dal mazzo e poi alla fine del turno gliela devo rimettere!
        // da fare
        return carta;
    }

}
