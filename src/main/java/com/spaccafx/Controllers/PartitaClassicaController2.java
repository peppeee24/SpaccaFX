package com.spaccafx.Controllers;


import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;

import javafx.scene.control.TextField;


import java.awt.*;
import java.io.IOException;
import java.util.Optional;

public class PartitaClassicaController2 {

    private Stage stage;
    private Scene scene;
    private Parent root;


    private int numeroGiocatori, numeroBot;
    private String difficolta, nomeGiocatore1, nomeGiocatore2, nomeGiocatore3, nomeGiocatore4;

    private int codice=-1;

    EasyBot E=new EasyBot();
    AdvancedBot A=new AdvancedBot();

    @FXML
    Tab playerTab, botTbb, creaTab;

    @FXML
    ChoiceBox<Integer> numeroGiocatoriMenu, numeroBotMenu;

    @FXML
    ChoiceBox<String> difficoltaBotMenu;

    @FXML
    TextField playerName1, playerName2, playerName3, playerName4;

    @FXML
    Label codicePartita, numeroBotLabel, difficoltaBotLabel, labelBot1, labelBot2, labelBot3;

    @FXML
    ImageView twoLabel, treeLabel, fourLabel, hardBot1, hardBot2, hardBot3, hardBot4, easyBot1, easyBot2, easyBot3;


    @FXML
    public void initialize() {

        setNumeroGiocatori();
        controlloGiocatori();
        setDifficolta();
        setNomeGiocatore1();
        setNomeGiocatore2();
        setNomeGiocatore3();
        setNomeGiocatore4();

        setNumeroBot();

    }



    public void setDifficolta() {


        difficoltaBotMenu.setOnAction(this::dB);
        this.controlloGiocatori();
    }

    public void dB(ActionEvent event){
        difficolta=difficoltaBotMenu.getValue();
        this.controlloGiocatori();
    }


    public void setNumeroGiocatori() {

        numeroGiocatoriMenu.setOnAction(this::nG);
        this.controlloGiocatori();
    }

    public void nG(ActionEvent event){
        numeroGiocatori=numeroGiocatoriMenu.getValue();
        this.controlloGiocatori();
    }

    String E1,E2,E3,A1,A2,A3;


    public void getEasyBot1(){
        E1= E.generaNomeBot();
    }

    public String getE1(){
        return E1;
    }

    public void getEasyBot2(){
        E2= E.generaNomeBot();
    }
    public String getE2(){
        return E2;
    }

    public void getEasyBot3(){
        E3=E.generaNomeBot();

    }

    public String getE3(){
        return E3;
    }


    public void getAdvBot1(){
        A1= A.generaNomeBot();
    }

    public String getA1(){
        return A1;
    }

    public void getAdvBot2(){
        A2= A.generaNomeBot();
    }
    public String getA2(){
        return A2;
    }

    public void getAdvBot3(){
        A3=A.generaNomeBot();

    }

    public String getA3(){
        return A3;
    }




