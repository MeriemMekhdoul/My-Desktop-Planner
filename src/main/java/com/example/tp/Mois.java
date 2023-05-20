package com.example.tp;

import java.util.Date;
import java.util.List;

public class Mois {
    private int numeroMois;
    private String nom;
    private List<Journee> journees;

    // Constructeur, getters, setters, etc.
    public int getNumeroMois() {
        return numeroMois;
    }

    public void setNumeroMois(int numeroMois) {
        this.numeroMois = numeroMois;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public Journee getJournee(Date date){
        Journee jour = null;
        for (Journee j : this.journees) {
            if (j.getDate() == date) {
                jour = j;
                break;
            }
        }
        return jour;
    }
}
