package com.spaccafx.Controllers;

import com.spaccafx.Controllers.PartitaClassicaController;

import com.spaccafx.Manager.Partita;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Optional;

public class PartitaClassicaController {

    private int numeroGiocatori;

    private int numeroBot;
    private String difficolta;
    private String nomeGiocatore1;

    private String nomeGiocatore2;
    private String nomeGiocatore3;
    private String nomeGiocatore4;

    private int codice;


    @FXML
    ChoiceBox<Integer> numeroGiocatoriMenu;

    @FXML
    ChoiceBox<Integer> numeroBotMenu;

    @FXML
    ChoiceBox<String> difficoltaBotMenu;

    @FXML
    TextField playerName1;

    @FXML
    TextField playerName2;

    @FXML
    TextField playerName3;

    @FXML
    TextField playerName4;

    @FXML
    Label codicePartita;

    @FXML
    Label numeroBotLabel;

    @FXML
    Label difficoltaBotLabel;

    @FXML
    Label twoLabel;
    @FXML
    Label treeLabel;
    @FXML
    Label fourLabel;


    @FXML
    public void initialize() {

        // setNumeroGiocatoriMenu();
        //   setDifficoltaBotMenu();
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
        difficolta = difficoltaBotMenu.getSelectionModel().getSelectedItem();
    }
/*

    public void setNumeroGiocatori() {
        //    numeroGiocatori = numeroGiocatoriMenu.getSelectionModel().getSelectedItem();

        SingleSelectionModel<Integer> selectionModel = numeroGiocatoriMenu.getSelectionModel();
        if (selectionModel.getSelectedItem() != null) {
            int numeroGiocatori = selectionModel.getSelectedItem().intValue();
            // Continua con la logica del tuo codice utilizzando numeroGiocatori
        } else {
            // Tratta il caso in cui nessuna opzione è stata selezionata nel ChoiceBox
        }

    }
*/

    public void setNumeroGiocatori() {

        /*
        SingleSelectionModel<Integer> selectionModel = numeroGiocatoriMenu.getSelectionModel();
        if (selectionModel.getSelectedItem() != null) {
            numeroGiocatori = selectionModel.getSelectedItem().intValue();
            controlloGiocatori(); // Chiamare il metodo per impostare le opzioni del numero di bot in base al numero di giocatori selezionato
        } else {
            // Tratta il caso in cui nessuna opzione è stata selezionata nel ChoiceBox
        }
               */

      //  numeroGiocatoriMenu.getItems().addAll(1,2,3,4);
        numeroGiocatoriMenu.setOnAction(this::nG);
        this.controlloGiocatori();
    }

    public void nG(ActionEvent event){
        numeroGiocatori=numeroGiocatoriMenu.getValue();
        this.controlloGiocatori();
    }


    public void controlloGiocatori() {
        // TODO sembra non legga le informazioni dal checkbox
        if (getNumeroGiocatori() == 4) {
          //  System.out.println("numero gio" + numeroGiocatori);
           // System.out.println("numero bot" + numeroBot);
            numeroBotMenu.getItems().addAll(0);
            difficoltaBotMenu.setVisible(false);
            numeroBotMenu.setVisible(false);
            difficoltaBotLabel.setVisible(false);
            numeroBotLabel.setText("Non ci sono bot");
            /*numeroBotMenu.setVisible(false);
            numeroBotLabel.setText("Hai impostato il numero massimo di giocaotri, quindi la partita non avrà bot");
            difficoltaBotMenu.setVisible(false);
            difficoltaBotLabel.setVisible(false);

             */
        } else if (getNumeroGiocatori() == 3) {
            numeroBotMenu.getItems().addAll(1);
            fourLabel.setVisible(false);
            playerName4.setVisible(false);

        } else if (getNumeroGiocatori() == 2) {
            numeroBotMenu.getItems().addAll(1, 2);
            treeLabel.setVisible(false);
            playerName3.setVisible(false);
            fourLabel.setVisible(false);
            playerName4.setVisible(false);

        } else if (getNumeroGiocatori() == 1) {
            numeroBotMenu.getItems().addAll(1, 2, 3);
            twoLabel.setVisible(false);
            playerName2.setVisible(false);
            treeLabel.setVisible(false);
            playerName3.setVisible(false);
            fourLabel.setVisible(false);
            playerName4.setVisible(false);

        }
    }

    /*
    public void setNumeroBot() {
        //  numeroBot = numeroBotMenu.getSelectionModel().getSelectedItem();

        SingleSelectionModel<Integer> selectionModel = numeroBotMenu.getSelectionModel();
        if (selectionModel.getSelectedItem() != null) {
            int numeroBot = selectionModel.getSelectedItem().intValue();
            // Continua con la logica del tuo codice utilizzando numeroBot
        } else {
            // Tratta il caso in cui nessuna opzione è stata selezionata nel ChoiceBox
        }

    }

*/
    public void setNumeroBot() {
        numeroBotMenu.setOnAction(this::nB);
        /*SingleSelectionModel<Integer> selectionModel = numeroBotMenu.getSelectionModel();
        if (selectionModel.getSelectedItem() != null) {
            numeroBot = selectionModel.getSelectedItem().intValue();
        } else {
            // Tratta il caso in cui nessuna opzione è stata selezionata nel ChoiceBox
        }*/
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



    /*

    public void setNumeroGiocatoriMenu() {

        numeroGiocatoriMenu.getItems().addAll(1, 2, 3);
    }




    public void setDifficoltaBotMenu(){
        difficoltaBotMenu.getItems().addAll("Facile","Avanzato");
    }


 */

    public void impostaGioco(ActionEvent actionEvent) throws IOException {

//TODO aggiunge contrlli, il tavolo gestisce max 4 giocatori, quindi se imposto 4 giocatori, posso impostare 0 bot,


        FXMLLoader playerScreen = new FXMLLoader(Spacca.class.getResource("PlayerScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(playerScreen.load());
        stage.setScene(scene);
        stage.show();


    }

    public void indietro(MouseEvent mouseEvent) throws IOException {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("SelectionMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }


    public void generaCodice(ActionEvent actionEvent) throws IOException {

   //     System.out.println("NNNNNNNNN"+numeroGiocatori);

        int somma= getNumeroGiocatori()+getNumeroBot();
      //  System.out.println("Non riesco a leggere le informazioni del checkbox");
// TODO non funziona perchè somma =0, sembra non legga le informazioni dal checkbox
        if(somma>1 && somma<5) {
            Partita P = new Partita(somma);
             codice = P.generaCodicePartita();
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
            codice=-1;
        }
    }

    public int getCodicePartita(){
        return codice;
    }


}


