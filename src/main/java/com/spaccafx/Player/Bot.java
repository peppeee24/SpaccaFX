package com.spaccafx.Player;

import com.spaccafx.Cards.*;
import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.*;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;

public abstract class Bot implements IGiocatore
{
    private String nome;
    private int vita, vitaExtra;
    protected Carta carta;
    private RuoloGiocatore ruolo;
    private int valoreDado;
    boolean isAlive;

    public Bot()
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = "Bot";
        this.vita = 2;
        vitaExtra = 0;
        this.carta = null;
        this.isAlive = true;
    }
    public Bot(String nome)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = nome;
        this.vita = 2;
        vitaExtra = 0;
        this.carta = null;
        this.isAlive = true;
    }
    public Bot(Carta carta)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = "Bot";
        this.vita = 2;
        vitaExtra = 0;
        this.carta = carta;
        this.isAlive = true;
    }
    public Bot(String nome, Carta carta)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome=nome;
        this.vita= 2;
        vitaExtra = 0;
        this.carta=carta;
        this.isAlive = true;
    }

    public void setNome(String nome){this.nome = nome;}
    public void setVita(int vita){this.vita = vita;}
    public void setRuolo(RuoloGiocatore ruolo){this.ruolo = ruolo;}
    public void setDado(int valoreDado){this.valoreDado = valoreDado;}
    public void setCarta(Carta carta){this.carta = carta;}
    public void setIsAlive(boolean isAlive){this.isAlive = isAlive;}

    public String getNome(){return this.nome;}
    public int getVita(){return  this.vita;}
    public Carta getCarta(){return this.carta;}
    public int getValoreDado(){return this.valoreDado;}
    public int getVitaExtra(){return this.vitaExtra;}
    public RuoloGiocatore getRuolo(){return this.ruolo;}
    public boolean getIsAlive(){return this.isAlive;}


    //public abstract int Scelta(Partita p);
    public abstract void SceltaBotUI(Partita p, TavoloController TC);
    public abstract boolean attivoEffetto(Partita p, TavoloController TC);
    public abstract String generaNomeBot();


    public void addVitaExtra() {vitaExtra = 1;}
    public void removeVitaExtra(){vitaExtra = 0;}
    public boolean hasVitaExtra(){return vitaExtra == 1;} // restituisce true se ha 1 come vita extra
    public void setVitaExtra(int vitaExtra){this.vitaExtra = vitaExtra;}



}
