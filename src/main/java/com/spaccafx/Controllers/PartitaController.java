package com.spaccafx.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.awt.*;

public class PartitaController
{
    @FXML
    private Label welcomeText;



    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Ciao!");
    }



    // Passaggio dalla spalshScreen iniziare alla seconda schermata

    @FXML
    Pane startScreen;
    @FXML
    Pane secondScreen;

    @FXML
    Button playButton;
    public void startGame(ActionEvent actionEvent) {
        startScreen.setDisable(true);
        startScreen.setVisible(false);
        secondScreen.setDisable(false);
        secondScreen.setVisible(true);


    }
@FXML
Toggle italian;

    @FXML
    Toggle english;


// Seconda schermata, scelta opzione Partita Classica

    @FXML
    Pane classicGame;


    public void iniziaPartita(ActionEvent actionEvent) {

        secondScreen.setDisable(true);
        secondScreen.setVisible(false);
        classicGame.setDisable(false);
        classicGame.setVisible(true);

    }

    @FXML
    ChoiceBox<Integer> numeroGiocatoriMenu;

    @FXML
    public void initialize() {
        secondScreen.setVisible(false);
        secondScreen.setDisable(true);
        classicGame.setVisible(false);
        classicGame.setDisable(true);
        loginScreen.setVisible(false);
        loginScreen.setDisable(true);

        setNumeroGiocatoriMenu();
    }

    public void setNumeroGiocatoriMenu() {
        numeroGiocatoriMenu.getItems().addAll(1, 2, 3, 4);
    }

    @FXML
    Button sendGameSetting;
    public void impostaGioco(ActionEvent actionEvent) {
        int numeroGiocatori=numeroGiocatoriMenu.getSelectionModel().getSelectedItem();
// TODO tutte le altre opzioni dei choice box
    }


    // Seconda schheramta
    public void iniziaTorneo(ActionEvent actionEvent) {


    }

    public void informazioniGioco(ActionEvent actionEvent) {


    }
    @FXML
    Pane loginScreen;

    public void impostazioni(ActionEvent actionEvent) {
        secondScreen.setDisable(true);
        secondScreen.setVisible(false);
        loginScreen.setDisable(false);
        loginScreen.setVisible(true);

    }



    // Impostazioni schemta login

    @FXML
    Label statusLabel;
    @FXML
    TextField userField;
    @FXML
    PasswordField passwordField;



   private  String user="root";
     private String pwd="root";

    public void loginAction(ActionEvent actionEvent){
        String UserField=userField.getText();
        String PasswordField=passwordField.getText();
if(user.compareTo(UserField)==0 && pwd.compareTo(PasswordField)==0) {

    // TODO Login OK, passa alla prossima schemata
    statusLabel.setText("Credenziali corrette, impostare reindirizzamento");

} else {
    statusLabel.setText("Credenziali Errate");
}
}


// Altra schemrata

    }


