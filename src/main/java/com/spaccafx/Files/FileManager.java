package com.spaccafx.Files;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.spaccafx.Cards.Carta;
import com.spaccafx.Cards.CartaImprevisto;
import com.spaccafx.Cards.CartaNormale;
import com.spaccafx.Cards.CartaProbabilita;
import com.spaccafx.Enums.GameStatus;
import com.spaccafx.Enums.GameType;
import com.spaccafx.Enums.RuoloGiocatore;
import com.spaccafx.Enums.SemeCarta;
import com.spaccafx.Interface.IGiocatore;
import com.spaccafx.Manager.Partita;
import com.spaccafx.Player.AdvancedBot;
import com.spaccafx.Player.EasyBot;
import com.spaccafx.Player.Giocatore;
import javafx.scene.image.Image;
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

    public static void creaPartitaSuFile(int codicePartita, int passwordPartita, ArrayList<IGiocatore> giocatori, GameType gameType, GameStatus status)
    {
        try
        {
            // Crea un oggetto JSON per la nuova partita
            JSONObject nuovaPartita = new JSONObject();
            nuovaPartita.put("Id_Partita", codicePartita);
            nuovaPartita.put("Password", passwordPartita);
            nuovaPartita.put("Stato", status.toString());
            nuovaPartita.put("Tipo", gameType.toString());
            nuovaPartita.put("CurrentGiocatore", 0);
            nuovaPartita.put("PosMazziere", 0);
            nuovaPartita.put("Round", 1);
            nuovaPartita.put("isGameRunning", false);
            nuovaPartita.put("cDistaccoMazziere", 0);

            JSONObject giocatoriList = new JSONObject();

            for (int i = 0; i < giocatori.size(); i++) {
                IGiocatore giocatore = giocatori.get(i);
                JSONObject player = new JSONObject();
                player.put("Nome", giocatore.getNome());
                player.put("Istanza", giocatore.getClass().getSimpleName());
                player.put("IsAlive", true); // TODO da cambiare

                // creare array carte del giocatore
                // se nullo imposto dei valori di default che vuol dire che il gioco deve ancora iniziare
                // Ottieni la carta del giocatore (assumendo che ogni giocatore ha solo una carta)
                Carta cartaPlayer = giocatore.getCarta();

                if (cartaPlayer != null)
                {
                    // Creazione dell'oggetto JSON per la carta
                    JSONObject cartaJSON = new JSONObject();
                    cartaJSON.put("Valore", cartaPlayer.getValore());
                    cartaJSON.put("Seme", cartaPlayer.getSeme().toString());

                    // Aggiungi l'oggetto cartaJSON al giocatore
                    player.put("Carta", cartaJSON);
                }
                else
                {
                    // Creazione dell'oggetto JSON per la carta
                    JSONObject cartaJSON = new JSONObject();
                    cartaJSON.put("Valore", 1);
                    cartaJSON.put("Seme", SemeCarta.VERME.toString()); // metto un seme a caso, TODO DA SISTEMARE

                    // Aggiungi l'oggetto cartaJSON al giocatore
                    player.put("Carta", cartaJSON);
                }

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

    // andiamo a salvare tutti i nuovi dati della partita, andandoli a sovrascrivere

    public static void sovrascriviSalvataggiPartita(Partita partitaToSave)
    {
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject) parser.parse(new FileReader(partiteFile));

            // Ottieni l'array delle partite
            JSONArray partiteArray = (JSONArray) root.get("Partite");

            for (Object partitaObject : partiteArray)
            {
                JSONObject partitaJSON = (JSONObject) partitaObject;
                int idPartitaCorrente = Integer.parseInt(partitaJSON.get("Id_Partita").toString());

                // Verifica se l'ID della partita corrente corrisponde all'ID della partita che vuoi aggiornare
                if (idPartitaCorrente == partitaToSave.getCodicePartita())
                {
                    // Aggiorna lo stato della partita
                    partitaJSON.put("Stato", partitaToSave.getPartitaStatus().toString());
                    partitaJSON.put("isGameRunning", partitaToSave.isGameRunning());
                    partitaJSON.put("Round", partitaToSave.getCurrentRound());
                    partitaJSON.put("CurrentGiocatore", partitaToSave.getCurrentGiocatorePos());
                    partitaJSON.put("PosMazziere", partitaToSave.getPosMazziere());
                    partitaJSON.put("cDistaccoMazziere", partitaToSave.getDistaccoMazziere());


                    // Aggiorna le informazioni dei giocatori
                    JSONObject giocatoriList = (JSONObject) partitaJSON.get("Giocatori");


                    // TODO IL FOR FA SISTEMATO NEL CASO IN CUI DEI GIOCATORI SONO MORTI E NON SI ANDREBBERO A SOVRASCRIVERE TUTTI QUELLI PRESENTI
                    for (int i = 1; i <= partitaToSave.giocatori.size(); i++)
                    {
                        String nomeGiocatore = "Giocatore" + i;
                        JSONObject giocatoreJSON = (JSONObject) giocatoriList.get(nomeGiocatore);

                        IGiocatore nuovoGiocatore = partitaToSave.giocatori.get(i - 1);

                        giocatoreJSON.put("Vita-Extra", nuovoGiocatore.getVitaExtra());
                        giocatoreJSON.put("Ruolo", nuovoGiocatore.getRuolo().toString().toUpperCase());
                        giocatoreJSON.put("IsAlive", true); // todo da cambiare
                        giocatoreJSON.put("Vita-Extra", nuovoGiocatore.getVitaExtra());
                        giocatoreJSON.put("Vite", nuovoGiocatore.getVita());

                        // Salva le informazioni della carta attuale
                        JSONObject cartaJSON = new JSONObject();
                        cartaJSON.put("Valore", nuovoGiocatore.getCarta().getValore());
                        cartaJSON.put("Seme", nuovoGiocatore.getCarta().getSeme().toString());
                        giocatoreJSON.put("Carta", cartaJSON);

                    }

                    // Sovrascrivi il file con i dati aggiornati
                    try (FileWriter fileWriter = new FileWriter(partiteFile))
                    {
                        fileWriter.write(root.toJSONString());
                        System.out.println("Dati della partita aggiornati con successo nel file JSON.");
                        return; // Esci dal metodo una volta che la partita è stata trovata e aggiornata
                    }
                }
            }

            System.out.println("Partita non trovata per l'ID: " + partitaToSave.getCodicePartita());
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }
    }


    //TODO DA VERIFICARE
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

    public static Partita convertiJSONAPartita(JSONObject partitaJSON)
    {

        // Estrarre i dati dall'oggetto JSON e creare un oggetto Partita
        int idPartita = Integer.parseInt(partitaJSON.get("Id_Partita").toString()); // prendo id
        int password = Integer.parseInt(partitaJSON.get("Password").toString()); // prendo password
        int currentGioocatorePos = Integer.parseInt(partitaJSON.get("CurrentGiocatore").toString()); // prendo giocatore attuale
        int posMazziere = Integer.parseInt(partitaJSON.get("PosMazziere").toString()); // prendo pos mazziere
        boolean isGameRunning = Boolean.parseBoolean(partitaJSON.get("isGameRunning").toString()); // prendo pos mazziere
        GameStatus gameStatus = GameStatus.valueOf((String) partitaJSON.get("Stato"));  // prendo stato
        int cDistaccoMazziere = Integer.parseInt(partitaJSON.get("cDistaccoMazziere").toString()); // prendo il distacco del mazziere


        JSONObject giocatoriObject = (JSONObject) partitaJSON.get("Giocatori");

        // Esempio: crea una nuova istanza di Partita con i dati estratti
        Partita partita = new Partita(giocatoriObject.size()); // imposto quanti giocatori ci sono
        partita.setCodicePartita(idPartita);
        partita.setPasswordPartita(password);
        partita.setPartitaStatus(gameStatus); // impostiamo lo stato che prendiamo
        partita.setCurrentGiocatorePos(currentGioocatorePos);
        partita.setPosMazziere(posMazziere);
        partita.setIsGameRunning(isGameRunning);
        partita.setDistaccoMazziere(cDistaccoMazziere);

        // Aggiungi giocatori alla partita
        for (Object giocatoreKey : giocatoriObject.keySet())
        {
            String nomeGiocatore = (String) giocatoreKey;
            JSONObject giocatoreJSON = (JSONObject) giocatoriObject.get(nomeGiocatore);

            String nomePlayer = giocatoreJSON.get("Nome").toString();
            int vitaExtra = Integer.parseInt(giocatoreJSON.get("Vita-Extra").toString());
            boolean isAlive = Boolean.parseBoolean(giocatoreJSON.get("IsAlive").toString());
            int vite = Integer.parseInt(giocatoreJSON.get("Vite").toString());
            String istanza = giocatoreJSON.get("Istanza").toString();
            RuoloGiocatore ruoloGiocatore = RuoloGiocatore.valueOf((String) giocatoreJSON.get("Ruolo"));

            IGiocatore giocatore = null;

            switch (istanza.toUpperCase())
            {
                case "GIOCATORE":   giocatore = new Giocatore(nomePlayer); // crea il giocatore in base all istanza
                                    break;
                case "ADVANCEDBOT": giocatore = new AdvancedBot(nomePlayer); break;
                case "EASYBOT": giocatore = new EasyBot(nomePlayer); break;
                default: giocatore = new Giocatore(nomePlayer);
            }

            giocatore.setNome(nomePlayer);
            giocatore.setVita(vite);
            giocatore.setVitaExtra(vitaExtra);
            giocatore.setRuolo(ruoloGiocatore);

            //TODO mettere se il giocatore e vivo

            System.out.println("Giocatore: " + giocatore.getNome() );

            JSONObject cartaObject = (JSONObject) giocatoreJSON.get("Carta"); // prendo la carta dal player
            Carta cartaPlayer = null;
            int valore = Integer.parseInt(cartaObject.get("Valore").toString());
            SemeCarta semeCarta = SemeCarta.valueOf((String) cartaObject.get("Seme"));

            Image cartaImage;
            cartaImage = new Image(FileManager.class.getResource("/Assets/Cards/" + semeCarta.toString() + "/" + semeCarta.toString() + valore + ".PNG").toString());

            //TODO METTERE SE L EFFETTO DELLA CARTA E STATO GIA ATTIVATO O MENO
            switch (semeCarta)
            {
                case PROBABILITA:   cartaPlayer = new CartaProbabilita(valore, semeCarta);
                    break;
                case SQUALO:   cartaPlayer = new CartaNormale(valore, semeCarta);
                    break;
                case PESCE:   cartaPlayer = new CartaNormale(valore, semeCarta);
                    break;
                case VERME:   cartaPlayer = new CartaNormale(valore, semeCarta);
                    break;
                case IMPREVISTO:   cartaPlayer = new CartaImprevisto(valore, semeCarta);
                    break;
                default:
                    System.out.println("Errore nella ripresa di una partita!");
                    System.exit(-1);
            }

            cartaPlayer.setImage(cartaImage); // imposto la visuale della carta
            giocatore.setCarta(cartaPlayer);

            partita.aggiungiGiocatore(giocatore);
        }

        System.out.println("Giocatori vivi in questa partita: " + partita.giocatori.size());
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

    public static ArrayList<Carta> getPlayerCarte(int codicePartita)
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
                        return convertiCartePartitaJSON(partitaJSON);
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

    public static ArrayList<Carta> convertiCartePartitaJSON(JSONObject partitaJSON)
    {
        ArrayList<Carta> playerCards = new ArrayList<>();

        JSONObject giocatoriObject = (JSONObject) partitaJSON.get("Giocatori");

        for (Object giocatoreKey : giocatoriObject.keySet())
        {
            String nomeGiocatore = (String) giocatoreKey;
            JSONObject giocatoreJSON = (JSONObject) giocatoriObject.get(nomeGiocatore);

            JSONObject cartaObject = (JSONObject) giocatoreJSON.get("Carta"); // prendo la carta dal player

            Carta cartaPlayer = null;
            int valore = Integer.parseInt(cartaObject.get("Valore").toString());
            SemeCarta semeCarta = SemeCarta.valueOf((String) cartaObject.get("Seme"));

            switch (semeCarta)
            {
                case PROBABILITA:   cartaPlayer = new CartaProbabilita(valore, semeCarta);
                    break;
                case SQUALO:   cartaPlayer = new CartaNormale(valore, semeCarta);
                    break;
                case PESCE:   cartaPlayer = new CartaNormale(valore, semeCarta);
                    break;
                case VERME:   cartaPlayer = new CartaNormale(valore, semeCarta);
                    break;
                case IMPREVISTO:   cartaPlayer = new CartaImprevisto(valore, semeCarta);
                    break;
                default:
                    System.out.println("Errore nella ripresa di una partita!");
                    System.exit(-1);
            }

            playerCards.add(cartaPlayer); // aggiungiamo la carta
            System.out.println("Trovata carta: " + cartaPlayer.toString());
        }

        return playerCards;
    }

    //endregion



}

