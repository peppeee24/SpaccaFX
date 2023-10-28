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

    public CartaProbabilita(int valore, SemeCarta seme, TavoloController TC)
    {
        super(valore, seme, TC);
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
            case 1:  ScambiaCartaConMazzoUI(partita, currentGiocatore, TC); break;
            case 2:  ScopriCartaGiocatoreSuccessivoUI(partita, currentGiocatore, TC, partita.mazzo); break;
            case 3:  AumentaVitaConDadoUI(partita, currentGiocatore, TC); break;
            default: break;
        }

        // TODO OGNI VOLTA CHE SI ESEGUE L'EFFETTO BISOGNA FAR RIPOSARE IL THREAD PER FAR FERMARE IL GIOCO UN PICCOLO LASSO DI TEMPO

        this.attivato=true;
    }

    private void AumentaVitaConDadoUI(Partita partita, IGiocatore currentGiocatore, TavoloController TC){
        // TODO gestire caso in cui si vinca due volte la vita
// TODO non funziona il primo banner
        TC.mostraCartaSpeciale("Probabilita", "Tira il dado, se il numero che ti esce è uguale al valore della tua carta vinci una vita!");
        TC.disableDice();
        System.out.println(currentGiocatore.getNome() + " TIRA IL DADO!");
        int valoreDadoNew = partita.lancioDadoSingolo();
        TC.rollLite(valoreDadoNew, partita.getCurrentGiocatorePos());
        System.out.println("Valore dado: " + valoreDadoNew);

        //capire se settare o no il nuovo valore del dado con setDado(). Non dovrebbe servire
        if(valoreDadoNew==currentGiocatore.getCarta().getValore() ){
           // TC.setLifeGold(currentGiocatore.getNome());
            System.out.println("Complimenti " + currentGiocatore.getNome() + " hai vinto una vita");
            currentGiocatore.setVita(currentGiocatore.getVita() + 1);
            TC.gestisciVite();
            System.out.println("Ora possiedi " + currentGiocatore.getVita() + " vite");
            TC.mostraCartaSpeciale("Complimenti " + currentGiocatore.getNome() + " hai vinto una vita","Ora possiedi " + currentGiocatore.getVita() + " vite");
        }
        else{
            System.out.println("Sei stato sfortunato, niente vita per te. Gioca il tuo turno");
            TC.mostraCartaSpeciale("Sei stato sfortunato, niente vita per te.","Gioca il tuo turno");


        }
    }

   /* private void ScopriCartaGiocatoreSuccessivo(Partita partita, IGiocatore currentGiocatore)
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


    */

            /*if(indexCG >= partita.giocatori.size()-1)//se è l'ultimo giocatore del tavolo MA non è il mazziere allora vede la carta del primo giocatore
                indexNG=0;
            else //altrimenti prendo il giocatore successivo nell'arrayList dei giocatroie
                indexNG = indexCG+1; */

            /*System.out.println("SEI GIOCATORE E GUARDI CARTA DAL GIOCATORE NEXT CON VAL: " + partita.giocatori.get(indexNG).getCarta().toString());
        }
    }

             */


    private void ScopriCartaGiocatoreSuccessivoUI(Partita partita, IGiocatore currentGiocatore, TavoloController TC, Mazzo m)
    {
        System.out.println("\nEFFETTO ATTIVATO: SCOPRO CARTA GIOCATORE SUCCESSIVO\n");
        TC.mostraCartaSpeciale("Probabilita", "Mostra la carta del tuo giocatore successivo");

        int indexCG = partita.giocatori.indexOf(currentGiocatore);
        int indexNG=0;
        if(currentGiocatore.getRuolo() == RuoloGiocatore.MAZZIERE)
        {//se il giocaotre che pesca la carta è mazziere deve vedere la prima carta del mazzo

            System.out.println("SEI MAZZIERE E GUARDI CARTA DAL MAZZO CON VALORE: " + partita.mazzo.mazzoCarte.get(partita.mazzo.mazzoCarte.size()-1).toString());
            //TODO caso mazziere GRAFICAMENTE

            TC.mostraMazzoCentrale(m.mostraUltimaCartaMazzo());

        }
        else
        {
            if(!(indexCG >= partita.giocatori.size()-1))//se è l'ultimo giocatore del tavolo MA non è il mazziere allora vede la carta del primo giocatore
                indexNG = indexCG+1;
            TC.mostraCarta(indexNG); // TODO va in conflitto con il metodo set carta tavolo


            /*if(indexCG >= partita.giocatori.size()-1)//se è l'ultimo giocatore del tavolo MA non è il mazziere allora vede la carta del primo giocatore
                indexNG=0;
            else //altrimenti prendo il giocatore successivo nell'arrayList dei giocatroie
                indexNG = indexCG+1; */


            System.out.println("SEI GIOCATORE E GUARDI CARTA DAL GIOCATORE NEXT CON VAL: " + partita.giocatori.get(indexNG).getCarta().toString());
        }
    }

    /*private void ScambiaCartaConMazzo(Partita partita, IGiocatore currentGiocatore)
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

     */


    private void ScambiaCartaConMazzoUI(Partita partita, IGiocatore currentGiocatore, TavoloController TC)
    {
        TC.mostraCartaSpeciale("Probabilita", "Puoi scambiare una carta con il mazzo. Se sei mazziere puoi farlo 2 volte");

        if(currentGiocatore instanceof Giocatore) // se sono un giocatore normale
        {
            TC.showScambiaBlu(true, true, true);
        }
        else // se sono un bot
        {
            // TODO FARE CASO DEI BOT
            /*
            System.out.println("Sono un BOT");
            if(((Bot)currentGiocatore).Scelta(partita) == 1) // scambio la carta
            {
                System.out.println("Il bot ha deciso di scambiare la carta con il mazzo");
                currentGiocatore.setCarta(partita.mazzo.PescaCarta());
            }
            else
                System.out.println("Il bot ha rifiutato lo scambiato con il mazzo");

             */
        }
        // updateCarteUI(); // carte grafiche player
        // updateGameUI(); // mazziere/vite
        // UpdateUI(); round
    }

    public boolean isAttivato(){return this.attivato;}
}