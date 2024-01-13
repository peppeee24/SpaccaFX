package com.spaccafx.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class SinglePlayerScoreboardController
{
    @FXML
    Label playerPosition, playerName, playerRounds, playerLifes;

    @FXML
    AnchorPane singleAnchorPane;


    public void setData(int playerPosition, String playerName, int playerRounds, int playerLifes)
    {
        this.playerPosition.setText(Integer.toString(playerPosition));
        this.playerName.setText(playerName);
        this.playerRounds.setText(Integer.toString(playerRounds));
        this.playerLifes.setText(Integer.toString(playerLifes));
    }

    public AnchorPane getAnchorPane(){return this.singleAnchorPane;}

}
