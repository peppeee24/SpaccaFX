package com.spaccafx.Interface;

import com.spaccafx.Cards.Carta;
import com.spaccafx.Enums.RuoloGiocatore;

public interface IGiocatore
{
    // region # SETTER
    public void setNome(String nome);
    public void setVita(int vita);
    public void setRuolo(RuoloGiocatore ruolo);
    public void setDado(int valoreDado);
    public void setPlayerRounds(int playerRounds);
    public void setCarta(Carta carta);
    public void setVitaExtra(int vitaExtra);
    // endregion

    // region # GETTER
    public String getNome();
    public int getVita();
    public Carta getCarta();
    public int getValoreDado();
    public int getVitaExtra();
    public int getPlayerRounds();
    public RuoloGiocatore getRuolo();
    //endregion

    // region # METHODS
    public void addVitaExtra();
    public void removeVitaExtra();
    public boolean hasVitaExtra();
    //endregion

}
