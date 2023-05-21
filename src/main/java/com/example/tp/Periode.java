package com.example.tp;

import java.time.LocalDate;

public class Periode {
    private LocalDate dateDebut;
    private LocalDate datefin;

    public Periode(LocalDate dd, LocalDate df){
        this.dateDebut = dd;
        this.datefin = df;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDatefin() {
        return datefin;
    }

    public void setDatefin(LocalDate datefin) {
        this.datefin = datefin;
    }
}
