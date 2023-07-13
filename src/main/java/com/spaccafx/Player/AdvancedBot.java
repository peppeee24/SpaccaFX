package com.spaccafx.Player;

import com.spaccafx.Cards.Carta;

public class AdvancedBot extends Bot
{
    public AdvancedBot(){
        super();
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
    public boolean Scelta() { //ipotizziamo che l'advancedBot scelga in modo intelligente se switchare o no la carta
        return carta.getValore() > 4;
    }

    @Override
    public String generaNomeBot() { //generiamo nomi per i bot(es: user1234)
        int numeroBot=(int)(Math.random() * (9999 - 1 + 1) + 1);
        String nomeBot="user" + numeroBot;
        return nomeBot;
    }
}
