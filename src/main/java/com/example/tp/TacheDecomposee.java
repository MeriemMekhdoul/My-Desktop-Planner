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
        Creneau creneau1= plan.FindCreneauTacheSimple(this);
        if (creneau1!= null){
        System.out.println("J'ai trouver un creneau qui peut contenir toute la tache"+creneau1.afficherCreneau());}
        LocalDate date = jour.getDate() ;
        Duration duree = this.duree;
        List<Creneau> listeCreneau= new ArrayList<>();
        System.out.println(plan.getDateFin());
        System.out.println("liste creneau"+jour.getCreneauxLibres().size());
        for(Creneau cr: jour.getCreneauxLibres() ){
        System.out.println("Le creneau vraiment habeltni \n"+cr.afficherCreneau());}
        int i=1;
        if (creneau1 == null) {
            while (!date.equals(plan.getDateFin())&& !duree.isZero()){
                Journee jour1 = user.getCalendar(date.getYear()).getJournee(date);
                System.out.println("je suis dans la boucle while");
                for(Creneau cr: jour1.getCreneauxLibres() ){
                    System.out.println("Le creneau vraiment habeltni PT-2\n"+cr.afficherCreneau());
                    System.out.println("je suis dans la boucle for");
                    Duration dureeCreneau= Duration.between(cr.getHeureDebut(),cr.getHeureFin());
                    Taches tache= new TacheSimple();
                    tache.setDuree(dureeCreneau);
                    tache.setName(this.getName()+i);
                    tache.setEtat(this.etat);
                    tache.setCategorie(this.categorie);
                    tache.setPriorite(this.getPriorite());
                    tache.setDeadline(this.deadline);
                    i++;
                    /** penser a decomposer le dernier creneau **/
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
                System.out.println("je suis dans la FIN DE FOR");
            }
            System.out.println("la tache was not programmed ");
            user.getUnsheduledTaches().add(this);
            return null ;
        }else{
            System.out.println("la tache peut se programmer dans un seul creneau");
            return null;
        }
    }
}