package com.example.tp;

import com.example.tp.utilities.Categorie;
import com.example.tp.Creneau;

import java.time.LocalDate;
import java.util.Date;

abstract class Taches {
    public Taches(){
        creneau= new Creneau();
    }

    protected LocalDate deadline;
    protected String name;
    protected Priorite priorite ;
    protected Categorie categorie ;
    protected Etat etat;
    protected String duree ;
    protected Creneau creneau;

    public void setName(String Name){
        this.name=Name ;
    }
    public String getName(){
        return  this.name;
    }
    public void setPriorite(Priorite prio){
        this.priorite=prio ;
    }  public Priorite getPriorite(){
        return this.priorite;

    }  public Etat getEtat(){
        return this.etat;
    }
    public void setEtat(Etat etat1){
        this.etat=etat1;
    }
    public void setDuree(String Duree){
        this.duree= Duree;
    }
    public String getDuree(){
        return this.duree;
    }
    public Creneau getCreneau(){
        return  this.creneau;
    }

    public void setCreneau(Creneau c) {
      this.creneau=c;
    }
    public void setCategorie(Categorie catg){
        this.categorie=catg ;
    }
    public Categorie getCategorie(){
        return this.categorie;

    }
    public LocalDate getDeadline(){
        return this.deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
