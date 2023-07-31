package com.spaccafx.Controllers;

import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Toggle;

import javafx.stage.Stage;


import java.io.IOException;

public class SelectionMenuController
{




    public void iniziaPartita(ActionEvent actionEvent) throws IOException {

        FXMLLoader PartitaClassica = new FXMLLoader(Spacca.class.getResource("PartitaClassica.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(PartitaClassica.load());
        stage.setScene(scene);
        stage.show();

    }




    // Seconda schheramta
    public void iniziaTorneo(ActionEvent actionEvent) throws IOException {
        FXMLLoader Torneo = new FXMLLoader(Spacca.class.getResource("Torneo.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Torneo.load());
        stage.setScene(scene);
        stage.show();

    }



    public void indietro(MouseEvent mouseEvent) throws IOException {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }
}