    public void controlloGiocatori() {
        labelBot1.setVisible(false);
        labelBot2.setVisible(false);
        labelBot3.setVisible(false);
        hardBot1.setVisible(false);
        hardBot2.setVisible(false);
        hardBot3.setVisible(false);
        easyBot1.setVisible(false);
        easyBot2.setVisible(false);
        easyBot3.setVisible(false);


        // TODO sembra non legga le informazioni dal checkbox
        if (getNumeroGiocatori() == 4) {

            numeroBotMenu.getItems().addAll(0);
            difficoltaBotMenu.setVisible(false);
            numeroBotMenu.setVisible(false);
            difficoltaBotLabel.setVisible(false);
            numeroBotLabel.setText("Non ci sono bot");


        } else if (getNumeroGiocatori() == 3) {
            numeroBotMenu.getItems().addAll(1);
            fourLabel.setVisible(false);
            playerName4.setVisible(false);
            if(getDifficolta().equals("Difficile")) {
                labelBot1.setText(getA1());
                labelBot1.setVisible(true);
                hardBot1.setVisible(true);
            } else{
                labelBot1.setText(getE1());
                labelBot1.setVisible(true);
                easyBot1.setVisible(true);
            }

        } else if (getNumeroGiocatori() == 2) {
            numeroBotMenu.getItems().addAll(1, 2);
            treeLabel.setVisible(false);
            playerName3.setVisible(false);
            fourLabel.setVisible(false);
            playerName4.setVisible(false);
            if(getDifficolta().equals("Difficile")) {
                labelBot1.setText(getA1());
                labelBot1.setVisible(true);
                hardBot1.setVisible(true);
                labelBot2.setText(getA2());
                labelBot2.setVisible(true);
                hardBot2.setVisible(true);
            } else{
                labelBot1.setText(getE1());
                labelBot1.setVisible(true);
                easyBot1.setVisible(true);
                labelBot2.setText(getE2());
                labelBot2.setVisible(true);
                easyBot2.setVisible(true);
            }



        } else if (getNumeroGiocatori() == 1) {
            numeroBotMenu.getItems().addAll(1, 2, 3);
            twoLabel.setVisible(false);
            playerName2.setVisible(false);
            treeLabel.setVisible(false);
            playerName3.setVisible(false);
            fourLabel.setVisible(false);
            playerName4.setVisible(false);
            if(getDifficolta().equals("Difficile")) {
                labelBot1.setText(getA1());
                labelBot1.setVisible(true);
                hardBot1.setVisible(true);
                labelBot2.setText(getA2());
                labelBot2.setVisible(true);
                hardBot2.setVisible(true);
                labelBot3.setText(getA3());
                labelBot3.setVisible(true);
                hardBot3.setVisible(true);
            } else{
                labelBot1.setText(getE1());
                labelBot1.setVisible(true);
                easyBot1.setVisible(true);
                labelBot2.setText(getE2());
                labelBot2.setVisible(true);
                easyBot2.setVisible(true);
                labelBot3.setText(getE3());
                labelBot3.setVisible(true);
                easyBot3.setVisible(true);
            }



        }
    }


    public void setNumeroBot() {
        numeroBotMenu.setOnAction(this::nB);

    }

    public void nB(ActionEvent event){
        numeroBot=numeroBotMenu.getValue();
    }



    public void setNomeGiocatore1() {
        nomeGiocatore1 = playerName1.getText();

    }

    public void setNomeGiocatore2() {
        nomeGiocatore2 = playerName2.getText();

    }

    public void setNomeGiocatore3() {
        nomeGiocatore3 = playerName3.getText();

    }

    public void setNomeGiocatore4() {
        nomeGiocatore4 = playerName4.getText();

    }


    public String getNomeGiocatore1() {
        return nomeGiocatore1;
    }

    public String getNomeGiocatore2() {
        return nomeGiocatore2;
    }

    public String getNomeGiocatore3() {
        return nomeGiocatore3;
    }

    public String getNomeGiocatore4() {
        return nomeGiocatore4;
    }

    public int getNumeroGiocatori() {
        return numeroGiocatori;
    }

    public int getNumeroBot() {
        return numeroBot;
    }

    public String getDifficolta() {

        return difficolta;
    }






    public void indietro(MouseEvent mouseEvent) throws IOException {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("SelectionMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }

int mucca;
    public void generaCodice(ActionEvent actionEvent) throws IOException {

        int somma= getNumeroGiocatori()+getNumeroBot();

        if(somma>1 && somma<5) {
            Partita P = new Partita(somma);
             codice = P.generaCodicePartita();
             mucca= codice;
             System.out.println("Codice"+codice);
            codicePartita.setText("Codice: "+String.valueOf(codice));
            codicePartita.wrapTextProperty().set(true);
           // codicePartita.getStyleClass().add("copiable-label");
            // TODO non riesco a rendere selezionabile la label

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Codice partita generato");
            alert.setContentText("Comunica il codice ai giocatori che dovranno inserirlo successivamente");
            Optional<ButtonType> result = alert.showAndWait();



        } else { //teoricamente non entra mai siccome il numero dei bot va di pari passo a quello dei giocatori, lo teniamo solo per avere una sicurezza maggiore
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Parametri errati");
            alert.setContentText("Si puà giocare tra 2 e 4 giocatori, compresi bot, rivedi le impostazioni");
            Optional<ButtonType> result = alert.showAndWait();

        }
    }

    public void impostaGioco(ActionEvent actionEvent) throws IOException {

//TODO aggiunge contrlli, il tavolo gestisce max 4 giocatori, quindi se imposto 4 giocatori, posso impostare 0 bot,


        FXMLLoader playerScreen = new FXMLLoader(Spacca.class.getResource("PlayerScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(playerScreen.load());
        stage.setScene(scene);
        stage.show();

        PlayerScreenController PSC = playerScreen.getController();
        PSC.login(codice);


    }






}


