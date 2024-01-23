package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Spacca;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class AudioController {

    @FXML
    private Slider volumeSlider;

    @FXML
    private AudioClip audioClip1, audioClip2;

    @FXML
    private Button musicaONButton, musicaONButton2, musicaOFFButton, musicaOFFButton2;

    @FXML
    private ImageView soundImage;

    private ShareData shareData;

    public void setShareData(ShareData shareData) {
        this.shareData = shareData;
    }

    public void initialize() throws URISyntaxException {
        ShareData.getInstance().setAudioController(this);

        audioClip1 = audioClipSetting("/Assets/Game/Environment/Sounds/BackgroundMusic/ColonnaSonora.wav");
        audioClip2 = audioClipSetting("/Assets/Game/Environment/Sounds/BackgroundMusic/lounge.wav");

        setupVolumeSlider();
    }

    private AudioClip audioClipSetting(String resourcePath) throws URISyntaxException {
        try {
            return new AudioClip(AudioManager.class.getResource(resourcePath).toURI().toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore nella riproduzione del suono");
            return null;
        }
    }

    private void setupVolumeSlider() {
        if (volumeSlider != null) {
            // Set initial values
            volumeSlider.setValue(audioClip1.getVolume() * 100);

            // Bind audioClip1 to the volumeSlider
            volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                setVolume(audioClip1, newValue.doubleValue());
            });

            // Additional binding for audioClip2
            volumeSlider.setValue(audioClip2.getVolume() * 100);

            // Bind audioClip2 to the volumeSlider
            volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                setVolume(audioClip2, newValue.doubleValue());
            });
        } else {
            System.err.println("Lo slider del volume non è stato inizializzato correttamente.");
        }
    }

    private void setVolume(AudioClip audioClip, double value) {
        if (audioClip != null) {
            audioClip.setVolume(value / 100);
        } else {
            System.err.println("AudioClip is null");
        }
    }


    public void playAudio1() {
        if (audioClip1 != null) {
            audioClip1.play();
        } else {
            System.err.println("AudioClip1 is null");
        }
    }

    public void playAudio2() {
        if (audioClip2 != null) {
            audioClip2.play();
        } else {
            System.err.println("AudioClip2 is null");
        }
    }

    public void pauseAudio1() {
        if (audioClip1 != null) {
            audioClip1.stop();
        } else {
            System.err.println("AudioClip is null");
        }
    }

    public void pauseAudio2() {
        if (audioClip2 != null) {
            audioClip2.stop();
        } else {
            System.err.println("AudioClip is null");
        }
    }

    public void musicaPlayButton() throws IOException {
        AudioManager.bottoneSuono();
        playAudio1();
    }

    public void musicaPlayButton2() throws IOException {
        AudioManager.bottoneSuono();
        playAudio2();
    }

    public void musicaStopButton() throws IOException {
        AudioManager.bottoneSuono();
        pauseAudio1();
    }

    public void musicaStopButton2() throws IOException {
        AudioManager.bottoneSuono();
        pauseAudio2();
    }

    public void indietro(MouseEvent mouseEvent) throws IOException {
        AudioManager.bottoneSuono();
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }
}