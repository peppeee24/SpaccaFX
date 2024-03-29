package com.spaccafx;

import com.spaccafx.ExternalApps.SpaccaTGBot;


import com.spaccafx.Files.ResourceLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.*;
import java.io.*;

public class Spacca extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // region # SCHEDE
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

                    InputStream styleStream = Spacca.class.getResourceAsStream("PartitaStyle.css");

                    if (styleStream == null) {
                        System.out.println("File CSS non trovato");
                    } else {
                        scene.getStylesheets().add(getClass().getResource("PartitaStyle.css").toExternalForm());
                        System.out.println("Impostato CSS!");
                    }


                    stage.setTitle("SpaccaFX - Game");
                    stage.getIcons().add(ResourceLoader.gameIcons());
                    stage.setOnCloseRequest(event -> {
                        // Aggiungi qui la logica di chiusura dell'applicazione
                        System.out.println("L'utente sta chiudendo l'applicazione");
                        // Chiudi l'applicazione
                        System.exit(0);
                    });

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
        // endregion
    }

    public static void main(String[] args) {
        launch();
        InitializeTelegramBot();
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