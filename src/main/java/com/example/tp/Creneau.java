package com.example.tp;

import java.time.LocalTime;

public class Creneau {
    private LocalTime HeureDebut ;
    private LocalTime HeureFin ;
    private Boolean bloque ;
    static public LocalTime dureeMIN ;

    public Creneau(){}
    public Creneau(LocalTime HD, LocalTime HF, Boolean bloque){
        this.HeureDebut = HD;
        this.HeureFin = HF;
        this.bloque = bloque;
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
}
