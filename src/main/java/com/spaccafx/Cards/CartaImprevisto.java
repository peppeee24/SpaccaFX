package com.spaccafx.Cards;

import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.RuoloGiocatore;
import com.spaccafx.Enums.SemeCarta;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import javafx.application.Platform;

public class CartaImprevisto extends Carta
{
    private boolean attivato = false;

    public CartaImprevisto(int valore, SemeCarta seme, TavoloController TC)
    {
        super(valore, seme, TC);
    }

    @Override
    public String toString(){return this.getValore() + " di IMPREVISTO";}

    @Override
    public void Effetto(Partita partita, IGiocatore currentGiocatore, TavoloController TC)
    {
        if(this.attivato)
            return;

        int scelta = (int)((1 + Math.random() * 2)); //genero o 1 o 2 che sono i "codici" delle scelte;

        switch(scelta)
        {
            case 1:  //PerdiVitaConDado(partita, currentGiocatore, TC); break;
            case 2:  ObbligoScambioConMazzo(partita, currentGiocatore, TC); break;
            case 3:  ScopriTuaCarta(partita, TC); break;
            default: break;
        }

        // TODO OGNI VOLTA CHE SI ESEGUE L'EFFETTO BISOGNA FAR RIPOSARE IL THREAD PER FAR FERMARE IL GIOCO UN PICCOLO LASSO DI TEMPO

        this.attivato=true;
    }


    private void ScopriTuaCarta(Partita partita, TavoloController TC){
        //TC.mostraCartaSpeciale("Imprevisto", "Mostra la tua carta a tutti i giocatori");
        TC.mostraCarta(partita.getCurrentGiocatorePos()); // todo LA CARTA DEVE RIMANERE VISIBILE PER TUTTO IL ROUND

        //TODO in codice non è utile, va codificato poi con la parte graFICA
    }

    private void ObbligoScambioConMazzo(Partita partita, IGiocatore currentGiocatore, TavoloController TC) // TODO rivedere controllaMano con questo metodo, problema setcartagiocatore
    {
        //attesa...
     //   TC.mostraCartaSpeciale("Imprevisto", "Sei obbligato a scambiare la tua carta con una dal mazzo");

        //TC.showScambiaBlu(true, false, false);

        System.out.println("IMPREVISTO! - Sei obbligato a scambiare la tua carta con una dal mazzo");
        Carta newCarta = partita.mazzo.PescaCartaSenzaEffetto();
       // partita.giocatori.get(partita.giocatori.indexOf(currentGiocatore)).setCarta(newCarta);
        currentGiocatore.setCarta(newCarta);
        System.out.println("La carta che hai pescato è: " + currentGiocatore.getCarta().toString()); //TODO nuovo metodo per fare in modo che ti dica il valore e di che tipo è la carta ad esempio: 2 di cane
        System.out.println("Ora fai la tua mossa");

        TC.updateCarteUI();
        // attesa ...
        partita.passaTurnoUI();
    }


    // TODO SGAMBE FAI LA GRAFICA PER ATTIVARE IL METODO
    private void PerdiVitaConDado(Partita partita, IGiocatore currentGiocatore, TavoloController TC)
    {
        System.out.println("IMPREVISTO - Tira il dado, se il numero che ti esce è uguale al valore della tua carta perdi una vita!");
        //TC.mostraCartaSpeciale("IMPREVISTO","Tira il dado, se il numero che ti esce è uguale al valore della tua carta perdi una vita!"); // controlla

        System.out.println(currentGiocatore.getNome() + " TIRA IL DADO!");

        int valoreDadoNew = partita.lancioDadoSingolo();
        TC.rollLite(valoreDadoNew, partita.getCurrentGiocatorePos());
        // todo non mostra il dado
        System.out.println("Valore dado: " + valoreDadoNew);
        //capire se settare o no il nuovo valore del dado con setDado(). Non dovrebbe servire
        if(valoreDadoNew==currentGiocatore.getCarta().getValore()){
            System.out.println("Che sfortuna " + currentGiocatore.getNome() + " hai perso una vita");
            //TC.mostraCartaSpeciale("Che sfortuna","Hai perso una vita");
            if(currentGiocatore.getVita()==1){ //TODO decidere se eliminare o no. Al momento implementata l'opzione della NON eliminazione
                //caso eliminazione, gestire eliminazione
                /*System.out.println("Avevi solo una vita rimasta " + currentGiocatore.getNome() + " sei stato ELIMINATO!");
                currentGiocatore.setVita(currentGiocatore.getVita() - 1);*/
                //caso NON eliminazione
                //TC.mostraCartaSpeciale("Che fortuan","Siccome hai solo una vita rimanente non vieni eliminato");
                System.out.println("Siccome hai solo una vita rimanente non vieni eliminato");
                System.out.println("Il gioco prosegue");
            }

            currentGiocatore.setVita(currentGiocatore.getVita() - 1);
            TC.updateVitaUI();
            System.out.println("Ora possiedi " + currentGiocatore.getVita() + " vite");
        }
        else{
            System.out.println("Sei stato fortunato, niente vita persa per te. Gioca il tuo turno");
            //TC.mostraCartaSpeciale("Sei stato fortunato","Non hai perso nessuna vita");
        }
    }

    public boolean isAttivato(){return this.attivato;}
    public void setAttivato(boolean attivato){this.attivato=attivato;}
}
