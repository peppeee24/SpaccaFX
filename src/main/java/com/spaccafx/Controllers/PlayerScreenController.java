package com.spaccafx.Controllers;

import com.spaccafx.Manager.Partita;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class PlayerScreenController {


    private int pwd;

    @FXML
    PasswordField passwordField;
    PartitaClassicaController2 partitaClassicaController;

    public PlayerScreenController() { // costruttore
       // this.pwd = ShareData.getInstance().getPassword();
        this.partitaClassicaController = ShareData.getInstance().getPartitaClassicaController();
        this.pwd=partitaClassicaController.P.getPasswordPartita();
    }

    public void loginAction(ActionEvent actionEvent) throws IOException { // bottone inizia

        // TODO CREARE UNA PASSWORD PER OGNI PARTITA != DAL ID PARTITA

        int PasswordField = Integer.parseInt(passwordField.getText());
        System.out.println("Codice Ricevuto: " + this.pwd);

        //System.out.println("PWD:" + pwd);
        System.out.println("Codice Inserito:" + PasswordField);


        if (pwd == PasswordField) {

            ShareData sharedData = ShareData.getInstance();
            sharedData.setPartitaClassicaController(partitaClassicaController);
            sharedData.setPartita(partitaClassicaController.P);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Accesso Eseguito");
            alert.setContentText("Stai per entrare nel gioco");
            Optional<ButtonType> result = alert.showAndWait();


            FXMLLoader loaderTavolo = new FXMLLoader(Spacca.class.getResource("Tavolo2.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent root = loaderTavolo.load();

            // TavoloController tc = loaderTavolo.getController();
            //  tc.passaggioController(this);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        } else {


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText("Codice partita Errato");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }


    public void indietro(MouseEvent mouseEvent) throws IOException {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("PartitaSelector.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }


}








