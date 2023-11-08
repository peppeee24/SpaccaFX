package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Files.FileManager;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Spacca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Toggle;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PartitaSelectorController
{

    @FXML
    private GridPane IdGridPartite;
    private ScrollPane IdScrollPartite;

    //Pane pane2, pane3, pane4, pane5, pane6, pane7;

    // TODO impostare caricamento partite da file

    PartitaClassicaController2 PC;

    Partita partita;

    ArrayList<MatchData> matchList;

    public void initialize()
    {
        //  betaDisable();

        matchList = new ArrayList<MatchData>();
        caricaTuttePartiteDisponibili();

        //ShareData sharedData = ShareData.getInstance();
        //ShareData.getInstance().setPartitaSelectorController(this);
        //this.PC = sharedData.getPartitaClassicaController();
        //this.partita = sharedData.getPartita();
        //partita1Label.setText("Partita: " + partita.getCodicePartita());


    }




    public void indietro(MouseEvent mouseEvent) throws IOException
    {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("SelectionMenuGiocatore.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }

    private void caricaTuttePartiteDisponibili()
    {
        try
        {
            if (FileManager.partiteFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(FileManager.partiteFile));

                // Ottieni l'array delle partite
                JSONArray partiteArray = (JSONArray) root.get("Partite");

                int colonna = 0, riga = 1;

                // Carica tutte le partite
                try
                {
                    for (Object partitaObject : partiteArray)
                    {

                        System.out.println("Carico una partita JSON");

                        JSONObject partitaJSON = (JSONObject) partitaObject;

                        MatchData newMatchData = new MatchData();
                        int idPartita = Integer.parseInt(partitaJSON.get("Id_Partita").toString());
                        int password = Integer.parseInt(partitaJSON.get("Password").toString());
                        String stato = partitaJSON.get("Stato").toString();

                        newMatchData.setIdMatch("ID_Partita: " + idPartita);
                        newMatchData.setCodice(idPartita);
                        newMatchData.setPassword(password);
                        newMatchData.setState(stato);

                        matchList.add(newMatchData);

                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(Spacca.class.getResource("ItemMatch.fxml"));
                        System.out.println("Location fxml-loader: " + fxmlLoader.getLocation());

                        AnchorPane anchorPane = fxmlLoader.load();

                        MatchItemController matchItemController = fxmlLoader.getController();
                        matchItemController.setData(newMatchData);

                        if(colonna == 3)
                        {
                            colonna = 0;
                            riga++;
                        }

                        IdGridPartite.add(anchorPane, colonna++, riga);

                        // set  Grid Width
                        IdGridPartite.setMinWidth(Region.USE_COMPUTED_SIZE);
                        IdGridPartite.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        IdGridPartite.setMaxWidth(Region.USE_COMPUTED_SIZE);


                        GridPane.setMargin(anchorPane, new Insets(10));
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("[FILE-MANAGER] Non e presente la partita che cerchi!");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}


