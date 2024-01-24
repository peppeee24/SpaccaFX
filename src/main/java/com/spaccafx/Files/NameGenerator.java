package com.spaccafx.Files;

import com.spaccafx.Interface.IGiocatore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NameGenerator
{
    public static String generaNomeBotEasy()
    { //generiamo nomi per i bot(es: user1234)
        int numeroBot=(int)(1 + (Math.random() * 9999));
        String nomeBot="EzBot_" + numeroBot;
        return nomeBot;
    }

    public static String generaNomeBotAdvanced() //generiamo nomi per i bot(es: user1234)
    {
        int numeroBot=(int)(1 + (Math.random() * 9999));
        String nomeBot="AdvBot_" + numeroBot;
        return nomeBot;
    }

    public static boolean controllaNomiDiversi(ArrayList<IGiocatore> listaGiocatori)
    {
        // Creiamo un set per memorizzare i nomi unici
        Set<String> nomiUnici = new HashSet<>();

        // Iteriamo attraverso gli oggetti IGiocatore nell'ArrayList
        for (IGiocatore giocatore : listaGiocatori) {
            // Otteniamo il nome del giocatore
            String nomeGiocatore = giocatore.getNome();

            // Verifichiamo se il nome è null o vuoto o composto solo da spazi
            if (nomeGiocatore == null || nomeGiocatore.trim().isEmpty()) {
                // Se il nome non è valido, restituisci false
                return false;
            }

            // Verifichiamo se il nome è già presente nel set
            if (nomiUnici.contains(nomeGiocatore)) {
                // Se il nome è già presente, restituisci false
                return false;
            } else {
                // Aggiungiamo il nome al set
                nomiUnici.add(nomeGiocatore);
            }
        }

        // Se siamo arrivati a questo punto, significa che tutti i nomi sono diversi e validi
        return true;
    }

    public static void stampaNomi(ArrayList<IGiocatore> listaGiocatori)
    {
        for (IGiocatore giocatore : listaGiocatori)
        {
            System.out.println("Giocatore: " + giocatore.getNome());
            System.out.println("Istanza: " + giocatore.getClass().getSimpleName());
        }
    }
}
