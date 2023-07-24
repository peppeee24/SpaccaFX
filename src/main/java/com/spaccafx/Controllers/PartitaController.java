package com.spaccafx.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class PartitaController
{
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Ciao!");
    }

    Pane startScreen;

    public void startGame(ActionEvent actionEvent) {
        this.startScreen= this.startScreen;
        this.startScreen.setOpacity(0);
    }
}