package com.spaccafx.Cards;

import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.*;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import javafx.scene.image.Image;

public abstract class Carta
{
    protected int valore;

    protected Image image;
    protected SemeCarta seme;

    protected TavoloController TC;

    public Carta(int valore) {this.valore = valore;}
    public Carta(int valore, SemeCarta seme) {this.valore = valore; this.seme = seme;}
    public Carta(int valore, SemeCarta seme, TavoloController TC) {this.valore = valore; this.seme = seme; this.TC = TC;}

    public void setValore(int valore){this.valore = valore;}
    // TODO INSERIRE IL SETBACK
    public void setImage(Image image){this.image = image;}
    public void setSeme(SemeCarta seme){this.seme = seme;}
    public void setTavoloController(TavoloController TC){this.TC = TC;}


    public int getValore(){return this.valore;}
    public Image getImmagineCarta(){return this.image;}

    public SemeCarta getSeme(){return this.seme;}
    public TavoloController getTavoloController(){return  this.TC;}


    public abstract String toString();

    //public abstract void Effetto(Partita partita);

    public abstract void Effetto(Partita partita, IGiocatore currentGiocatore, TavoloController TC);
    // Effetto per le carte imprevisto e probabilità

}
