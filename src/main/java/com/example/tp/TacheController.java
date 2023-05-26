package com.example.tp;

import com.example.tp.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @FXML
    private Button NvTache;
    @FXML
    private HBox BloqueHbox;
    @FXML
    private Button Annuler;
    @FXML
    private HBox EtatHbox;
    @FXML
    private Label label;
    private Journee jour ;
    private List<Taches> listeTache;
    public Creneau creneau;
    private User user;
    @FXML
    private HBox HboxRadioButton;
    private  LocalDate dateFinTacheSimple;
    @FXML
    private VBox VboxPeriod;
    @FXML
    private TextField periodiciteText;
    @FXML
    private DatePicker FinPeriodicite;
    private  boolean stopLoop = false;


    public TacheController(){}
    public Planning getPlanning() {
        return planning;
    }
    public void setPlanning(Planning planning) {
        this.planning = planning;
    }
    public TacheController(Taches t){
        this.tache=t;
    }
    public TacheController(Creneau c){
        this.creneau=c;
    }
    public TacheController(Planning planning){
        this.planning=planning;
    }
    public Taches getTache() {
        return this.tache;
    }
    public void setTache(Taches tache) {
        this.tache = tache;
    }

    @FXML
    private Taches handleSaveButton(Boolean manu) {
        if (tache==null) {
            try {
                boolean isSimple = simple.isSelected();
                boolean isDecomposable = decomposable.isSelected();


                if (isSimple && !isDecomposable) {
                    TacheSimple tacheSimple = new TacheSimple();
                    RemplirTache(tacheSimple);
                    System.out.println(manu);

                    if (!manu){
                        if (!periodiciteText.getText().isEmpty()) {
                            tacheSimple.setPeriodicite(Integer.parseInt(periodiciteText.getText()));
                        if (FinPeriodicite.getValue() != null) {
                            tacheSimple.setFinPeriodicite(FinPeriodicite.getValue());
                        }
                        }
                        return tacheSimple;
                    }
                } else if (!isSimple && isDecomposable) {
                    TacheDecomposee tacheDecomposee = new TacheDecomposee();
                    RemplirTache(tacheDecomposee);
                    return tacheDecomposee;
                    } else {
                        throw new HnadleException("Vous devez choisir un type pour votre tâche.");
                    }

            } catch (HnadleException e) {
                showError(e.getMessage(),6);
            }
        }
        else {
            // Modifying an existing task
            System.out.println("je visualise une tache deja existante ");
            tache.setName(NomTache.getText());
            tache.setCategorie(categorie.getValue());
            tache.setPriorite(priorite.getValue());
            tache.setEtat(etat.getValue());
            tache.setDeadline(deadline.getValue());
            tache.getCreneau().setBloque(bloquee.isSelected());
        }
        return null;
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
            NomTache.setText(tache.getName());
            Duration duration = tache.getDuree(); // Example duration of 2 hours
            String durationText = duration.toString();
            Duree.setText(durationText);
            priorite.setValue(tache.getPriorite());
            etat.setValue(tache.getEtat());
            categorie.setValue(tache.getCategorie());
            deadline.setValue(tache.getDeadline());
            bloquee.setSelected(tache.getCreneau().getBloque());

        }

    }
    public void Periodicite(){

        simple.setOnAction(event ->{
            VboxPeriod.setVisible(true);

                });
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

                user.getBadges().forEach((badge, value) -> {
                    System.out.println("Badge: " + badge.name() + ", Value: " + value);
                });
                // Perform additional actions for when "Completed" is selected
            }

    }}
    public void NvSetTaches(){
            List<Taches> liste= new ArrayList<>();
            BloqueHbox.setVisible(false);
            etat.setDisable(true);
            Periodicite();
            Réinitialiser();
            NvTache.setOnAction(event ->{
                liste.add(handleSaveButton(false));
                Réinitialiser();
            });
            Annuler.setOnAction(event -> handleCancelButton());
            Valider.setOnMouseClicked(mouseEvent ->{
                liste.add(handleSaveButton(false));
                planning.Planification(liste,LocalDate.now());
            });

    }
    @FXML
    public void handleCancelButton(){
        this.tache = null;
        Réinitialiser();

    }
    @FXML
    public void AjouterUneSeulTache(){
        List<Taches> list = new ArrayList<>();
        NvTache.setVisible(false);
        BloqueHbox.setVisible(false);
        etat.setDisable(true);
        Periodicite();
        Valider.setOnAction(event ->{
                clearErrorMessages();
                list.add(handleSaveButton(false));
                if (!erreursPresentes()){
                    planning.Planification(list,LocalDate.now());
                    Stage stage = (Stage) NvTache.getScene().getWindow();
                    stage.close();}


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
        Valider.setOnAction(event ->{
            try {
                handleSaveButton(true);
                Duration duree = Duration.between(creneau.getHeureDebut(),creneau.getHeureFin());

                if(tache instanceof TacheDecomposee){
                    List<Taches> listeTache= new ArrayList<>();
                    listeTache.add(tache);
                    user.PlanningActuelleJour(jour.getDate()).Planification(listeTache,jour.getDate());
                    listeTache.clear();
                }
                else {
                    if(duree.compareTo(tache.getDuree())>=0)
                        user.PlanningActuelleJour(jour.getDate()).FindCreneauTacheSimple(tache,jour.getDate());

                }
                if (!erreursPresentes()){
                    Stage stage = (Stage) NvTache.getScene().getWindow();
                    stage.close();}
                else
                    throw new HnadleException("erreur dans le remplissage ");

            } catch (HnadleException e) {
               // throw new RuntimeException(e);
            }
        });
        Annuler.setOnAction(event -> {
            Stage stage = (Stage) NvTache.getScene().getWindow();
            stage.close();});
    }
    public void VisualiserTache(){
        label.setText("Ma tâche");
        NvTache.setVisible(false);
        simple.setDisable(true);
        decomposable.setDisable(true);
        Valider.setOnAction(e1 -> {
            try {
                handleSaveButton(false);
                UpdateEncouragement();
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
                this.tache=t;
                listeTache.add(t);
                if (jour!=null) {
                    jour.addtache(t);// ajouter dans la liste de tache de la journee
                }
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



