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
    private int playerRounds;

    public Bot()
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = "Bot";
        this.vita = 2;
        vitaExtra = 0;
        this.carta = null;
    }
    public Bot(String nome)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = nome;
        this.vita = 2;
        vitaExtra = 0;
        this.carta = null;
    }
    public Bot(Carta carta)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome = "Bot";
        this.vita = 2;
        vitaExtra = 0;
        this.carta = carta;
    }
    public Bot(String nome, Carta carta)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome=nome;
        this.vita= 2;
        vitaExtra = 0;
        this.carta=carta;
    }

    public Bot(String nome, int rounds, int vite, int viteExtra)
    {
        this.ruolo = RuoloGiocatore.GIOCATORE;
        this.nome=nome;
        this.vita= vite;
        this.vitaExtra = viteExtra;
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

    // region # GETTER
    public String getNome(){return this.nome;}
    public int getVita(){return  this.vita;}
    public Carta getCarta(){return this.carta;}
    public int getValoreDado(){return this.valoreDado;}
    public int getVitaExtra(){return this.vitaExtra;}
    public int getPlayerRounds(){return this.playerRounds;}
    public RuoloGiocatore getRuolo(){return this.ruolo;}
    //endregion


    // region # METHODS ABSTRACT
    public abstract void SceltaBotUI(Partita p, TavoloController TC);
    public abstract boolean attivoEffetto(Partita p);
    // endregion

    // region # METHODS
    public void addVitaExtra() {vitaExtra = 1;}
    public void removeVitaExtra(){vitaExtra = 0;}
    public boolean hasVitaExtra(){return vitaExtra == 1;} // restituisce true se ha 1 come vita extra
    //endregion

}
