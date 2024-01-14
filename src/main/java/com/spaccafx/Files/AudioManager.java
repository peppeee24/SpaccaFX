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
URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/ButtonClick.wav");
File sound =new File(risorsa.toURI());

            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore nella riproduzione del suono bottone");
        }
        //}
    }


    public static void distribuisciCarteSuono () {
        //  if(suoniON.isSelected()) {
        try {
            URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/distribuisciCarte.wav");
            File sound =new File(risorsa.toURI());
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono distribuisci carte");
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
            System.out.println("Errore nella riproduzione del suono giraCarte");
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
            System.out.println("Errore nella riproduzione del suono vittoria");
        }
        // }
    }

    public static void dadoSuono () {
        //   if(suoniON.isSelected()) {
        try {
            URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/dice2.wav");
            File sound =new File(risorsa.toURI());
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono dado" + e.getMessage());
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
            System.out.println("Errore nella riproduzione del suono errore");
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
            System.out.println("Errore nella riproduzione del suono prendete");
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
            System.out.println("Errore nella riproduzione del suono vita UP");
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
            System.out.println("Errore nella riproduzione del suono vita down");
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
            System.out.println("Errore nella riproduzione del suono imprevisto");
        }
        // }
    }
    public static void probabilitaSuono () {
        //   if(suoniON.isSelected()) {
        try {
            URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/Game/prob2.wav");
            File sound =new File(risorsa.toURI());
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono probabilità" + e.getMessage());
        }
        // }
    }



}
