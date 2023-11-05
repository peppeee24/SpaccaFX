package com.spaccafx.Files;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class AudioManager {

    public AudioManager(){

    }


    public static void bottoneSuono () {
        //   if(suoniON.isSelected()) {

        try {

            File sound = new File("/Users/federicosgambelluri/IdeaProjects/SpaccaFX/src/main/resources/Assets/Game/Environment/Sounds/UI/ButtonClick.wav");
        //    File sound = new File("/Assets/Game/Environment/Sounds/UI/ButtonClick.wav");

         //  System.out.println(sound.getAbsolutePath());
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
            File sound = new File("/Users/federicosgambelluri/IdeaProjects/SpaccaFX/src/main/resources/Assets/Game/Environment/Sounds/Game/distribuisciCarte.wav");
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
            File sound = new File("/Users/federicosgambelluri/IdeaProjects/SpaccaFX/src/main/resources/Assets/Game/Environment/Sounds/Game/giraCarte.wav");
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
            File sound = new File("/Users/federicosgambelluri/IdeaProjects/SpaccaFX/src/main/resources/Assets/Game/Environment/Sounds/Game/Victory.wav");
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
            File sound = new File("/Users/federicosgambelluri/IdeaProjects/SpaccaFX/src/main/resources/Assets/Game/Environment/Sounds/Game/dice.wav");
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
            File sound = new File("/Users/federicosgambelluri/IdeaProjects/SpaccaFX/src/main/resources/Assets/Game/Environment/Sounds/Game/errore.wav");
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
            File sound = new File("/Users/federicosgambelluri/IdeaProjects/SpaccaFX/src/main/resources/Assets/Game/Environment/Sounds/Game/perdente.wav");
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
            File sound = new File("/Users/federicosgambelluri/IdeaProjects/SpaccaFX/src/main/resources/Assets/Game/Environment/Sounds/Game/lifeUP.wav");
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
            File sound = new File("/Users/federicosgambelluri/IdeaProjects/SpaccaFX/src/main/resources/Assets/Game/Environment/Sounds/Game/lifeDOWN.wav");
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (Exception e) {
            System.out.println("Errore nella riproduzione del suono");
        }
        // }
    }

    // TODO aggiungere suoni imprevisto e probabilità
}
