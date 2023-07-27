package com.spaccafx.Controllers;

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

public class LoginController {

    // Impostazioni schemta login


    @FXML
    TextField userField;
    @FXML
    PasswordField passwordField;


    private String user = "root";
    private String pwd = "root";

    public void loginAction(ActionEvent actionEvent) {
        String UserField = userField.getText();
        String PasswordField = passwordField.getText();

        if (user.compareTo(UserField) == 0 && pwd.compareTo(PasswordField) == 0) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Accesso Eseguito");
            alert.setContentText("Stai per essere reindirizzato");
            Optional<ButtonType> result = alert.showAndWait();

        } else {


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText("Username o Password non corretti");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }


    public void indietro(MouseEvent mouseEvent) throws IOException {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("SelectionMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }

}


