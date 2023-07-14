package com.spaccafx;

import com.spaccafx.Manager.GameManager;
import com.spaccafx.Player.Giocatore;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.*;

import java.io.*;

public class Spacca extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Spacca.class.getResource("Partita.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(Spacca.class.getResource("spaccafx/Partita.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Prova controller!");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
        //Scanner s = new Scanner(System.in);

        //System.out.println("Inserisci quanti giocatori vuoi: ");
        //int nGiocatori = s.nextInt();

        /*Giocatore p1 = new Giocatore("Magli");
        Giocatore p2 = new Giocatore("Giuseppe");
        Giocatore p3 = new Giocatore("Alfredo");
        Giocatore p4 = new Giocatore("Sofia");

        GameManager game = new GameManager(4);

        game.aggiungiGiocatore(p1);
        game.aggiungiGiocatore(p2);
        game.aggiungiGiocatore(p3);
        game.aggiungiGiocatore(p4);

        game.StartGame();

        System.exit(0);*/

    }
}