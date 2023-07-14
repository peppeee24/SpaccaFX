package com.spaccafx.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PartitaController
{
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Nigga!");
    }
}