package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SelectionMenuGiocatoreController {


    public void iniziaPartita(ActionEvent actionEvent) throws IOException // lista delle partite
    {
        AudioManager.bottoneSuono();
        FXMLLoader PartitaClassica = new FXMLLoader(Spacca.class.getResource("PartitaSelector.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(PartitaClassica.load());
        stage.setScene(scene);
        stage.show();

    }


    // Seconda schheramta
    public void iniziaTorneo(ActionEvent actionEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader Torneo = new FXMLLoader(Spacca.class.getResource("Torneo.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Torneo.load());
        stage.setScene(scene);
        stage.show();

    }


    public void indietro(MouseEvent mouseEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }
}


