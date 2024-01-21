package com.spaccafx.ExternalApps;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class SpaccaTGBot extends TelegramLongPollingBot
{
    // TELEGRAM EMOJI
    // https://core.telegram.org/blackberry/chat-emoji
    private String grafico_emoji = EmojiParser.parseToUnicode("\uD83D\uDCCA");
    private String corona_emoji = EmojiParser.parseToUnicode("\uD83D\uDC51");
    private String riavvio_emoji = EmojiParser.parseToUnicode("\u26A0");
    private String clessidra_emoji = EmojiParser.parseToUnicode("\u23F3");
    private String centro_emoji = EmojiParser.parseToUnicode("\uD83C\uDFAF");
    private String paper_emoji = EmojiParser.parseToUnicode("\uD83D\uDCDD");
    private String italia_emoji = EmojiParser.parseToUnicode("\uD83C\uDDEE\uD83C\uDDF9");
    private String libro_emoji = EmojiParser.parseToUnicode("\uD83D\uDCDA");



    public SpaccaTGBot()
    {
        //messaggioRiavvio();
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        System.out.println(update.getMessage().getText());
        String command = update.getMessage().getText();
    }

    public void messaggioRiavvio()
    {
        SendMessage snd = new SendMessage();
        snd.setChatId("-1001985772857");
        snd.setText(riavvio_emoji + " [DEBUG] - *NON FARE CASO A QUESTO MESSAGGIO* "); // capisco quante instanze crea

        try
        {
            // We want to test that we actually sent out this message with the contents "Hello!"
            execute(snd);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }


    }

    public void messaggioVincitorePartita(int codicePartita, String vincitore)
    {
        SendMessage snd = new SendMessage();
        snd.setChatId("-1001985772857");


        switch ((int)(0 + Math.random() * (3 - 1)))
        {
            case 0:
                snd.setText(corona_emoji + " [EVENTO] (PARTITA - " + codicePartita + " ) Acclamate tutti il vincitore " + vincitore + ". Grazie per aver giocato!"); break;
            case 1:
                snd.setText(corona_emoji +" [EVENTO] (PARTITA - " + codicePartita + " ) Congratulazioni " + vincitore + " per la vittoria! Buon proseguimento!"); break;
            case 2:
                snd.setText(corona_emoji + " [EVENTO] (PARTITA - " + codicePartita + " ) E stato troppo facile per " + vincitore + " vincere.. Ottimo lavoro!"); break;
            case 3:
                snd.setText(corona_emoji + " [EVENTO] (PARTITA - " + codicePartita + " ) Che giocate " + vincitore + "! Ottima vittoria!!"); break;
            default:
                snd.setText(corona_emoji + " [EVENTO] (PARTITA - " + codicePartita + " ) Belle mosse: " + vincitore + ". Buon proseguimento!"); break;
        }

        try
        {
            // We want to test that we actually sent out this message with the contents "Hello!"
            execute(snd);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    public void messaggioVincitorePartitaTorneo(int codiceTorneo, int currentMatch, String vincitore)
    {
        SendMessage snd = new SendMessage();
        snd.setChatId("-1001985772857");


        switch ((int)(0 + Math.random() * (3 - 1)))
        {
            case 0:
                snd.setText(clessidra_emoji + " [EVENTO] (TORNEO - " + codiceTorneo + " ) ( MATCH: "+ currentMatch + "/4 ) Acclamate tutti il vincitore " + vincitore + ". Grazie per aver giocato!"); break;
            case 1:
                snd.setText(clessidra_emoji +" [EVENTO] (TORNEO - " + codiceTorneo + " ) ( MATCH: " + currentMatch + "/4 ) Congratulazioni " + vincitore + " per la vittoria! Buon proseguimento!"); break;
            case 2:
                snd.setText(clessidra_emoji + " [EVENTO] (TORNEO - " + codiceTorneo + " ) ( MATCH: " + currentMatch + "/4 ) E stato troppo facile per " + vincitore + " vincere.. Ottimo lavoro!"); break;
            case 3:
                snd.setText(clessidra_emoji + " [EVENTO] (TORNEO - " + codiceTorneo + " ) ( MATCH: " + currentMatch + "/4 ) Che giocate " + vincitore + "! Ottima vittoria!!"); break;
            default:
                snd.setText(clessidra_emoji + " [EVENTO] (TORNEO - " + codiceTorneo + " ) ( MATCH: " + currentMatch + "/4 ) Belle mosse: " + vincitore + ". Buon proseguimento!"); break;
        }

        try
        {
            // We want to test that we actually sent out this message with the contents "Hello!"
            execute(snd);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    public void messaggioVincitoreTorneo(int codiceTorneo, String vincitore)
    {
        SendMessage snd = new SendMessage();
        snd.setChatId("-1001985772857");

        snd.setText(centro_emoji + " [EVENTO] (TORNEO - " + codiceTorneo + " ) " + vincitore + " ha vinto il torneo! Ottimo lavoro, complimenti!!");

        try
        {
            // We want to test that we actually sent out this message with the contents "Hello!"
            execute(snd);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername()
    {
        return "SpaccaFXBot";
    }

    @Override
    public String getBotToken()
    {
        return "6497003349:AAFdBTRDVSi8WP_JR9XpUtYqwuVGhJZLRkQ";
    }

}
