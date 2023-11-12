package com.spaccafx.Files;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class AudioManager {

    public AudioManager(){

    }


    public static void bottoneSuono () {
        //   if(suoniON.isSelected()) {

        try {
URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/UI/ButtonClick.wav");
File sound =new File(risorsa.toURI());

            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore nella riproduzione del suono");
        }
        //}
    }


    public static void distribuisciCarteSuono () {
        //  if(suoniON.isSelected()) {
        try {
            URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/distribusciCarte.wav");
            File sound =new File(risorsa.toURI());
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono");
        }
        // }
    }

    public static void giraCarteSuono () {
        // if(suoniON.isSelected()) {
        try {
            URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/giraCarte.wav");
            File sound =new File(risorsa.toURI());
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono");
        }
        // }
    }

    public static void vittoriaSuono () {
        //   if(suoniON.isSelected()) {
        try {
            URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/Victory.wav");
            File sound =new File(risorsa.toURI());
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono");
        }
        // }
    }

    public static void dadoSuono () {
        //   if(suoniON.isSelected()) {
        try {
            URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/dice.wav");
            File sound =new File(risorsa.toURI());
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono");
        }
        // }
    }

    public static void erroreSuono () {
        //   if(suoniON.isSelected()) {
        try {
            URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/errore.wav");
            File sound =new File(risorsa.toURI());
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono");
        }
        // }
    }

    public static void perdenteSuono () {
        //   if(suoniON.isSelected()) {
        try {
            URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/perdente.wav");
            File sound =new File(risorsa.toURI());
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono");
        }
        // }
    }

    public static void vitaUPSuono () {
        //   if(suoniON.isSelected()) {
        try {
            URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/lifeUP.wav");
            File sound =new File(risorsa.toURI());
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono");
        }
        // }
    }

    public static void vitaDownSuono () {
        //   if(suoniON.isSelected()) {
        try {
            URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/lifeDOWN.wav");
            File sound =new File(risorsa.toURI());
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono");
        }
        // }
    }

    public static void imprevistoSuono () {
        //   if(suoniON.isSelected()) {
        try {
            URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/imprevisto.wav");
            File sound =new File(risorsa.toURI());
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono");
        }
        // }
    }
    public static void probabilitaSuono () {
        //   if(suoniON.isSelected()) {
        try {
            URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/probabilita.wav");
            File sound =new File(risorsa.toURI());
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono");
        }
        // }
    }

    public static void disattivaSuoni(){


    }





}
