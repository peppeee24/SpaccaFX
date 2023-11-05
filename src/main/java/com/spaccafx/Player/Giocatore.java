package com.spaccafx.Player;

import com.spaccafx.Cards.*;
import com.spaccafx.Enums.*;
import com.spaccafx.Interface.IGiocatore;

public class Giocatore implements IGiocatore
{
    private String nome;
    private int vita, vitaExtra;
    private Carta carta;
    private RuoloGiocatore ruolo;
    private int valoreDado;

    public Giocatore()
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = "Giocatore";
        this.vita = 2;
        vitaExtra = 0;
        this.carta = null;
    }

    public Giocatore(Carta carta)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = "Giocatore";
        this.vita = 2;
        vitaExtra = 0;
        this.carta = carta;
    }

    public Giocatore(String nome)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = nome;
        this.vita = 2;
        vitaExtra = 0;
        this.carta = null;
    }

    public Giocatore(String nome, Carta  carta)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = nome;
        this.vita = 2;
        vitaExtra = 0;
        this.carta = carta;
    }

    public void setNome(String nome){this.nome = nome;}
    public void setVita(int vita){this.vita = vita;}
    public void setRuolo(RuoloGiocatore ruolo){this.ruolo = ruolo;}
    public void setDado(int valoreDado){this.valoreDado = valoreDado;}
    public void setCarta(Carta carta){this.carta = carta;}

    public String getNome(){return this.nome;}
    public int getVita(){return  this.vita;}
    public Carta getCarta(){return this.carta;}
    public int getValoreDado(){return this.valoreDado;}
    public int getVitaExtra(){return this.vitaExtra;}
    public RuoloGiocatore getRuolo(){return this.ruolo;}

    public void addVitaExtra() {vitaExtra = 1;}
    public void removeVitaExtra(){vitaExtra = 0;}
    public boolean hasVitaExtra(){return vitaExtra == 1;} // restituisce true se ha 1 come vita extra

}
