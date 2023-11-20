package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class PreGameController
{

    @FXML
    Button giocaButton;

    @FXML
    Label numerovite1, numerovite2,numerovite3,numerovite4,giocatore1,giocatore2,giocatore3,giocatore4;

    @FXML
    ImageView user1, user2, user3, user4, cuore1, cuore2, cuore3, cuore4;
    public void apriTavolo(ActionEvent actionEvent) throws IOException // bottone inizia
    {
        AudioManager.bottoneSuono();



            FXMLLoader loaderTavolo = new FXMLLoader(Spacca.class.getResource("Tavolo3Prototype.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent root = loaderTavolo.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true); // todo FIXXARE PROBLEMI GRAFICA ADATTIVA
            stage.show();

        TavoloController tavoloController = loaderTavolo.getController();
        //tavoloController.inizializzaClassePartita(this.codicePartita);


        }


    public void indietro(MouseEvent mouseEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("PartitaSelector.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }





}








