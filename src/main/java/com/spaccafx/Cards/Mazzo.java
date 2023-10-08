package com.spaccafx.Cards;

import com.spaccafx.Enums.SemeCarta;

import java.util.ArrayList;
import java.util.Collections;

public class Mazzo
{
    ArrayList<Carta> mazzoCarte;
    ArrayList<Carta> cartePescate;

    public Mazzo()
    {
        mazzoCarte = new ArrayList<Carta>();
        cartePescate = new ArrayList<Carta>();

        RiempiMazzo(); // per ora facciamo che riempiamo il mazzo da 30 carte
    }

    public void RiempiMazzo() // riempo il mazzo di 20 carte (per ora)
    {
        CreoCarte();
        MescolaMazzo();
    }

    private void CreoCarteNormali()
    {
        // RIEMPO CON LE CARTE NORMALI
        for (SemeCarta seme : SemeCarta.values())
        {
            for (int c = 1; c < 3; c++) // da 1 a 10 (1 - 11)
            {
                mazzoCarte.add(new CartaNormale(c, seme));
            }
        }
    }

    private void CreoCarteImprevisto()
    {
        // RIEMPO CON LE CARTE IMPREVISTO
        for (int c = 7; c < 10; c++)  // 3 CARTE IMPREVISTO (valori da 7 a 9)
        {
            mazzoCarte.add(new CartaImprevisto(c));
        }
    }


    private void CreoCarteProbabilita()
    {
        // RIEMPO CON LE CARTE PROBABILITA
        for (int c = 2; c < 4; c++) // 3 CARTE PROBABILITA (valori da 2 a 4)
        {
            mazzoCarte.add(new CartaProbabilita(c));
        }
    }

    private void CreoCarte()
    {
        mazzoCarte.clear();
        CreoCarteNormali();
        // TODO FARE CARTE IMPREVISTO E PROB DA 2 A 9
        CreoCarteImprevisto();
        //CreoCarteProbabilita();
    }

    public Carta PescaCarta() // carta randomica
    {
        Carta carta = mazzoCarte.get(mazzoCarte.size() - 1); // prendo l ultima carta dal mazzo

        // una volta che viene pescata una carta, la devo eliminare dal mazzo e poi alla fine del turno gliela devo rimettere!
        cartePescate.add(carta);
        mazzoCarte.remove(carta);
        System.out.println("Ho rimosso la carta pescata dal mazzo delle carte! (" + mazzoCarte.size() + " rimanenti)");

        return carta;
    }

    public void MescolaMazzo()
    {
        // rimetto tutte le carte pescate
        if(!cartePescate.isEmpty()) // Se pieno
        {
            //mazzoCarte.addAll(cartePescate);
            cartePescate.clear(); // cancello le carte che ho pescato
            CreoCarte();

        }

        // faccio lo shuffle del mazzo
        Collections.shuffle(mazzoCarte);
        StampaMazzo();
        // TODO Quando attivo un effetto di una carta che ne cambia il suo valore
        //      bisogna evitare che quello venga salvato altrimenti finisce nel mazzo con un valore sballato

        System.out.println("Ho mescolato il mazzo (" + mazzoCarte.size() + " carte)");
    }

    public void StampaMazzo()
    {
        for(Carta cartaMazzo: mazzoCarte)
        {
            System.out.println(cartaMazzo.toString());
        }
    }
}
