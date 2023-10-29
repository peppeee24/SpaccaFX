package com.spaccafx.Player;

import com.spaccafx.Cards.Carta;
import com.spaccafx.Enums.RuoloGiocatore;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;

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
    public void SceltaBotUI(Partita p){
        System.out.println("[EZ-BOT] ho la carta con valore: " + carta.getValore());

        int scelta = (int)((1 + Math.random() * 2));
        if(scelta==1){
            p.passaTurnoUI();
        }
        else
            p.ScambiaCartaUI();
    }

    @Override
    public boolean attivoEffetto(Partita p)
    {
        System.out.println("[EZ-BOT - EFFETTO] ho la carta con valore: " + carta.getValore());
        int scelta = (int)((1 + Math.random() * 2));

        return scelta != 1; // se la scelta e diverso da 1 allora ritorna true e attiva effetto bot
    }


    @Override
    public String generaNomeBot() { //generiamo nomi per i bot(es: user1234)
        int numeroBot=(int)(1 + (Math.random() * 1000));
        String nomeBot="EzBot_" + numeroBot;
        return nomeBot;
    }
}
