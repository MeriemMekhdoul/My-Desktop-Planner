package com.example.tp;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class Journee implements Serializable {
    private LocalDate date;
    private List<Creneau> creneauxLibres;
    private List<Creneau> creneauxPris;
    private List<Taches> tacheList;
    private int NbrTacheCompletedToday=0;

    public Journee(){}
    public Journee(LocalDate date){
        this.date = date;
        this.creneauxLibres = new ArrayList<>();
        this.creneauxPris = new ArrayList<>();
        this.tacheList = new ArrayList<>();
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void addCreneauLibre(Creneau nvcreneau){
        if (this.creneauxLibres.contains(nvcreneau))
            System.out.println("creneau deja existant");
        else
            this.creneauxLibres.add(nvcreneau);
    }
    public void suppCreneauLibre(Creneau creneau){
        if (!this.creneauxLibres.contains(creneau))
            System.out.println("creneau non existant");
        else
            this.creneauxLibres.remove(creneau);
    }
    public void addCreneauPris(Creneau nvcreneau){
        if (this.creneauxPris.contains(nvcreneau))
            System.out.println("creneau deja existant");
        else
            this.creneauxPris.add(nvcreneau);
    }
    public void suppCreneauPris(Creneau creneau){
        if (!this.creneauxPris.contains(creneau))
           System.out.println("creneau non existant");
        else
            this.creneauxPris.remove(creneau);

    }
    public void addtache(Taches tache){
        if (this.tacheList.contains(tache))
            System.out.println("tache deja existante");
        else
            this.tacheList.add(tache);
    }
    public void supptache(Taches tache){
        this.tacheList.remove(tache);
    }
    public List<Taches> getTacheList() {
        return tacheList;
    }
    public void IncrementerNbrCompleted(int Nbr){
        NbrTacheCompletedToday++ ;
    }
    public List<Creneau> getCreneauxLibres() {
        return creneauxLibres;
    }
    public List<Creneau> getCreneauxPris() {
        return creneauxPris;
    }
    public String afficherJournee() {
        StringBuilder sb = new StringBuilder();
        sb.append("Date : ").append(date).append("\n");
        sb.append("Créneaux libres : ").append("\n");
        for (Creneau creneau : creneauxLibres) {
            sb.append("- ").append(creneau.afficherCreneau()).append("\n");
        }
        sb.append("Créneaux pris : ").append("\n");
        for (Creneau creneau : creneauxPris) {
            sb.append("- ").append(creneau.afficherCreneau()).append("\n");
        }
        return sb.toString();
    }
    public int getNbrTacheCompletedToday() {
        return NbrTacheCompletedToday;
    }
    public void setNbrTacheCompletedToday(int nbrTacheCompletedToday) {
        NbrTacheCompletedToday = nbrTacheCompletedToday;
    }
    public Taches getTache(Creneau creneau){
        for (Taches tache: this.tacheList) {
            if (tache.getCreneau().equals(creneau))
                return tache;
        }
        return null;
    }

}
