package com.example.tp;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    private List<Taches> highPriorityTasks=new ArrayList<>();
    private List<Taches> mediumPriorityTasks =new ArrayList<>();
    private List<Taches> lowPriorityTasks=new ArrayList<>();

    private Map<Badge,Integer> badges;
    public Planning(){
    }
    public Planning(LocalDate dateDebut, LocalDate dateFin){
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.tacheList = new ArrayList<>();
        this.badges=new HashMap<>();
    }
    public void addBadge(Badge badge) {
        badges.put(badge, badges.getOrDefault(badge, 0) + 1);
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
         if ((!tache.getCreneau().getBloque())){
            if (tache.getPriorite().equals(Priorite.HIGH)) {
                return 3;
            } else if (tache.getPriorite().equals(Priorite.MEDIUM)) {
                return 2;
            } else {
                return 1;
            }
            }return 0;
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
    }
    public Creneau FindCreneauTacheSimple(Taches tache,LocalDate date){
        User user = UserManager.getUser();
        while (!date.equals(dateFin) && !date.isAfter(tache.getDeadline())) {
            Journee jour = user.getCalendar(date.getYear()).getJournee(date);
            for (Creneau creneau: jour.getCreneauxLibres()){
                Duration dureeCreneau= Duration.between(creneau.getHeureDebut(),creneau.getHeureFin());
                if (tache.getDuree().compareTo(dureeCreneau)<=0){
                    jour.suppCreneauLibre(creneau);
                    Creneau creneauTPM= new Creneau();
                    creneauTPM.setHeureFin(creneau.getHeureDebut().plus(tache.getDuree()));
                    creneauTPM.setHeureDebut(creneau.getHeureDebut());
                    tache.setCreneau(creneauTPM);
                    List<Creneau> liste = creneau.decomposable(tache);
                    for (Creneau c: liste) {
                        c.setDate(jour.getDate());
                        jour.addCreneauLibre(c);
                    }
                    user.addTache(tache);
                    tache.setCreneau(creneau);
                    jour.addCreneauPris(creneau);
                    jour.addtache(tache);
                    return creneau;
                }
            }
            date=date.plusDays(1);

        }
        user.getUnsheduledTaches().add(tache);
        return null;
    }
    public  List<Creneau> FindCreneauTachePeriodique(TacheSimple tache){
        boolean PasCreneau=true;
        User user = UserManager.getUser();
        List<Creneau> listCreneau= new ArrayList<>();
        Creneau cr=FindCreneauTacheSimple(tache,LocalDate.now());
        if(cr!=null){
            LocalDate date= cr.getDate().plusDays(tache.getPeriodicite());
          while(!date.equals(tache.getFinPeriodicite()) && !date.isAfter(tache.getFinPeriodicite()) && !date.equals(tache.getDeadline())){
            Journee jour = user.getCalendar(date.getYear()).getJournee(date);
            System.out.println(jour.getDate());

            System.out.println(jour.getCreneauxLibres().size());
            for (Creneau creneau: jour.getCreneauxLibres()){
                Duration dureeCreneau= Duration.between(creneau.getHeureDebut(),creneau.getHeureFin());
                if ((creneau.getHeureDebut().equals(cr.getHeureDebut()) && tache.getDuree().compareTo(dureeCreneau) <= 0) || (creneau.getHeureDebut().isBefore(cr.getHeureDebut()) && creneau.getHeureFin().isAfter(cr.getHeureFin()))||((creneau.getHeureFin().equals(cr.getHeureFin()) && tache.getDuree().compareTo(dureeCreneau) <= 0)))
                {// les conditions c'est  pour programmer la tache exactement dans le mm creneau
                    listCreneau.add(creneau);//ici jai add ga3 le creneau jsp et j ai pas set dans tache
                    PasCreneau= true;
                    break;
                }
               else
                    PasCreneau=false;
            }
            if (jour.getCreneauxLibres().isEmpty()|| !PasCreneau){
                openPopup("Pas de creneau libre dans la journee"+jour.getDate()+"pour planifier cette tache");
                System.out.println(PasCreneau);
                System.out.println("ne peut pas garentir la periodicite tous les "+tache.getPeriodicite()+" jour");
                return null;
            }
            date = date.plusDays(tache.getPeriodicite());
            if(date.isAfter(tache.getDeadline())){
                openPopup("Ce n'est pas possible de planifier cette tache jusqu'au "+ tache.getFinPeriodicite()+ "car sa deadline est pour le "+tache.getDeadline());
                return null;
            }
        }
          return listCreneau;
        }
        else{
            System.out.println("la tache ne peut pas etre plannifier");
            return null ;
        }
    }
    public void Planification(List<Taches> tacheList, LocalDate date){
        List<Taches> listetemp = new ArrayList<>();
        User user = UserManager.getUser();
        TrieListeTache(tacheList);
        listetemp.addAll(highPriorityTasks);
        listetemp.addAll(mediumPriorityTasks);
        listetemp.addAll(lowPriorityTasks);
        List<Taches> TmpList= new ArrayList<>();

        for (Taches tache:listetemp) {
            if(tache instanceof TacheSimple){
                if(!this.tacheList.contains(tache)){
                    this.addtache(tache) ;
                    TacheSimple simple = (TacheSimple) tache;
                    if (simple.getPeriodicite()==0){
                    FindCreneauTacheSimple(tache,LocalDate.now());
                    TmpList.add(tache);
                    }
                    else{
                    List<Creneau> cr = this.FindCreneauTachePeriodique(simple);
                    simple.setPeriodicite(0);
                    if (cr== null){
                        System.out.println("pas possible");
                        // ouvrir popup
                    }
                    else {
                        Journee j;
                        System.out.println(cr.size());
                        for (int i =0; i< cr.size();i++){
                            j = user.getCalendar(cr.get(i).getDate().getYear()).getJournee(cr.get(i).getDate()) ;
                            Creneau cr1 = cr.get(i);
                            TacheSimple tacheSimple1 = new TacheSimple(simple);
                            user.addTache(tacheSimple1);
                            this.addtache(tacheSimple1);
                            tacheSimple1.setCreneau(cr1);
                            j.suppCreneauLibre(cr1);
                            List<Creneau> liste = cr1.decomposable(tacheSimple1);
                            for (Creneau c: liste) {
                                c.setDate(j.getDate());
                                j.addCreneauLibre(c);
                            }
                            j.addCreneauPris(cr1);
                            j.getTacheList().add(tacheSimple1);
                            TmpList.add(tacheSimple1);

                        }
                       }
                    }
                }}
            else {
                TacheDecomposee decomposable = (TacheDecomposee) tache;
                Journee j = user.getCalendar(date.getYear()).getJournee(date);
                int i=0;
                //LocalDate date = LocalDate.now();
                List<Creneau> cr=decomposable.DecomposerTache(j,this);
                if (cr!= null){
                    for(Creneau creneau1 :cr){
                        while( !j.getDate().equals(this.getDateFin())){
                            j = user.getCalendar(date.getYear()).getJournee(date);
                            if(!this.tacheList.contains(decomposable.getSimple().get(i))){
                            if (j.getCreneauxLibres().contains(creneau1)){
                                j.addCreneauPris(creneau1);
                                j.suppCreneauLibre(creneau1);
                                j.addtache(decomposable.getSimple().get(i));
                                this.getTacheList().add(decomposable.getSimple().get(i));
                                TmpList.add(decomposable.getSimple().get(i));
                                date = date.plusDays(1);
                                this.addtache(decomposable.getSimple().get(i)) ;
                                user.addTache(decomposable.getSimple().get(i));
                                i++;
                                break;
                            }
                            else {
                                System.out.println("non trouver");
                            }
                        }
                            date = date.plusDays(1);}
                    }
                }
            }
        }
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("TacheValidation.fxml"));
        fxmlLoader.setControllerFactory(obj -> new TacheValidation(TmpList));
        Parent root1 = null;
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }
    private void openPopup(String s) {
        // Create a new stage for the pop-up window
        Stage popupStage = new Stage();

        // Set the modality to APPLICATION_MODAL to block input events for other windows
        popupStage.initModality(Modality.APPLICATION_MODAL);

        // Set the title of the pop-up window
        popupStage.setTitle("Pop-up Window");

        // Create a label to display a message in the pop-up window
        Label label = new Label(s);
        label.setStyle("-fx-font-family: 'Calibri';" +
                "-fx-text-fill: #aaaaaa;"+ "-fx-font-size: 16px;");
        label.setWrapText(true);
        Label label1 = new Label("la tache sera planifier que dans le premier creneau q'elle a trouver");
        label.setStyle("-fx-font-family: 'Calibri';" +
                "-fx-text-fill:#23574B ;"+ "-fx-font-size: 14px;");
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        // Create a button to close the pop-up window
        Button continueButton = new Button("Annules la planification periodique");
        continueButton.setStyle("-fx-text-fill: white;" +
                "-fx-background-color: #3F9984;" +
                "-fx-font-family: 'Calibri';" +
                "-fx-background-radius: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-font-weight: bold;"+
                "-fx-font-size: 16px;");
        continueButton.setPrefWidth(200);
        continueButton.setPrefHeight(40);
        continueButton.setWrapText(true);
        continueButton.setOnAction(event -> popupStage.close());

        // Create the layout for the pop-up window
        VBox popupRoot = new VBox(20);
        popupRoot.setAlignment(Pos.CENTER);
        popupRoot.setPadding(new Insets(10));
        popupRoot.getChildren().addAll(label1,label, continueButton);

        // Create the scene for the pop-up window
        Scene popupScene = new Scene(popupRoot, 400, 200);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
    public void replanification() {
        List<Taches> tachesNonPlanifiees = new ArrayList<>();

        for (Taches tache : this.tacheList) {
            if (!tache.getCreneau().getBloque()) {
                Journee jour = UserManager.getUser().getCalendar(tache.getCreneau().getDate().getYear()).getJournee(tache.getCreneau().getDate());
                tachesNonPlanifiees.add(tache);
                jour.suppCreneauPris(tache.getCreneau());
                jour.addCreneauLibre(tache.getCreneau());
                jour.supptache(tache);
            }
        }

        // Supprimer les tâches de la liste principale après la boucle
        this.tacheList.removeAll(tachesNonPlanifiees);
        for (Taches t:tachesNonPlanifiees) {
            System.out.println(t.getName());
            UserManager.getUser().SuppTache(t);
        }
        Planification(tachesNonPlanifiees,this.dateDebut);
    }
    public Map<Badge, Integer> getBadges() {
        return badges;
    }
    public void setBadges(Map<Badge, Integer> badges) {
        this.badges = badges;
    }
}
