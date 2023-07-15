package com.spaccafx.Player;

import com.spaccafx.Cards.Carta;
import com.spaccafx.Interface.IGiocatore;

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
    public int Scelta() { //ipotizziamo che l'advancedBot scelga in modo intelligente se switchare o no la carta
        if(carta.getValore()<=4)
            return 2; //non cambia la carta
        else
            return 1; // cambia la carta
    }

    @Override
    public String generaNomeBot() //generiamo nomi per i bot(es: user1234)
    {
        int numeroBot=(int)(Math.random() * (9999 - 1 + 1) + 1);
        String nomeBot="AdvBot_" + numeroBot;
        return nomeBot;
    }
}
