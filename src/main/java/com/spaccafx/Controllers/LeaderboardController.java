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
    // TODO aggiungere suono leaderboard e salvataggio (in tavolo controller)
    // TODO Controllare tutti i suoini
    // TODO fixare popup probabilta NON hai vinto vita extra
    // TODO bug distribuzione carte con imprevisto e passo turno (recupero carte dai file)
    // TODO sisitemare leaderbord (guardare prima la vita e poi il numero di round)

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


