package com.spaccafx.Controllers;

import com.spaccafx.Spacca;
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

import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AudioController {

    @FXML
    Tab tabVolume, tabRiconoscimenti;
    Slider volumeSlider;
    CheckBox suoniON;
    ImageView soundImage;


    //TODO impostare tutti i parametri per i suoni
    // https://youtu.be/sLQOP1DvbXo
    // Video utile per il volume


    public void disattivaSuoni(ActionEvent event) {

      /*  if(suoniON.isSelected()) {

        }
        else {
           soundImage.setVisible(false);
        }

       */
    }

    public void salvaAudio(ActionEvent actionEvent) throws IOException {
        // TODO impostare valori su PC

        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }



    public void bottoneSuono () {
     //   if(suoniON.isSelected()) {

            try {
                File sound = new File(getClass().getResource("/Assets/Game/Environment/Sounds/UI/ButtonClick.mp3").toString());
                Clip c = AudioSystem.getClip();
                c.open(AudioSystem.getAudioInputStream(sound));
                c.start();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Errore nella riproduzione del suono");
            }
        //}
    }

    public void distribuisciCarteSuono () {
      //  if(suoniON.isSelected()) {
            try {
                File sound = new File("Sounds/Game/distribuisciCarte.wav");
                Clip c = AudioSystem.getClip();
                c.open(AudioSystem.getAudioInputStream(sound));
                c.start();
            } catch (Exception e) {
                System.out.println("Errore nella riproduzione del suono");
            }
       // }
    }

    public void giraCarteSuono () {
       // if(suoniON.isSelected()) {
            try {
                File sound = new File("Sounds/Game/giraCarte.wav");
                Clip c = AudioSystem.getClip();
                c.open(AudioSystem.getAudioInputStream(sound));
                c.start();
            } catch (Exception e) {
                System.out.println("Errore nella riproduzione del suono");
            }
       // }
    }

    public void vittoriaSuono () {
     //   if(suoniON.isSelected()) {
            try {
                File sound = new File("Sounds/game/Victory.wav");
                Clip c = AudioSystem.getClip();
                c.open(AudioSystem.getAudioInputStream(sound));
                c.start();
            } catch (Exception e) {
                System.out.println("Errore nella riproduzione del suono");
            }
       // }
    }


    public void volume(ActionEvent actionEvent) throws IOException {
        // TODO impostare valori su PC

    }



        public void indietro(MouseEvent mouseEvent) throws IOException {
        FXMLLoader Indietro = new FXMLLoader(Spacca.class.getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(Indietro.load());
        stage.setScene(scene);
        stage.show();
    }

}


