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

        RiempiMazzo(); // per ora facciamo che riempiamo il mazzo da 40 carte
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
            for (int c = 1; c < 11; c++)
            {
                mazzoCarte.add(new CartaNormale(c, seme));
            }
        }
    }

    private void CreoCarteImprevisto()
    {
        // RIEMPO CON LE CARTE IMPREVISTO
        for (int c = 1; c < 4; c++)  // 3 CARTE IMPREVISTO
        {
            mazzoCarte.add(new CartaImprevisto(c));
        }
    }

    private void CreoCarteProbabilita()
    {
        // RIEMPO CON LE CARTE PROBABILITA
        for (int c = 1; c < 4; c++) // 3 CARTE PROBABILITA
        {
            mazzoCarte.add(new CartaProbabilita(c));
        }
    }

    private void CreoCarte()
    {
        CreoCarteNormali();
        CreoCarteImprevisto();
        CreoCarteProbabilita();
    }

    public Carta PescaCarta() // carta randomica
    {
        int cartaRandomInt = (int)(0 + Math.random() * (mazzoCarte.size() - 1)); // 0 a 29 - CARTE  NORMALI <<  ---  >> 0 - 35 CARTE NORMALI + IMPREVISTI E PROBABILITA
        Carta carta = mazzoCarte.get(cartaRandomInt); // do una carta a caso

        // una volta che viene pescata una carta, la devo eliminare dal mazzo e poi alla fine del turno gliela devo rimettere!
        cartePescate.add(carta);
        mazzoCarte.remove(carta);
        System.out.println("Ho rimosso la carta pescata dal mazzo delle carte! (" + mazzoCarte.size() + " rimanenti)");

        return carta;
    }

    public void MescolaMazzo()
    {
        System.out.println("Ho mescolato il mazzo");

        // rimetto tutte le carte pescate
        if(!cartePescate.isEmpty()) // Se pieno
        {
            mazzoCarte.addAll(cartePescate);
            cartePescate.clear(); // cancello le carte che ho pescato
        }

        // faccio lo shuffle del mazzo
        Collections.shuffle(mazzoCarte);
        StampaMazzo();

    }

    private void StampaMazzo()
    {
        for(Carta cartaMazzo: mazzoCarte)
        {
            System.out.println(cartaMazzo.toString());
        }
    }
}
