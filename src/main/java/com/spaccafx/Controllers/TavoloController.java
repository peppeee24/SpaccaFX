package com.spaccafx.Controllers;

import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;

public class TavoloController {



    @FXML
    Label nomeGiocatoreLabel,nomeBot1Label, nomeBot2Label, nomeBot3Label, cartaSpecialeLabel;

    @FXML
    ImageView bot1Space,bot2Space,bot3Space;

    @FXML
    Panel cartaSpecialePanel;

    public void getCartaSpeciale() {
        cartaSpecialeLabel.setVisible(false);
        cartaSpecialePanel.setVisible(false);
    }

    PartitaClassicaController2 PC=new PartitaClassicaController2();
    public void initialize(){
        setTable();
    }

    public void setTable() {
        PC.getNumeroGiocatori();
        PC.getDifficolta();
        PC.getNumeroBot();


        nomeGiocatoreLabel.setText(PC.getNomeGiocatore1());
        nomeBot1Label.setText(PC.getA1());


    }


    public void scambiaCarta(ActionEvent actionEvent) {

    }

    public void passaTurno(ActionEvent actionEvent) {

    }




}


