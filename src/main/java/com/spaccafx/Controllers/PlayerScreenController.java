package com.spaccafx.Controllers;

import com.spaccafx.Manager.Partita;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    Partita P;


    PartitaClassicaController2 PC;

    public void inizializza(PartitaClassicaController2 pc) {
       this.PC=pc;
       this.P=PC.P;
    }

    public void loginAction(ActionEvent actionEvent) throws IOException {

        int PasswordField = Integer.parseInt(passwordField.getText());
        this.pwd=P.getCodicePartita();
        System.out.println("PWD:" + pwd);
        System.out.println("PAASVEFDNV:" + PasswordField);


        if (pwd == PasswordField) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Accesso Eseguito");
            alert.setContentText("Stai per entrare nel gioco");
            Optional<ButtonType> result = alert.showAndWait();

/*
            FXMLLoader tavolo = new FXMLLoader(Spacca.class.getResource("Tavolo.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(tavolo.load());
            stage.setScene(scene);
            stage.show();

 */

            FXMLLoader tavoloLoader = new FXMLLoader(Spacca.class.getResource("Tavolo.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(tavoloLoader.load());
            stage.setScene(scene);
            stage.show();

            TavoloController TC = new TavoloController();
            TC.inizializza(this);


        } else {


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText("Codice partita Errato");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }


    public void indietro(MouseEvent mouseEvent) throws IOException {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("LoginController.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }



}








