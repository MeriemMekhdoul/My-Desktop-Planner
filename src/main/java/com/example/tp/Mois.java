package com.example.tp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Mois implements Serializable {
    private final int numeroMois;
    private final String nom;
    private List<Journee> journees;

    public Mois(int numeroMois) {
        this.numeroMois = numeroMois;
        this.nom = obtenirNomMois(numeroMois);
        this.journees = new ArrayList<>();
        initialiserJournees();
    }

    private void initialiserJournees() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, numeroMois - 1);
        int nombreJours = getNombreJoursDansMois(numeroMois);

        for (int i = 1; i <= nombreJours; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            LocalDate date = calendar.getTime().toInstant().atZone(calendar.getTimeZone().toZoneId()).toLocalDate();
            Journee journee = new Journee(date);
            journees.add(journee);
        }
    }

    public int getNumeroMois() {
        return numeroMois;
    }

    public String getNom() {
        return nom;
    }

    public List<Journee> getJournees() {
        return journees;
    }

    public Journee getJournee(int jour) {
        return journees.get(jour - 1);
    }

    private String obtenirNomMois(int numeroMois) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, numeroMois - 1);
        int mois = calendar.get(Calendar.MONTH);
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }

    private int getNombreJoursDansMois(int numMois) {
        switch (numMois) {
            case 1, 3, 5, 7, 8, 10, 12 -> {
                return 31;
            }
            case 4, 6, 9, 11 -> {
                return 30;
            }
            case 2 -> {
                if (estAnneeBissextile()) {
                    return 29;
                } else {
                    return 28;
                }
            }
            default -> throw new IllegalArgumentException("Mois invalide : " + numMois);
        }
    }
    private boolean estAnneeBissextile() {
        int annee = Calendar.getInstance().get(Calendar.YEAR);

        if (annee % 4 != 0) {
            return false;
        } else if (annee % 100 != 0) {
            return true;
        } else return annee % 400 == 0;
    }

    public String afficherMois() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mois : ").append(nom).append("\n");
        sb.append("Nombre de jours : ").append(journees.size()).append("\n");
        for (Journee journee : journees) {
            sb.append(journee.afficherJournee()).append("\n");
        }
        return sb.toString();
    }


}
