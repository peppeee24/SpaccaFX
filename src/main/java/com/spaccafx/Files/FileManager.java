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
    public static File torneiFile = new File("Tornei.json"); // unico file con più partite

    public static void creaPartitaSuFile(int codicePartita, int passwordPartita, ArrayList<IGiocatore> giocatori, GameType gameType, GameStatus status, int maxCarteNormali, int maxCarteSpeciali, int numeroPlayerVite)
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
            nuovaPartita.put("cartaGiaScambiata", false);
            nuovaPartita.put("maxCarteNormali", maxCarteNormali);
            nuovaPartita.put("maxCarteSpeciali", maxCarteSpeciali);
            nuovaPartita.put("numeroPlayerVite", numeroPlayerVite);

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

            // TODO DOVREBBE CREARE 5 PARTITE LA CUI 5 E VUOTA PERCHE CI ANDRANNO POI I VINCITORI.
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


    // endregion

}

