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
    public TacheDecomposee(){
        super();
        Simple=new ArrayList<>();
    }
    public   List<Creneau>  DecomposerTache (Journee jour, Planning plan){
        User user = UserManager.getUser();
        Creneau creneau1= plan.FindCreneauTacheSimple(this,jour.getDate());
        LocalDate date = jour.getDate() ;
        Duration duree = this.duree;
        List<Creneau> listeCreneau= new ArrayList<>();

        int i=1;
        if (creneau1 == null) {
            while (!date.equals(plan.getDateFin()) && !duree.isZero() && !date.isAfter(this.getDeadline())){
                Journee jour1 = user.getCalendar(date.getYear()).getJournee(date);
                for(Creneau cr: jour1.getCreneauxLibres() ){
                    Duration dureeCreneau= Duration.between(cr.getHeureDebut(),cr.getHeureFin());
                    Taches tache= new TacheSimple();
                    tache.setDuree(dureeCreneau);
                    tache.setName(this.getName()+i);
                    tache.setEtat(this.etat);
                    tache.setCategorie(this.categorie);
                    tache.setPriorite(this.getPriorite());
                    tache.setDeadline(this.deadline);
                    i++;
                    Duration dureeFinal= duree;
                    duree= duree.minus(dureeCreneau);
                    System.out.println(duree);
                    if (duree.isZero()|| duree.isNegative()){
                        tache.setDuree(dureeFinal);
                        tache.setCreneau(cr);
                        if (!duree.isZero())
                            jour1.addCreneauLibre(cr.decomposable(tache).get(0));
                        Simple.add((TacheSimple) tache);
                        listeCreneau.add(cr);
                        return listeCreneau;
                    }
                    Simple.add((TacheSimple) tache);
                    listeCreneau.add(cr);
                    tache.setCreneau(cr);
                }
                date=date.plusDays(1);
            }
            user.getUnsheduledTaches().add(this);
            return null ;
        }else{
            System.out.println("la tache peut se programmer dans un seul creneau");
            return null;
        }
    }
}