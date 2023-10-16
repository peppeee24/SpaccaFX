package com.spaccafx.Player;

import com.spaccafx.Cards.Carta;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;

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
    public int Scelta(Partita p) { //ipotizziamo che l'advancedBot scelga in modo intelligente se switchare o no la carta
        if(carta.getValore()<= (int)(p.mazzo.getMaxCarteNormali() / 2)) // TODO CAMBIARE LA SCELTA SE IL VALORE DELLA CARTA E <= MAZZO.MAXCARTENORMALI/2
            return 2; //non cambia la carta
        else
            return 1; // cambia la carta
    }

    @Override
    public String generaNomeBot() //generiamo nomi per i bot(es: user1234)
    {
        int numeroBot=(int)(1 + (Math.random() * 1000));
        String nomeBot="AdvBot_" + numeroBot;
        return nomeBot;
    }
}
