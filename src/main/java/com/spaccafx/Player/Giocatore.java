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
    private int playerRounds;

    public Giocatore()
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = "Giocatore";
        this.vita = 1;
        vitaExtra = 0;
        this.carta = null;
    }

    public Giocatore(Carta carta)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = "Giocatore";
        this.vita = 1;
        vitaExtra = 0;
        this.carta = carta;
    }

    public Giocatore(String nome)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = nome;
        this.vita = 1;
        vitaExtra = 0;
        this.carta = null;
    }

    public Giocatore(String nome, Carta  carta)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = nome;
        this.vita = 1;
        vitaExtra = 0;
        this.carta = carta;
    }

    public Giocatore(String nome, int rounds, int vite, int vitaExtra)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome=nome;
        this.vita= vite;
        this.vitaExtra = vitaExtra;
        this.carta=null;
        this.playerRounds = rounds;
    }

    // region # SETTER
    public void setNome(String nome){this.nome = nome;}
    public void setVita(int vita){this.vita = vita;}
    public void setRuolo(RuoloGiocatore ruolo){this.ruolo = ruolo;}
    public void setDado(int valoreDado){this.valoreDado = valoreDado;}
    public void setPlayerRounds(int playerRounds){this.playerRounds = playerRounds;}
    public void setCarta(Carta carta){this.carta = carta;}
    public void setVitaExtra(int vitaExtra){this.vitaExtra = vitaExtra;}
    // endregion

    // region #GETTER
    public String getNome(){return this.nome;}
    public int getVita(){return  this.vita;}
    public Carta getCarta(){return this.carta;}
    public int getValoreDado(){return this.valoreDado;}
    public int getVitaExtra(){return this.vitaExtra;}
    public int getPlayerRounds(){return this.playerRounds;}
    public RuoloGiocatore getRuolo(){return this.ruolo;}
    // endregion

    // region # METHODS
    public void addVitaExtra() {vitaExtra = 1;}
    public void removeVitaExtra(){vitaExtra = 0;}
    public boolean hasVitaExtra(){return vitaExtra == 1;} // restituisce true se ha 1 come vita extra
    // endregion

}
