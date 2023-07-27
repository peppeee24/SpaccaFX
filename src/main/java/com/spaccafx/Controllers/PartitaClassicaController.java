package com.spaccafx.Controllers;

import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;

public class PartitaClassicaController {

    @FXML
    ChoiceBox<Integer> numeroGiocatoriMenu;

    @FXML
    ChoiceBox<String> difficoltaBotMenu;

    @FXML
    public void initialize() {

        setNumeroGiocatoriMenu();
        setDifficoltaBotMenu();;
    }

    public void setNumeroGiocatoriMenu() {

        numeroGiocatoriMenu.getItems().addAll(1, 2, 3, 4);
    }

    public void setDifficoltaBotMenu(){
        difficoltaBotMenu.getItems().addAll("Facile","Avanzato");
    }

    public void impostaGioco(ActionEvent actionEvent) {
        int numeroGiocatori = numeroGiocatoriMenu.getSelectionModel().getSelectedItem();
        String difficolta = difficoltaBotMenu.getSelectionModel().getSelectedItem();

    }

    public void indietro(MouseEvent mouseEvent) throws IOException {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("SelectionMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }

    public void caricaPartita(ActionEvent actionEvent) {


    }


}


