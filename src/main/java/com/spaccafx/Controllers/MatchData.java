package com.spaccafx.Controllers;

import com.spaccafx.Enums.GameStatus;
import com.spaccafx.Enums.GameType;

public class MatchData
{
    GameStatus status;
    GameType gameType;
    int codice, password;


    public GameStatus getStatus(){return this.status;}
    public GameType getGameType(){return this.gameType;}
    public int getCodice() {
        return codice;
    }
    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setCodice(int codice) {
        this.codice = codice;
    }
    public void setStatus(GameStatus status){this.status = status;}
    public void setGameType(GameType gameType){this.gameType = gameType;}

}
