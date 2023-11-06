package com.spaccafx.Controllers;

import com.spaccafx.Manager.Partita;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MatchItemController
{
    @FXML
    private Label partitaId;

    @FXML
    private Button playButtonId;

    @FXML
    private Label stateId;

    MatchData matchData;

    public void setData(MatchData matchData)
    {
        this.partitaId.setText(matchData.getIdMatch());
        playButtonId.setVisible(true);
        stateId.setText("State: INIZIA");
    }
}
