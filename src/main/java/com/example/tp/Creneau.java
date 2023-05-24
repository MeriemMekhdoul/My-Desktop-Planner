package com.example.tp;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Creneau implements Serializable,Decomposable {
    private LocalDate date;
    private LocalTime HeureDebut;
    private LocalTime HeureFin;
    private Boolean bloque = false;
    private static Duration dureeMIN;

    public Creneau() {
    }

    public Creneau(LocalTime HD, LocalTime HF/*,LocalDate date*/) {
        //this.date = date;
        this.HeureDebut = HD;
        this.HeureFin = HF;
        //this.bloque = false;
    }

    public static Duration getDureeMIN() {
        return dureeMIN;
    }

    public static void setDureeMIN(Duration dureeMIN) {
        Creneau.dureeMIN = dureeMIN;
    }

    public LocalTime getHeureDebut() {
        return HeureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        HeureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return HeureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        HeureFin = heureFin;
    }

    public Boolean getBloque() {
        return bloque;
    }

    public void setBloque(Boolean bloque) {
        this.bloque = bloque;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String afficherCreneau() {
        return "Heure de début : " + HeureDebut +
                "\nHeure de fin : " + HeureFin ;
               // "\nBloqué : " + bloque;
    }

    @Override
    public List<Creneau> decomposable(Taches tache) {
        LocalTime heureDebut = tache.getCreneau().getHeureDebut();
        LocalTime heureFin = tache.getCreneau().getHeureFin();
        List<Creneau> Listcreneaux = new ArrayList<>();
        User user= UserManager.getUser();
        Duration min = user.getMinCreneau();
        if (((heureDebut.equals(this.HeureDebut)) || (heureDebut.minus(min).equals(this.HeureDebut))) && (heureFin.plus(min).isBefore(this.HeureFin))) {
            Creneau creneau = new Creneau();
            creneau.setHeureDebut(heureFin);
            creneau.setHeureFin(this.HeureFin);
            Listcreneaux.add(creneau);
            this.HeureFin= heureFin;
        }
        if ((heureDebut.minus(min).isAfter(this.HeureDebut)) && ((heureFin.equals(this.HeureFin)) || (heureFin.plus(min).equals(this.HeureFin)))) {
            Creneau creneau = new Creneau();
            creneau.setHeureDebut(this.HeureDebut);
            creneau.setHeureFin(heureDebut);
            Listcreneaux.add(creneau);
            this.HeureDebut = heureDebut;
        }
        if ((heureDebut.minus(min).compareTo(this.HeureDebut) > 0) && (heureFin.plus(min).compareTo(this.HeureFin) < 0)) {
            Creneau creneau = new Creneau();
            creneau.setHeureDebut(this.HeureDebut);
            creneau.setHeureFin(heureDebut);
            Listcreneaux.add(creneau);
            Creneau creneau2 = new Creneau();
            creneau2.setHeureDebut(heureFin);
            creneau2.setHeureFin(this.HeureFin);
            Listcreneaux.add(creneau2);
            this.HeureDebut = heureDebut;
            this.HeureFin = heureFin;
        }
        return Listcreneaux;
    }
}
