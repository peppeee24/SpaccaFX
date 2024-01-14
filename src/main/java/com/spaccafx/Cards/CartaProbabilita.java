package com.spaccafx.Cards;

import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Files.AudioManager;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.Giocatore;
import com.spaccafx.Enums.*;
import javafx.application.Platform;

public class CartaProbabilita extends Carta
{
    public CartaProbabilita(int valore, SemeCarta seme)
    {
        super(valore, seme);
    }

    @Override
    public String toString(){return this.getValore() + " di PROBABILITA";}

    @Override
    public void Effetto(Partita partita, IGiocatore currentGiocatore, TavoloController TC)
    {
        if(partita.isGameStopped())
            return;

        if(this.attivato)
            return;

        System.out.println("[PROBABILITA] Attivato effetto carta: "+ toString() );


        int scelta = (int)((1 + Math.random() * 2)); //genero o 1 o 2 che sono i "codici" delle scelte;

        switch(scelta)
        {
            case 1:  AumentaVitaConDadoUI(partita, currentGiocatore, TC); break;
            case 2:  ScambiaCartaConMazzoUI(partita, currentGiocatore, TC); break;
            default: break;
        }

        this.attivato=true;
    }

    private void AumentaVitaConDadoUI(Partita partita, IGiocatore currentGiocatore, TavoloController TC)
    {
        Thread thread = new Thread(() -> {
            try {

                Platform.runLater(() ->
                {
                    if(partita.isGameStopped())
                        return;

                    TC.setExitGame(false);
                    AudioManager.probabilitaSuono();
                    System.out.println("[PROBABILITA] Aumenta vita con dado");
                    TC.mostraBannerAttesa("PROBABILITA [DADI]", "Lancia un dado. Se il valore uscito equivale a quello della tua carta prendi una vita");
                });

                Thread.sleep(4000);

                Platform.runLater(() ->
                {
                    TC.nascondiBannerAttesa();

                    if(currentGiocatore instanceof  Bot) // caso bot
                    {
                        TC.gestisciPulsanti(false, false, false); // togliamo i pulsanti
                    }


                    if(currentGiocatore.hasVitaExtra()) // ha gia una vita extra
                    {
                        System.out.println("[PROBABILITA] Hai gia ottenuto una vita extra");
                        TC.mostraBannerAttesa("PROBABILITA [DADI]", "Hai gia ottenuto una vita extra");
                    }
                    else
                    {
                        System.out.println("[PROBABILITA] Se ti esce il valore del dado uguale a quello della carta prendi una vita");
                        System.out.println("[PROBABILITA] " + currentGiocatore.getNome() + " TIRA IL DADO!");

                        int valoreDadoNew = partita.lancioDadoSingolo();
                        TC.rollLite(valoreDadoNew, partita.getCurrentGiocatorePos()); // TODO DA CAPIRE
                        System.out.println("[PROBABILITA] Valore dado: " + valoreDadoNew);


                        if(valoreDadoNew == currentGiocatore.getCarta().getValore())
                        {
                            System.out.println("[PROBABILITA] Complimenti " + currentGiocatore.getNome() + " hai vinto una vita");
                            currentGiocatore.addVitaExtra();
                            TC.updateVitaUI();
                            System.out.println("[PROBABILITA] Ora possiedi " + currentGiocatore.getVita() + " vite");
                            TC.mostraBannerAttesa("PROBABILITA [DADI]", "Ti e uscito il valore del dado (" + valoreDadoNew + ") e hai guadagnato una vita-extra!");
                            AudioManager.vitaUPSuono();
                        }
                        else
                        {
                            System.out.println("[PROBABILITA] Sei stato sfortunato, niente vita per te. Gioca il tuo turno");
                            TC.mostraBannerAttesa("PROBABILITA [DADI]", "Sei stato sfortunato, e uscito il valore (" + valoreDadoNew + "). Niente vita-extra");

                        }
                    }
                });

                Thread.sleep(4000);

                Platform.runLater(() ->
                {
                    TC.nascondiBannerAttesa();
                    TC.setExitGame(true);
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void ScambiaCartaConMazzoUI(Partita partita, IGiocatore currentGiocatore, TavoloController TC) // ok pensiamo
    {
        // todo impostare suono per scambio com mazzo
        Thread thread = new Thread(() -> {
            try {

                Platform.runLater(() ->
                {
                    if(partita.isGameStopped())
                        return;

                    TC.setExitGame(false);
                    AudioManager.probabilitaSuono();
                    System.out.println("[PROBABILITA] Posso scambiare la carta con il mazzo");
                    TC.mostraBannerAttesa("PROBABILITA [SCAMBIO-EXTRA]", "Puoi scambiare la carta con il mazzo");
                });

                Thread.sleep(4000);

                Platform.runLater(() ->
                {
                    TC.nascondiBannerAttesa();


                    if(currentGiocatore instanceof Giocatore) // se sono un giocatore normale
                    {
                        TC.pulsanteScambiaMazzo(true); // metto il bottone scambio extra
                    }
                    else // se sono un bot
                    {
                        TC.gestisciPulsanti(false, false, false);
                        System.out.println("[GAME] Sono un BOT");

                        if(((Bot)currentGiocatore).attivoEffetto(partita, TC)) // scambio la carta
                        {
                            System.out.println("[PROBABILITA] Il bot ha deciso di usare la probabilita scambio!");
                            // TODO capire come fare attesa per far capire se il bot ha deciso di usare la prob o no!
                            currentGiocatore.setCarta(partita.mazzo.PescaCartaSenzaEffetto());
                            TC.updateCarteUI();
                        }
                        else
                        {
                            System.out.println("[PROBABILITA] Il bot ha rifiutato lo scambiato con il mazzo");
                        }
                        TC.setExitGame(false);
                    }

                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
}