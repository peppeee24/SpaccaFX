package com.spaccafx.Cards;

import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.RuoloGiocatore;
import com.spaccafx.Enums.SemeCarta;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.Giocatore;
import javafx.application.Platform;

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
    public void Effetto(Partita partita, IGiocatore currentGiocatore, TavoloController TC)
    {
        if(this.attivato)
            return;

        int scelta = (int)((1 + Math.random() * 2)); //genero o 1 o 2 che sono i "codici" delle scelte;

        switch(scelta)
        {
            case 1:  ObbligoPasso(partita, currentGiocatore, TC); break;
            case 2:  ObbligoScambioConMazzo(partita, currentGiocatore, TC); break;
            default: break;
        }

        this.attivato=true;
    }

    private void ObbligoPasso(Partita partita, IGiocatore currentGiocatore, TavoloController TC)
    {
        TC.gestisciPulsanti(false,false,false);
        System.out.println("[IMPREVISTO] Sei obbligato a passare il turno!");
        TC.mostraBannerAttesa("IMPREVISTO [PASSO-FORZATO]", "Sei obbligato a passare il turno!");

        Thread thread = new Thread(() -> {
            try {

                Thread.sleep(4000);

                Platform.runLater(() ->
                {
                    TC.nascondiBannerAttesa();
                    partita.passaTurnoUI();
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    private void ObbligoScambioConMazzo(Partita partita, IGiocatore currentGiocatore, TavoloController TC) // TODO rivedere controllaMano con questo metodo, problema setcartagiocatore
    {

        Thread thread = new Thread(() -> {
            try {
                Platform.runLater(() ->
                {
                    TC.gestisciPulsanti(false,false,false);
                    System.out.println("[IMPREVISTO] Sei obbligato a scambiare la carta con il mazzo");
                    TC.mostraBannerAttesa("IMPREVISTO [SCAMBIO-FORZATO]", "Sei obbligato a scambiare la carta con il mazzo");
                });


                Thread.sleep(4000);

                Platform.runLater(() ->
                {
                    TC.nascondiBannerAttesa();


                    Carta newCarta = partita.mazzo.PescaCartaSenzaEffetto(); // restituisce la prima carta senza effetto
                    currentGiocatore.setCarta(newCarta);
                    System.out.println("[IMPREVISTO] La carta che hai pescato è: " + currentGiocatore.getCarta().toString());

                    TC.updateCarteUI();

                });

                Thread.sleep(2000); // attesa per lasciar vedere le carte che prende

                Platform.runLater(partita::passaTurnoUI);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    public boolean isAttivato(){return this.attivato;}
    public void setAttivato(boolean attivato){this.attivato=attivato;}
}
