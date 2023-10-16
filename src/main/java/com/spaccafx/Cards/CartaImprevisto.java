package com.spaccafx.Cards;

import com.spaccafx.Enums.RuoloGiocatore;
import com.spaccafx.Enums.SemeCarta;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;

public class CartaImprevisto extends Carta
{
    private boolean attivato = false;

    public CartaImprevisto(int valore, SemeCarta seme)
    {
        super(valore, seme);
    }

    @Override
    public String toString(){return this.getValore() + " di IMPREVISTO";}

    @Override
    public void Effetto(Partita partita, IGiocatore currentGiocatore)
    {
        if(attivato)
            return;


        int scelta = (int)((1 + Math.random() * 2)); //genero o 1 o 2 che sono i "codici" delle scelte;

        switch(scelta)
        {
            case 1:  PerdiVitaConDado(partita, currentGiocatore);break;
            case 2:  ObbligoScambioConMazzo(partita, currentGiocatore); break;
            //case 3: ScopriTuaCarta(); break;
            default: break;
        }
        this.attivato=true;
    }


    private void ScopriTuaCarta(Partita partita, IGiocatore giocatore){
        //TODO in codice non è utile, va codificato poi con la parte graFICA
    }

    private void ObbligoScambioConMazzo(Partita partita, IGiocatore currentGiocatore){
        System.out.println("IMPREVISTO! - Sei obbligato a scambiare la tua carta con una dal mazzo");
        Carta newCarta = partita.mazzo.PescaCarta();
        partita.giocatori.get(partita.giocatori.indexOf(currentGiocatore)).setCarta(newCarta);
        System.out.println("La carta che hai pescato è: " + currentGiocatore.getCarta().getValore()); //TODO nuovo metodo per fare in modo che ti dica il valore e di che tipo è la carta ad esempio: 2 di cane
        System.out.println("Ora fai la tua mossa");
    }

    private void PerdiVitaConDado(Partita partita, IGiocatore currentGiocatore){
        System.out.println("IMPREVISTO - Tira il dado, se il numero che ti esce è uguale al valore della tua carta perdi una vita!");
        System.out.println(currentGiocatore.getNome() + " TIRA IL DADO!");
        int valoreDadoNew = partita.lancioDadoSingolo();
        System.out.println("Valore dado: " + valoreDadoNew);
        //capire se settare o no il nuovo valore del dado con setDado(). Non dovrebbe servire
        if(valoreDadoNew==currentGiocatore.getCarta().getValore()){
            System.out.println("Che sfortuna " + currentGiocatore.getNome() + " hai perso una vita");
            if(currentGiocatore.getVita()==1){ //TODO decidere se eliminare o no. Al momento implementata l'opzione della NON eliminazione
                //caso eliminazione, gestire eliminazione
                /*System.out.println("Avevi solo una vita rimasta " + currentGiocatore.getNome() + " sei stato ELIMINATO!");
                currentGiocatore.setVita(currentGiocatore.getVita() - 1);*/
                //caso NON eliminazione
                System.out.println("Siccome hai solo una vita rimanente non vieni eliminato");
                System.out.println("Il gioco prosegue");
            }

            currentGiocatore.setVita(currentGiocatore.getVita() - 1);
            System.out.println("Ora possiedi " + currentGiocatore.getVita() + " vite");
        }
        else{
            System.out.println("Sei stato fortunato, niente vita persa per te. Gioca il tuo turno");
        }
    }

}
