package com.spaccafx.Files;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {



    // region #PARTITA
    static File partiteFile = new File("Partite.json"); // unico file con più partite

    public static void salvaInformazioniPartita(int codicePartita, int passwordPartita, ArrayList<IGiocatore> giocatori) {
        // Create an ObjectMapper to work with JSON
        ObjectMapper objectMapper = new ObjectMapper();

        // Create a root JSON object
        ObjectNode root = objectMapper.createObjectNode();

        // Add "Id_Partita" and "Password" fields to the root object
        root.put("Id_Partita", codicePartita);
        root.put("Password", passwordPartita);

        // Create a JSON object for the "Giocatori" field
        ObjectNode giocatoriList = objectMapper.createObjectNode();

        for (int i = 0; i < giocatori.size(); i++) {
            IGiocatore giocatore = giocatori.get(i);
            ObjectNode player = objectMapper.createObjectNode();
            player.put("Nome", giocatore.getNome());
            player.put("Istanza", giocatore.getClass().getSimpleName());
            player.put("IsAlive", true);

            String cartaPlayer = giocatore.getCarta() == null ? "NESSUNA" : giocatore.getCarta().toString();

            player.put("CartaAttuale", cartaPlayer);
            player.put("Ruolo", giocatore.getRuolo().toString());
            player.put("Vite", giocatore.getVita());
            player.put("Vita-Extra", giocatore.getVitaExtra());
            giocatoriList.set("Giocatore" + (i + 1), player);
        }

        // Add the "Giocatori" object to the root object
        root.set("Giocatori", giocatoriList);

        try (FileWriter fileWriter = new FileWriter(partiteFile, true))
        {
            // Convert the JSON structure to a string and write it to a file
            objectMapper.writeValue(fileWriter, root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // carico una determinata partita in base al codice che gli passo
    public Partita caricaPartita(int codicePartita) {
        Partita partita = null;

        try {
            // Creare un ObjectMapper per lavorare con JSON
            ObjectMapper objectMapper = new ObjectMapper();

            // Leggere il file JSON
            JsonNode rootNode = objectMapper.readTree(partiteFile);

            // Trovare l'oggetto partita con il codice specifico
            JsonNode partitaNode = null;

            for (JsonNode partitaData : rootNode) {
                int idPartita = partitaData.get("Id_Partita").asInt();
                if (idPartita == codicePartita) {
                    partitaNode = partitaData;
                    break; // Trovata la partita corrispondente, uscire dal ciclo
                }
            }

            if (partitaNode != null) {
                // Estrarre i dati relativi alla partita
                int idPartita = partitaNode.get("Id_Partita").asInt();
                int passwordPartita = partitaNode.get("Password").asInt();

                // Estrarre l'oggetto "Giocatori"
                JsonNode giocatoriNode = partitaNode.get("Giocatori");

                // Creare un'istanza della Partita
                partita = new Partita(4);

                // Iterare attraverso i giocatori e aggiungerli alla partita
                for (JsonNode giocatoreNode : giocatoriNode) {
                    String nome = giocatoreNode.get("Nome").asText();
                    String istanza = giocatoreNode.get("Istanza").asText();
                    boolean isAlive = giocatoreNode.get("IsAlive").asBoolean();
                    String ruolo = giocatoreNode.get("Ruolo").asText();
                    int vite = giocatoreNode.get("Vite").asInt();
                    int vitaExtra = giocatoreNode.get("Vita-Extra").asInt();

                    // Qui puoi creare oggetti giocatore o fare altro con i dati
                    //IGiocatore giocatore = new IGiocatore(nome, istanza, isAlive, ruolo, vite, vitaExtra);
                    //partita.aggiungiGiocatore(giocatore);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return partita;
    }
    //endregion



}

