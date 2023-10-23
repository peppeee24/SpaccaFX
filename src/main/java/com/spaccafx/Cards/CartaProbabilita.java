package com.spaccafx.Cards;

import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.Giocatore;
import com.spaccafx.Enums.*;
import javafx.application.Platform;

import java.util.Scanner;

public class CartaProbabilita extends Carta
{
    boolean attivato = false;

    public CartaProbabilita(int valore, SemeCarta seme)
    {
        super(valore, seme);
    }

    @Override
    public String toString(){return this.getValore() + " di PROBABILITA";}

    @Override
    public void Effetto(Partita partita, IGiocatore currentGiocatore, TavoloController TC)
    {
        if(attivato)
            return;

        //partita.mazzo.StampaMazzo(); // DA CANCELLARE PER DEBUG!

        int scelta = (int)((1 + Math.random() * 3)); //genero o 1 o 2 che sono i "codici" delle scelte;

        switch(scelta)
        {
            case 1: ;//AumentaVitaConDado(partita, currentGiocatore); break;
            case 2: ;//ScopriCartaGiocatoreSuccessivo(partita, currentGiocatore); break;
            case 3: effettoTest(partita, currentGiocatore, TC); break;
            default: break;
        }

        this.attivato=true;
    }

    private void AumentaVitaConDado(Partita partita, IGiocatore currentGiocatore){



        System.out.println("PROBABILITA - Tira il dado, se il numero che ti esce è uguale al valore della tua carta vinci una vita!");
        System.out.println(currentGiocatore.getNome() + " TIRA IL DADO!");
        int valoreDadoNew = partita.lancioDadoSingolo();
        System.out.println("Valore dado: " + valoreDadoNew);
        //capire se settare o no il nuovo valore del dado con setDado(). Non dovrebbe servire
        if(valoreDadoNew==currentGiocatore.getCarta().getValore()){
            System.out.println("Complimenti " + currentGiocatore.getNome() + " hai vinto una vita");
            currentGiocatore.setVita(currentGiocatore.getVita() + 1);
            System.out.println("Ora possiedi " + currentGiocatore.getVita() + " vite");
        }
        else{
            System.out.println("Sei stato sfortunato, niente vita per te. Gioca il tuo turno");
        }
    }

    private void ScopriCartaGiocatoreSuccessivo(Partita partita, IGiocatore currentGiocatore)
    {
        System.out.println("\nEFFETTO ATTIVATO: SCOPRO CARTA GIOCATORE SUCCESSIVO\n");
        int indexCG = partita.giocatori.indexOf(currentGiocatore);
        int indexNG=0;
        if(currentGiocatore.getRuolo() == RuoloGiocatore.MAZZIERE)
        {//se il giocaotre che pesca la carta è mazziere deve vedere la prima carta del mazzo

            System.out.println("SEI MAZZIERE E GUARDI CARTA DAL MAZZO CON VALORE: " + partita.mazzo.mazzoCarte.get(partita.mazzo.mazzoCarte.size()-1).toString());
            //TODO caso mazziere GRAFICAMENTE
        }
        else
        {
            if(!(indexCG >= partita.giocatori.size()-1))//se è l'ultimo giocatore del tavolo MA non è il mazziere allora vede la carta del primo giocatore
                indexNG = indexCG+1;


            /*if(indexCG >= partita.giocatori.size()-1)//se è l'ultimo giocatore del tavolo MA non è il mazziere allora vede la carta del primo giocatore
                indexNG=0;
            else //altrimenti prendo il giocatore successivo nell'arrayList dei giocatroie
                indexNG = indexCG+1; */

            System.out.println("SEI GIOCATORE E GUARDI CARTA DAL GIOCATORE NEXT CON VAL: " + partita.giocatori.get(indexNG).getCarta().toString());
        }
    }

    private void ScambiaCartaConMazzo(Partita partita, IGiocatore currentGiocatore)
    {
        System.out.println("PROBABILITA - Posso scambiare carta con il mazzo");

        if(currentGiocatore instanceof Giocatore) // se sono un giocatore normale
        {
            System.out.println("Sono un PLAYER");
            Scanner s = new Scanner(System.in);

            System.out.println("Digita 1 se vuoi scambiare con il mazzo, un altro numero se vuoi rifiutare!");
            if(s.nextInt() == 1)
            {
                System.out.println("Hai scambiato la carta con il mazzo");
                currentGiocatore.setCarta(partita.mazzo.PescaCarta());
            }
            else
                System.out.println("Hai rifiutato lo scambiato con il mazzo");

        }
        else // se sono un bot
        {
            System.out.println("Sono un BOT");
            if(((Bot)currentGiocatore).Scelta(partita) == 1) // scambio la carta
            {
                System.out.println("Il bot ha deciso di scambiare la carta con il mazzo");
                currentGiocatore.setCarta(partita.mazzo.PescaCarta());
            }
            else
                System.out.println("Il bot ha rifiutato lo scambiato con il mazzo");
        }
    }

    private void effettoTest(Partita partita, IGiocatore currentGiocatore, TavoloController TC)
    {

        TC.mostraCartaSpeciale("Prob", "Scopri carta giocatore successivo");

        // Creare un thread che eseguirà i metodi dopo un ritardo
        Thread thread = new Thread(() -> {
            try {
                // Dormire per un certo numero di millisecondi (ad esempio, 2000 millisecondi o 2 secondi)
                Thread.sleep(2000);

                // Eseguire i metodi successivi all'interno di Platform.runLater
                // Altri metodi da eseguire dopo il ritardo
                Platform.runLater(TC::nascondiCartaSpeciale);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Avviare il thread
        thread.start();
    }
}