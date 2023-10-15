package com.spaccafx.Controllers;

import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;
import com.spaccafx.Spacca;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TavoloController {



    @FXML
    Label nomeGiocatoreLabel,nomeBot1Label, nomeBot2Label, nomeBot3Label, cartaSpecialeLabel;

    @FXML
    ImageView bot1Space,bot2Space,bot3Space, humanPlayerSpace;

    @FXML
    ImageView life1BT1, life2BT1, life3BT1, life1BT2, life2BT2, life3BT2, life1BT3, life2BT3,  life3BT3, life1PL, life2PL, life3PL;

    @FXML
    ImageView mazziereBT1, mazziereBT2, mazziereBT3, mazzierePL;

    @FXML
    Pane cartaSpecialePanel;

    @FXML
    Button bottoneLacio;

    Random random = new Random();

    @FXML
    private ImageView diceImage;




   private  PartitaClassicaController2 PC;


    private Partita partita;

    public TavoloController()
    {
        ShareData sharedData = ShareData.getInstance();
        ShareData.getInstance().setTavoloController(this);
        this.PC = sharedData.getPartitaClassicaController();
        this.partita = sharedData.getPartita();
    }





    public void getCartaSpeciale() {
        cartaSpecialeLabel.setVisible(false);
        cartaSpecialePanel.setVisible(false);
    }



    public void initialize(){
        setLableTable();
    //   partita.startGame();



    }


public void disableDice(){
        diceImage.setVisible(false);
}

    public void showDice(){
        diceImage.setVisible(true);
    }

    public void lancia(ActionEvent actionEvent) {
        partita.lancioDadiIniziale();

    }

/*

    @FXML
    void roll(ActionEvent event) {

     //   bottoneLacio.setDisable(true);

        Thread thread = new Thread(){
            public void run(){
                System.out.println("Thread Running");
                try {
                    for (int i = 0; i < 15; i++) {
                        File file = new File("src/sample/dice/dice" + (random.nextInt(6)+1)+".png");
                        diceImage.setImage(new Image(file.toURI().toString()));
                        Thread.sleep(50);
                    }
                    // bottoneLacio.setDisable(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

 */
      public  void roll(int valoreDado) {
showDice();
            Thread thread = new Thread(){
                public void run(){
                    System.out.println("Thread Running");
                    try {
                        for (int i = 0; i < 15; i++) {
                            // numeroDado=valoreDado;
                            System.out.println("Valore dato in roll:"+valoreDado);
                            File file = new File("src/Assets/Game/Environment/dice/dice" + valoreDado+".PNG");
                            diceImage.setImage(new Image(file.toURI().toString()));
                            Thread.sleep(50);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();


        }





    public void setLableTable() {
        getCartaSpeciale();

       partita.StampaInfoGiocatori();

           nomeGiocatoreLabel.setText(PC.P.giocatori.get(0).getNome());
           nomeBot1Label.setText(PC.P.giocatori.get(1).getNome());
           nomeBot2Label.setText(PC.P.giocatori.get(2).getNome());
           nomeBot3Label.setText(PC.P.giocatori.get(3).getNome());

      //  disableDice();
    }


    public void scambiaCarta(ActionEvent actionEvent) {
//partita.scambiaCarta();
    }

    public void passaTurno(ActionEvent actionEvent) {
partita.passaTurno();
    }

    public void impostazioneInizialeCarte() {
      //  Image back = new Image("../../Assets/Cards/back.PNG");
     //   Image back = new Image(getClass().getResource("../../Assets/Cards/back.PNG").toString());


        // Crea un oggetto ImageView e assegna l'immagine ad esso
       //ImageView bot1Space = new ImageView(image);
       // bot1Space.setImage(back); // Cambia il percorso all'immagine desiderata
       // bot2Space.setImage(back); // Cambia il percorso all'immagine desiderata
        //bot3Space.setImage(back); // Cambia il percorso all'immagine desiderata

    }

    public void gestisciVite(){




    }

    public void gestisciMazziere() {
        // partita.giocatori.get(posMazziere).getNome();
//disableDice();
            String mazziere =  partita.getMazziereNome();
            if (nomeGiocatoreLabel.getText().equalsIgnoreCase(mazziere)) {
                mazziereBT1.setVisible(false);
                mazziereBT2.setVisible(false);
                mazziereBT3.setVisible(false);
            } else if (nomeBot1Label.getText().equalsIgnoreCase(mazziere)) {
                mazzierePL.setVisible(false);
                mazziereBT2.setVisible(false);
                mazziereBT3.setVisible(false);
            } else if (nomeBot2Label.getText().equalsIgnoreCase(mazziere)) {
                mazzierePL.setVisible(false);
                mazziereBT1.setVisible(false);
                mazziereBT3.setVisible(false);
            } else {
                mazzierePL.setVisible(false);
                mazziereBT1.setVisible(false);
                mazziereBT2.setVisible(false);
            }
        }

    }






