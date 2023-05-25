package com.example.tp;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.time.Year;
import java.util.*;
import java.time.LocalDate;

public class Planning implements Serializable {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private List<Taches> tacheList;

    List<Taches> highPriorityTasks=new ArrayList<>();
    List<Taches> mediumPriorityTasks =new ArrayList<>();
    List<Taches> lowPriorityTasks=new ArrayList<>();

    public Planning(){
    }
    public Planning(LocalDate dateDebut, LocalDate dateFin){
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.tacheList = new ArrayList<>();
        //this.journeeList = new ArrayList<>();
    }

    //debutSetterGetter
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    public LocalDate getDateFin() {
        return dateFin;
    }
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public List<Taches> getTacheList() {
        return tacheList;
    }
    public void setTacheList(List<Taches> tacheList) {
        this.tacheList = tacheList;
    }
    //endSetterGetter

    public void addtache(Taches tache){
        if(!this.tacheList.contains(tache))
            this.tacheList.add(tache);
    }
    public void supptache(Taches tache){
        this.tacheList.remove(tache);
    }
    public void TrieListeTache (List<Taches> tacheList){

        Comparator<Taches> priorityComparator = Comparator.comparingInt(tache -> {
            if (tache.getPriorite().equals(Priorite.HIGH)) {
                return 3;
            } else if (tache.getPriorite().equals(Priorite.MEDIUM)) {
                return 2;
            } else {
                return 1;
            }
        });
        Collections.sort(tacheList, priorityComparator);
        Comparator<Taches> deadlineComparator = Comparator.comparing(Taches::getDeadline);
        Collections.sort(tacheList, deadlineComparator);
        for (Taches tache : tacheList) {
            Priorite priority = tache.getPriorite();
            if (priority.equals(Priorite.HIGH)) {
                highPriorityTasks.add(tache);
            } else if (priority.equals(Priorite.MEDIUM)) {
                mediumPriorityTasks.add(tache);
            } else {
                lowPriorityTasks.add(tache);
            }
        }System.out.println("Med");
        for (Taches t: mediumPriorityTasks){
            System.out.println("Name="+t.getName());
            System.out.println("Etat="+t.getEtat());
            System.out.println("Duree="+t.getDuree());
            System.out.println("prio="+t.getPriorite());
            System.out.println("deadline="+t.getDeadline());
        }System.out.println("LOW");
        for (Taches t: lowPriorityTasks){

            System.out.println("name="+t.getName());
            System.out.println("Etat="+t.getEtat());
            System.out.println("Duree="+t.getDuree());
            System.out.println("prio="+t.getPriorite());
            System.out.println("deadline="+t.getDeadline());
        }System.out.println("Hight");
        for (Taches t: highPriorityTasks){
            System.out.println("Name="+t.getName());
            System.out.println("Etat="+t.getEtat());
            System.out.println("Duree="+t.getDuree());
            System.out.println("prio="+t.getPriorite());
            System.out.println("deadline="+t.getDeadline());
        }
    }
    public Creneau FindCreneauTacheSimple(Taches tache){
        LocalDate date= LocalDate.now();
        User user = UserManager.getUser();
        while (!date.equals(dateFin)) {
            Journee jour = user.getCalendar(date.getYear()).getJournee(date);
            for (Creneau creneau: jour.getCreneauxLibres()){
                Duration dureeCreneau= Duration.between(creneau.getHeureDebut(),creneau.getHeureFin());
                if (tache.getDuree().compareTo(dureeCreneau)<=0){
                    System.out.println("creneau trouver"+creneau.afficherCreneau()+" \n"+jour.getDate());
                    jour.suppCreneauLibre(creneau);
                    tache.setCreneau(creneau);
                    List<Creneau> liste = creneau.decomposable(tache);
                    for (Creneau c: liste) {
                        c.setDate(jour.getDate());
                        jour.addCreneauLibre(c);
                        System.out.println("boucle liste creneau");
                    }
                    jour.addCreneauPris(creneau);
                    jour.addtache(tache);
                    return creneau;
                }
            }
            date=date.plusDays(1);
        }
            System.out.println("aucun creneau libre pour ajouter cette tache");/** le mettre en pop up**/
            user.getUnsheduledTaches().add(tache);// je supprime de la liste des taches??
           // user.getTacheList().remove(tache);
        return null;
    }


