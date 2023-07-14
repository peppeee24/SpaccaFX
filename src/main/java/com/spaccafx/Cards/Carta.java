package com.spaccafx.Cards;

import com.spaccafx.Enums.*;
import javafx.scene.image.Image;

public class Carta
{
    private int valore;
    private SemeCarta seme;
    private Image image;

    public Carta(int valore)
    {
        this.valore = valore;
    }

    public Carta(int valore, SemeCarta seme)
    {
        this.valore = valore;
        this.seme = seme;
    }

    public void setValore(int valore){this.valore = valore;}
    public void setSeme(SemeCarta seme){this.seme = seme;}

    public int getValore(){return this.valore;}
    //public Sprite getSprite(){return this.image;}
    public SemeCarta getSeme(){return this.seme;}
    public String toString(){return " " + this.valore + " di " + this.seme;}
    public void setImage(Image image){}

}
