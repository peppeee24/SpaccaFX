package com.spaccafx.Controllers;

import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class TorneoController {

    //private   int numeroGiocatori;
   // private  String difficolta;
   // private String nomeGiocatore;



    @FXML
    ChoiceBox<Integer> numeroGiocatoriMenu;

    @FXML
    ChoiceBox<String> difficoltaBotMenu;

    @FXML
    TextField playerName;
    @FXML
    public void initialize() {

       // setNumeroGiocatoriMenu();
     //   setDifficoltaBotMenu();
        setDifficolta();
        setNomeGiocatore();
        setNumeroGiocatori();

    }


   private PartitaModel partitaModel =new PartitaModel();

    public void setDifficolta() {
        String difficoltaSelezionata = difficoltaBotMenu.getSelectionModel().getSelectedItem();
        if (difficoltaSelezionata != null) {
            partitaModel.setDifficolta(difficoltaSelezionata);
        } else {
            // Gestisci il caso in cui non è stata selezionata alcuna opzione
            // Puoi stampare un messaggio di errore o fornire un valore predefinito, ad esempio:
            partitaModel.setDifficolta("Nessuna selezione");
        }
    }


    public void setNumeroGiocatori() {
        Integer numeroGiocatoriSelezionato = numeroGiocatoriMenu.getSelectionModel().getSelectedItem();
        if (numeroGiocatoriSelezionato != null) {
            partitaModel.setNumeroGiocatori(numeroGiocatoriSelezionato.intValue());
        } else {
            // Gestisci il caso in cui non è stata selezionata alcuna opzione
            // Puoi stampare un messaggio di errore o fornire un valore predefinito, ad esempio:
            partitaModel.setNumeroGiocatori(0);
        }
    }


            public void setNomeGiocatore() {
        partitaModel.setNomeGiocatore(playerName.getText());
    }

    /*

    public void setNumeroGiocatoriMenu() {

        numeroGiocatoriMenu.getItems().addAll(1, 2, 3);
    }




    public void setDifficoltaBotMenu(){
        difficoltaBotMenu.getItems().addAll("Facile","Avanzato");
    }


 */

    public void impostaGioco(ActionEvent actionEvent) throws IOException {
        FXMLLoader nextPage = new FXMLLoader(Spacca.class.getResource("Tavolo.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(nextPage.load());
        stage.setScene(scene);

        TavoloController tavoloController = nextPage.getController();
        tavoloController.setPartitaModel(partitaModel); // Passa il modello condiviso

        stage.show();



    }

    public void indietro(MouseEvent mouseEvent) throws IOException {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("SelectionMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }

    public void caricaPartita(ActionEvent actionEvent) throws IOException {
// TODO impostare metodo che legge da file o da Database


        FXMLLoader nextPage = new FXMLLoader(Spacca.class.getResource("Tavolo.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(nextPage.load());
        stage.setScene(scene);
        stage.show();

    }


}


