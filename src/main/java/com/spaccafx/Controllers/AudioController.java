package com.spaccafx.Controllers;

import com.spaccafx.Files.AudioManager;
import com.spaccafx.Spacca;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
/*
 * If you get "cannot access class com.sun.glass.utils.NativeLibLoader" exception you may need to
 * add a VM argument: --add-modules javafx.controls,javafx.media as explained here:
 * https://stackoverflow.com/questions/53237287/module-error-when-running-javafx-media-application
 */


// https://youtu.be/-D2OIekCKes



public class AudioController {

    @FXML
    Tab tabVolume, tabRiconoscimenti;
    @FXML
    Slider volumeSlider;

    @FXML
    ImageView soundImage;
    @FXML
    static MediaPlayer player, player2;

    @FXML
    Button musicaONButton,musicaONButton2, musicaOFFButton,musicaOFFButton2;

    private ShareData shareData; // Aggiungi un riferimento all'istanza di ShareData

    public void setShareData(ShareData shareData) {
        this.shareData = shareData;
    }



    public void initialize() throws URISyntaxException {

        ShareData.getInstance().setAudioController(this); // gli passo classe partitacontroller
        //  ShareData.getInstance().set(this.P);
        playerSetting();
        playerSetting2();
        //   playAudio();

// Inizializza l'istanza di ShareData e setta se stesso come AudioController


        if (volumeSlider != null) {
            volumeSlider.setValue(player.getVolume() * 100);

            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    player.setVolume(volumeSlider.getValue() / 100);
                    player2.setVolume(volumeSlider.getValue() / 100);
                }
            });
            ShareData shareData = ShareData.getInstance();
            shareData.setAudioController(this);

        } else {
            System.err.println("Lo slider del volume non Ã¨ stato inizializzato correttamente.");
        }

        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                resetMedia(); // Riavvia la canzone dall'inizio
                playAudio(); // Avvia la riproduzione
            }
        });

    }



    public  void playerSetting() throws URISyntaxException {
        URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/BackgroundMusic/ColonnaSonora.wav");
        File sound =new File(risorsa.toURI());
        Media media = new Media(risorsa.toString());
        player = new MediaPlayer(media);
        player.setOnError(() -> System.out.println(media.getError().toString()));

    }

    public  void playerSetting2() throws URISyntaxException {
        URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/BackgroundMusic/lounge.wav");
        File sound =new File(risorsa.toURI());
        Media media2 = new Media(risorsa.toString());
        player2 = new MediaPlayer(media2);
        player2.setOnError(() -> System.out.println(media2.getError().toString()));

    }

    //play audio
    public  void playAudio()
    {
        player.play();
    }
    public  void playAudio2()
    {
        player2.play();
    }

    //pause audio
    public  void pauseAudio()
    {
        if (player.getStatus().equals(Status.PAUSED) || player2.getStatus().equals(Status.PAUSED) )
        {
            System.out.println("audio is already paused");
            return;
        }
        player.pause();
        player2.pause();
    }

    public  void pauseAudio2()
    {
        if ( player2.getStatus().equals(Status.PAUSED) )
        {
            System.out.println("audio is already paused");
            return;
        }

        player2.pause();
    }


    public void resetMedia() {
        player.seek(Duration.seconds(0));
    }


    public void resetMedia2() {
        player2.seek(Duration.seconds(0));
    }



    public void musicaPlayButton(ActionEvent actionEvent) throws IOException {
        AudioManager.bottoneSuono();
        playAudio();

    }

    public void musicaPlayButton2(ActionEvent actionEvent) throws IOException {
        AudioManager.bottoneSuono();
        playAudio2();

    }

    public void musicaStopButton(ActionEvent actionEvent) throws IOException {
        AudioManager.bottoneSuono();
        pauseAudio();

    }

    public void musicaStopButton2(ActionEvent actionEvent) throws IOException {
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