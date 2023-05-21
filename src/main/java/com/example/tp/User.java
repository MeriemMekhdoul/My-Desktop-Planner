package com.example.tp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class User {
   private List<Taches> tacheList ;
   private  List<Projet> listeProjet;
   private  String Pseudo;
   private String passward;
   private List<Categorie> categorie;
   private List<Taches> UnsheduledTahces = new ArrayList<>();
   private List<Calendrier> calendriers;

 //  private Historique histo ;
   static private int minTaskDaily ;

   public List<Calendrier> getCalendriers(){
       return this.calendriers;
   }
   public int getMinTaskDaily(){
      return this.minTaskDaily;
   }
   public  void setMinTaskDaily(int min){
      this.minTaskDaily= min;
   }
   private List<Planning> planningList;

   public User() {

       this.tacheList = new ArrayList<>();
       this.categorie = new ArrayList<>();
       this.calendriers = new ArrayList<>();
   }

   public List<Projet> getListeProjet() {
      return listeProjet;
   }

   public void setListeProjet(List<Projet> listeProjet) {
      this.listeProjet = listeProjet;
   }

   public List<Taches> getTacheList() {
      return tacheList;
   }

   public void setTacheList(List<Taches> tacheList) {
      this.tacheList = tacheList;
   }

   public List<Planning> getPlanningList() {
      return planningList;
   }

   public void setPlanningList(List<Planning> planningList) {
      this.planningList = planningList;
   }

   public String getPseudo() {
      return Pseudo;
   }

   public void setPseudo(String pseudo) {
      Pseudo = pseudo;
   }

    public Calendrier getCalendar(int annee) {
        Calendrier calendrier = null;
        for (Calendrier c : this.calendriers) {
            if (c.getAnnee() == annee) {
                calendrier = c;
                break;
            }
        }
        return calendrier;
    }
    public Calendrier newCalender(Calendrier calendrier){
       this.calendriers.add(calendrier);
       return calendrier;
    }

    public void setCalendar(Calendrier calendar) {
    }
    public  void addTache(Taches newTache){
       tacheList.add(newTache);
    }

    public List<Taches> getUnsheduledTahces() {
        return UnsheduledTahces;
    }

    public void setUnsheduledTahces(List<Taches> unsheduledTahces) {
        UnsheduledTahces = unsheduledTahces;
    }
    public void addPoject(Projet newProjet )
    {
        listeProjet.add(newProjet);
    }
    public void SuppTache (Taches suppTache){
       tacheList.remove(suppTache);
        // Creneau creneau= suppTache.getCreneau();
    }
    public void SuppProjet(Projet suppProjet){
        listeProjet.remove(suppProjet);
    }


    public List<Categorie> getCategorie() {
        return categorie;
    }

    public void setCategorie(List<Categorie> categorie) {
        this.categorie = categorie;
    }

    public String getPassward() {
        return passward;
    }

    public void setPassward(String passward) {
        this.passward = passward;
    }
}
