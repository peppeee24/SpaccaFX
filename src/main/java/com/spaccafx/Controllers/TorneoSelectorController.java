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

public class TorneoSelectorController
{

    @FXML
    private GridPane IdGridTorneo;
    private ScrollPane IdScrollTorneo;

    PartitaClassicaController2 PC;

    Partita partita;

    ArrayList<MatchData> matchList;

    public void initialize()
    {
        //  betaDisable();

        matchList = new ArrayList<MatchData>();
        caricaTuttePartiteTorneoDisponibili();
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

    private void caricaTuttePartiteTorneoDisponibili()
    {
        try
        {
            if (FileManager.torneiFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(FileManager.torneiFile));

                // Ottieni l'array delle partite
                JSONArray torneiArray = (JSONArray) root.get("Tornei");

                int colonna = 0, riga = 1;

                // Carica tutte le partite
                try
                {
                    for (Object torneoObject : torneiArray)
                    {

                        System.out.println("Carico un torneo JSON");

                        JSONObject torneoJSON = (JSONObject) torneoObject;

                        MatchData newMatchData = new MatchData();
                        int idTorneo = Integer.parseInt(torneoJSON.get("Id_Torneo").toString());
                        int password = Integer.parseInt(torneoJSON.get("Password").toString());
                        GameStatus gameStatus = GameStatus.valueOf((String) torneoJSON.get("Stato"));


                        newMatchData.setCodice(idTorneo); // id torneo trovato
                        newMatchData.setPassword(password); // password trovata
                        newMatchData.setStatus(gameStatus);

                        matchList.add(newMatchData);

                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(Spacca.class.getResource("ItemTorneo.fxml"));
                        System.out.println("Location fxml-loader: " + fxmlLoader.getLocation());

                        AnchorPane anchorPane = fxmlLoader.load();

                        TorneoItemController torneoItemController = fxmlLoader.getController();
                        torneoItemController.setData(newMatchData);

                        if(colonna == 3)
                        {
                            colonna = 0;
                            riga++;
                        }

                        IdGridTorneo.add(anchorPane, colonna++, riga);

                        // set  Grid Width
                        IdGridTorneo.setMinWidth(Region.USE_COMPUTED_SIZE);
                        IdGridTorneo.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        IdGridTorneo.setMaxWidth(Region.USE_COMPUTED_SIZE);


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
                System.out.println("[FILE-MANAGER] Non e presente il torneo che cerchi!");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}


