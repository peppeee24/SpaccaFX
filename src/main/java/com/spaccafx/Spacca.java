package com.spaccafx;

import com.spaccafx.ExternalApps.SpaccaTGBot;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.*;

public class Spacca extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        Thread thread = new Thread(() -> {
            try {

                Platform.runLater(() ->
                {
                    //stage.initStyle(StageStyle.UTILITY); // per lasciare solo la x

                    FXMLLoader MainMenu = new FXMLLoader(Spacca.class.getResource("Splash1.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(MainMenu.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
               //     scene.getStylesheets().add(getClass().getResource("grafica.css").toExternalForm());

                    InputStream styleStream = Spacca.class.getResourceAsStream("PartitaStyle.css");

                    if (styleStream == null) {
                        System.out.println("File CSS non trovato");
                    } else {
                        scene.getStylesheets().add(getClass().getResource("PartitaStyle.css").toExternalForm());
                        System.out.println("Impostato CSS!");
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