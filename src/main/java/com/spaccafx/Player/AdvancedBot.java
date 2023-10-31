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
        System.out.println("[HARD-BOT] ho la carta con valore: " + carta.getValore());
        int min = (int)(p.mazzo.getMaxCarteNormali() / 2);
        System.out.println("[HARD-BOT] Il valore minimo per NON passare e: " + min);

        if(carta.getValore() <= min)
            p.passaTurnoUI(); //non cambia la carta
        else
            p.ScambiaCartaUI(); // cambia la carta
    }

    @Override
    public boolean attivoEffetto(Partita p)
    {
        System.out.println("[HARD-BOT - EFFETTO] ho la carta con valore: " + carta.getValore());
        int min = (int)(p.mazzo.getMaxCarteNormali() / 2);
        System.out.println("[HARD-BOT] Il valore minimo per NON passare e: " + min);

        return carta.getValore() > min;
    }


    @Override
    public String generaNomeBot() //generiamo nomi per i bot(es: user1234)
    {
        int numeroBot=(int)(1 + (Math.random() * 1000));
        String nomeBot="AdvBot_" + numeroBot;
        return nomeBot;
    }
}
