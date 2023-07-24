package com.spaccafx;

import com.spaccafx.Manager.GameManager;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Manager.Torneo;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.*;

import java.io.*;

public class Spacca extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {

    //    FXMLLoader fxmlLoader = new FXMLLoader(Spacca.class.getResource("Partita2.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(Spacca.class.getResource("loadscreen.fxml"));
       // Scene scene = new Scene(fxmlLoader.load(), 1197, 812);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Alpha Build SpaccaFX");

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        //Alert alert = new Alert(Alert.AlertType.ERROR);
        //alert.setTitle("Test alert");
        //alert.setContentText("ERRORE DURANTE IL CARICAMENTO DEI FILE, ESEGUI NUOVAMENTE!");
        //<ButtonType> result = alert.showAndWait();


/*
        Parent root = new FXMLLoader.load(getClass().getResource("loadscreen.fxml"));
      //  Scene scene = new Scene(fxmlLoader.load(), 1197, 812);

        stage.setTitle("Alpha Build SpaccaFX");

        stage.setResizable(false);
        stage.setScene(new Scene (root));
        stage.show();
        */



    }

    public static void main(String[] args)
    {
        launch();
        //Scanner s = new Scanner(System.in);

        //System.out.println("Inserisci quanti giocatori vuoi: ");
        //int nGiocatori = s.nextInt();

        /*Giocatore p1 = new Giocatore("Magli");
        Giocatore p2 = new Giocatore("Giuseppe");*/
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


        /*Partita game = new Partita(2);

        game.aggiungiGiocatore(p1);
        game.aggiungiGiocatore(p2);
        game.aggiungiGiocatore(p3);
        game.aggiungiGiocatore(p4);
        game.aggiungiGiocatore(b1);
        game.aggiungiGiocatore(b2);

        game.StartGame();*/

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
        torneo.StartTorneo();

        System.exit(0);

    }
}