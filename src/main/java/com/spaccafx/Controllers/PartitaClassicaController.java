package com.spaccafx.Controllers;

import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PartitaClassicaController {

    @FXML
    ChoiceBox<Integer> numeroGiocatoriMenu;

    @FXML
    public void initialize() {

        setNumeroGiocatoriMenu();
    }

    public void setNumeroGiocatoriMenu() {
        numeroGiocatoriMenu.getItems().addAll(1, 2, 3, 4);
    }


    public void impostaGioco(ActionEvent actionEvent) {
        int numeroGiocatori = numeroGiocatoriMenu.getSelectionModel().getSelectedItem();
// TODO tutte le altre opzioni dei choice box
    }


}


