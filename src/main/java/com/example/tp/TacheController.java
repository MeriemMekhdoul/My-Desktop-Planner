package com.example.tp;

import com.example.tp.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import  java.lang.*;

public class TacheController implements Initializable {

    @FXML
    private TextField Duree;
    private Taches tache;

    @FXML
    private TextField NomTache;

    @FXML
    private CheckBox bloquee;

    @FXML
    private ComboBox<Categorie> categorie;

    @FXML
    private DatePicker deadline;

    @FXML
    private RadioButton decomposable;

    @FXML
    private ComboBox<Priorite> priorite;

    @FXML
    private RadioButton simple;
    @FXML
    private VBox Vbox;
    @FXML
    private ComboBox<Etat> etat;
    @FXML
    private Button Valider;
    private Planning planning;

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public Planning getPlanning() {
        return planning;
    }

    private Journee jour ;

    private List<Taches> listeTache;
    public TacheController(Taches t){
        this.tache=t;
    }
    public Creneau creneau;
    private User user;
    @FXML
    private HBox HboxRadioButton;
    public TacheController(Creneau c){
        this.creneau=c;
    }
    public TacheController(Planning planning){
        this.planning=planning;
    }

    public TacheController(){}

    @FXML
    private void handleSaveButton(Boolean manu) {
        if (tache==null) {
            try {
                boolean isSimple = simple.isSelected();
                boolean isDecomposable = decomposable.isSelected();

                if (isSimple && !isDecomposable) {
                    TacheSimple tacheSimple = new TacheSimple();
                    RemplirTache(tacheSimple);
                    System.out.println(manu);

                    if (!manu){
                        System.out.println("Periodicite ="+periodicite);
                        if (!periodiciteText.getText().isEmpty()) {
                            periodicite = Integer.parseInt(periodiciteText.getText());
                            System.out.println("je set la periodicite: " + periodicite);
                        }
                        if (FinPeriodicite.getValue() != null) {
                            dateFinTacheSimple = FinPeriodicite.getValue();
                        }
                    if (periodicite !=0 ) {
                        tacheSimple.setPeriodicite(periodicite);
                        tacheSimple.setFinPeriodicite(dateFinTacheSimple);
                        System.out.println("JE SUIS DANS LA PERIEODICITE");
                        List<Creneau> cr = planning.FindCreneauTachePeriodique(tacheSimple);
                        if (cr== null){
                            System.out.println("pas possible");
                            // ouvrir popup
                        }
                        else {
                            System.out.println("CR NON NULL JE VAIS SET LES TACHES");
                            Journee j;
                            System.out.println(cr.size());
                            for (int i =0; i< cr.size();i++){
                               j = user.getCalendar(cr.get(i).getDate().getYear()).getJournee(cr.get(i).getDate()) ;
                               Creneau cr1 = cr.get(i);
                               System.out.println("J'AFFICHE LES CRENEAUX  "+cr1.afficherCreneau());
                               TacheSimple tacheSimple1 = new TacheSimple(tacheSimple);
                               System.out.println("tache name is "+tacheSimple1.getName());
                               user.addTache(tacheSimple1);
                               planning.addtache(tacheSimple1);
                               tacheSimple1.setCreneau(cr1);
                                j.suppCreneauLibre(cr1);
                                List<Creneau> liste = cr1.decomposable(tacheSimple1);
                                for (Creneau c: liste) {
                                    c.setDate(j.getDate());
                                    j.addCreneauLibre(c);
                                    System.out.println("boucle liste creneau");
                                }
                                j.addCreneauPris(cr1);
                                j.getTacheList().add(tacheSimple1);
                            }
                        }
                    }else {
                        //si manuelle je remplis seulement, sinon je lui trouve un creneau
                            tacheSimple.setCreneau(planning.FindCreneauTacheSimple(tacheSimple));
                    }
                    }
                } else if (!isSimple && isDecomposable) {
                    TacheDecomposee tacheDecomposee = new TacheDecomposee();
                    RemplirTache(tacheDecomposee);
                    Journee j = user.getCalendar(LocalDate.now().getYear()).getJournee(LocalDate.now());
                    System.out.println("Jour AYUJOURDHUI + "+j.getDate());
                    int i=0;
                    LocalDate date = LocalDate.now();
                    List<Creneau> cr=tacheDecomposee.DecomposerTache(j,planning);
                    System.out.println("Jour APRES APPEL DECOMPOSER + "+j.getDate());
                    System.out.println("PRINT JOURNEE+ "+j.afficherJournee());
                    System.out.println("Jour APRES APPEL DECOMPOSER GET SIZE LISTE MERDE + "+j.getCreneauxLibres().size());
                    if (cr!= null){
                      for(Creneau creneau1 :cr){
                            System.out.println("ON A TROUV CRENEAU .............."+creneau1.afficherCreneau());
                           //boolean trouv = false;
                            //chercher journee voir si elle continent creneau
                           while( !j.getDate().equals(planning.getDateFin())){
                               j = user.getCalendar(date.getYear()).getJournee(date);
                               System.out.println("WHIIILLEEEE");
                               System.out.println(j.getCreneauxLibres().size());
                               if (j.getCreneauxLibres().contains(creneau1)){
                                   System.out.println("if in the while XXXXXXXXXXXX"+creneau1.afficherCreneau());
                                   j.addCreneauPris(creneau1);
                                   j.suppCreneauLibre(creneau1);
                                   j.addtache(tacheDecomposee.getSimple().get(i));
                                   i++;
                                   break;
                               }
                                date = date.plusDays(1);
                                   //trouv = false;

                           }
                       }
                    }
                    } else {
                        throw new HnadleException("Vous devez choisir un type pour votre tâche.");
                    }

            } catch (HnadleException e) {
                showError(e.getMessage(),6);
            }
        }
        else {
            // Modifying an existing task
           RemplirTache(tache);
        }

    }

