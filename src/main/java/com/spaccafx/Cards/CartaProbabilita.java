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
        if(this.attivato)
            return;

        //partita.mazzo.StampaMazzo(); // DA CANCELLARE PER DEBUG!

        int scelta = (int)((1 + Math.random() * 3)); //genero o 1 o 2 che sono i "codici" delle scelte;

        switch(scelta)
        {
            case 1:  //ScambiaCartaConMazzoUI(partita, currentGiocatore, TC); break;

            case 2:  //AumentaVitaConDadoUI(partita, currentGiocatore, TC); break;

            case 3:  AumentaVitaConDadoUI(partita, currentGiocatore, TC); break;

            default: break;
        }

        // TODO OGNI VOLTA CHE SI ESEGUE L'EFFETTO BISOGNA FAR RIPOSARE IL THREAD PER FAR FERMARE IL GIOCO UN PICCOLO LASSO DI TEMPO

        this.attivato=true;
    }

    private void AumentaVitaConDadoUI(Partita partita, IGiocatore currentGiocatore, TavoloController TC)
    {
        // TODO gestire caso in cui si vinca due volte la vita

        if(currentGiocatore.hasVitaExtra()) // ha gia una vita extra
        {
            System.out.println("[PROBABILITA] Hai gia ottenuto una vita extra");
        }
        else
        {
            System.out.println("[PROBABILITA] Se ti esce il valore del dado uguale a quello della carta prendi una vita");
            System.out.println("[PROBABILITA] " + currentGiocatore.getNome() + " TIRA IL DADO!");

            int valoreDadoNew = partita.lancioDadoSingolo();
            TC.rollLite(valoreDadoNew, partita.getCurrentGiocatorePos());
            System.out.println("[PROBABILITA] Valore dado: " + valoreDadoNew);


            // da rimettere nell if
            System.out.println("[PROBABILITA] Complimenti " + currentGiocatore.getNome() + " hai vinto una vita");
            currentGiocatore.addVitaExtra();
            TC.updateVitaUI();
            System.out.println("[PROBABILITA] Ora possiedi " + currentGiocatore.getVita() + " vite");

            /*
            if(valoreDadoNew == currentGiocatore.getCarta().getValore())
            {
                System.out.println("[PROBABILITA] Complimenti " + currentGiocatore.getNome() + " hai vinto una vita");
                currentGiocatore.addVitaExtra();
                TC.updateVitaUI();
                System.out.println("[PROBABILITA] Ora possiedi " + currentGiocatore.getVita() + " vite");
            }
            else
            {
                System.out.println("[PROBABILITA] Sei stato sfortunato, niente vita per te. Gioca il tuo turno");
            }

             */
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
        //TC.mostraCartaSpeciale("Probabilita", "Mostra la carta del tuo giocatore successivo");

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


    private void ScambiaCartaConMazzoUI(Partita partita, IGiocatore currentGiocatore, TavoloController TC) // ok pensiamo
    {
        //TC.mostraCartaSpeciale("Probabilita", "Puoi scambiare una carta con il mazzo. Se sei mazziere puoi farlo 2 volte");
        System.out.println("[PROBABILITA] Posso scambiare la carta con il mazzo");

        if(currentGiocatore instanceof Giocatore) // se sono un giocatore normale
        {
            TC.gestisciPulsanti(true, true, true);
        }
        else // se sono un bot
        {
            System.out.println("[GAME] Sono un BOT");

            if(((Bot)currentGiocatore).attivoEffetto(partita)) // scambio la carta
            {
                System.out.println("[PROBABILITA] Il bot ha deciso di usare la probabilita scambio!");
                currentGiocatore.setCarta(partita.mazzo.PescaCarta());
            }
            else
            {
                System.out.println("[PROBABILITA] Il bot ha rifiutato lo scambiato con il mazzo");
            }

        }
        // updateCarteUI(); // carte grafiche player
        // updateGameUI(); // mazziere/vite
        // UpdateUI(); round
    }

    public boolean isAttivato(){return this.attivato;}
}