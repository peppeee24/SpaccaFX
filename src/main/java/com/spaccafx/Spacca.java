package com.spaccafx;

import com.spaccafx.Manager.GameManager;
import com.spaccafx.Player.Giocatore;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.*;

import java.io.IOException;

public class Spacca extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Spacca.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        primaryStage.setTitle("Prova controller!");
        //primaryStage.initStyle(StageStyle.UNDECORATED);


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        Scanner s = new Scanner(System.in);

        //System.out.println("Inserisci quanti giocatori vuoi: ");
        //int nGiocatori = s.nextInt();

        Giocatore p1 = new Giocatore("Armando");
        Giocatore p2 = new Giocatore("Giuseppe");

        GameManager game = new GameManager(2);

        game.aggiungiGiocatore(p1);
        game.aggiungiGiocatore(p2);

        game.StartGame();
        game.StampaInfoGiocatori();

        System.exit(0);
        //launch();
    }
}