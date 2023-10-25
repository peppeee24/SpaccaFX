package com.spaccafx.Player;

import com.spaccafx.Cards.*;
import com.spaccafx.Enums.*;
import com.spaccafx.Interface.IGiocatore;

public class Giocatore implements IGiocatore
{
    private String nome;
    private int vita;
    private Carta carta;
    private RuoloGiocatore ruolo;
    private int valoreDado;

    private int pos;
    //TODO controlla costruttore come cambia con pos

    public Giocatore()
    {
        this.nome = "Giocatore";
        this.vita = 3;
        this.carta = null;
    }

    public Giocatore(Carta carta)
    {
        this.nome = "Giocatore";
        this.vita = 3;
        this.carta = carta;
    }

    public Giocatore(String nome)
    {
        this.nome = nome;
        this.vita = 3;
        this.carta = null;
    }

    public Giocatore(String nome, Carta  carta)
    {
        this.nome = nome;
        this.vita = 3;
        this.carta = carta;
    }

    public void setNome(String nome){this.nome = nome;}
    public void setVita(int vita){this.vita = vita;}
    public void setRuolo(RuoloGiocatore ruolo){this.ruolo = ruolo;}
    public void setDado(int valoreDado){this.valoreDado = valoreDado;}
    public void setCarta(Carta carta){this.carta = carta;}
    public void setPos(int pos){this.pos=pos;}

    public String getNome(){return this.nome;}
    public int getVita(){return  this.vita;}
    public Carta getCarta(){return this.carta;}
    public int getValoreDado(){return this.valoreDado;}
    public RuoloGiocatore getRuolo(){return this.ruolo;}

}
