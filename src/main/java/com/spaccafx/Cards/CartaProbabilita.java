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

        int scelta = (int)((1 + Math.random() * 3)); //genero o 1 o 2 che sono i "codici" delle scelte;

        switch(scelta)
        {
            case 1: DiminuisciValoreCarta(); break;
            case 2: DiminuisciValoreCarta(); break;
            case 3: DiminuisciValoreCarta(); break;    // per ora tutti uguali come test!!
            default: break;
        }
    }

    private void DiminuisciValoreCarta()
    {
        System.out.println("PROBABILITA - Diminuisco il valore della tua carta!");
        System.out.println("Valore prima: " + getValore());
        this.setValore(this.getValore() - 1);
        System.out.println("Valore dopo: " + getValore());
    }

    private void ScambiaCartaConMazzo()
    {
        System.out.println("Posso scambiare la carta con il giocatore successivo!");
    }

    private void ScopriCartaGiocatoreSuccessivo()
    {
        System.out.println("Mostro carta giocatore successivo!");
    }
}
