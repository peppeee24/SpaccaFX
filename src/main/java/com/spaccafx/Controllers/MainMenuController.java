package com.spaccafx.Controllers;

import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.stage.Stage;
import java.io.IOException;

public class MainMenuController {

    public void startGame(ActionEvent actionEvent) throws IOException {

        FXMLLoader Login = new FXMLLoader(Spacca.class.getResource("Login.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Login.load());
        stage.setScene(scene);
        stage.show();

    }


    @FXML
    Toggle italian;

    @FXML
    Toggle english;

    // TODO da impostare



}



