package com.spaccafx.Controllers;

import com.spaccafx.Manager.Partita;

public class ShareData {
    private static ShareData instance;

    private PartitaClassicaController2 partitaClassicaController;
    private Partita partita;

    private TavoloController tavoloController;
    private int codice;

    private ShareData() {
    }

    public static ShareData getInstance() {
        if (instance == null) {
            instance = new ShareData();
        }
        return instance;
    }

    public PartitaClassicaController2 getPartitaClassicaController() {
        return partitaClassicaController;
    }

    public void setPartitaClassicaController(PartitaClassicaController2 partitaClassicaController) {
        this.partitaClassicaController = partitaClassicaController;
    }

    public TavoloController getTavoloController() {
        return tavoloController;
    }

    public void setTavoloController(TavoloController tavoloController) {

        this.tavoloController = tavoloController;
    }

    public Partita getPartita() {
        return partita;
    }

    public void setPartita(Partita partita) {
        this.partita = partita;
    }

    public void setCodice(Partita partita){
        this.codice=partita.getCodicePartita();
    }

    public int getCodice(){
        return codice;
    }
}
