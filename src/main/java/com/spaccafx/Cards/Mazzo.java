package com.spaccafx.Cards;

import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.SemeCarta;
import com.spaccafx.Files.FileManager;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Mazzo
{
    ArrayList<Carta> mazzoCarte;
    ArrayList<Carta> cartePescate;
    private int carteNormali;
    private int carteSpeciali;

    public Mazzo()
    {
        mazzoCarte = new ArrayList<Carta>();
        cartePescate = new ArrayList<Carta>();

        carteNormali = 5;
        carteSpeciali = 5;

        RiempiMazzo(); // per ora facciamo che riempiamo il mazzo da 30 carte
    }

    public Mazzo(int carteNormali, int carteSpeciali)
    {
        mazzoCarte = new ArrayList<Carta>();
        cartePescate = new ArrayList<Carta>();

        this.carteNormali = carteNormali;
        this.carteSpeciali = carteSpeciali;

        RiempiMazzo(); // per ora facciamo che riempiamo il mazzo da 30 carte
    }

    public Mazzo(boolean flag)
    {
        mazzoCarte = new ArrayList<Carta>();
        cartePescate = new ArrayList<Carta>();

        carteNormali = 5;
        carteSpeciali = 5;

        if(flag)
            RiempiMazzo(); // per ora facciamo che riempiamo il mazzo da 30 carte
    }

    public void RiempiMazzo() // riempo il mazzo di 20 carte (per ora)
    {
        CreoCarte();
        MescolaMazzo();
    }

    private void CreoCarteNormali()
    {
        System.out.println("Ho creato le carti normali, max: " + getMaxCarteNormali());
        // RIEMPO CON LE CARTE NORMALI
        for (int c=1; c<=carteNormali; c++) // SQUALO
        {
            CartaNormale carta = new CartaNormale(c, SemeCarta.SQUALO);
            Image cartaImage = new Image(getClass().getResource("/Assets/Cards/" + SemeCarta.SQUALO + "/" + SemeCarta.SQUALO + c + ".PNG").toString());

            carta.setImage(cartaImage);
            carta.setCartaEffettoAttivato(true);
            mazzoCarte.add(carta);
        }

        for (int c=1; c<=carteNormali; c++) // PESCE
        {
            CartaNormale carta = new CartaNormale(c, SemeCarta.PESCE);
            Image cartaImage = new Image(getClass().getResource("/Assets/Cards/" + SemeCarta.PESCE + "/" + SemeCarta.PESCE + c + ".PNG").toString());

            carta.setImage(cartaImage);
            carta.setCartaEffettoAttivato(true);
            mazzoCarte.add(carta);
        }

        for (int c=1; c<=carteNormali; c++) // VERME
        {
            CartaNormale carta = new CartaNormale(c, SemeCarta.VERME);
            Image cartaImage = new Image(getClass().getResource("/Assets/Cards/" + SemeCarta.VERME + "/" + SemeCarta.VERME + c + ".PNG").toString());

            carta.setImage(cartaImage);
            carta.setCartaEffettoAttivato(true);
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

            Image cartaImage = new Image(getClass().getResource("/Assets/Cards/" + SemeCarta.IMPREVISTO + "/" + SemeCarta.IMPREVISTO + newVal + ".PNG").toString());
            cartaImprevisto.setImage(cartaImage);
            cartaImprevisto.setCartaEffettoAttivato(false);

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

            Image cartaImage = new Image(getClass().getResource("/Assets/Cards/" + SemeCarta.PROBABILITA + "/" + SemeCarta.PROBABILITA + newVal + ".PNG").toString());
            cartaProbabilita.setImage(cartaImage);
            cartaProbabilita.setCartaEffettoAttivato(false);

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

    public void CreoCarte()
    {
        System.out.println("[MAZZO] STO CREANDO UN NUOVO MAZZO...");
        mazzoCarte.clear();

        CreoCarteNormali();
        CreoCarteImprevisto();
        CreoCarteProbabilita();

        Collections.shuffle(mazzoCarte);
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
            ((CartaProbabilita) carta).setCartaEffettoAttivato(true);
        if(carta instanceof  CartaImprevisto)
            ((CartaImprevisto) carta).setCartaEffettoAttivato(true);

        return carta;
    }

    public Carta mostraUltimaCartaMazzo()
    {
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
            CreoCarte(); // TODO EVITARE CHE OGNI VOLTA VENGA RICREATO MAZZO, SPRECO RISORSE

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

    public void EliminaCarte(ArrayList<Carta> carteDaEliminare) // serve per eliminare le carte che sono duplicate una volta che le carico dal file
    {
        System.out.println("Carte prima dell'eliminazione: " + mazzoCarte.toString());

        for (Carta cartaDaEliminare : carteDaEliminare) {
            System.out.println("Devo eliminare la carta: " + cartaDaEliminare.toString());

            // Itera attraverso mazzoCarte e rimuovi la carta corrente se è uguale a cartaDaEliminare
            Iterator<Carta> iterator = mazzoCarte.iterator();
            while (iterator.hasNext()) {
                Carta currentCarta = iterator.next();
                System.out.println("Sto valutando la carta: " + currentCarta.toString());
                if (currentCarta.equals(cartaDaEliminare)) {
                    iterator.remove();
                    System.out.println("Carta rimossa!");
                    break;
                }
            }
        }

        System.out.println("Carte dopo l'eliminazione: " + mazzoCarte.toString());
    }

    public ArrayList<Carta> getMazzoCarte(){return  this.mazzoCarte;}
}
