package com.spaccafx.Cards;

import com.spaccafx.Enums.*;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import javafx.scene.image.Image;

public abstract class Carta
{
    private int valore;
    private Image image;


    public Carta(int valore)
    {
        this.valore = valore;
    }


    public void setValore(int valore){this.valore = valore;}

    public void setImage(Image image){this.image = image;}


    public int getValore(){return this.valore;}
    public Image getImmagineCarta(){return this.image;}


    public abstract String toString();

    //public abstract void Effetto(Partita partita);

    public abstract void Effetto(Partita partita, IGiocatore currentGiocatore);
    // Effetto per le carte imprevisto e probabilità

}
