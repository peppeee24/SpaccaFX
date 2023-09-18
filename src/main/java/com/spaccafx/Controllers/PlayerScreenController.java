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


    @FXML
    PasswordField passwordField;
PartitaClassicaController PC =new PartitaClassicaController();

    Partita P =new Partita(PC.getNumeroGiocatori());
    //Partita P =new Partita(4);
    private String pwd = PC.getCodicePartita();

    public void loginAction(ActionEvent actionEvent) throws IOException {

        String PasswordField = passwordField.getText();

        System.out.println("PWD:" + pwd);
        System.out.println("PAASVEFDNV:" + PasswordField);

        if ( pwd.compareTo(PasswordField) == 0) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Accesso Eseguito");
            alert.setContentText("Stai per entrare nel gioco");
            Optional<ButtonType> result = alert.showAndWait();

// TODO Sistemare pasaggio codice partita, in quanto lo da sempre errato perchè ne viene genrato uno diverso rispetto a quello delal classe PartitaClassicaController

            FXMLLoader tavolo = new FXMLLoader(Spacca.class.getResource("Tavolo.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(tavolo.load());
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
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("LoginController.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }


}


