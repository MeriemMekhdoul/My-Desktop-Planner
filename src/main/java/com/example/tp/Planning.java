package com.example.tp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Planning implements Serializable {
    private Date dateDebut;
    private Date dateFin;
    private List<Journee> journeeList;
    private List<Taches> tacheList;

    public Planning(){
        this.tacheList=new ArrayList();
        this.journeeList=new ArrayList<>();

    }
    public Planning(Date dateDebut, Date dateFin, List<Taches> tacheList){
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.tacheList = new ArrayList<>(tacheList);
        //this.journeeList = new ArrayList<>();
    }

    //debutSetterGetter
    public Date getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }
    public Date getDateFin() {
        return dateFin;
    }
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public List<Journee> getJourneeList() {
        return journeeList;
    }
    public void setJourneeList(List<Journee> journeeList) {
        this.journeeList = journeeList;
    }
    public List<Taches> getTacheList() {
        return tacheList;
    }
    public void setTacheList(List<Taches> tacheList) {
        this.tacheList = tacheList;
    }
    //endSetterGetter

    /*public void addjournee(Journee journee){
        this.journeeList.add(journee);
    }
    public void suppjournee(Journee journee){
        this.journeeList.remove(journee);
    }*/
    public void addtache(Taches tache){
        this.tacheList.add(tache);
    }
    public void supptache(Taches tache){
        this.tacheList.remove(tache);
    }
}
