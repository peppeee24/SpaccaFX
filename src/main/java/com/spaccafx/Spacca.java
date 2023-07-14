package com.spaccafx;

import com.spaccafx.Manager.GameManager;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.EasyBot;
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
        Scene scene = new Scene(fxmlLoader.load(), 1600, 850);
        stage.setTitle("Alpha Build SpaccaFX");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
        //Scanner s = new Scanner(System.in);

        //System.out.println("Inserisci quanti giocatori vuoi: ");
        //int nGiocatori = s.nextInt();

        Giocatore p1 = new Giocatore("Magli");
        /*Giocatore p2 = new Giocatore("Giuseppe");
        Giocatore p3 = new Giocatore("Alfredo");
        Giocatore p4 = new Giocatore("Sofia");*/
        Bot b1 = new AdvancedBot();
        b1.setNome(b1.generaNomeBot());
        Bot b2 = new EasyBot();
        b2.setNome(b2.generaNomeBot());


        GameManager game = new GameManager(3);

        game.aggiungiGiocatore(p1);
        /*game.aggiungiGiocatore(p2);
        game.aggiungiGiocatore(p3);
        game.aggiungiGiocatore(p4);*/
        game.aggiungiGiocatore(b1);
        game.aggiungiGiocatore(b2);

        game.StartGame();

        System.exit(0);

    }
}