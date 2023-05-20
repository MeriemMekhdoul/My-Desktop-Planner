package com.example.tp;

import java.util.ArrayList;
import java.util.List;

public class Calendrier {
    private int annee;
    private List<Mois> mois;

    // Constructeur, getters, setters, etc.
    public int getAnnee() {
        return annee;
    }
    public void setAnnee(int annee) {
        this.annee = annee;
    }
    public Mois getMonth(int mois){
        return this.mois.get(mois);
    }


}
