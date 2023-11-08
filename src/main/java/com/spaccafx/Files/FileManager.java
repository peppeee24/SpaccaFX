package com.spaccafx.Files;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager
{

    // region #PARTITA
    public static File partiteFile = new File("Partite.json"); // unico file con più partite

    public static void salvaPartitaSuFile(int codicePartita, int passwordPartita, ArrayList<IGiocatore> giocatori)
    {
        try
        {
            // Crea un oggetto JSON per la nuova partita
            JSONObject nuovaPartita = new JSONObject();
            nuovaPartita.put("Id_Partita", codicePartita);
            nuovaPartita.put("Password", passwordPartita);
            nuovaPartita.put("Stato", "FINISH");

            JSONObject giocatoriList = new JSONObject();

            for (int i = 0; i < giocatori.size(); i++) {
                IGiocatore giocatore = giocatori.get(i);
                JSONObject player = new JSONObject();
                player.put("Nome", giocatore.getNome());
                player.put("Istanza", giocatore.getClass().getSimpleName());
                player.put("IsAlive", true); // da cambiare

                // creare array carte del giocatore
                // se nullo imposto dei valori di default che vuol dire che il gioco deve ancora iniziare
                String cartaPlayer = giocatore.getCarta() == null ? "NESSUNA" : giocatore.getCarta().toString();

                player.put("CartaAttuale", cartaPlayer);
                player.put("Ruolo", giocatore.getRuolo().toString());
                player.put("Vite", giocatore.getVita());
                player.put("Vita-Extra", giocatore.getVitaExtra());

                giocatoriList.put("Giocatore" + (i + 1), player);
            }

            nuovaPartita.put("Giocatori", giocatoriList);

            // Leggi il JSON esistente dal file, se esiste
            JSONObject root = new JSONObject();
            if (partiteFile.exists())
            {
                JSONParser parser = new JSONParser();
                root = (JSONObject) parser.parse(new FileReader(partiteFile));
            }

            // Aggiungi la nuova partita ai dati esistenti
            JSONArray partiteArray = (JSONArray) root.get("Partite");
            if (partiteArray == null)
            {
                partiteArray = new JSONArray();
            }
            partiteArray.add(nuovaPartita);
            root.put("Partite", partiteArray);

            // Sovrascrivi il file con i dati aggiornati
            try (FileWriter fileWriter = new FileWriter(partiteFile))
            {
                fileWriter.write(root.toJSONString());
            }

            System.out.println("Partita aggiunta con successo al file JSON.");

        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }
    }

    public static int creaCodicePartitaUnico()
    {
        int maxTentativi = 999; // Massimo numero di tentativi per generare un codice unico

        try {
            if (!partiteFile.exists()) {
                return (int) (1 + Math.random() * maxTentativi); // Se il file non esiste, genera casualmente un codice
            }

            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject) parser.parse(new FileReader(partiteFile));

            // Ottieni l'array delle partite
            JSONArray partiteArray = (JSONArray) root.get("Partite");

            for (int i = 1; i <= maxTentativi; i++) {
                boolean codeExists = false;

                for (Object partitaObject : partiteArray) {
                    JSONObject partitaJSON = (JSONObject) partitaObject;
                    int idPartita = Integer.parseInt(partitaJSON.get("Id_Partita").toString());

                    if (idPartita == i) {
                        codeExists = true;
                        break; // Il codice esiste, passa al successivo
                    }
                }

                if (!codeExists) {
                    return i; // Il codice è unico, restituiscilo
                }
            }

            // Se hai esaurito tutti i possibili codici, gestiscilo in base alle tue esigenze.
            System.out.println("[FILE-MANAGER] Errore: Impossibile generare un codice ID unico.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println("[FILE-MANAGER] Errore");
        }

        return -1; // Valore speciale che indica un errore o impossibilità di generare un codice unico
    }

    // carico una determinata partita in base al codice che gli passo
    public static Partita leggiPartitaDaFile(int codicePartita)
    {
        try
        {
            if (partiteFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(partiteFile));

                // Ottieni l'array delle partite
                JSONArray partiteArray = (JSONArray) root.get("Partite");

                // Cerca la partita con il codicePartita specificato
                for (Object partitaObject : partiteArray)
                {
                    JSONObject partitaJSON = (JSONObject) partitaObject;
                    int idPartita = Integer.parseInt(partitaJSON.get("Id_Partita").toString());
                    if (idPartita == codicePartita)
                    {
                        return convertiJSONAPartita(partitaJSON);
                    }
                }
            }
            else
            {
                System.out.println("[FILE-MANAGER] Non e presente la partita che cerchi!");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null; // Restituisci null se la partita non è stata trovata o ci sono errori
    }

    public static Partita convertiJSONAPartita(JSONObject partitaJSON) {
        // Estrarre i dati dall'oggetto JSON e creare un oggetto Partita
        int idPartita = Integer.parseInt(partitaJSON.get("Id_Partita").toString());
        int password = Integer.parseInt(partitaJSON.get("Password").toString());
        JSONObject giocatoriObject = (JSONObject) partitaJSON.get("Giocatori");

        // Esempio: crea una nuova istanza di Partita con i dati estratti
        Partita partita = new Partita(giocatoriObject.size()); // imposto quanti giocatori ci sono
        partita.setCodicePartita(idPartita);
        partita.setPasswordPartita(password);

        // Aggiungi giocatori alla partita
        for (Object giocatoreKey : giocatoriObject.keySet()) {
            String nomeGiocatore = (String) giocatoreKey;
            JSONObject giocatoreJSON = (JSONObject) giocatoriObject.get(nomeGiocatore);

            String nomePlayer = giocatoreJSON.get("Nome").toString();
            int vitaExtra = Integer.parseInt(giocatoreJSON.get("Vita-Extra").toString());
            boolean isAlive = Boolean.parseBoolean(giocatoreJSON.get("IsAlive").toString());
            int vite = Integer.parseInt(giocatoreJSON.get("Vite").toString());
            String istanza = giocatoreJSON.get("Istanza").toString();

            IGiocatore giocatore = null;

            switch (istanza.toUpperCase())
            {
                case "GIOCATORE":   giocatore = new Giocatore(nomePlayer); // crea il giocatore in base all istanza
                                    break;
                case "ADVANCEDBOT": giocatore = new AdvancedBot(nomePlayer); break;
                case "EASYBOT": giocatore = new EasyBot(nomePlayer); break;
                default: giocatore = new Giocatore(nomePlayer);
            }

            System.out.println("Giocatore: " + giocatore.getNome() );
            partita.aggiungiGiocatore(giocatore);
        }
        System.out.println("Carico partita con giocatori: " + partita.giocatori.size());
        System.out.println("giocatore1: " + partita.giocatori.get(0).getNome());
        System.out.println("giocatore2: " + partita.giocatori.get(1).getNome());
        System.out.println("giocatore3: " + partita.giocatori.get(2).getNome());
        System.out.println("giocatore4: " + partita.giocatori.get(3).getNome());
        return partita;
    }

    public static int getPasswordPartita(int codicePartita)
    {
        try
        {
            if (partiteFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(partiteFile));

                // Ottieni l'array delle partite
                JSONArray partiteArray = (JSONArray) root.get("Partite");

                // Cerca la partita con il codicePartita specificato
                for (Object partitaObject : partiteArray)
                {
                    JSONObject partitaJSON = (JSONObject) partitaObject;
                    int idPartita = Integer.parseInt(partitaJSON.get("Id_Partita").toString());

                    if (idPartita == codicePartita) // ho trovato la mia partita
                    {
                        int passwordPartita = Integer.parseInt(partitaJSON.get("Password").toString());
                        return passwordPartita; // ho trovato la mia partita, do il codice
                    }
                }
            }
            else
            {
                System.out.println("[FILE-MANAGER] Non e presente la partita che cerchi!");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        // todo non va bene lasciarlo cosi
        return 0; // se non ho trovato nessuna password
    }

    //endregion



}

