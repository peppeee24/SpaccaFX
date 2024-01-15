package com.spaccafx.Controllers;


import com.spaccafx.Manager.Partita;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.awt.*;

public class LeaderboardController
{

    @FXML
    private GridPane IdGridLeaderboard;
    private ScrollPane IdScrollLeaderboard;

    Partita p;

    public void initialize()
    {

    }

    public void setDataLeaderboard(Partita p)
    {
        this.p = p;
    }

    public GridPane getGridPane()
    {
        return this.IdGridLeaderboard;
    }

    public ScrollPane getScrollPane()
    {
        return this.IdScrollLeaderboard;
    }

}


