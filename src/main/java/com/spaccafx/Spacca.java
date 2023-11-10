package com.spaccafx;

import com.spaccafx.Cards.CartaImprevisto;
import com.spaccafx.ExternalApps.SpaccaTGBot;
import com.spaccafx.Files.AudioManager;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.GameManager;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Manager.Torneo;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;


import javafx.application.Application;
import javafx.application.Platform;
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

// TODO inserire animazioni
        Thread thread = new Thread(() -> {
            try {

                Platform.runLater(() ->
                {
                    FXMLLoader MainMenu = new FXMLLoader(Spacca.class.getResource("Splash1.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(MainMenu.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle("Alpha Build SpaccaFX");
                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.show();
                });

                Thread.sleep(3000);

                Platform.runLater(() -> {

                    FXMLLoader MainMenu = new FXMLLoader(Spacca.class.getResource("Splash2.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(MainMenu.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle("Alpha Build SpaccaFX");

                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.show();
                });



                Thread.sleep(3000);

                Platform.runLater(() -> {

                    FXMLLoader MainMenu = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(MainMenu.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle("Alpha Build SpaccaFX");

                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.show();
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();


/*
        try {

            FXMLLoader MainMenu = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
            Scene scene = new Scene(MainMenu.load());
            stage.setTitle("Alpha Build SpaccaFX");

            stage.setResizable(true);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


 */
    }

    public static void main(String[] args) {
        launch();
        //InitializeTelegramBot(); // TODO IL BOT DEVE INIZIALIZZARSI SOLO UNA VOLTA, QUINDI DEVE MANDARE IL MESSAGGIO DI AVVIO UNA SOLA VOLTA NEL CANALE TG QUANDO SI ESEGUE IL PROGRAMMA
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
}