package com.spaccafx.Controllers;

import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController
{

    // Impostazioni schemta login

    @FXML
    Label statusLabel;
    @FXML
    TextField userField;
    @FXML
    PasswordField passwordField;



   private  String user="root";
     private String pwd="root";

    public void loginAction(ActionEvent actionEvent){
        String UserField=userField.getText();
        String PasswordField=passwordField.getText();
if(user.compareTo(UserField)==0 && pwd.compareTo(PasswordField)==0) {

    // TODO Login OK, passa alla prossima schemata
    statusLabel.setText("Credenziali corrette, impostare reindirizzamento");

} else {
    statusLabel.setText("Credenziali Errate");
}
}


// Altra schemrata

    }


