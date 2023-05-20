package com.example.tp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Journee {
    private Date date;
    private List<Creneau> creneauxLibres;
    private List<Creneau> creneauxPris;
    private List<Taches> tacheList;

    public Journee(){}
    public Journee(Date date){
        this.date = date;
        this.creneauxLibres = new ArrayList<>();
        this.creneauxPris = new ArrayList<>();
        this.tacheList = new ArrayList<>();
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void addCreneauLibre(Creneau nvcreneau){
        this.creneauxLibres.add(nvcreneau);
    }
    public void SuppCreneauLibre(Creneau creneau){
        this.creneauxLibres.remove(creneau);
    }
    public void addCreneauPris(Creneau nvcreneau){
        this.creneauxPris.add(nvcreneau);
    }
    public void suppCreneauPris(Creneau creneau){
        this.creneauxPris.remove(creneau);
    }
    public void addtache(Taches tache){
        this.tacheList.add(tache);
    }
    public void supptache(Taches tache){
        this.tacheList.remove(tache);
    }
    public List<Taches> getTacheList() {
        return tacheList;
    }
    public List<Creneau> getCreneauxLibres() {
        return creneauxLibres;
    }
    public List<Creneau> getCreneauxPris() {
        return creneauxPris;
    }

}
