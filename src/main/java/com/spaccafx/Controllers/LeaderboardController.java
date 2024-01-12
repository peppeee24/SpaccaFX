package com.spaccafx.Controllers;

import com.spaccafx.Enums.GameStatus;
import com.spaccafx.Files.AudioManager;
import com.spaccafx.Files.FileManager;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Spacca;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class LeaderboardController
{

    @FXML
    private GridPane IdGridGiocatore;
    private ScrollPane IdScrollPartite;

    PartitaClassicaController2 PC;

    Partita partita;

    ArrayList<MatchData> matchList;

    public void initialize()
    {



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
                        GameStatus gameStatus = GameStatus.valueOf((String) partitaJSON.get("Stato"));


                        newMatchData.setCodice(idPartita); // id partita trovato
                        newMatchData.setPassword(password); // password trovata
                        newMatchData.setStatus(gameStatus);

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

                        IdGridGiocatore.add(anchorPane, colonna++, riga);

                        // set  Grid Width
                        IdGridGiocatore.setMinWidth(Region.USE_COMPUTED_SIZE);
                        IdGridGiocatore.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        IdGridGiocatore.setMaxWidth(Region.USE_COMPUTED_SIZE);


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


