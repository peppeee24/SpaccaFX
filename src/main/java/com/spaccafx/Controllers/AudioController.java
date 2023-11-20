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
    Slider volumeSlider;
    CheckBox suoniOFF, musicaOFF, musicaON;
    ImageView soundImage;
    MediaPlayer player;



    public void initialize() throws URISyntaxException {
        suoniOFF = new CheckBox();
        musicaOFF = new CheckBox();
        musicaON = new CheckBox();
       // volumeSlider =new Slider();
        playerSetting();
        //   playAudio();
        if (volumeSlider != null) {
            volumeSlider.setValue(player.getVolume() * 100);

            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    player.setVolume(volumeSlider.getValue() / 100);
                }
            });
        } else {
            System.err.println("Lo slider del volume non è stato inizializzato correttamente.");
        }
    }



    public void playerSetting() throws URISyntaxException {
        URL risorsa= AudioManager.class.getResource("/Assets/Game/Environment/Sounds/BackgroundMusic/ColonnaSonora.wav");
        File sound =new File(risorsa.toURI());
        Media media = new Media(risorsa.toString());
        player = new MediaPlayer(media);
        player.setOnError(() -> System.out.println(media.getError().toString()));

    }

    //play audio
    public void playAudio()
    {
        player.play();
    }

    //pause audio
    public  void pauseAudio()
    {
        if (player.getStatus().equals(Status.PAUSED))
        {
            System.out.println("audio is already paused");
            return;
        }
        player.pause();
    }

    public void resetMedia() {
        player.seek(Duration.seconds(0));
    }



    public void disattivaSuoni(ActionEvent event) { // Rivedere come gestire
        if (!suoniOFF.isSelected());{
            // TODO da capire come implementare
            AudioManager.disattivaSuoni();
            System.out.println("Hai selezionato il checkbox per disattivare i suoni");
        }


    }


    public void disattivaMusica(ActionEvent event) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (!musicaOFF.isSelected() == true){
            pauseAudio();
            System.out.println("Ho premuto il checkbox per disattivare l'audio");
        }

    }

    public void attivaMusica(ActionEvent event) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (!musicaON.isSelected() == true){
            resetMedia();
            playAudio();
            System.out.println("Ho premuto il checkbox per attivare l'audio");
        }

    }



    public void indietro(MouseEvent mouseEvent) throws IOException {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }

}