    // j ai pas test jsp si ca marche
    public  List<Creneau> FindCreneauTachePeriodique(TacheSimple tache){
        boolean PasCreneau=true;
        User user = UserManager.getUser();
        List<Creneau> listCreneau= new ArrayList<>();
        Creneau cr=FindCreneauTacheSimple(tache);
        if(cr!=null){
            listCreneau.add(cr);
            LocalDate date= cr.getDate();
        while(!date.equals(tache.getFinPeriodicite())){
            Journee jour = user.getCalendar(date.getYear()).getJournee(date);
            for (Creneau creneau: jour.getCreneauxLibres()){
                Duration dureeCreneau= Duration.between(creneau.getHeureDebut(),creneau.getHeureFin());
                if ((creneau.getHeureDebut().equals(cr.getHeureDebut()) && tache.getDuree().compareTo(dureeCreneau) <= 0) || (creneau.getHeureDebut().isBefore(cr.getHeureDebut()) && creneau.getHeureFin().isAfter(cr.getHeureFin()))||((creneau.getHeureFin().equals(cr.getHeureFin()) && tache.getDuree().compareTo(dureeCreneau) <= 0) ))
                {// les conditions c'est  pour programmer la tache exactement dans le mm creneau
                    listCreneau.add(creneau);//ici jai add ga3 le creneau jsp et j ai pas set dans tache
                    PasCreneau= true;
                    break;
                }
               else
                    PasCreneau=false;
            }
            if (jour.getCreneauxLibres().isEmpty()|| !PasCreneau){
                System.out.println("ne peut pas garentir la periodicite tous les "+tache.getPeriodicite()+" jour");
                return null;
            }
            date = date.plusDays(tache.getPeriodicite());
        }
          return listCreneau;
        }
        else{
            System.out.println("la tache ne peut pas etre plannifier");
            return null ;
        }
    }
    private void Planification(){
        User user = UserManager.getUser();
        for (Taches tache:highPriorityTasks) {
            if(tache instanceof TacheSimple){
                TacheSimple simple = (TacheSimple) tache;
                if (simple.getPeriodicite()==0){
                FindCreneauTacheSimple(tache);}
                else
                    FindCreneauTachePeriodique(simple);// la sortie c'est une liste de creneaux , du coup j'ai pas encor programmer ces taches j'ai juste trouvé ou les mettre(creneaux)

            }
            else {
                TacheDecomposee decomposable = (TacheDecomposee) tache;
                decomposable.DecomposerTache(user.getCalendar(LocalDate.now().getYear()).getJournee(LocalDate.now()),this);
            }
        }
        for (Taches tache:mediumPriorityTasks) {
            if(tache instanceof TacheSimple){
                TacheSimple simple = (TacheSimple) tache;
                if (simple.getPeriodicite()==0){
                    FindCreneauTacheSimple(tache);}
                else
                    FindCreneauTachePeriodique(simple);// la sortie c'est une liste de creneaux , du coup j'ai pas encor programmer ces taches j'ai juste trouvé ou les mettre(creneaux)

            }
            else {
                TacheDecomposee decomposable = (TacheDecomposee) tache;
                decomposable.DecomposerTache(user.getCalendar(LocalDate.now().getYear()).getJournee(LocalDate.now()),this);
            }
        }
        for (Taches tache:lowPriorityTasks) {
            if(tache instanceof TacheSimple){
                TacheSimple simple = (TacheSimple) tache;
                if (simple.getPeriodicite()==0){
                    FindCreneauTacheSimple(tache);}
                else
                    FindCreneauTachePeriodique(simple);// la sortie c'est une liste de creneaux , du coup j'ai pas encor programmer ces taches j'ai juste trouvé ou les mettre(creneaux)

            }
            else {
                TacheDecomposee decomposable = (TacheDecomposee) tache;
                decomposable.DecomposerTache(user.getCalendar(LocalDate.now().getYear()).getJournee(LocalDate.now()),this);
            }
        }
        if (user.getUnsheduledTaches()!= null){
            //load le fxml des taches unshecheduled
        }

    }

}
