package com.spaccafx.Files;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class AudioManager {

    public AudioManager(){

    }

    public static void bottoneSuono() {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/ButtonClick.wav");
    }

    public static void distribuisciCarteSuono() {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/distribuisciCarte.wav");
    }

    public static void giraCarteSuono() {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/giraCarte.wav");
    }

    public static void vittoriaSuono () {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/Victory.wav");
    }

    public static void dadoSuono () {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/dice.wav");
    }

    public static void erroreSuono () {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/errore.wav");
    }

    public static void perdenteSuono () {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/perdente.wav");
    }

    public static void vitaUPSuono () {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/lifeUP.wav");
    }

    public static void vitaDownSuono () {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/lifeDOWN.wav");
    }

    public static void imprevistoSuono () {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/imprevisto.wav");
    }

    public static void leaderboardSuono () {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/lead.wav");
    }
    public static void probabilitaSuono () {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/probabilita.wav");
    }

    public static void warningSuono () {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/warningSound.wav");
    }

    public static void confirmSuono () {
        ResourceLoader.loadAudio("/Assets/Game/Environment/Sounds/Game/confirmSound.wav");
    }
}