    private  LocalDate dateFinTacheSimple;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user=UserManager.getUser();
        listeTache= new ArrayList<>();
        priorite.getItems().setAll(Priorite.values());
        priorite.setValue(Priorite.HIGH);
        etat.setValue(Etat.NOT_REALIZED);
        etat.getItems().setAll(Etat.values());
        List<Categorie> catego= user.getCategorie();
        ObservableList<Categorie> categoryList = FXCollections.observableArrayList(catego);
        // Set the items of the ComboBox to the categoryList
        categorie.setItems(categoryList);

        if (tache!=null){
            System.out.println("Tache non null********************************************************");
            NomTache.setText(tache.getName());
            Duration duration = tache.getDuree(); // Example duration of 2 hours
            String durationText = duration.toString();
            Duree.setText(durationText);
            priorite.setValue(tache.getPriorite());
            etat.setValue(tache.getEtat());
            categorie.setValue(tache.getCategorie());
            deadline.setValue(tache.getDeadline());

        }

    }
    @FXML
    private VBox VboxPeriod;

    @FXML
    private TextField periodiciteText;
    @FXML
    private DatePicker FinPeriodicite;
    private int periodicite= 0;
    public void Periodicite(){

        simple.setOnAction(event ->{
            VboxPeriod.setVisible(true);

                });
               /* if (simple.isSelected()) {
                // Create a new button
                newHbox.getChildren().clear();
                Label label1= new Label("Souhaitez-vous planifier votre tâche de manière récurrente ?");
                Button OUIButton = new Button("oui");
                newHbox.getChildren().addAll(label1,OUIButton);
                newHbox.setFillHeight(true);
                newHbox.setAlignment(Pos.CENTER_LEFT);
                // Calculate the index at which the button should be added
                int index = Vbox.getChildren().indexOf(HboxRadioButton) + 1;
                // Add the button at the calculated index
                if (!Vbox.getChildren().contains(newHbox))
                {   Vbox.getChildren().add(index,newHbox );
                    OUIButton.setOnAction(event1 -> {
                        System.out.println("OUII POUR PERIODICITE ");
                        newHbox.getChildren().clear();
                        newHbox.setSpacing(5);
                        Label label2= new Label("Repeter tous les:");
                        TextField textField= new TextField();
                        Label label3 = new Label("jours jusqu'au");
                        DatePicker datefin=new DatePicker();
                        newHbox.getChildren().addAll(label2,textField,label3,datefin);
                        textField.setOnAction(event2 -> {
                            String enteredText = textField.getText();
                            System.out.println("Entered Text: " + enteredText);
                            // Perform further processing with the entered text
                        });
                            if (!textField.getText().isEmpty()) {
                                periodicite = Integer.parseInt(textField.getText());
                                System.out.println("je set la periodicite: " + periodicite);
                            }
                            if (datefin.getValue() != null) {
                                dateFinTacheSimple = datefin.getValue();
                            }
                        });
                    }
            }});*/
        decomposable.setOnAction(event -> {
            if (decomposable.isSelected()) {
                System.out.println("j ai clicker sur decomposable");
                // Remove the button from the VBox
                VboxPeriod.setVisible(false);
            }

        });

    }
    public  void UpdateEncouragement(){
        if (tache!=null){
            Etat selectedValue = etat.getSelectionModel().getSelectedItem();
            if (selectedValue.equals(Etat.COMPLETED)) {
                System.out.println("Completed option selected");
                Encouragement encouragement=user.getEncouragement();
                Planning plan =user.TrouverPlanning(tache);
                encouragement.updateBadges(tache,plan);
                // Perform additional actions for when "Completed" is selected
            }

    }}
    public Taches getTache() {
        return this.tache;
    }

    public void setTache(Taches tache) {
        this.tache = tache;
    }
    @FXML
    private Button NvTache;
    private  boolean stopLoop = false;
    public void NvSetTaches(){
            BloqueHbox.setVisible(false);
            etat.setDisable(true);
            Periodicite();
            Réinitialiser();
            NvTache.setOnAction(event ->{
                handleSaveButton(false);
                Réinitialiser();
            });
            Annuler.setOnAction(event -> handleCancelButton());
            Valider.setOnMouseClicked(mouseEvent ->{
                handleSaveButton(false);
                /**ici faire la redirection vers le module qui genere auto un set de taches**/
                Stage stage= (Stage) NvTache.getScene().getWindow();
                stage.close();
                for (Taches t: user.getTacheList()){
                    System.out.println("Etat="+t.getEtat());
                    System.out.println("Duree="+t.getDuree());
                    System.out.println("prio="+t.getPriorite());
                    System.out.println("name="+t.getName());
                    System.out.println("deadline="+t.getDeadline());
                }
            });

    }
    @FXML
    private HBox BloqueHbox;
    @FXML
    private Button Annuler;
    @FXML
    public void handleCancelButton(){
        this.tache = null;
        Réinitialiser();

    }
    @FXML
    public void AjouterUneSeulTache(){
        NvTache.setVisible(false);
        BloqueHbox.setVisible(false);
        etat.setDisable(true);
        Periodicite();
        Valider.setOnAction(event ->{
            /**ici faire la redirection vers le module qui genere auto une tache**/
            try {
                handleSaveButton(false);
                if (!erreursPresentes()){
                    Stage stage = (Stage) NvTache.getScene().getWindow();
                    stage.close();}
                else
                    throw new HnadleException("erreur dans le remplissage ");
            } catch (HnadleException e) {

            }
            for (Taches t: user.getTacheList()){
                System.out.println("Etat="+t.getEtat());
                System.out.println("Duree="+t.getDuree());
                System.out.println("prio="+t.getPriorite());
                System.out.println("name="+t.getName());
                System.out.println("deadline="+t.getDeadline());
            }
            for (Taches t: listeTache){
                System.out.println("Etat="+t.getEtat());
                System.out.println("Duree="+t.getDuree());
                System.out.println("prio="+t.getPriorite());
                System.out.println("name="+t.getName());
                System.out.println("deadline="+t.getDeadline());
            }
        });
        Annuler.setOnAction(event -> {
            Stage stage = (Stage) NvTache.getScene().getWindow();
            stage.close();});
    }
    @FXML
    public void AjouterTacheManu(){
        NvTache.setVisible(false);
        BloqueHbox.setVisible(true);
        etat.setDisable(true);
        HBox hbox = new HBox(20);
        Label heureD= new Label("Heure de debut");
        List<Integer> heure = new ArrayList<>();
        for (int i = 0; i <= 24; i++) {
            heure.add(i);
        }
        ComboBox<Integer> HeureDebut= new ComboBox<>();
        HeureDebut.getItems().addAll(heure);
        for (int i = 0; i <= 59; i++) {
            heure.add(i);
        }
        ComboBox<Integer> MinDebut= new ComboBox<>();
        MinDebut.getItems().addAll(heure);
        hbox.getChildren().addAll(heureD,HeureDebut,MinDebut);
        Vbox.getChildren().add(2,hbox);
        Valider.setOnAction(event ->{
            /**ici faire la redirection vers le module qui genere auto une tache**/
            try {
                handleSaveButton(true);
               /* if (HeureDebut.getValue()!=null){
                    Creneau cr = new Creneau();
                    //cr.setHeureDebut(HeureDebut.getValue());
                }*/
                tache.setCreneau(creneau);
                jour.suppCreneauLibre(creneau);
                List<Creneau> liste = creneau.decomposable(tache);
                for (Creneau c: liste) {
                    c.setDate(jour.getDate());
                    jour.addCreneauLibre(c);
                    System.out.println("boucle liste creneau");
                }
                System.out.println("je suis dans le remplisage :"+tache.getCreneau().afficherCreneau());
                System.out.println("je suis dans le remplisage 2 :"+creneau.afficherCreneau());
                jour.addCreneauPris(creneau);
                if (!erreursPresentes()){
                    Stage stage = (Stage) NvTache.getScene().getWindow();
                    stage.close();}
                else
                    throw new HnadleException("erreur dans le remplissage ");
            } catch (HnadleException e) {
                //throw new RuntimeException(e);
            }
        });
        Annuler.setOnAction(event -> {
            Stage stage = (Stage) NvTache.getScene().getWindow();
            stage.close();});
    }

    @FXML
    private HBox EtatHbox;
    @FXML
    private Label label;
    public void VisualiserTache(){
        label.setText("Ma tâche");
        NvTache.setVisible(false);
        simple.setDisable(true);
        decomposable.setDisable(true);
        Valider.setOnAction(e1 -> {
            try {
                handleSaveButton(false);
                if (erreursPresentes()){
                Stage stage = (Stage) NvTache.getScene().getWindow();
                stage.close();}
                else
                    throw new HnadleException("erreur dans le remplissage ");
            } catch (HnadleException e) {
                  System.out.println("erreur de remplissage");
                }
        });
        Annuler.setOnAction(event -> {
            Stage stage = (Stage) NvTache.getScene().getWindow();
            stage.close();});
    }
    private Boolean erreursPresentes(){
        return Vbox.getChildren().stream()
                .anyMatch(node -> node instanceof Label && ((Label) node).getStyle().equals("-fx-text-fill: red;"));
    }
    public void Réinitialiser(){
        //réinitialiser les champs
        NomTache.setText("");
        simple.setSelected(false);
        decomposable.setSelected(false);
        Duree.setText("0");
        bloquee.setSelected(false);
        tache=null;
    }


    public void setJour(Journee jour) {
        this.jour = jour;
    }
    public void  RemplirTache(Taches t){
        try {
            LocalDate selectedDate = deadline.getValue();
            if (selectedDate != null) {
                LocalDate currentDate = LocalDate.now();
                if (selectedDate.isBefore(currentDate)) {
                    throw new HnadleException("La date limite doit être supérieure à la date d'aujourd'hui.");
                }
                t.setDeadline(selectedDate);
            } else {
                throw new HnadleException("Vous devez sélectionner une date limite.");
            }

            if (Duree.getText().isEmpty()) {
                throw new HnadleException("Vous devez saisir la durée de votre tâche.");
            } else {
                StringConverter<LocalTime> converter = new LocalTimeStringConverter();
                LocalTime localTime = converter.fromString(Duree.getText());
                if (localTime != null) {
                    LocalTime referenceTime = LocalTime.MIDNIGHT;
                    Duration duration = Duration.between(referenceTime, localTime);
                    t.setDuree(duration);
                } else {
                    throw new HnadleException("Le format de la durée n'est pas respecté.");
                }
            }

            t.setName(NomTache.getText());
            t.setCategorie(categorie.getValue());
            t.setPriorite(priorite.getValue());
            t.setEtat(Etat.NOT_REALIZED);
            t.creneau.setBloque(bloquee.isSelected());

            if(tache!= null){
                simple.setDisable(true);
                decomposable.setDisable(true);
            }
            else{
                this.tache=t;// maybe i'll need it
                user.addTache(t);//ajouter dans la liste des taches du user
                listeTache.add(t);/** est ce que ns7a9ha???**/
                if (jour!=null) {
                    jour.addtache(t);// ajouter dans la liste de tache de la journee
                }
                if (planning!= null)
                {   planning.addtache((Taches)t);
                    System.out.println("Le planning est non null"+planning.getDateDebut());}// i don't know about this one
               // user.PlanningActuelle(tache).FindCreneauTacheSimple(tache);

            }
        } catch (HnadleException e) {
            showError(e.getMessage(),2);
        } catch (DateTimeParseException e) {
            showError("Format de date invalide pour la durée.",1);
        }
    }

    private void showError(String message,int i) {
        // Remove any previous error messages
        clearErrorMessages();

        // Display the error message in red above the elements
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red;");
        Vbox.getChildren().add(i, errorLabel);
    }



    private void clearErrorMessages() {
        // Remove any previous error messages from the VBox
        Vbox.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getStyle().equals("-fx-text-fill: red;"));
    }
}

    // Other methods, event handlers, etc.



