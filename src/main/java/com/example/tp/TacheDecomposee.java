package com.example.tp;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public class TacheDecomposee extends Taches  {


    private List<TacheSimple> Simple;

    public List<TacheSimple> getSimple() {
        return Simple;
    }

    public void setSimple(List<TacheSimple> simple) {
        Simple = simple;
    }
    public   List<Creneau>  DecomposerTache (Journee jour, Planning plan){
        User user = UserManager.getUser();
        Creneau creneau1= plan.FindCreneauTacheSimple(this);
        LocalDate date = jour.getDate() ;
        Duration duree = this.duree;
        List<Creneau> listeCreneau= new ArrayList<>();
        System.out.println(plan.getDateFin());
        for(Creneau cr: jour.getCreneauxLibres() ){
        System.out.println(cr.afficherCreneau());}
        int i=1;
        if (creneau1 == null) {
            while (!date.equals(plan.getDateFin())|| duree.isZero()){

                for(Creneau cr: jour.getCreneauxLibres() ){
                    System.out.println("je suis dans la boucle for");
                    Duration dureeCreneau= Duration.between(creneau.getHeureDebut(),creneau.getHeureFin());
                    Taches tache= new TacheSimple();
                    tache=this;
                    tache.setDuree(duree.minus(dureeCreneau));
                    tache.setName(this.getName()+i);
                    i++;
                    Simple.add((TacheSimple) tache);
                    jour.addCreneauPris(cr);
                    jour.suppCreneauLibre(cr);
                    listeCreneau.add(cr);

                    duree= duree.minus(dureeCreneau);
                    System.out.println(duree);
                    if (duree.isZero()){
                        return listeCreneau;
                    }
                }
            }
            System.out.println("la tache was not programmed ");
            user.getUnsheduledTaches().add(this);
            return null ;
        }else{
            listeCreneau.add(creneau1);
            System.out.println("la tache peut se programmer dans un seul creneau");
            return listeCreneau;
        }
    }
}