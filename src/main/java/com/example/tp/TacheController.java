package com.example.tp;

import com.example.tp.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    public TacheController(Creneau c){
        this.creneau=c;
    }
    public TacheController(Planning planning){
        this.planning=planning;
    }

    public TacheController(){}

    @FXML
    private void handleSaveButton() {
        if (tache==null) {
            try {
                boolean isSimple = simple.isSelected();
                boolean isDecomposable = decomposable.isSelected();

                if (isSimple && !isDecomposable) {
                    Taches tacheSimple = new TacheSimple();
                    RemplirTache(tacheSimple);
                } else if (!isSimple && isDecomposable) {
                    Taches tacheDecomposee = new TacheDecomposee();
                    RemplirTache(tacheDecomposee);
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
            Réinitialiser();
            NvTache.setOnAction(event ->{
                handleSaveButton();
                Réinitialiser();
            });
            Annuler.setOnAction(event -> handleCancelButton());
            Valider.setOnMouseClicked(mouseEvent ->{
                handleSaveButton();
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
        Valider.setOnAction(event ->{
            /**ici faire la redirection vers le module qui genere auto une tache**/
            try {
                handleSaveButton();
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
        Annuler.setOnAction(event -> handleCancelButton());
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
                handleSaveButton();
                if (erreursPresentes()){
                Stage stage = (Stage) NvTache.getScene().getWindow();
                stage.close();}
                else
                    throw new HnadleException("erreur dans le remplissage ");
            } catch (HnadleException e) {
                  System.out.println("erreur de remplissage");
                }
        });
        Annuler.setOnAction(event -> handleCancelButton());
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
            if(creneau!= null){
                t.setCreneau(creneau);
                System.out.println(t.getCreneau().afficherCreneau());
            }

            if(tache!= null){
                simple.setDisable(true);
                decomposable.setDisable(true);
            }
            else{
                this.tache=t;// maybe i'll need it
                user.addTache(t);//ajouter dans la liste des taches du user
                listeTache.add(t);/** est ce que ns7a9ha???**/
                if (jour!=null)
                    jour.addtache(t);// ajouter dans la liste de tache de la journee
                if (planning!= null)
                {planning.addtache(t);
                    System.out.println("Le planning est non null"+planning.getDateDebut());}// i don't know about this one
                user.PlanningActuelle(tache).FindCreneauTacheSimple(tache);

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



