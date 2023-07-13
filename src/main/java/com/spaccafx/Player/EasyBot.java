package com.spaccafx.Player;

import com.spaccafx.Cards.Carta;

public class EasyBot extends Bot
{
    public EasyBot(){
        super();
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

    @Override
    public boolean Scelta() { //ipotizziamo che l'easyBot scelga randomicamente se switchare o no la carta
        int risultato = (int)((Math.random() * 1) + 1); //genero o 0 o 1
        if(risultato==1)
            return true; //scambio SI
        else
            return false; //scambio NO
    }

    @Override
    public String generaNomeBot() { //generiamo nomi per i bot(es: user1234)
        int numeroBot=(int)(Math.random() * (9999 - 1 + 1) + 1);
        String nomeBot="user" + numeroBot;
        return nomeBot;
    }
}
