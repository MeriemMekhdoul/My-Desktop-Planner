package com.example.tp;

import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class User implements Serializable {
   private List<Taches> tacheList ;
   private  List<Projet> listeProjet;
   private  String Pseudo;
   private String password;
   private List<Categorie> categorie;
   private List<Taches> UnsheduledTaches = new ArrayList<>();
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
    public User(String username, String Password) {
        this.Pseudo = username;
        this.password = hashPassword(Password);
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

    public List<Taches> getUnsheduledTaches() {
        return UnsheduledTaches;
    }

    public void setUnsheduledTaches(List<Taches> unsheduledTahces) {
        UnsheduledTaches = unsheduledTahces;
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
        return password;
    }

    public void setPassward(String passward) {
        this.password = passward;
    }
    public void SaveInfoUtilisateur() throws IOException {

        FileOutputStream fileout = new FileOutputStream(System.getProperty("user.home")+"\\MyDesktopPlanner\\UserInfo"+"-info.bin");
        ObjectOutput out = new ObjectOutputStream(fileout);
        out.writeObject(this);
        out.close();
        fileout.close();
    }
    public void LoadUtilisateur() throws IOException, ClassNotFoundException {
        User Utilisateur;
        FileInputStream filein = new FileInputStream(System.getProperty("user.home")+"\\MyDesktopPlanner\\UserInfo"+"-info.bin");
        ObjectInput in = new ObjectInputStream(filein);
        Utilisateur = (User) in.readObject();
        this.Pseudo= Utilisateur.getPseudo();
        this.password=Utilisateur.getPassward();
        this.tacheList=Utilisateur.getTacheList();
        this.listeProjet= Utilisateur.getListeProjet();
        this.categorie=Utilisateur.getCategorie();
        this.planningList= Utilisateur.getPlanningList();
        this.UnsheduledTaches= Utilisateur.getUnsheduledTaches();
        this.minTaskDaily=Utilisateur.getMinTaskDaily();
        filein.close();
        in.close();
    }
    public boolean checkPassword(String pword) {
        return BCrypt.checkpw(pword, password);
    }

    public String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }
}
