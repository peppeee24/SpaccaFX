package com.spaccafx.Enums;

public enum GameStatus
{
    STARTED, // caso in cui viene creata la partita e bisogna ancora fare la prima mossa
    PLAYING, // caso in cui il gioco sta continuando
    STOPPED, // caso in cui la partita viene sospesa e si clicca sul pulsante home
    ENDED // caso in cui la partita viene conclusa
}
