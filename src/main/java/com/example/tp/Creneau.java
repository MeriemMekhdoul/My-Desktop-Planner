package com.example.tp;

import java.time.LocalTime;
import java.time.LocalDate;

public class Creneau {
    //private LocalDate date;
    private LocalTime HeureDebut ;
    private LocalTime HeureFin ;
    private Boolean bloque = false;
    static public LocalTime dureeMIN ;

    public Creneau(){}
    public Creneau(LocalTime HD, LocalTime HF/*,LocalDate date*/){
        //this.date = date;
        this.HeureDebut = HD;
        this.HeureFin = HF;
        //this.bloque = false;
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
