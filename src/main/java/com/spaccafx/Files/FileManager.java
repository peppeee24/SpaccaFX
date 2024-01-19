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
    public static File partiteFile = new File("Partite.json"); // unico file con più partite
    public static File torneiFile = new File("Tornei.json"); // unico file con più partite

    // region #PARTITA

    public static void creaPartitaSuFile(int codicePartita, int passwordPartita, ArrayList<IGiocatore> giocatori, int maxCarteNormali, int maxCarteSpeciali, int numeroPlayerVite)
    {
        try
        {
            // Crea un oggetto JSON per la nuova partita
            JSONObject nuovaPartita = new JSONObject();
            nuovaPartita.put("Id_Partita", codicePartita);
            nuovaPartita.put("Password", passwordPartita);
            nuovaPartita.put("Stato", GameStatus.STARTED.toString());
            nuovaPartita.put("Tipo", GameType.PARTITA.toString());
            nuovaPartita.put("CurrentGiocatore", 0);
            nuovaPartita.put("PosMazziere", 0);
            nuovaPartita.put("Round", 1);
            nuovaPartita.put("isGameRunning", false);
            nuovaPartita.put("cDistaccoMazziere", 0);
            nuovaPartita.put("cartaGiaScambiata", false);
            nuovaPartita.put("maxCarteNormali", maxCarteNormali);
            nuovaPartita.put("maxCarteSpeciali", maxCarteSpeciali);
            nuovaPartita.put("numeroPlayerVite", numeroPlayerVite);

            // Dobbiamo creare un oggetto che abbia tutti i dati del vincitore (nome, istanza)
            JSONObject vincitore = new JSONObject();
            vincitore.put("Nome", "TBD");
            vincitore.put("Istanza", "Giocatore");

            nuovaPartita.put("Vincitore", vincitore);

            JSONObject giocatoriList = new JSONObject();

            for (int i = 0; i < giocatori.size(); i++) {
                IGiocatore giocatore = giocatori.get(i);
                JSONObject player = new JSONObject();
                player.put("Nome", giocatore.getNome());
                player.put("Istanza", giocatore.getClass().getSimpleName());

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
                    cartaJSON.put("Attivata", cartaPlayer.getCartaEffettoAttivato()); // teoricamente sempre su false

                    // Aggiungi l'oggetto cartaJSON al giocatore
                    player.put("Carta", cartaJSON);
                }
                else
                {
                    // Creazione dell'oggetto JSON per la carta
                    JSONObject cartaJSON = new JSONObject();
                    cartaJSON.put("Valore", 1);
                    cartaJSON.put("Seme", SemeCarta.VERME.toString()); // metto un seme a caso
                    cartaJSON.put("Attivata", true);

                    // Aggiungi l'oggetto cartaJSON al giocatore
                    player.put("Carta", cartaJSON);
                }

                player.put("Ruolo", giocatore.getRuolo().toString());
                player.put("Vite", numeroPlayerVite);
                player.put("Vita-Extra", giocatore.getVitaExtra());
                player.put("PlayerRounds", giocatore.getPlayerRounds());

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
                    partitaJSON.put("isGameRunning", partitaToSave.getIsGameRunning());
                    partitaJSON.put("Round", partitaToSave.getCurrentRound());
                    partitaJSON.put("CurrentGiocatore", partitaToSave.getCurrentGiocatorePos());
                    partitaJSON.put("PosMazziere", partitaToSave.getPosMazziere());
                    partitaJSON.put("cDistaccoMazziere", partitaToSave.getDistaccoMazziere());
                    partitaJSON.put("cartaGiaScambiata", partitaToSave.getCartaGiaScambiata());
                    partitaJSON.put("maxCarteNormali", partitaToSave.getMaxCarteNormali());
                    partitaJSON.put("maxCarteSpeciali", partitaToSave.getMaxCarteSpeciali());
                    partitaJSON.put("numeroPlayerVite", partitaToSave.getNumeroPlayerVite());

                    // Bisogna vedere se abbiamo anche un vincitore nella partita

                    IGiocatore giocatoreVincitore = partitaToSave.getVincitore();
                    if(giocatoreVincitore != null)
                    {
                        JSONObject vincitoreJSON = (JSONObject) partitaJSON.get("Vincitore");
                        vincitoreJSON.put("Nome", giocatoreVincitore.getNome());
                        vincitoreJSON.put("Istanza", giocatoreVincitore.getClass().getSimpleName());
                    }


                    // Aggiorna le informazioni dei giocatori
                    JSONObject giocatoriList = (JSONObject) partitaJSON.get("Giocatori");


                    for (int i = 1; i <= partitaToSave.giocatori.size(); i++)
                    {
                        String nomeGiocatore = "Giocatore" + i;
                        JSONObject giocatoreJSON = (JSONObject) giocatoriList.get(nomeGiocatore);

                        IGiocatore nuovoGiocatore = partitaToSave.giocatori.get(i - 1);

                        giocatoreJSON.put("Vita-Extra", nuovoGiocatore.getVitaExtra());
                        giocatoreJSON.put("Ruolo", nuovoGiocatore.getRuolo().toString().toUpperCase());
                        giocatoreJSON.put("Vita-Extra", nuovoGiocatore.getVitaExtra());
                        giocatoreJSON.put("Vite", nuovoGiocatore.getVita());
                        giocatoreJSON.put("PlayerRounds", nuovoGiocatore.getPlayerRounds());

                        // Salva le informazioni della carta attuale
                        JSONObject cartaJSON = new JSONObject();

                        Carta cartaGiocatore = nuovoGiocatore.getCarta();

                        if(cartaGiocatore == null)
                        {
                            cartaGiocatore = new CartaNormale(1, SemeCarta.VERME); // Dato che il player e morto salvo una carta di default casuale
                            cartaGiocatore.setCartaEffettoAttivato(true);
                        }


                        cartaJSON.put("Valore", cartaGiocatore.getValore());
                        cartaJSON.put("Seme", cartaGiocatore.getSeme().toString());
                        cartaJSON.put("Attivata", cartaGiocatore.getCartaEffettoAttivato());

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

    public static int creaCodicePartitaUnico()
    {
        int maxTentativi = 9999; // Massimo numero di tentativi per generare un codice unico
        int codice = 0;

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
                codice = (int) (1 + Math.random() * maxTentativi);

                for (Object partitaObject : partiteArray) {
                    JSONObject partitaJSON = (JSONObject) partitaObject;
                    int idPartita = Integer.parseInt(partitaJSON.get("Id_Partita").toString());

                    if (idPartita == codice)
                    {
                        System.out.println("Il codice che ho generato " + codice + " e gia associato ad una partita, ne creo un altro.");
                        codeExists = true;
                        break; // Il codice esiste, passa al successivo
                    }
                }

                if (!codeExists)
                {
                    System.out.println("Il codice che ho generato univoco della partita e: " + codice);
                    return codice; // Il codice è unico, restituiscilo
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

    public static int creaPasswordPartitaUnica()
    {
        int maxTentativi = 9999; // Massimo numero di tentativi per generare un codice unico
        int password = 0;

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
                password = (int) (1 + Math.random() * maxTentativi);

                for (Object partitaObject : partiteArray) {
                    JSONObject partitaJSON = (JSONObject) partitaObject;
                    int passwordPartita = Integer.parseInt(partitaJSON.get("Password").toString());

                    if (passwordPartita == password)
                    {
                        System.out.println("La password che ho generato " + password + " e gia associato ad una partita, ne creo un altra.");
                        codeExists = true;
                        break; // Il codice esiste, passa al successivo
                    }
                }

                if (!codeExists)
                {
                    System.out.println("La password che ho generato univoca della partita e: " + password);
                    return password; // Il codice è unico, restituiscilo
                }
            }

            // Se hai esaurito tutti i possibili codici, gestiscilo in base alle tue esigenze.
            System.out.println("[FILE-MANAGER] Errore: Impossibile generare una password ID unica.");
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
        GameType gameType = GameType.valueOf((String) partitaJSON.get("Tipo"));  // prendo stato
        int cDistaccoMazziere = Integer.parseInt(partitaJSON.get("cDistaccoMazziere").toString()); // prendo il distacco del mazziere
        boolean cartaGiaScambiata = Boolean.parseBoolean(partitaJSON.get("cartaGiaScambiata").toString()); // prendo se il player ha gia fatto lo scambio o no
        int currentRound = Integer.parseInt(partitaJSON.get("Round").toString()); // prendo il round attuale della partita
        int maxCarteNormali = Integer.parseInt(partitaJSON.get("maxCarteNormali").toString()); // prendo il max carte normali
        int maxCarteSpeciali = Integer.parseInt(partitaJSON.get("maxCarteSpeciali").toString()); // prendo il max carte speciali
        int numeroPlayerVite = Integer.parseInt(partitaJSON.get("numeroPlayerVite").toString()); // prendo il n di vite iniziali dei player


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
        partita.setCartaGiaScambiata(cartaGiaScambiata);
        partita.setCurrentRound(currentRound);
        partita.setMaxCarteNormali(maxCarteNormali);
        partita.setMaxCarteSpeciali(maxCarteSpeciali);
        partita.setNumeroPlayerVite(numeroPlayerVite);
        partita.setGameType(gameType);

        // Aggiungi giocatori alla partita
        for (Object giocatoreKey : giocatoriObject.keySet())
        {
            String nomeGiocatore = (String) giocatoreKey;
            JSONObject giocatoreJSON = (JSONObject) giocatoriObject.get(nomeGiocatore);

            String nomePlayer = giocatoreJSON.get("Nome").toString();
            int vitaExtra = Integer.parseInt(giocatoreJSON.get("Vita-Extra").toString());
            int vite = Integer.parseInt(giocatoreJSON.get("Vite").toString());
            String istanza = giocatoreJSON.get("Istanza").toString();
            RuoloGiocatore ruoloGiocatore = RuoloGiocatore.valueOf((String) giocatoreJSON.get("Ruolo"));
            int playerRounds = Integer.parseInt(giocatoreJSON.get("PlayerRounds").toString());

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
            giocatore.setPlayerRounds(playerRounds);

            System.out.println("Giocatore: " + giocatore.getNome() );

            JSONObject cartaObject = (JSONObject) giocatoreJSON.get("Carta"); // prendo la carta dal player
            Carta cartaPlayer = null;
            int valore = Integer.parseInt(cartaObject.get("Valore").toString());
            SemeCarta semeCarta = SemeCarta.valueOf((String) cartaObject.get("Seme"));
            boolean isEffettoCartaAttivato = Boolean.parseBoolean(cartaObject.get("Attivata").toString());

            Image cartaImage;
            cartaImage = new Image(FileManager.class.getResource("/Assets/Cards/" + semeCarta.toString() + "/" + semeCarta.toString() + valore + ".PNG").toString());

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

            cartaPlayer.setCartaEffettoAttivato(isEffettoCartaAttivato);
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

    public static ArrayList<Carta> getPlayerCartePartita(int codicePartita)
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

    public static ArrayList<Carta> convertiCartePartitaJSON(JSONObject partitaJSON) // prendo carte solo giocatori vivi
    {
        ArrayList<Carta> playerCards = new ArrayList<>();

        JSONObject giocatoriObject = (JSONObject) partitaJSON.get("Giocatori");

        for (Object giocatoreKey : giocatoriObject.keySet())
        {
            String nomeGiocatore = (String) giocatoreKey;
            JSONObject giocatoreJSON = (JSONObject) giocatoriObject.get(nomeGiocatore);

            RuoloGiocatore ruoloGiocatore = RuoloGiocatore.valueOf((String) giocatoreJSON.get("Ruolo"));

            if(ruoloGiocatore != RuoloGiocatore.MORTO)
            {
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
                System.out.println("Trovata carta giocatore vivo: " + cartaPlayer.toString());
            }

        }

        return playerCards;
    }

    //endregion


    // region #TORNEI

    public static void creaTorneoSuFile(int codiceTorneo, int passwordTorneo, ArrayList<Partita> partite, GameType gameType, GameStatus status, int maxCarteNormali, int maxCarteSpeciali, int numeroPlayerVite)
    {
        try
        {
            // Crea un oggetto JSON per la nuova partita
            JSONObject nuovoTorneo = new JSONObject();
            nuovoTorneo.put("Id_Torneo", codiceTorneo);
            nuovoTorneo.put("Password", passwordTorneo);
            nuovoTorneo.put("Stato", status.toString());
            nuovoTorneo.put("MaxCarteNormali", maxCarteNormali);
            nuovoTorneo.put("MaxCarteSpeciali", maxCarteSpeciali);
            nuovoTorneo.put("NumeroPlayerVite", numeroPlayerVite);
            nuovoTorneo.put("Tipo", gameType.toString());
            nuovoTorneo.put("CurrentMatch", 0);

            JSONObject partiteList = new JSONObject();

            // CREO LE MIE 4 PARTITE
            for(int c=0; c<4; c++) // creo le mie 4 partite con i giocatori
            {
                // Crea un oggetto JSON per la nuova partita del torneo
                JSONObject nuovaPartitaTorneo = new JSONObject();
                nuovaPartitaTorneo.put("cartaGiaScambiata", false);
                nuovaPartitaTorneo.put("Tipo", GameType.TORNEO.toString());
                nuovaPartitaTorneo.put("Stato",  GameStatus.STARTED.toString());
                nuovaPartitaTorneo.put("CurrentGiocatore", 0);
                nuovaPartitaTorneo.put("PosMazziere", 0);
                nuovaPartitaTorneo.put("Round", 1);
                nuovaPartitaTorneo.put("isGameRunning", false);
                nuovaPartitaTorneo.put("cDistaccoMazziere", 0);

                // Dobbiamo creare un oggetto che abbia tutti i dati del vincitore (nome, istanza)
                JSONObject vincitore = new JSONObject();
                vincitore.put("Nome", "TBD");
                vincitore.put("Istanza", "Giocatore");

                nuovaPartitaTorneo.put("Vincitore", vincitore);


                JSONObject giocatoriList = new JSONObject(); // creo i dati per ogni singolo gicatore

                for (int i = 0; i < partite.get(c).giocatori.size(); i++)
                {
                    IGiocatore giocatore = partite.get(c).giocatori.get(i);
                    JSONObject player = new JSONObject();
                    player.put("Nome", giocatore.getNome());
                    player.put("Istanza", giocatore.getClass().getSimpleName());

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
                        cartaJSON.put("Attivata", cartaPlayer.getCartaEffettoAttivato()); // teoricamente sempre su false

                        // Aggiungi l'oggetto cartaJSON al giocatore
                        player.put("Carta", cartaJSON);
                    }
                    else
                    {
                        // Creazione dell'oggetto JSON per la carta
                        JSONObject cartaJSON = new JSONObject();
                        cartaJSON.put("Valore", 1);
                        cartaJSON.put("Seme", SemeCarta.VERME.toString()); // metto un seme a caso
                        cartaJSON.put("Attivata", true);

                        // Aggiungi l'oggetto cartaJSON al giocatore
                        player.put("Carta", cartaJSON);
                    }

                    player.put("Ruolo", giocatore.getRuolo().toString());
                    player.put("Vite", numeroPlayerVite);
                    player.put("Vita-Extra", giocatore.getVitaExtra());
                    player.put("PlayerRounds", giocatore.getPlayerRounds());

                    giocatoriList.put("Giocatore" + (i + 1), player);

                }

                nuovaPartitaTorneo.put("Giocatori", giocatoriList);
                partiteList.put("Partita" + (c + 1), nuovaPartitaTorneo);
            }

            nuovoTorneo.put("Partite", partiteList);

            // Adesso creo la mia partita finale
            JSONObject partitaFinale = new JSONObject();
            creaPartitaTorneoFinale(nuovoTorneo, partitaFinale, numeroPlayerVite);


            // Leggi il JSON esistente dal file, se esiste
            JSONObject root = new JSONObject();
            if (torneiFile.exists())
            {
                JSONParser parser = new JSONParser();
                root = (JSONObject) parser.parse(new FileReader(torneiFile));
            }

            // Aggiungi la nuova partita ai dati esistenti
            JSONArray partiteArray = (JSONArray) root.get("Tornei");
            if (partiteArray == null)
            {
                partiteArray = new JSONArray();
            }
            partiteArray.add(nuovoTorneo);
            root.put("Tornei", partiteArray);

            // Sovrascrivi il file con i dati aggiornati
            try (FileWriter fileWriter = new FileWriter(torneiFile))
            {
                fileWriter.write(root.toJSONString());
            }

            System.out.println("Torneo aggiunto con successo al file JSON.");

        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }
    }

    public static int creaCodiceTorneoUnico()
    {
        int maxTentativi = 9999; // Massimo numero di tentativi per generare un codice unico
        int codice = 0;

        try {
            if (!torneiFile.exists()) {
                return (int) (1 + Math.random() * maxTentativi); // Se il file non esiste, genera casualmente un codice
            }

            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject) parser.parse(new FileReader(torneiFile));

            // Ottieni l'array delle partite
            JSONArray partiteArray = (JSONArray) root.get("Tornei");

            for (int i = 1; i <= maxTentativi; i++) {
                boolean codeExists = false;
                codice = (int) (1 + Math.random() * maxTentativi);

                for (Object partitaObject : partiteArray) {
                    JSONObject partitaJSON = (JSONObject) partitaObject;
                    int idPartita = Integer.parseInt(partitaJSON.get("Id_Torneo").toString());

                    if (idPartita == codice)
                    {
                        System.out.println("Il codice che ho generato " + codice + " e gia associato ad un torneo, ne creo un altro.");
                        codeExists = true;
                        break; // Il codice esiste, passa al successivo
                    }
                }

                if (!codeExists)
                {
                    System.out.println("Il codice che ho generato univoco del torneo e: " + codice);
                    return codice; // Il codice è unico, restituiscilo
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

    public static int creaPasswordTorneoUnico()
    {
        int maxTentativi = 9999; // Massimo numero di tentativi per generare un codice unico
        int password = 0;

        try {
            if (!torneiFile.exists()) {
                return (int) (1 + Math.random() * maxTentativi); // Se il file non esiste, genera casualmente un codice
            }

            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject) parser.parse(new FileReader(torneiFile));

            // Ottieni l'array delle partite
            JSONArray partiteArray = (JSONArray) root.get("Tornei");

            for (int i = 1; i <= maxTentativi; i++) {
                boolean codeExists = false;
                password = (int) (1 + Math.random() * maxTentativi);

                for (Object partitaObject : partiteArray) {
                    JSONObject partitaJSON = (JSONObject) partitaObject;
                    int idPartita = Integer.parseInt(partitaJSON.get("Password").toString());

                    if (idPartita == password)
                    {
                        System.out.println("La password che ho generato " + password + " e gia associato ad un torneo, ne creo un altra.");
                        codeExists = true;
                        break; // Il codice esiste, passa al successivo
                    }
                }

                if (!codeExists)
                {
                    System.out.println("La password che ho generato univoca del torneo e: " + password);
                    return password; // Il codice è unico, restituiscilo
                }
            }

            // Se hai esaurito tutti i possibili codici, gestiscilo in base alle tue esigenze.
            System.out.println("[FILE-MANAGER] Errore: Impossibile generare una password torneo unica.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println("[FILE-MANAGER] Errore");
        }

        return -1; // Valore speciale che indica un errore o impossibilità di generare un codice unico
    }

    public static void creaPartitaTorneoFinale(JSONObject torneoJSON,JSONObject partitaFinale, int numeroPlayerVite) {
        // Crea un oggetto JSON per la nuova partita finale del torneo
        partitaFinale.put("cartaGiaScambiata", false);
        partitaFinale.put("Tipo", GameType.TORNEO.toString());
        partitaFinale.put("Stato", GameStatus.STARTED.toString());
        partitaFinale.put("CurrentGiocatore", 0);
        partitaFinale.put("PosMazziere", 0);
        partitaFinale.put("Round", 1);
        partitaFinale.put("isGameRunning", false);
        partitaFinale.put("cDistaccoMazziere", 0);

        // Dobbiamo creare un oggetto che abbia tutti i dati del vincitore (nome, istanza)
        JSONObject vincitore = new JSONObject();
        vincitore.put("Nome", "TBD");
        vincitore.put("Istanza", "Giocatore");

        partitaFinale.put("Vincitore", vincitore);


        JSONObject giocatoriList = new JSONObject(); // creo i dati per ogni singolo gicatore

        for (int i = 0; i < 4; i++)
        {
            //IGiocatore giocatore = partite.get(c).giocatori.get(i);
            JSONObject player = new JSONObject();
            player.put("Nome", null);
            player.put("Istanza", null);

            // creare array carte del giocatore
            // se nullo imposto dei valori di default che vuol dire che il gioco deve ancora iniziare
            // Ottieni la carta del giocatore (assumendo che ogni giocatore ha solo una carta)
            Carta cartaPlayer = null;

            if (cartaPlayer != null) {
                // Creazione dell'oggetto JSON per la carta
                JSONObject cartaJSON = new JSONObject();
                cartaJSON.put("Valore", cartaPlayer.getValore());
                cartaJSON.put("Seme", cartaPlayer.getSeme().toString());
                cartaJSON.put("Attivata", cartaPlayer.getCartaEffettoAttivato()); // teoricamente sempre su false

                // Aggiungi l'oggetto cartaJSON al giocatore
                player.put("Carta", cartaJSON);
            } else {
                // Creazione dell'oggetto JSON per la carta
                JSONObject cartaJSON = new JSONObject();
                cartaJSON.put("Valore", 1);
                cartaJSON.put("Seme", SemeCarta.VERME.toString()); // metto un seme a caso
                cartaJSON.put("Attivata", true);

                // Aggiungi l'oggetto cartaJSON al giocatore
                player.put("Carta", cartaJSON);
            }

            player.put("Ruolo", "GIOCATORE");
            player.put("Vite", numeroPlayerVite);
            player.put("Vita-Extra", 0);
            player.put("PlayerRounds", 0);

            giocatoriList.put("Giocatore" + (i + 1), player);
        }
        partitaFinale.put("Giocatori", giocatoriList);
        torneoJSON.put("PartitaFinale", partitaFinale);
    }

    // carico una determinata partita in base al codice che gli passo
    public static ArrayList<Partita> leggiTorneoDaFile(int codiceTorneo)
    {
        try
        {
            if (torneiFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(torneiFile));

                // Ottieni l'array delle partite
                JSONArray torneoArray = (JSONArray) root.get("Tornei");

                // Cerca la partita con il codicePartita specificato
                for (Object torneoObject : torneoArray)
                {
                    JSONObject torneoJSON = (JSONObject) torneoObject;
                    int idTorneo = Integer.parseInt(torneoJSON.get("Id_Torneo").toString());
                    if (idTorneo == codiceTorneo)
                    {
                        return convertiJSONAPartitaTorneo(torneoJSON);
                    }
                }
            }
            else
            {
                System.out.println("[FILE-MANAGER] Non e presente il torneo che cerchi!");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return null; // Restituisci null se la partita non è stata trovata o ci sono errori
    }

    public static ArrayList<Partita> convertiJSONAPartitaTorneo(JSONObject torneoJSON)
    {
        ArrayList<Partita> partite = new ArrayList<Partita>();
        JSONObject partiteObject = (JSONObject) torneoJSON.get("Partite");

        // Adesso devo prendere i dati delle singole partite
        for(Object nomePartita : partiteObject.keySet())
        {
            // Prendo la mia singola partita
            JSONObject singolaPartitaJSON = (JSONObject) partiteObject.get(nomePartita);

            // Estrarre i dati dall'oggetto JSON e creare un oggetto Partita
            int currentGioocatorePos = Integer.parseInt(singolaPartitaJSON.get("CurrentGiocatore").toString()); // prendo giocatore attuale
            int posMazziere = Integer.parseInt(singolaPartitaJSON.get("PosMazziere").toString()); // prendo pos mazziere
            boolean isGameRunning = Boolean.parseBoolean(singolaPartitaJSON.get("isGameRunning").toString()); // prendo pos mazziere
            GameStatus gameStatus = GameStatus.valueOf((String) singolaPartitaJSON.get("Stato"));  // prendo stato
            GameType gameType = GameType.valueOf((String) singolaPartitaJSON.get("Tipo"));  // prendo stato
            int cDistaccoMazziere = Integer.parseInt(singolaPartitaJSON.get("cDistaccoMazziere").toString()); // prendo il distacco del mazziere
            boolean cartaGiaScambiata = Boolean.parseBoolean(singolaPartitaJSON.get("cartaGiaScambiata").toString()); // prendo se il player ha gia fatto lo scambio o no
            int currentRound = Integer.parseInt(singolaPartitaJSON.get("Round").toString()); // prendo il round attuale della partita


            JSONObject giocatoriObject = (JSONObject) singolaPartitaJSON.get("Giocatori");

            // Esempio: crea una nuova istanza di Partita con i dati estratti
            Partita singolaPartitaTorneo = new Partita(4); // imposto quanti giocatori ci sono
            singolaPartitaTorneo.setPartitaStatus(gameStatus); // impostiamo lo stato che prendiamo
            singolaPartitaTorneo.setCurrentGiocatorePos(currentGioocatorePos);
            singolaPartitaTorneo.setPosMazziere(posMazziere);
            singolaPartitaTorneo.setIsGameRunning(isGameRunning);
            singolaPartitaTorneo.setDistaccoMazziere(cDistaccoMazziere);
            singolaPartitaTorneo.setCartaGiaScambiata(cartaGiaScambiata);
            singolaPartitaTorneo.setCurrentRound(currentRound);
            singolaPartitaTorneo.setGameType(gameType);

            // Aggiungi giocatori alla partita
            for (Object giocatoreKey : giocatoriObject.keySet())
            {
                String nomeGiocatore = (String) giocatoreKey;
                JSONObject giocatoreJSON = (JSONObject) giocatoriObject.get(nomeGiocatore);

                String nomePlayer = giocatoreJSON.get("Nome").toString();
                int vitaExtra = Integer.parseInt(giocatoreJSON.get("Vita-Extra").toString());
                int vite = Integer.parseInt(giocatoreJSON.get("Vite").toString());
                String istanza = giocatoreJSON.get("Istanza").toString();
                RuoloGiocatore ruoloGiocatore = RuoloGiocatore.valueOf((String) giocatoreJSON.get("Ruolo"));
                int playerRounds = Integer.parseInt(giocatoreJSON.get("PlayerRounds").toString());

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
                giocatore.setPlayerRounds(playerRounds);

                System.out.println("Giocatore: " + giocatore.getNome() );

                JSONObject cartaObject = (JSONObject) giocatoreJSON.get("Carta"); // prendo la carta dal player
                Carta cartaPlayer = null;
                int valore = Integer.parseInt(cartaObject.get("Valore").toString());
                SemeCarta semeCarta = SemeCarta.valueOf((String) cartaObject.get("Seme"));
                boolean isEffettoCartaAttivato = Boolean.parseBoolean(cartaObject.get("Attivata").toString());

                Image cartaImage;
                cartaImage = new Image(FileManager.class.getResource("/Assets/Cards/" + semeCarta.toString() + "/" + semeCarta.toString() + valore + ".PNG").toString());

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

                cartaPlayer.setCartaEffettoAttivato(isEffettoCartaAttivato);
                cartaPlayer.setImage(cartaImage); // imposto la visuale della carta
                giocatore.setCarta(cartaPlayer);

                singolaPartitaTorneo.aggiungiGiocatore(giocatore);
            }

            // Aggiungi la partita alla lista
            partite.add(singolaPartitaTorneo);

        }

        return partite;
    }

    public static int getCurrentMatchTorneo(int codiceTorneo)
    {
        try
        {
            if (torneiFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(torneiFile));

                // Ottieni l'array delle partite
                JSONArray torneoArray = (JSONArray) root.get("Tornei");

                // Cerca la partita con il codicePartita specificato
                for (Object torneoObject : torneoArray)
                {
                    JSONObject torneoJSON = (JSONObject) torneoObject;
                    int idTorneo = Integer.parseInt(torneoJSON.get("Id_Torneo").toString());
                    if (idTorneo == codiceTorneo)
                    {
                        return Integer.parseInt(torneoJSON.get("CurrentMatch").toString());
                    }
                }
            }
            else
            {
                System.out.println("[FILE-MANAGER] Non e presente il torneo che cerchi!");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return 0; // Restituisci null se la partita non è stata trovata o ci sono errori
    }


    public static Partita getCurrentPartitaTorneo(int codiceTorneo, int currentMatch)
    {
        try
        {
            if (torneiFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(torneiFile));

                // Ottieni l'array delle partite
                JSONArray torneoArray = (JSONArray) root.get("Tornei");

                // Cerca la partita con il codicePartita specificato
                for (Object torneoObject : torneoArray)
                {
                    JSONObject torneoJSON = (JSONObject) torneoObject;
                    int idTorneo = Integer.parseInt(torneoJSON.get("Id_Torneo").toString());
                    if (idTorneo == codiceTorneo)
                    {
                        return convertiSpecificaPartitaTorneo(torneoJSON, currentMatch);
                    }
                }
            }
            else
            {
                System.out.println("[FILE-MANAGER] Non e presente il torneo che cerchi!");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return null; // Restituisci null se la partita non è stata trovata o ci sono errori
    }


    public static Partita convertiSpecificaPartitaTorneo(JSONObject torneoJSON, int currentMatch)
    {
        // Estrarre i dati dall'oggetto JSON generici del torneo
        int maxCarteNormali = Integer.parseInt(torneoJSON.get("MaxCarteNormali").toString()); // prendo il max carte normali
        int maxCarteSpeciali = Integer.parseInt(torneoJSON.get("MaxCarteSpeciali").toString()); // prendo il max carte speciali

        Partita p = convertiJSONAPartitaTorneo(torneoJSON).get(currentMatch);

        System.out.println("partita " + p.giocatori.get(0).getNome());

        p.setMaxCarteSpeciali(maxCarteSpeciali);
        p.setMaxCarteNormali(maxCarteNormali);

        return p;
    }

    public static boolean sovrascriviSalvataggiPartitaTorneo(Partita partitaToSave, int codiceTorneo, int currentMatch)
    {
        // adesso dobbiamo andare a prendere tra i tornei quello specifico del codice.
        // Una volta trovato andiamo a trovare il match specifico e andiamo ad assegnare tutti i dati della partita

        try
        {
            if (torneiFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(torneiFile));

                // Ottieni l'array delle partite
                JSONArray torneoArray = (JSONArray) root.get("Tornei");

                // Cerca la partita con il codicePartita specificato
                for (Object torneoObject : torneoArray)
                {
                    JSONObject torneoJSON = (JSONObject) torneoObject;
                    int idTorneo = Integer.parseInt(torneoJSON.get("Id_Torneo").toString());
                    if (idTorneo == codiceTorneo)
                    {
                        torneoJSON.put("CurrentMatch", currentMatch);
                        System.out.println("Ho trovato il mio torneo codice: " + codiceTorneo);
                        return cambiaDatiSingolaPartitaTorneo(torneoJSON, partitaToSave, currentMatch, root);
                    }
                }
            }
            else
            {
                System.out.println("[FILE-MANAGER] Non e presente il torneo che cerchi!");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    // TODO SISTEMARE E CONTROLLARE TUTTO
    public static boolean cambiaDatiSingolaPartitaTorneo(JSONObject torneoJSON, Partita partitaToSave, int currentMatch, JSONObject root)
    {
        JSONObject partiteObj = (JSONObject) torneoJSON.get("Partite");

        if (currentMatch >= 0 && currentMatch < 4) // compreso tra le 4 partite
        {
            // Adesso devo prendere i dati delle singole partite
            String gameName = "Partita"+(4 - currentMatch);
            JSONObject singolaPartitaJSON = (JSONObject) partiteObj.get(gameName);

            // Aggiorna lo stato della partita
            singolaPartitaJSON.put("Stato", partitaToSave.getPartitaStatus().toString()); // si
            singolaPartitaJSON.put("isGameRunning", partitaToSave.getIsGameRunning()); // si
            singolaPartitaJSON.put("Round", partitaToSave.getCurrentRound()); // si
            singolaPartitaJSON.put("CurrentGiocatore", partitaToSave.getCurrentGiocatorePos()); // si
            singolaPartitaJSON.put("PosMazziere", partitaToSave.getPosMazziere()); // si
            singolaPartitaJSON.put("cDistaccoMazziere", partitaToSave.getDistaccoMazziere()); // si
            singolaPartitaJSON.put("cartaGiaScambiata", partitaToSave.getCartaGiaScambiata()); // si



            // Bisogna vedere se abbiamo anche un vincitore nella partita
            IGiocatore giocatoreVincitore = partitaToSave.getVincitore();
            if(giocatoreVincitore != null)
            {
                JSONObject vincitoreJSON = (JSONObject) singolaPartitaJSON.get("Vincitore");
                vincitoreJSON.put("Nome", giocatoreVincitore.getNome());
                vincitoreJSON.put("Istanza", giocatoreVincitore.getClass().getSimpleName());
            }


            // Aggiorna le informazioni dei giocatori
            JSONObject giocatoriList = (JSONObject) singolaPartitaJSON.get("Giocatori");


            for (int i = 1; i <= partitaToSave.giocatori.size(); i++)
            {
                String nomeGiocatore = "Giocatore" + i;
                JSONObject giocatoreJSON = (JSONObject) giocatoriList.get(nomeGiocatore);

                IGiocatore nuovoGiocatore = partitaToSave.giocatori.get(i - 1);

                giocatoreJSON.put("Vita-Extra", nuovoGiocatore.getVitaExtra());
                giocatoreJSON.put("Ruolo", nuovoGiocatore.getRuolo().toString().toUpperCase());
                giocatoreJSON.put("Vita-Extra", nuovoGiocatore.getVitaExtra());
                giocatoreJSON.put("Vite", nuovoGiocatore.getVita());
                giocatoreJSON.put("PlayerRounds", nuovoGiocatore.getPlayerRounds());

                // Salva le informazioni della carta attuale
                JSONObject cartaJSON = new JSONObject();

                Carta cartaGiocatore = nuovoGiocatore.getCarta();

                if(cartaGiocatore == null)
                {
                    cartaGiocatore = new CartaNormale(1, SemeCarta.VERME); // Dato che il player e morto salvo una carta di default casuale
                    cartaGiocatore.setCartaEffettoAttivato(true);
                }


                cartaJSON.put("Valore", cartaGiocatore.getValore());
                cartaJSON.put("Seme", cartaGiocatore.getSeme().toString());
                cartaJSON.put("Attivata", cartaGiocatore.getCartaEffettoAttivato());

                giocatoreJSON.put("Carta", cartaJSON);

            }


            // Sovrascrivi il file con i dati aggiornati
            try (FileWriter fileWriter = new FileWriter(torneiFile))
            {
                fileWriter.write(root.toJSONString());
                System.out.println("Dati della partita" + currentMatch + " aggiornati con successo nel file JSON dei TORNEI.");
                return true; // Esci dal metodo una volta che la partita è stata trovata e aggiornata
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            System.out.println("Errore salvataggi partita: " + currentMatch + " nel file JSON dei TORNEI.");
            return false;

        }
        else
        {
            System.out.println("Indice partita non valido: " + currentMatch);
            return false;
        }
    }

    public static ArrayList<Carta> getPlayerCartePartitaTorneo(int codiceTorneo, int currentMatch)
    {
        try
        {
            if (torneiFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(torneiFile));

                // Ottieni l'array delle partite
                JSONArray torneoArray = (JSONArray) root.get("Tornei");

                // Cerca la partita con il codicePartita specificato
                for (Object torneoObject : torneoArray)
                {
                    JSONObject torneoJSON = (JSONObject) torneoObject;
                    int idTorneo = Integer.parseInt(torneoJSON.get("Id_Torneo").toString());
                    if (idTorneo == codiceTorneo)
                    {
                        System.out.println("Ho trovato il mio torneo codice: " + codiceTorneo);
                        return convertiCartePartitaTorneoJSON(torneoJSON, currentMatch);
                    }
                }
            }
            else
            {
                System.out.println("[FILE-MANAGER] Non e presente il torneo che cerchi!");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return null;
    }


    public static ArrayList<Carta> convertiCartePartitaTorneoJSON(JSONObject torneoJSON, int currentMatch) // prendo carte solo giocatori vivi
    {
        ArrayList<Carta> playerCards = new ArrayList<>();

        JSONObject partiteObj = (JSONObject) torneoJSON.get("Partite");

        if (currentMatch >= 0 && currentMatch < 4) // compreso tra le 5 partite
        {
            // Adesso devo prendere i dati delle singole partite
            String gameName = "Partita" + (4 - currentMatch);
            JSONObject singolaPartitaJSON = (JSONObject) partiteObj.get(gameName);

            JSONObject giocatoriObject = (JSONObject) singolaPartitaJSON.get("Giocatori");

            for (Object giocatoreKey : giocatoriObject.keySet())
            {
                String nomeGiocatore = (String) giocatoreKey;
                JSONObject giocatoreJSON = (JSONObject) giocatoriObject.get(nomeGiocatore);

                RuoloGiocatore ruoloGiocatore = RuoloGiocatore.valueOf((String) giocatoreJSON.get("Ruolo"));

                if(ruoloGiocatore != RuoloGiocatore.MORTO)
                {
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
                    System.out.println("Trovata carta giocatore vivo: " + cartaPlayer.toString());
                }

            }

            return playerCards;
        }

        System.out.println("Errore nel caricamento delle carte nel TORNEO!!");
        return null;
    }

    public static boolean aumentaCurrentMatchTorneo(int codiceTorneo, int currentMatch)
    {
        // adesso dobbiamo andare a prendere tra i tornei quello specifico del codice.
        // Una volta trovato andiamo a trovare il match specifico e andiamo ad assegnare tutti i dati della partita

        try
        {
            if (torneiFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(torneiFile));

                // Ottieni l'array delle partite
                JSONArray torneoArray = (JSONArray) root.get("Tornei");

                // Cerca la partita con il codicePartita specificato
                for (Object torneoObject : torneoArray)
                {
                    JSONObject torneoJSON = (JSONObject) torneoObject;
                    int idTorneo = Integer.parseInt(torneoJSON.get("Id_Torneo").toString());
                    if (idTorneo == codiceTorneo)
                    {
                        torneoJSON.put("CurrentMatch", currentMatch);

                        // Sovrascrivi il file con i dati aggiornati
                        try (FileWriter fileWriter = new FileWriter(torneiFile))
                        {
                            fileWriter.write(root.toJSONString());
                            System.out.println("Aumentato il current match della partita " + currentMatch + " nel file JSON dei TORNEI.");
                            return true; // Esci dal metodo una volta che la partita è stata trovata e aggiornata
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else
            {
                System.out.println("[FILE-MANAGER] Non e presente il torneo che cerchi!");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        System.out.println("[ERRORE] Salvataggi della partita " + currentMatch + " nel file JSON dei TORNEI FALLITA!!!");
        return false;
    }


    public static Partita getFinalePartitaTorneo(int codiceTorneo)
    {
        try
        {
            if (torneiFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(torneiFile));

                // Ottieni l'array delle partite
                JSONArray torneoArray = (JSONArray) root.get("Tornei");

                // Cerca la partita con il codicePartita specificato
                for (Object torneoObject : torneoArray)
                {
                    JSONObject torneoJSON = (JSONObject) torneoObject;
                    int idTorneo = Integer.parseInt(torneoJSON.get("Id_Torneo").toString());
                    if (idTorneo == codiceTorneo)
                    {
                        return restituisciFinalePartitaTorneo(torneoJSON);
                    }
                }
            }
            else
            {
                System.out.println("[FILE-MANAGER] Non e presente il torneo che cerchi!");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return null; // Restituisci null se la partita non è stata trovata o ci sono errori
    }

    public static Partita restituisciFinalePartitaTorneo(JSONObject torneoJSON)
    {
        // Estrarre i dati dall'oggetto JSON generici del torneo
        int maxCarteNormali = Integer.parseInt(torneoJSON.get("MaxCarteNormali").toString()); // prendo il max carte normali
        int maxCarteSpeciali = Integer.parseInt(torneoJSON.get("MaxCarteSpeciali").toString()); // prendo il max carte speciali

        Partita p = convertiJSONAPartitaFinaleTorneo(torneoJSON);

        System.out.println("partita " + p.giocatori.get(0).getNome());

        p.setMaxCarteSpeciali(maxCarteSpeciali);
        p.setMaxCarteNormali(maxCarteNormali);

        return p;
    }


    public static Partita convertiJSONAPartitaFinaleTorneo(JSONObject torneoJSON)
    {
        JSONObject partitaObject = (JSONObject) torneoJSON.get("PartitaFinale");

        // Estrarre i dati dall'oggetto JSON e creare un oggetto Partita
        int currentGioocatorePos = Integer.parseInt(partitaObject.get("CurrentGiocatore").toString()); // prendo giocatore attuale
        int posMazziere = Integer.parseInt(partitaObject.get("PosMazziere").toString()); // prendo pos mazziere
        boolean isGameRunning = Boolean.parseBoolean(partitaObject.get("isGameRunning").toString()); // prendo pos mazziere
        GameStatus gameStatus = GameStatus.valueOf((String) partitaObject.get("Stato"));  // prendo stato
        GameType gameType = GameType.valueOf((String) partitaObject.get("Tipo"));  // prendo stato
        int cDistaccoMazziere = Integer.parseInt(partitaObject.get("cDistaccoMazziere").toString()); // prendo il distacco del mazziere
        boolean cartaGiaScambiata = Boolean.parseBoolean(partitaObject.get("cartaGiaScambiata").toString()); // prendo se il player ha gia fatto lo scambio o no
        int currentRound = Integer.parseInt(partitaObject.get("Round").toString()); // prendo il round attuale della partita


        // Esempio: crea una nuova istanza di Partita con i dati estratti
        Partita partitaFinale = new Partita(4);
        partitaFinale.setPartitaStatus(gameStatus); // impostiamo lo stato che prendiamo
        partitaFinale.setCurrentGiocatorePos(currentGioocatorePos);
        partitaFinale.setPosMazziere(posMazziere);
        partitaFinale.setIsGameRunning(isGameRunning);
        partitaFinale.setDistaccoMazziere(cDistaccoMazziere);
        partitaFinale.setCartaGiaScambiata(cartaGiaScambiata);
        partitaFinale.setCurrentRound(currentRound);
        partitaFinale.setGameType(gameType);

        JSONObject giocatoriObject = (JSONObject) partitaObject.get("Giocatori");

        // Aggiungi giocatori alla partita
        for (Object giocatoreKey : giocatoriObject.keySet())
        {
            String nomeGiocatore = (String) giocatoreKey;
            JSONObject giocatoreJSON = (JSONObject) giocatoriObject.get(nomeGiocatore);

            String nomePlayer = giocatoreJSON.get("Nome").toString();
            int vitaExtra = Integer.parseInt(giocatoreJSON.get("Vita-Extra").toString());
            int vite = Integer.parseInt(giocatoreJSON.get("Vite").toString());
            String istanza = giocatoreJSON.get("Istanza").toString();
            RuoloGiocatore ruoloGiocatore = RuoloGiocatore.valueOf((String) giocatoreJSON.get("Ruolo"));
            int playerRounds = Integer.parseInt(giocatoreJSON.get("PlayerRounds").toString());

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
            giocatore.setPlayerRounds(playerRounds);

            System.out.println("Giocatore: " + giocatore.getNome() );

            JSONObject cartaObject = (JSONObject) giocatoreJSON.get("Carta"); // prendo la carta dal player
            Carta cartaPlayer = null;
            int valore = Integer.parseInt(cartaObject.get("Valore").toString());
            SemeCarta semeCarta = SemeCarta.valueOf((String) cartaObject.get("Seme"));
            boolean isEffettoCartaAttivato = Boolean.parseBoolean(cartaObject.get("Attivata").toString());

            Image cartaImage;
            cartaImage = new Image(FileManager.class.getResource("/Assets/Cards/" + semeCarta.toString() + "/" + semeCarta.toString() + valore + ".PNG").toString());

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

            cartaPlayer.setCartaEffettoAttivato(isEffettoCartaAttivato);
            cartaPlayer.setImage(cartaImage); // imposto la visuale della carta
            giocatore.setCarta(cartaPlayer);

            partitaFinale.aggiungiGiocatore(giocatore);
        }

        return partitaFinale;
    }

    public static boolean popolaPartitaFinaleTorneo(int codiceTorneo, Partita partitaCompleted)
    {
        try
        {
            if (torneiFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(torneiFile));

                // Ottieni l'array delle partite
                JSONArray torneoArray = (JSONArray) root.get("Tornei");

                // Cerca la partita con il codicePartita specificato
                for (Object torneoObject : torneoArray)
                {
                    JSONObject torneoJSON = (JSONObject) torneoObject;
                    int idTorneo = Integer.parseInt(torneoJSON.get("Id_Torneo").toString());
                    if (idTorneo == codiceTorneo)
                    {
                        return modificaGiocatoriFinaleTorneo(torneoJSON, partitaCompleted, root);
                    }
                }
            }
            else
            {
                System.out.println("[FILE-MANAGER] Non e presente il torneo che cerchi!");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    public static boolean modificaGiocatoriFinaleTorneo(JSONObject torneoJSON, Partita partitaCompleted, JSONObject root) {
        JSONObject partitaFinale = (JSONObject) torneoJSON.get("PartitaFinale");

        // Verifica se la partita finale esiste
        if (partitaFinale != null) {
            JSONObject giocatori = (JSONObject) partitaFinale.get("Giocatori");

            if (giocatori != null) {
                // Itera sui giocatori della partita finale
                for (Object giocatoreKey : giocatori.keySet()) {
                    String giocatoreName = (String) giocatoreKey;
                    JSONObject giocatore = (JSONObject) giocatori.get(giocatoreName);

                    // Verifica se il giocatore ha nome e istanza nulli
                    if (isGiocatoreSenzaNomeEistanza(giocatore)) {
                        // Popola i dati del vincitore nella partita finale
                        IGiocatore vincitore = partitaCompleted.getVincitore();
                        if (vincitore != null) {
                            giocatore.put("Istanza", vincitore.getClass().getSimpleName());
                            giocatore.put("Nome", vincitore.getNome());

                            // Salva le modifiche nel file
                            if (salvaModifiche(root)) {
                                System.out.println("Giocatore vincente della partita finale aggiornato con successo nel file JSON dei TORNEI.");
                                return true;
                            } else {
                                System.out.println("Errore durante il salvataggio delle modifiche nel file JSON dei TORNEI.");
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Errore aggiornamento giocatore vincente della partita finale nel file JSON dei TORNEI.");
        return false;
    }

    private static boolean isGiocatoreSenzaNomeEistanza(JSONObject giocatore) {
        return giocatore != null && giocatore.get("Nome") == null && giocatore.get("Istanza") == null;
    }

    private static boolean salvaModifiche(JSONObject root) {
        try (FileWriter fileWriter = new FileWriter(torneiFile)) {
            fileWriter.write(root.toJSONString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean sovrascriviSalvataggiPartitaFinaleTorneo(Partita partitaToSave, int codiceTorneo)
    {

        try
        {
            if (torneiFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(torneiFile));

                // Ottieni l'array delle partite
                JSONArray torneoArray = (JSONArray) root.get("Tornei");

                // Cerca la partita con il codicePartita specificato
                for (Object torneoObject : torneoArray)
                {
                    JSONObject torneoJSON = (JSONObject) torneoObject;
                    int idTorneo = Integer.parseInt(torneoJSON.get("Id_Torneo").toString());
                    if (idTorneo == codiceTorneo)
                    {
                        System.out.println("Ho trovato il mio torneo codice: " + codiceTorneo);
                        return cambiaDatiFinaleTorneo(torneoJSON, partitaToSave, root);
                    }
                }
            }
            else
            {
                System.out.println("[FILE-MANAGER] Non e presente il torneo che cerchi!");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    public static boolean cambiaDatiFinaleTorneo(JSONObject torneoJSON, Partita partitaToSave, JSONObject root)
    {
        JSONObject partitaFinaleJSON = (JSONObject) torneoJSON.get("PartitaFinale");

        // Aggiorna lo stato della partita
        partitaFinaleJSON.put("Stato", partitaToSave.getPartitaStatus().toString()); // si
        partitaFinaleJSON.put("isGameRunning", partitaToSave.getIsGameRunning()); // si
        partitaFinaleJSON.put("Round", partitaToSave.getCurrentRound()); // si
        partitaFinaleJSON.put("CurrentGiocatore", partitaToSave.getCurrentGiocatorePos()); // si
        partitaFinaleJSON.put("PosMazziere", partitaToSave.getPosMazziere()); // si
        partitaFinaleJSON.put("cDistaccoMazziere", partitaToSave.getDistaccoMazziere()); // si
        partitaFinaleJSON.put("cartaGiaScambiata", partitaToSave.getCartaGiaScambiata()); // si


        // Bisogna vedere se abbiamo anche un vincitore nella partita

        IGiocatore giocatoreVincitore = partitaToSave.getVincitore();
        if(giocatoreVincitore != null)
        {
            JSONObject vincitoreJSON = (JSONObject) partitaFinaleJSON.get("Vincitore");
            vincitoreJSON.put("Nome", giocatoreVincitore.getNome());
            vincitoreJSON.put("Istanza", giocatoreVincitore.getClass().getSimpleName());
        }

        // Aggiorna le informazioni dei giocatori
        JSONObject giocatoriList = (JSONObject) partitaFinaleJSON.get("Giocatori");


        for (int i = 1; i <= partitaToSave.giocatori.size(); i++)
        {
            String nomeGiocatore = "Giocatore" + i;
            JSONObject giocatoreJSON = (JSONObject) giocatoriList.get(nomeGiocatore);

            IGiocatore nuovoGiocatore = partitaToSave.giocatori.get(i - 1);

            giocatoreJSON.put("Vita-Extra", nuovoGiocatore.getVitaExtra());
            giocatoreJSON.put("Ruolo", nuovoGiocatore.getRuolo().toString().toUpperCase());
            giocatoreJSON.put("Vita-Extra", nuovoGiocatore.getVitaExtra());
            giocatoreJSON.put("Vite", nuovoGiocatore.getVita());
            giocatoreJSON.put("PlayerRounds", nuovoGiocatore.getPlayerRounds());

            // Salva le informazioni della carta attuale
            JSONObject cartaJSON = new JSONObject();

            Carta cartaGiocatore = nuovoGiocatore.getCarta();

            if(cartaGiocatore == null)
            {
                cartaGiocatore = new CartaNormale(1, SemeCarta.VERME); // Dato che il player e morto salvo una carta di default casuale
                cartaGiocatore.setCartaEffettoAttivato(true);
            }


            cartaJSON.put("Valore", cartaGiocatore.getValore());
            cartaJSON.put("Seme", cartaGiocatore.getSeme().toString());
            cartaJSON.put("Attivata", cartaGiocatore.getCartaEffettoAttivato());

            giocatoreJSON.put("Carta", cartaJSON);

        }

        // Sovrascrivi il file con i dati aggiornati
        try (FileWriter fileWriter = new FileWriter(torneiFile))
        {
            fileWriter.write(root.toJSONString());
            System.out.println("Finale aggiornata con successo nel file JSON dei TORNEI.");
            return true; // Esci dal metodo una volta che la partita è stata trovata e aggiornata
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Errore salvataggi partita finale nel file JSON dei TORNEI.");
        return false;
    }


    public static ArrayList<Carta> convertiCartePartitaFinaleTorneoJSON(JSONObject torneoJSON) // prendo carte solo giocatori vivi nel torneo finale
    {
        ArrayList<Carta> playerCards = new ArrayList<>();

        JSONObject partiteObj = (JSONObject) torneoJSON.get("PartitaFinale");
        JSONObject giocatoriObject = (JSONObject) partiteObj.get("Giocatori");

        for (Object giocatoreKey : giocatoriObject.keySet())
        {
            String nomeGiocatore = (String) giocatoreKey;
            JSONObject giocatoreJSON = (JSONObject) giocatoriObject.get(nomeGiocatore);

            RuoloGiocatore ruoloGiocatore = RuoloGiocatore.valueOf((String) giocatoreJSON.get("Ruolo"));

            if(ruoloGiocatore != RuoloGiocatore.MORTO)
            {
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
                System.out.println("Trovata carta giocatore vivo: " + cartaPlayer.toString());
            }

        }

        return playerCards;
    }


    public static ArrayList<Carta> getPlayerCartePartitaTorneoFinale(int codiceTorneo)
    {
        try
        {
            if (torneiFile.exists())
            {
                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(new FileReader(torneiFile));

                // Ottieni l'array delle partite
                JSONArray torneoArray = (JSONArray) root.get("Tornei");

                // Cerca la partita con il codicePartita specificato
                for (Object torneoObject : torneoArray)
                {
                    JSONObject torneoJSON = (JSONObject) torneoObject;
                    int idTorneo = Integer.parseInt(torneoJSON.get("Id_Torneo").toString());
                    if (idTorneo == codiceTorneo)
                    {
                        System.out.println("Ho trovato il mio torneo codice: " + codiceTorneo);
                        return convertiCartePartitaFinaleTorneoJSON(torneoJSON);
                    }
                }
            }
            else
            {
                System.out.println("[FILE-MANAGER] Non e presente il torneo che cerchi!");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    // endregion

}

