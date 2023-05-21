package com.example.tp;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.LocalDate;

public class Creneau implements Serializable {
    //private LocalDate date;
    private LocalTime HeureDebut ;
    private LocalTime HeureFin ;
    private Boolean bloque = false;
    private static Duration dureeMIN = Duration.ofMinutes(30);

    public Creneau(){}
    public Creneau(LocalTime HD, LocalTime HF/*,LocalDate date*/){
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

    /*public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }*/
    public String afficherCreneau() {
        return "Heure de début : " + HeureDebut +
                "\nHeure de fin : " + HeureFin +
                "\nBloqué : " + bloque;
    }

}
