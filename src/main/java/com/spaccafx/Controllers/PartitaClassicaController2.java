package com.spaccafx.Controllers;


import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.Bot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;
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
import java.util.ArrayList;
import java.util.Optional;

public class PartitaClassicaController2
{

    private int numeroGiocatori, numeroBotMenu;
    private String difficolta, nomeGiocatore1, nomeGiocatore2, nomeGiocatore3, nomeGiocatore4, E1,E2,E3,A1,A2,A3;


    Partita P;

    @FXML
    Tab playerTab, botTbb, creaTab;

    @FXML
    ChoiceBox<Integer> numeroGiocatoriMenu;

    @FXML
    ChoiceBox<String> difficoltaBotMenu;

    @FXML
    TextField playerName1, playerName2, playerName3, playerName4;

    @FXML
    Label codicePartita, numeroBotLabel, difficoltaBotLabel, labelBot1, labelBot2, labelBot3, botCounter;

    @FXML
    ImageView twoLabel, treeLabel, fourLabel, hardBot1, hardBot2, hardBot3, easyBot1, easyBot2, easyBot3;

EasyBot E=new EasyBot();
AdvancedBot A=new AdvancedBot();


    @FXML
    public void initialize() {

        setNumeroGiocatori();
      //  setNumeroBot();

    }

    public void setNumeroGiocatori() {
        numeroGiocatoriMenu.setOnAction(this::nG);
    }

    public void nG(ActionEvent event){
        numeroGiocatori=numeroGiocatoriMenu.getValue();
        setNumeroGiocatori();
        this.controlloGiocatori();
        setNumeroBot();

    }

    public int getNumeroGiocatori() {
        return numeroGiocatori;
    }


    public void setDifficolta() {
        difficoltaBotMenu.setOnAction(this::dB);

    }

    public void dB(ActionEvent event){
        difficolta=difficoltaBotMenu.getValue();

    }


    public String getDifficolta() {
        return difficolta;
    }


    public void setNumeroBot() {

        numeroBotMenu=4-getNumeroGiocatori();
        botCounter.setText(Integer.toString(numeroBotMenu));


    }

    public int getNumeroBot() {
        return numeroBotMenu;
    }



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

    public void nascondiBot(){
        labelBot1.setVisible(false);
        labelBot2.setVisible(false);
        labelBot3.setVisible(false);
        hardBot1.setVisible(false);
        hardBot2.setVisible(false);
        hardBot3.setVisible(false);
        easyBot1.setVisible(false);
        easyBot2.setVisible(false);
        easyBot3.setVisible(false);
        getAdvBot1();
        getAdvBot2();
        getAdvBot3();
        getEasyBot1();
        getEasyBot2();
        getEasyBot3();
    }


    public void salvaDifficolta(ActionEvent actionEvent) throws IOException
    {
        this.setDifficolta();
        System.out.println(difficolta);
        System.out.println("Imposto difficolta"+getDifficolta());
        this.impostaDifficolta();
    }


    public void impostaDifficolta()  {

        if (difficolta != null) {
            switch (getNumeroBot()) {
                case 0:
                    difficoltaBotMenu.setVisible(false);
                    difficoltaBotLabel.setVisible(false);
                    numeroBotLabel.setText("Non ci sono bot");
                    break;
                case 1:
                    if (getDifficolta().equals("Difficile") && !getDifficolta().isEmpty()) {
                        labelBot1.setText(getA1());
                        labelBot1.setVisible(true);
                        hardBot1.setVisible(true);
                    } else {
                        labelBot1.setText(getE1());
                        labelBot1.setVisible(true);
                        easyBot1.setVisible(true);
                    }
                    break;
                case 2:
                    if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                        labelBot1.setText(getA1());
                        labelBot1.setVisible(true);
                        hardBot1.setVisible(true);
                        labelBot2.setText(getA2());
                        labelBot2.setVisible(true);
                        hardBot2.setVisible(true);
                    } else {
                        labelBot1.setText(getE1());
                        labelBot1.setVisible(true);
                        easyBot1.setVisible(true);
                        labelBot2.setText(getE2());
                        labelBot2.setVisible(true);
                        easyBot2.setVisible(true);
                    }
                    break;
                case 3:
                    if (getDifficolta().equals("Difficile") && !getDifficolta().isEmpty()) {
                        labelBot1.setText(getA1());
                        labelBot1.setVisible(true);
                        hardBot1.setVisible(true);
                        labelBot2.setText(getA2());
                        labelBot2.setVisible(true);
                        hardBot2.setVisible(true);
                        labelBot3.setText(getA3());
                        labelBot3.setVisible(true);
                        hardBot3.setVisible(true);
                    } else {
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
                    break;
                default:

            }
        } else {
            System.out.println("Difficoltà è null");
        }
    }

    /*



    if (getNumeroGiocatori() == 4) {

        difficoltaBotMenu.setVisible(false);
        difficoltaBotLabel.setVisible(false);
        numeroBotLabel.setText("Non ci sono bot");


    } else if (getNumeroGiocatori() == 3) {
        fourLabel.setVisible(false);
        playerName4.setVisible(false);

        if(getDifficolta().equals("Difficile") && !getDifficolta().isEmpty()) {
            labelBot1.setText(getA1());
            labelBot1.setVisible(true);
            hardBot1.setVisible(true);
        } else{
            labelBot1.setText(getE1());
            labelBot1.setVisible(true);
            easyBot1.setVisible(true);
        }

    } else if (getNumeroGiocatori() == 2) {
        treeLabel.setVisible(false);
        playerName3.setVisible(false);
        fourLabel.setVisible(false);
        playerName4.setVisible(false);
        if(getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
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
        twoLabel.setVisible(false);
        playerName2.setVisible(false);
        treeLabel.setVisible(false);
        playerName3.setVisible(false);
        fourLabel.setVisible(false);
        playerName4.setVisible(false);
        if(getDifficolta().equals("Difficile") && !getDifficolta().isEmpty()) {
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

     */



