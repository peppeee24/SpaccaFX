package com.spaccafx.Interface;

import com.spaccafx.Cards.Carta;
import com.spaccafx.Enums.RuoloGiocatore;

public interface IGiocatore
{
    public void setNome(String nome);
    public void setVita(int vita);
    public void setRuolo(RuoloGiocatore ruolo);
    public void setDado(int valoreDado);
    public void setCarta(Carta carta);

    public String getNome();
    public int getVita();
    public Carta getCarta();
    public int getValoreDado();
    public int getVitaExtra();
    public RuoloGiocatore getRuolo();
    public boolean getIsAlive();

    public void addVitaExtra();
    public void removeVitaExtra();
    public boolean hasVitaExtra();
    public void setVitaExtra(int vitaExtra);
    public void setIsAlive(boolean isAlive);

}
