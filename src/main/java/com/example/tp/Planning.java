package com.example.tp;

import java.io.Serializable;
import java.time.Duration;
import java.time.Year;
import java.util.*;
import java.time.LocalDate;

public class Planning implements Serializable {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private List<Taches> tacheList;

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
        this.tacheList.add(tache);
    }
    public void supptache(Taches tache){
        this.tacheList.remove(tache);
    }
    public void TrieListeTache (List<Taches> tacheList){

        List<Taches> highPriorityTasks=new ArrayList<>();
        List<Taches> mediumPriorityTasks =new ArrayList<>();
        List<Taches> lowPriorityTasks=new ArrayList<>();
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

            System.out.println("Etat="+t.getEtat());
            System.out.println("Duree="+t.getDuree());
            System.out.println("prio="+t.getPriorite());
            System.out.println("deadline="+t.getDeadline());
        }
    }
    public  Creneau FindCreneauTacheSimple(Taches tache){
        LocalDate date= LocalDate.now();
        User user = UserManager.getUser();
        boolean trouver=false;
        while (!date.equals(dateFin)) {
            Journee jour = user.getCalendar(date.getYear()).getJournee(date);
            for (Creneau creneau: jour.getCreneauxLibres()){
                Duration dureeCreneau= Duration.between(creneau.getHeureDebut(),creneau.getHeureFin());
                if (tache.getDuree().compareTo(dureeCreneau)<=0){
                    System.out.println("creneau trouver"+creneau+"      "+jour.getDate());
                    for (Creneau c:creneau.decomposable(tache)) {
                        jour.addCreneauLibre(c);
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

}
