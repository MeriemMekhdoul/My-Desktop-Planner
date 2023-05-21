package com.example.tp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Calendrier {
    private int annee;
    private List<Mois> mois;

    public Calendrier(int annee) {   //constructeur
        this.annee = annee;
        this.mois = new ArrayList<>();
        initialiserMois();
    }

    private void initialiserMois() {
        for (int i = 1; i <= 12; i++) {
            Mois mois = new Mois(i);
            this.mois.add(mois);
        }
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public Mois getMois(int mois) {
        return this.mois.get(mois - 1); // L'index commence à 0, donc on soustrait 1
    }

    public Journee getJournee(LocalDate date) {
        int mois = date.getMonthValue();
        int jour = date.getDayOfMonth();

        Mois moisCorrespondant = getMois(mois);
        if (moisCorrespondant != null) {
            List<Journee> journees = moisCorrespondant.getJournees();
            if (jour >= 1 && jour <= journees.size()) {
                return journees.get(jour - 1);
            }
        }
        return null; // Retourner null si la journée n'est pas trouvée
    }

    public void afficherCalendrier() {
        StringBuilder sb = new StringBuilder();
        System.out.println("affichage calender .......");
        sb.append("Calendrier de l'année ").append(annee).append("\n");
        for (Mois m : mois) {
            sb.append(m.afficherMois()).append("\n");
        }
        System.out.println(sb);
    }

}
