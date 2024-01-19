package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Spacca;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LeaderboardScreenTorneoController
{
    @FXML
    ScrollPane IdScrollLeaderboard1,IdScrollLeaderboard2,IdScrollLeaderboard3,IdScrollLeaderboard4,IdScrollLeaderboard5;

    @FXML
    GridPane IdGridLeaderboard1,IdGridLeaderboard2,IdGridLeaderboard3,IdGridLeaderboard4,IdGridLeaderboard5;

    @FXML
    Label torneoNumeroLabel;



    public GridPane getGridPaneScoreboard(int c)
    {
        return switch (c) {
            case 0 -> this.IdGridLeaderboard1;
            case 1 -> this.IdGridLeaderboard2;
            case 2 -> this.IdGridLeaderboard3;
            case 3 -> this.IdGridLeaderboard4;
            case 4 -> this.IdGridLeaderboard5;
            default -> this.IdGridLeaderboard1;
        };
    }

    public void indietro(MouseEvent mouseEvent) throws IOException
    {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("TorneoSelector.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }
}
