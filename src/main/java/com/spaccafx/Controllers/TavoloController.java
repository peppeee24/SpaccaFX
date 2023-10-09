package com.spaccafx.Controllers;

import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

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

   private  PartitaClassicaController2 PC;


    private Partita partita;

    public TavoloController()
    {
        ShareData sharedData = ShareData.getInstance();
        this.PC = sharedData.getPartitaClassicaController();
        this.partita = sharedData.getPartita();
    }


    public void passaggioController(PlayerScreenController PSC){



      //  this.PC=PSC.partitaClassicaController;
      //  System.out.println("TC"+PC);
      //  this.partita=PSC.partitaClassicaController.P;
      //  System.out.println(partita);

    }


    public void getCartaSpeciale() {
        cartaSpecialeLabel.setVisible(false);
        cartaSpecialePanel.setVisible(false);
    }



    public void initialize(){
        setLableTable();

    }

    public void setLableTable() {
        getCartaSpeciale();

       partita.StampaInfoGiocatori();

           nomeGiocatoreLabel.setText(PC.P.giocatori.get(0).getNome());
           nomeBot1Label.setText(PC.P.giocatori.get(1).getNome());
           nomeBot2Label.setText(PC.P.giocatori.get(2).getNome());
           nomeBot3Label.setText(PC.P.giocatori.get(3).getNome());


    }


    public void scambiaCarta(ActionEvent actionEvent) {

    }

    public void passaTurno(ActionEvent actionEvent) {

    }

    public void gestisciVite(){
       // life1BT1.setVisible(true);
    }

    public void gestisciMazziere(){

    }




}


