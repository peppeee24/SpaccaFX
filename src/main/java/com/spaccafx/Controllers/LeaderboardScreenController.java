package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Spacca;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LeaderboardScreenController
{
    @FXML
    ScrollPane IdScrollLeaderboard;

    @FXML
    GridPane IdGridLeaderboard;


    public GridPane getGridPaneScoreboard(){return  this.IdGridLeaderboard;}

    public void indietro(MouseEvent mouseEvent) throws IOException
    {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("SelectionMenuGiocatore.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }
}
