package com.spaccafx.Controllers;

public class PartitaModel {
    private int numeroGiocatori;
    private String difficolta;
    private String nomeGiocatore;

    // Aggiungi qui i metodi getter e setter per i campi sopra


    public void setDifficolta(String d){
       this.difficolta = d;
    }


    public void setNumeroGiocatori(int n) {
        this.numeroGiocatori=n;
    }


    public void setNomeGiocatore(String nome){
        this.nomeGiocatore = nome;
    }


    public int getNumeroGiocatori(){
        return numeroGiocatori;
    }

    public String getNomeGiocatore(){
        return nomeGiocatore;
    }

    public String getDifficolta(){
        return difficolta;
    }


}


