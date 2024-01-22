package com.spaccafx.Files;

import javafx.scene.control.Alert;


import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ResourceLoader
{
    public static Image loadImage(String imagePath)
    {
        System.out.println("Percorso dell'immagine: " + imagePath);
        InputStream risorsa = ResourceLoader.class.getResourceAsStream(imagePath);

        try
        {
            if (risorsa != null)
            {
                // Converte l'InputStream in un array di byte
                byte[] audioBytes = risorsa.readAllBytes();

                // Crea un ByteArrayInputStream dal array di byte
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioBytes);

                // L'immagine è stata trovata, puoi utilizzare inputStream
                Image cartaImage = new Image(byteArrayInputStream);
            }
            else
            {
                System.out.println("L'immagine non è stata trovata: " + imagePath);
                Alert test = new Alert(Alert.AlertType.ERROR);
                test.setContentText("URL dell'immagine non trovato: " + imagePath);
                test.show();
            }
        }
        catch (Exception e)
        {
            System.out.println("L'immagine non è stata trovata: " + imagePath);
            Alert test = new Alert(Alert.AlertType.ERROR);
            test.setContentText("URL dell'immagine non trovato: " + imagePath);
            test.show();
        }

        return null;

    }
}
