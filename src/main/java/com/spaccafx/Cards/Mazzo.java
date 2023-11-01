package com.spaccafx.Cards;

import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.SemeCarta;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;

public class Mazzo
{
    ArrayList<Carta> mazzoCarte;
    ArrayList<Carta> cartePescate;

    // ArrayList<Integer> valoriMax = {1,2,3,4,5,6,7,8,9,10}; // TODO PER MIGLORARE EFFICIENZA SCELTA VALORI CARTE PROB E IMPREVISTO (da rivedere non sicuro sia meglio)
    private int carteNormali;
    private int carteSpeciali;

    public Mazzo()
    {
        mazzoCarte = new ArrayList<Carta>();
        cartePescate = new ArrayList<Carta>();

        carteNormali = 5; // DA TOGLIERE QUESTO ALLA FINE, CAMBIARLO
        carteSpeciali = 5; // DA TOGLIERE QUESTO ALLA FINE, CAMBIARLO
        // TODO FARE UNA VERIFICA PER VEDERE SE IL N. CARTE SPECIALI < CARTE NORMALI

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
        for (int c=1; c<=carteNormali; c++) // CANE
        {
            CartaNormale carta = new CartaNormale(c, SemeCarta.CANE);
            Image cartaImage = new Image(getClass().getResource("/Assets/Cards/Cane/c" + c + ".PNG").toString());

            carta.setImage(cartaImage);
            mazzoCarte.add(carta);
        }

        for (int c=1; c<=carteNormali; c++) // GATTO
        {
            CartaNormale carta = new CartaNormale(c, SemeCarta.GATTO);
            Image cartaImage = new Image(getClass().getResource("/Assets/Cards/Gatto/g" + c + ".PNG").toString());

            carta.setImage(cartaImage);
            mazzoCarte.add(carta);
        }

        for (int c=1; c<=carteNormali; c++) // TOPO
        {
            CartaNormale carta = new CartaNormale(c, SemeCarta.TOPO);
            Image cartaImage = new Image(getClass().getResource("/Assets/Cards/Topo/t" + c + ".PNG").toString());

            carta.setImage(cartaImage);
            mazzoCarte.add(carta);
        }
    }

    private void CreoCarteImprevisto()
    {
        for(int c=1; c<=carteSpeciali; c++)
        {
            // RIEMPO CON LE CARTE PROBABILITA
            int newVal = (int)(1 + Math.random() * (carteNormali)); // valore carta da 1 a MAXCARTENORNMALI (ho tanti valori tanti quanti il n. max di carte normali)
            System.out.println("Valore nuova carta Imprevisto: " + newVal);

            while(!(isCartaUnica(newVal, SemeCarta.IMPREVISTO))) // fino a quando non esiste cambio valore
                newVal = (int)(1 + Math.random() * (carteNormali)); // valore carta da 1 a MAXCARTENORNMALI (ho tanti valori tanti quanti il n. max di carte normali)

            System.out.println("Imposto valore: " + newVal);

            CartaImprevisto cartaImprevisto = new CartaImprevisto(newVal, SemeCarta.IMPREVISTO);

            Image cartaImage = new Image(getClass().getResource("/Assets/Cards/Imprevisto/i" + newVal + ".PNG").toString());
            cartaImprevisto.setImage(cartaImage);

            mazzoCarte.add(cartaImprevisto); // vuol dire che e unica e la creo
        }
    }


    private void CreoCarteProbabilita()
    {
        for(int c=1; c<=carteSpeciali; c++)
        {
            // RIEMPO CON LE CARTE PROBABILITA
            int newVal = (int)(1 + Math.random() * (carteNormali));  // valore carta da 1 a MAXCARTENORNMALI (ho tanti valori tanti quanti il n. max di carte normali)
            System.out.println("Valore nuova carta Probabilita: " + newVal);

            while(!(isCartaUnica(newVal, SemeCarta.PROBABILITA))) // fino a quando non esiste cambio valore
                newVal = (int)(1 + Math.random() * (carteNormali));  // valore carta da 1 a MAXCARTENORNMALI (ho tanti valori tanti quanti il n. max di carte normali)

            System.out.println("Imposto valore: " + newVal);

            CartaProbabilita cartaProbabilita = new CartaProbabilita(newVal, SemeCarta.PROBABILITA);

            Image cartaImage = new Image(getClass().getResource("/Assets/Cards/Probabilita/p" + newVal + ".PNG").toString());
            cartaProbabilita.setImage(cartaImage);

            mazzoCarte.add(cartaProbabilita); // vuol dire che e unica e la creo

        }
    }

    private boolean isCartaUnica(int valore, SemeCarta seme)
    {
        for(Carta carta : mazzoCarte)
        {
            if(carta.seme == seme && carta.getValore() == valore)
            {
                System.out.println("(!) Esiste gia una carta con valore: " + valore);
                return false; // esiste gia una carta uguale
            }

        }

        return true; // non esiste
    }

    private void CreoCarte()
    {
        mazzoCarte.clear();

        CreoCarteNormali();
        CreoCarteImprevisto();
        CreoCarteProbabilita();
    }

    public Carta PescaCarta() //prende la prima carta dal mazzo
    {
        Carta carta = mazzoCarte.get(mazzoCarte.size() - 1); // prendo l ultima carta dal mazzo

        // una volta che viene pescata una carta, la devo eliminare dal mazzo e poi alla fine del turno gliela devo rimettere!
        cartePescate.add(carta);
        mazzoCarte.remove(carta);
        System.out.println("Ho rimosso la carta pescata dal mazzo delle carte! (" + mazzoCarte.size() + " rimanenti)");

        return carta;
    }

    public Carta PescaCartaSenzaEffetto() //prende la prima carta dal mazzo
    {
        Carta carta = mazzoCarte.get(mazzoCarte.size() - 1); // prendo l ultima carta dal mazzo

        // una volta che viene pescata una carta, la devo eliminare dal mazzo e poi alla fine del turno gliela devo rimettere!
        cartePescate.add(carta);
        mazzoCarte.remove(carta);
        System.out.println("Ho rimosso la carta pescata dal mazzo delle carte! (" + mazzoCarte.size() + " rimanenti)");

        if(carta instanceof  CartaProbabilita)
            ((CartaProbabilita) carta).setAttivato(true);
        if(carta instanceof  CartaImprevisto)
            ((CartaImprevisto) carta).setAttivato(true);

        return carta;
    }

    public Carta mostraUltimaCartaMazzo(){
         Carta carta = mazzoCarte.get(mazzoCarte.size() - 1);
         return  carta;// prendo l ultima carta dal mazzo

        // una volta che viene pescata una carta, la devo eliminare dal mazzo e poi alla fine del turno gliela devo rimettere!

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

        System.out.println("[MAZZIERE] Ho mescolato il mazzo (" + mazzoCarte.size() + " carte)");
    }

    public void StampaMazzo()
    {
        for(Carta cartaMazzo: mazzoCarte)
        {
            System.out.println(cartaMazzo.toString());
        }
    }

    public int getMaxCarteNormali(){return this.carteNormali;}
    public int getMaxCarteSpeciali(){return this.carteSpeciali;}
}
