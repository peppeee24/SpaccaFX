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

    PartitaClassicaController2 PC;


    public void setPartitaClassicaController(PartitaClassicaController2 pcc){this.PC=pcc;}

    public TavoloController()
    {

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

        if(PC.getNumeroGiocatori()==4){

            nomeGiocatoreLabel.setText(PC.getNomeGiocatore1());
            nomeBot1Label.setText(PC.getNomeGiocatore2());
            nomeBot2Label.setText(PC.getNomeGiocatore3());
            nomeBot3Label.setText(PC.getNomeGiocatore4());
        } else if(PC.getNumeroGiocatori()==3){
            nomeGiocatoreLabel.setText(PC.getNomeGiocatore1());
            nomeBot1Label.setText(PC.getNomeGiocatore2());
            nomeBot2Label.setText(PC.getNomeGiocatore3());
            if(PC.getDifficolta().equals("Facile"))
            nomeBot3Label.setText(PC.getE1());
            else
                nomeBot3Label.setText(PC.getA1());
        } else if(PC.getNumeroGiocatori()==2){
            nomeGiocatoreLabel.setText(PC.getNomeGiocatore1());
            nomeBot1Label.setText(PC.getNomeGiocatore2());
            if(PC.getDifficolta().equals("Facile")){
                nomeBot2Label.setText(PC.getE1());
                nomeBot3Label.setText(PC.getE2());
            }else {
                nomeBot2Label.setText(PC.getA1());
                nomeBot3Label.setText(PC.getA2());
            }
        } else if (PC.getNumeroGiocatori()==1) {
            nomeGiocatoreLabel.setText(PC.getNomeGiocatore1());
            if (PC.getDifficolta().equals("Facile")) {
                nomeBot1Label.setText(PC.getE1());
                nomeBot2Label.setText(PC.getE2());
                nomeBot3Label.setText(PC.getE3());
            } else {
                nomeBot1Label.setText(PC.getA1());
                nomeBot2Label.setText(PC.getA2());
                nomeBot3Label.setText(PC.getA3());
            }
        }




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


