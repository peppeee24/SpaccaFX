package com.spaccafx.Player;

import com.spaccafx.Cards.Carta;
import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.RuoloGiocatore;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import javafx.application.Platform;

public class EasyBot extends Bot
{
    public EasyBot(){
        super();
        //setNome(generaNomeBot());
    }
    public EasyBot(String nome){
        super(nome);
    }
    public EasyBot(Carta carta){
        super(carta);
    }
    public EasyBot(String nome, Carta carta){
        super(nome,carta);
    }
    public EasyBot(String nome, int playerRounds, int vite, int viteExtra) {super(nome, playerRounds, vite, viteExtra);}


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

                    System.out.println("[EZ-BOT] Sto facendo la scelta...");
                    TC.mostraBannerAttesa("[EZ-BOT]", "Sto decidendo la scelta...");
                });


                Thread.sleep(3000);

                Platform.runLater(() -> {
                    TC.nascondiBannerAttesa();

                    System.out.println("[EZ-BOT] ho la carta con valore: " + carta.getValore());

                    int scelta = (int)((1 + Math.random() * 2));
                    if(scelta==1){
                        p.passaTurnoUI();
                    }
                    else
                        p.ScambiaCartaUI();
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
    }


    @Override
    public boolean attivoEffetto(Partita p)
    {
        int scelta = (int)((1 + Math.random() * 2));
        return scelta == 1;
    }
}
