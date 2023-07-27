package com.spaccafx.Controllers;

import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Toggle;

import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class MainMenuController {


    public void startGame(ActionEvent actionEvent) throws IOException {
        FXMLLoader SelectionMenu = new FXMLLoader(Spacca.class.getResource("SelectionMenu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(SelectionMenu.load());
        stage.setScene(scene);
        stage.show();

    }




    @FXML
    Toggle italian;

    @FXML
    Toggle english;

    // TODO da impostare

    public void indietro(MouseEvent click) throws IOException {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("MainMenuController.fxml"));
        Stage stage = (Stage) ((Node) click.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }




}


