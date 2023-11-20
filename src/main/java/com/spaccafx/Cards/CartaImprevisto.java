package com.spaccafx.Cards;

import com.spaccafx.Controllers.TavoloController;
import com.spaccafx.Enums.RuoloGiocatore;
import com.spaccafx.Enums.SemeCarta;
import com.spaccafx.Files.AudioManager;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.Giocatore;
import javafx.application.Platform;

public class CartaImprevisto extends Carta
{
    public CartaImprevisto(int valore, SemeCarta seme)
    {
        super(valore, seme);
    }

    @Override
    public String toString(){return this.getValore() + " di IMPREVISTO";}

    @Override
    public void Effetto(Partita partita, IGiocatore currentGiocatore, TavoloController TC)
    {
        if(partita.isGameStopped())
            return;

        if(this.attivato)
            return;



        TC.gestisciPulsanti(false,false,false);

        System.out.println("[IMPREVISTO] Attivato effetto carta: "+ toString() );

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
        Thread thread = new Thread(() -> {
            try {

                Platform.runLater(() ->
                {
                    TC.setExitGame(false);
                    AudioManager.imprevistoSuono();
                    System.out.println("[IMPREVISTO] Sei obbligato a passare il turno!");
                    TC.mostraBannerAttesa("IMPREVISTO [PASSO-FORZATO]", "Sei obbligato a passare il turno!");
                });

                Thread.sleep(4000);

                Platform.runLater(() ->
                {
                    TC.nascondiBannerAttesa();
                    //this.attivatoSpecial = true;
                    partita.passaTurnoUI(); // viene riattivato qui il pulsante exit
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
                    TC.setExitGame(false);
                    AudioManager.imprevistoSuono();
                    System.out.println("[IMPREVISTO] Sei obbligato a scambiare la carta con il mazzo");
                    TC.mostraBannerAttesa("IMPREVISTO [SCAMBIO-FORZATO]", "Sei obbligato a scambiare la carta con il mazzo e a passare!");
                });


                Thread.sleep(4000);

                Platform.runLater(() ->
                {
                    TC.nascondiBannerAttesa();


                    Carta newCarta = partita.mazzo.PescaCartaSenzaEffetto(); // restituisce la prima carta senza effetto
                    currentGiocatore.setCarta(newCarta);
                    System.out.println("[IMPREVISTO] La carta che hai pescato è: " + currentGiocatore.getCarta().toString());
                    //this.attivatoSpecial = true;

                    TC.mostraBannerAttesa("IMPREVISTO [SCAMBIO-FORZATO]", "Hai pescato " + newCarta.toString());
                    TC.updateCarteUI();
                });

                Thread.sleep(3000); // attesa per lasciar vedere le carte che prende

                Platform.runLater(partita::passaTurnoUI); // viene riattivato qui il pulsante


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
}
