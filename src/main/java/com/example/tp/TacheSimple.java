package com.example.tp;

import java.time.LocalDate;

public class TacheSimple extends  Taches{

    private int periodicite;
    private LocalDate FinPeriodicite;
    public TacheSimple(){}
    public TacheSimple (TacheSimple t){
        this.duree=t.getDuree();
        this.name=t.name;
        this.etat=t.getEtat();
        this.categorie=t.getCategorie();
        this.priorite=t.getPriorite();
        this.deadline=t.getDeadline();
    }

    public int getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(int periodicite) {
        this.periodicite = periodicite;
    }

    public LocalDate getFinPeriodicite() {
        return FinPeriodicite;
    }

    public void setFinPeriodicite(LocalDate finPeriodicite) {
        FinPeriodicite = finPeriodicite;
    }
}
