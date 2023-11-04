package com.spaccafx.Files;

import com.spaccafx.Interface.IGiocatore;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager
{
    static File partiteFile = new File("Partite.json"); // unico file con più partite

    public static void salvaInformazioniPartita(int codicePartita, int passwordPartita, ArrayList<IGiocatore> giocatori) {
        try {
            JSONObject obj = new JSONObject();

            obj.put("ID_Partita", codicePartita);
            obj.put("Password", passwordPartita);

            for (IGiocatore giocatore : giocatori) {
                obj.put("Nome", giocatore.getNome());
                obj.put("Istanza", giocatore.getClass().getSimpleName());
            }

            FileWriter fileWriter = new FileWriter(partiteFile, true);
            fileWriter.close();



        } catch (IOException e) {
            e.printStackTrace();
            // Gestisci l'eccezione in modo appropriato
        }
    }
}

