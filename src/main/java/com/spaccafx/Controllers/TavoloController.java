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

import java.io.IOException;

public class TavoloController {
    /*

    //PartitaClassicaController PC = new PartitaClassicaController();
    Partita partitaModel =new Partita();

    @FXML
    Label nomeGiocatoreLabel,nomeBot1Label, nomeBot2Label, nomeBot3Label;

    @FXML
    ImageView bot1Space,bot2Space,bot3Space;

    public void initialize(){
        setTable();
    }

    public void setPartitaModel(Partita partitaModel) {
        this.partitaModel = partitaModel;
        setTable();
    }
    public void setTable() {
        nomeGiocatoreLabel.setText(partitaModel.getNomeGiocatore1());

        if (partitaModel.getDifficolta() == "Facile") {
            Bot b0 = new EasyBot();

            if (partitaModel.getNumeroGiocatori() == 1) {

                bot1Space.setOpacity(0);
                bot3Space.setOpacity(0);
                nomeBot1Label.setOpacity(0);
                nomeBot3Label.setOpacity(0);

                nomeBot2Label.setText(b0.getNome());

            } else if ((partitaModel.getNumeroGiocatori() == 2)) {
                Bot b1 = new EasyBot();
                //  bot3Space.setSize(0, 0);
                nomeBot1Label.setText(b1.getNome());
                nomeBot3Label.setOpacity(0);
                nomeBot2Label.setText(b0.getNome());

            } else {
                Bot b1 = new EasyBot();
                Bot b2 = new EasyBot();

                nomeBot1Label.setText(b1.getNome());
                nomeBot3Label.setText(b2.getNome());
                nomeBot2Label.setText(b0.getNome());
            }


        } else {
            Bot b00 = new AdvancedBot();

            if (partitaModel.getNumeroGiocatori() == 1) {
                bot1Space.setOpacity(0);
                bot3Space.setOpacity(0);
                nomeBot1Label.setOpacity(0);
                nomeBot3Label.setOpacity(0);

                nomeBot2Label.setText(b00.getNome());

            } else if ((partitaModel.getNumeroGiocatori() == 2)) {
                Bot b11 = new AdvancedBot();
                //  bot3Space.setSize(0, 0);
                nomeBot1Label.setText(b11.getNome());
                nomeBot3Label.setOpacity(0);
                nomeBot2Label.setText(b00.getNome());

            } else {
                Bot b11 = new AdvancedBot();
                Bot b22 = new AdvancedBot();

                nomeBot1Label.setText(b11.getNome());
                nomeBot3Label.setText(b22.getNome());
                nomeBot2Label.setText(b00.getNome());
            }

        }


    }


    public void scambiaCarta(ActionEvent actionEvent) {

    }

    public void passaTurno(ActionEvent actionEvent) {

    }



     */
}


