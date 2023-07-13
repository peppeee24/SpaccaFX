package com.spaccafx.Player;

import com.spaccafx.Cards.*;
import com.spaccafx.Enums.*;

public abstract class Bot
{
    private String nome;
    private int vita;
    protected Carta carta;
    private RuoloGiocatore ruolo;
    private int valoreDado;

    public Bot()
    {
        this.nome = "Bot";
        this.vita = 2;
        this.carta = null;
    }
    public Bot(String nome)
    {
        this.nome = nome;
        this.vita = 2;
        this.carta = null;
    }
    public Bot(Carta carta)
    {
        this.nome = "Bot";
        this.vita = 2;
        this.carta = carta;
    }
    public Bot(String nome, Carta carta)
    {
        this.nome=nome;
        this.vita=2;
        this.carta=carta;
    }

    //valutare se tenere i get e i set di tutto

    public void setNome(String nome){this.nome = nome;}
    public void setVita(int vita){this.vita = vita;}
    public void setRuolo(RuoloGiocatore ruolo){this.ruolo = ruolo;}
    public void setDado(int valoreDado){this.valoreDado = valoreDado;}
    public void setCarta(Carta carta){this.carta = carta;}

    public String getNome(){return this.nome;}
    public int getVita(){return  this.vita;}
    public Carta getCarta(){return this.carta;}
    public int getValoreDado(){return this.valoreDado;}
    public RuoloGiocatore getRuolo(){return this.ruolo;}
    public abstract boolean Scelta();
    public abstract String generaNomeBot();




}
