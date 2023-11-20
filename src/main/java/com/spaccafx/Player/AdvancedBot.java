package com.spaccafx.Player;

import com.spaccafx.Cards.Carta;
import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Manager.Partita;
import javafx.application.Platform;

public class AdvancedBot extends Bot
{
    public AdvancedBot(){
        super();
        setNome(generaNomeBot());
    }
    public AdvancedBot(String nome){
        super(nome);
    }
    public AdvancedBot(Carta carta){
        super(carta);
    }
    public AdvancedBot(String nome, Carta carta){
        super(nome,carta);
    }


    @Override
    public void SceltaBotUI(Partita p, TavoloController TC)
    {
        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() ->
                {
                    if(p.isGameStopped())
                        return;

                    TC.setExitGame(false);
                    TC.gestisciPulsanti(false, false, false);

                    System.out.println("[ADV-BOT] Sto facendo la scelta...");
                    TC.mostraBannerAttesa("[ADV-BOT]", "Sto decidendo la scelta...");
                });


                Thread.sleep(3000);

                Platform.runLater(() -> {
                    TC.nascondiBannerAttesa();

                    System.out.println("[HARD-BOT] ho la carta con valore: " + carta.getValore());
                    int min = (int)(p.mazzo.getMaxCarteNormali() / 2);
                    System.out.println("[HARD-BOT] Il valore minimo per NON passare e: " + min);

                    if(carta.getValore() <= min)
                        p.passaTurnoUI(); //non cambia la carta
                    else
                        p.ScambiaCartaUI(); // cambia la carta
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
    }

    @Override
    public boolean attivoEffetto(Partita p, TavoloController TC)
    {

        final int[] min = { 0 }; // Dichiarare min al di fuori del thread

        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() ->
                {
                    System.out.println("[ADV-BOT] Sto facendo la scelta...");
                    TC.mostraBannerAttesa("[ADV-BOT]", "Sto decidendo la scelta...");
                });


                Thread.sleep(3000);

                Platform.runLater(() -> {
                    TC.nascondiBannerAttesa();

                    System.out.println("[HARD-BOT - EFFETTO] ho la carta con valore: " + carta.getValore());
                    min[0] = (int)(p.mazzo.getMaxCarteNormali() / 2);
                    System.out.println("[HARD-BOT] Il valore minimo per NON passare e: " +  min[0]);
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();

        try {
            thread.join(); // Attendi che il thread termini
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return carta.getValore() >  min[0];
    }


    @Override
    public String generaNomeBot() //generiamo nomi per i bot(es: user1234)
    {
        int numeroBot=(int)(1 + (Math.random() * 1000));
        String nomeBot="AdvBot_" + numeroBot;
        return nomeBot;
    }
}