    public void controlloGiocatori() {


    if (getNumeroGiocatori() == 3) {
            fourLabel.setVisible(false);
            playerName4.setVisible(false);



        } else if (getNumeroGiocatori() == 2) {
            treeLabel.setVisible(false);
            playerName3.setVisible(false);
            fourLabel.setVisible(false);
            playerName4.setVisible(false);





        } else if (getNumeroGiocatori() == 1) {
            twoLabel.setVisible(false);
            playerName2.setVisible(false);
            treeLabel.setVisible(false);
            playerName3.setVisible(false);
            fourLabel.setVisible(false);
            playerName4.setVisible(false);

            }


        }


        public void salvaNomi(ActionEvent actionEvent) throws IOException{
        switch (getNumeroGiocatori()) {
            case 1:
                setNomeGiocatore1();
                break;
            case 2:
                setNomeGiocatore1();
                setNomeGiocatore2();
                break;
            case 3:
                setNomeGiocatore1();
                setNomeGiocatore2();
                setNomeGiocatore3();
                break;
            case 4:
                setNomeGiocatore1();
                setNomeGiocatore2();
                setNomeGiocatore3();
                setNomeGiocatore4();
                break;
            default:


        }
        this.nascondiBot();


        }











    public void setNomeGiocatore1() {
        nomeGiocatore1 = playerName1.getText();

        /*
        if(nomeGiocatore1.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campo vuoto");
            alert.setContentText("Inserisci un nome per continuare a giocare");
            Optional<ButtonType> result = alert.showAndWait();
        }

         */

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












    public void indietro(MouseEvent mouseEvent) throws IOException
    {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("SelectionMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }





    public void generaCodice(ActionEvent actionEvent) throws IOException
    {

        int somma= getNumeroGiocatori()+getNumeroBot();

        if(somma>1 && somma<5) {
             this.P = new Partita(somma);
             P.generaCodicePartita();

             System.out.println("Codice Generato: "+P.getCodicePartita());
            codicePartita.setText("Codice: "+ P.getCodicePartita());
         //   codicePartita.wrapTextProperty().set(true);
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


        ArrayList<IGiocatore> GiocatoriPartita =new ArrayList<>();

       if (getNumeroGiocatori()==1) {
           System.out.println("Nome giocatore 1: "+getNomeGiocatore1());
           GiocatoriPartita.add(new Giocatore(getNomeGiocatore1()));
       }  else if(getNumeroGiocatori()==2){
           GiocatoriPartita.add(new Giocatore(getNomeGiocatore1()));
           GiocatoriPartita.add(new Giocatore(getNomeGiocatore2()));
        } else if(getNumeroGiocatori()==3){
           GiocatoriPartita.add(new Giocatore(getNomeGiocatore1()));
           GiocatoriPartita.add(new Giocatore(getNomeGiocatore2()));
           GiocatoriPartita.add(new Giocatore(getNomeGiocatore3()));
       } else {
           GiocatoriPartita.add(new Giocatore(getNomeGiocatore1()));
           GiocatoriPartita.add(new Giocatore(getNomeGiocatore2()));
           GiocatoriPartita.add(new Giocatore(getNomeGiocatore3()));
           GiocatoriPartita.add(new Giocatore(getNomeGiocatore4()));
       }


        switch (getNumeroBot()) {

            case 3:

                if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                    GiocatoriPartita.add(new EasyBot(getA1()));
                    GiocatoriPartita.add(new EasyBot(getA2()));
                    GiocatoriPartita.add(new EasyBot(getA3()));
                } else {
                    GiocatoriPartita.add(new EasyBot(getE1()));
                    GiocatoriPartita.add(new EasyBot(getE2()));
                    GiocatoriPartita.add(new EasyBot(getE3()));

                }
                break;
            case 2:

                if (getDifficolta().equalsIgnoreCase("Difficile") && !getDifficolta().isEmpty()) {
                    GiocatoriPartita.add(new EasyBot(getA1()));
                    GiocatoriPartita.add(new EasyBot(getA2()));
                } else {
                    GiocatoriPartita.add(new EasyBot(getE1()));
                    GiocatoriPartita.add(new EasyBot(getE2()));
                }
                break;
            case 1:
                if (getDifficolta().equals("Difficile") && !getDifficolta().isEmpty()) {
                    GiocatoriPartita.add(new EasyBot(getA1()));
                } else {
                    GiocatoriPartita.add(new EasyBot(getE1()));
                }
                break;
            default:
                break;
        }


        P.aggiungiListaGiocatori(GiocatoriPartita);



        FXMLLoader impostaGioco = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(impostaGioco.load());
        stage.setScene(scene);

        // Mi permette di creare un "oggetto del controller", in questo modo riesco a passare tutto al controller senza creare un nuovo oggetto della classe che provocherebbe due istanze aperte
     //   MainMenuController mmc = impostaGioco.getController();
      //  mmc.setPartitaClassicaController(this);
        ShareData.getInstance().setPartitaClassicaController(this);
        ShareData.getInstance().setPartita(this.P);
        ShareData.getInstance().setCodice(this.P);

        stage.show();


    }










}


