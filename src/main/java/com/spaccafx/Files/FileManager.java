package com.spaccafx.Files;

import com.spaccafx.Manager.Partita;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager
{

    public FileManager()
    {

    }


    public static void salvaInformazioniPartita(Partita partitaDaSalvare)
    {
        // cerchiamo la partita nel file, se esiste il codice sovrascrive i dati
        // Scrivi le informazioni su un file

        File fileSalvataggi = new File( "Partite.txt"); // unico file con piu partite

        System.out.println("Path salvataggi partita: " + fileSalvataggi.getPath());

        try
        {
            if (fileSalvataggi.exists())
            {
                // Il file esiste, sovraisci i dati
                FileWriter writer = new FileWriter(fileSalvataggi);







                writer.close();
            } else
            {
                // Il file non esiste, crea un nuovo file
                fileSalvataggi.createNewFile();
                FileWriter writer = new FileWriter(fileSalvataggi);



                writer.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            // Gestisci l'eccezione in modo appropriato
        }




        /*
        // Ottieni le informazioni della partita
        List<IGiocatore> giocatori = P.getListaGiocatori();
        String codicePartita = P.getCodicePartita();

        // Crea un oggetto PartitaInfo
        PartitaInfo partitaInfo = new PartitaInfo(giocatori, codicePartita);

        // Serializza l'oggetto PartitaInfo in una stringa JSON o un altro formato di tua scelta
        String informazioniPartitaJSON = convertiInJSON(partitaInfo);

         */


    }

    // Questo metodo converte un oggetto PartitaInfo in una stringa JSON (puoi usare una libreria come Gson)
    /*private String convertiInJSON(PartitaInfo partitaInfo)
    {
        // Implementa la logica per la conversione in JSON
        // Esempio con Gson:
        // Gson gson = new Gson();
        // return gson.toJson(partitaInfo);

        // Ritorna una stringa JSON di esempio per ora
        return "{"giocatori": [...], "codicePartita": "..."}";
    }

     */
}

