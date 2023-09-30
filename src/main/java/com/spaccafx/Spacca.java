package com.spaccafx;

import com.spaccafx.ExternalApps.SpaccaTGBot;
import com.spaccafx.Manager.GameManager;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Manager.Torneo;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.*;

import java.io.*;

public class Spacca extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        try {

            FXMLLoader MainMenu = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
            Scene scene = new Scene(MainMenu.load());
            stage.setTitle("Alpha Build SpaccaFX");

            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }






        /*
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Test alert");
        alert.setContentText("ERRORE DURANTE IL CARICAMENTO DEI FILE, ESEGUI NUOVAMENTE!");
       Optional <ButtonType> result = alert.showAndWait();
*/


    }

    public static void main(String[] args) {
        launch();
        InitializeTelegramBot();



        Giocatore p1 = new Giocatore("tester22");
        Giocatore p2 = new Giocatore("peppeee24");
        //Giocatore p3 = new Giocatore("Stronzo");

        Partita p = new Partita(2);
        p.aggiungiGiocatore(p1);
        p.aggiungiGiocatore(p2);
        p.generaCodicePartita();
        //p.aggiungiGiocatore(p3);

        p.StartGame();
        //System.exit(0);

        /*
        Bot b1 = new AdvancedBot();
        Bot b2 = new EasyBot();
        Bot b3 = new AdvancedBot();
        Bot b4 = new EasyBot();
        Bot b5 = new AdvancedBot();
        Bot b6 = new EasyBot();
        Bot b7 = new AdvancedBot();
        Bot b8 = new EasyBot();
        Bot b9 = new AdvancedBot();
        Bot b10 = new EasyBot();
        Bot b11 = new AdvancedBot();
        Bot b12 = new EasyBot();
        Bot b13 = new AdvancedBot();
        Bot b14 = new EasyBot();
        Bot b15 = new AdvancedBot();
        Bot b16 = new EasyBot();


        //TODO implementazione della scelta di fare un torneo a 8 o 16 persone
        Torneo torneo = new Torneo(16);
        //torneo.aggiungiGiocatoreTorneo(p1);
        //torneo.aggiungiGiocatoreTorneo(p2);
        torneo.aggiungiGiocatoreTorneo(b1);
        torneo.aggiungiGiocatoreTorneo(b2);
        torneo.aggiungiGiocatoreTorneo(b3);
        torneo.aggiungiGiocatoreTorneo(b4);
        torneo.aggiungiGiocatoreTorneo(b5);
        torneo.aggiungiGiocatoreTorneo(b6);
        torneo.aggiungiGiocatoreTorneo(b7);
        torneo.aggiungiGiocatoreTorneo(b8);
        torneo.aggiungiGiocatoreTorneo(b9);
        torneo.aggiungiGiocatoreTorneo(b10);
        torneo.aggiungiGiocatoreTorneo(b11);
        torneo.aggiungiGiocatoreTorneo(b12);
        torneo.aggiungiGiocatoreTorneo(b13);
        torneo.aggiungiGiocatoreTorneo(b14);
        torneo.aggiungiGiocatoreTorneo(b15);
        torneo.aggiungiGiocatoreTorneo(b16);
        torneo.StartTorneo();*/



    }


    public static void InitializeTelegramBot()
    {
        try
        {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new SpaccaTGBot());
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }

    }





    // TODO: Metodo per far terminare il programma quando si clicca x NON FUNZIONA

    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
        // Aggiungi l'handler per l'evento di chiusura qui
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                // Chiudi il programma quando la finestra viene chiusa
                System.exit(0);
            }
        });
    }
}