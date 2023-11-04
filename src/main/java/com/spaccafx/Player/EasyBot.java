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
        setNome(generaNomeBot());
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

    /*@Override
    public int Scelta(Partita p) { //ipotizziamo che l'easyBot scelga randomicamente se switchare o no la carta
        return (int)((1 + Math.random() * 2)); //genero o 1 o 2 che sono i "codici" delle scelte;
    }

     */

    @Override
    public void SceltaBotUI(Partita p, TavoloController TC)
    {
        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() ->
                {
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
    public boolean attivoEffetto(Partita p, TavoloController TC) {
        final boolean[] attivaEffetto = { false };

        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    System.out.println("[EZ-BOT] Sto facendo la scelta...");
                    TC.mostraBannerAttesa("[EZ-BOT]", "Sto decidendo la scelta...");
                });

                Thread.sleep(3000);

                Platform.runLater(() -> {
                    TC.nascondiBannerAttesa();

                    System.out.println("[EZ-BOT - EFFETTO] ho la carta con valore: " + carta.getValore());
                    int scelta = (int)(1 + Math.random() * 2);
                    attivaEffetto[0] = scelta != 1;
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

        return attivaEffetto[0];
    }


    @Override
    public String generaNomeBot() { //generiamo nomi per i bot(es: user1234)
        int numeroBot=(int)(1 + (Math.random() * 1000));
        String nomeBot="EzBot_" + numeroBot;
        return nomeBot;
    }
}
