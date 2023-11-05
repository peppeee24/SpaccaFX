package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Toggle;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.io.IOException;

public class PartitaSelectorController {

    @FXML
    Button playButton1;
    Label partita1Label;
    Pane pane2, pane3, pane4, pane5, pane6, pane7;

    // TODO impostare caricamento partite da file

    PartitaClassicaController2 PC;

    Partita partita;

    public void initialize() {
        //  betaDisable();

        ShareData sharedData = ShareData.getInstance();
        ShareData.getInstance().setPartitaSelectorController(this);
        this.PC = sharedData.getPartitaClassicaController();
        this.partita = sharedData.getPartita();
        //partita1Label.setText("Partita: " + partita.getCodicePartita());


    }

    // fare for e cambiare id per ogni bottone     --- Play_IDPartita

    public void play1(ActionEvent actionEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader playerScreen = new FXMLLoader(Spacca.class.getResource("PlayerScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = playerScreen.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }



    public void betaDisable() {
        pane2.setVisible(false);
        pane3.setVisible(false);
        pane4.setVisible(false);
        pane5.setVisible(false);
        pane6.setVisible(false);
        pane7.setVisible(false);

    }


    public void indietro(MouseEvent mouseEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("SelectionMenuGiocatore.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }
}


