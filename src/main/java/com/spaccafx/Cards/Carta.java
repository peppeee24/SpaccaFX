package com.spaccafx.Cards;

import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.*;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import javafx.scene.image.Image;

import java.util.Objects;

public abstract class Carta
{
    protected int valore;
    protected boolean attivato = false;

    protected Image image;
    protected SemeCarta seme;

    public Carta(int valore) {this.valore = valore;}
    public Carta(int valore, SemeCarta seme) {this.valore = valore; this.seme = seme;}

    public void setValore(int valore){this.valore = valore;}
    public void setImage(Image image){this.image = image;}
    public void setSeme(SemeCarta seme){this.seme = seme;}
    public void setCartaEffettoAttivato(boolean attivato){this.attivato = attivato;}


    public int getValore(){return this.valore;}
    public Image getImmagineCarta(){return this.image;}
    public SemeCarta getSeme(){return this.seme;}
    public boolean getCartaEffettoAttivato(){return this.attivato;}


    public abstract String toString();

    public abstract void Effetto(Partita partita, IGiocatore currentGiocatore, TavoloController TC);

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Carta otherCarta = (Carta) obj;
        return valore == otherCarta.valore && this.seme.equals(otherCarta.seme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valore, this.seme);
    }

}
