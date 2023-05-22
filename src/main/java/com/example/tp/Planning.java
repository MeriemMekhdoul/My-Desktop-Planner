package com.example.tp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

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
        }
        for (Taches t: mediumPriorityTasks){
            System.out.println("Med");
            System.out.println("Etat="+t.getEtat());
            System.out.println("Duree="+t.getDuree());
            System.out.println("prio="+t.getPriorite());
            System.out.println("deadline="+t.getDeadline());
        }
        for (Taches t: lowPriorityTasks){
            System.out.println("LOW");
            System.out.println("name="+t.getName());
            System.out.println("Etat="+t.getEtat());
            System.out.println("Duree="+t.getDuree());
            System.out.println("prio="+t.getPriorite());
            System.out.println("deadline="+t.getDeadline());
        }
        for (Taches t: highPriorityTasks){
            System.out.println("Hight");
            System.out.println("Etat="+t.getEtat());
            System.out.println("Duree="+t.getDuree());
            System.out.println("prio="+t.getPriorite());
            System.out.println("deadline="+t.getDeadline());
        }
    }

}
