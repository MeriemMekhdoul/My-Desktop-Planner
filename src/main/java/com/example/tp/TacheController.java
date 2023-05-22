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

    private List<Taches> listeTache;
    public TacheController(Taches t){
        this.tache=t;
    }
    public TacheController(Planning planning){
        this.planning=planning;
    }

    public TacheController(){}

    @FXML
    private void handleSaveButton() {
        boolean isBlocked = bloquee.isSelected();
        StringConverter<LocalTime> converter = new LocalTimeStringConverter();
        String durationText = Duree.getText();
        LocalTime localTime = converter.fromString(durationText);
        Categorie catg = categorie.getValue();
        Priorite prio = priorite.getValue();
        LocalDate selectedDate = deadline.getValue();
        if (tache==null) {
            // Check the state of the checkboxes
            boolean isSimple = simple.isSelected();
            boolean isDecomposable = decomposable.isSelected();


            if (isSimple && !isDecomposable) {
                Taches tacheSimple = new TacheSimple();
                if (selectedDate != null) {
                   // Date date = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    tacheSimple.setDeadline(selectedDate);
                }
                if (Duree.getText().isEmpty()) {
                    Label error = new Label("vous devez saisir la durée de votre tache");
                    Vbox.getChildren().add(2, error);
                    error.setStyle("-fx-text-fill:red;");
                }
                else{
                    if (localTime != null) {
                        LocalTime referenceTime = LocalTime.MIDNIGHT;
                        Duration duration = Duration.between(referenceTime, localTime);
                        tacheSimple.setDuree(duration);
                    } else {
                        // Invalid input, handle accordingly
                        System.out.println("Invalid duration input");
                    }

                }
                tacheSimple.setName(NomTache.getText());
                tacheSimple.setCategorie(catg);
                tacheSimple.setPriorite(prio);
                tacheSimple.setEtat(Etat.NOT_REALIZED);
                tacheSimple.creneau.setBloque(isBlocked);
                user.addTache(tacheSimple);
                listeTache.add(tacheSimple);/** est ce que ns7a9ha???**/
                if (planning!= null)
                {planning.addtache(tacheSimple); }
            } else if (!isSimple && isDecomposable) {
                Taches tacheDecomposee = new TacheDecomposee();
                if (selectedDate != null) {
                    //Date date = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    tacheDecomposee.setDeadline(selectedDate);
                }
                tacheDecomposee.setName(NomTache.getText());
                System.out.println(tacheDecomposee.getName());
                Label error = new Label("vous devez saisir la durée de votre tache");

                if (Duree.getText().isEmpty()) {
                    Vbox.getChildren().add(1, error);
                    error.setStyle("-fx-text-fill:red;");

                }
                else{
                    if (localTime != null) {
                        LocalTime referenceTime = LocalTime.MIDNIGHT;
                        Duration duration = Duration.between(referenceTime, localTime);
                        tacheDecomposee.setDuree(duration);
                    } else {
                        // Invalid input, handle accordingly
                        System.out.println("Invalid duration input");
                    }

                System.out.println(tacheDecomposee.getDuree());
                }
                System.out.println(Duree.getText());
                tacheDecomposee.setCategorie(catg);
                tacheDecomposee.setPriorite(prio);
                System.out.println(tacheDecomposee.getPriorite());
                tacheDecomposee.setEtat(Etat.NOT_REALIZED);
                System.out.println(tacheDecomposee.getEtat());
                tacheDecomposee.creneau.setBloque(isBlocked);
                user.addTache(tacheDecomposee);
                listeTache.add(tacheDecomposee);
                if (planning!= null)
                {planning.addtache(tacheDecomposee); }
            } else {
                Label error = new Label("vous devez choisir un type pour votre tache");
                Vbox.getChildren().add(5, error);
                error.setStyle("-fx-text-fill:red;");
            }
        }
        else {
            // Modifying an existing task
            // Update the task's attributes based on user input

            tache.setName(NomTache.getText());
            if (localTime != null) {
                LocalTime referenceTime = LocalTime.MIDNIGHT;
                Duration duration = Duration.between(referenceTime, localTime);
                tache.setDuree(duration);
            } else {
                // Invalid input, handle accordingly
                System.out.println("Invalid duration input");
            }
            tache.setCategorie(catg);
            tache.setPriorite(prio);
            tache.setEtat(etat.getValue());
            if (selectedDate != null) {
                tache.setDeadline(selectedDate);
            }
            simple.setDisable(true);
            decomposable.setDisable(true);
            tache.creneau.setBloque(isBlocked);
        }
    }
    private User user;
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
        return tache;
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
            handleSaveButton() ;
            /**ici faire la redirection vers le module qui genere auto une tache**/
            Stage stage =(Stage) NvTache.getScene().getWindow();
            stage.close();
            for (Taches t: user.getTacheList()){
                System.out.println("Etat="+t.getEtat());
                System.out.println("Duree="+t.getDuree());
                System.out.println("prio="+t.getPriorite());
                System.out.println("name="+t.getName());
                System.out.println("deadline="+t.getDeadline());
            }});
        Annuler.setOnAction(event -> handleCancelButton());
    }

    @FXML
    private HBox EtatHbox;
    public void VisualiserTache(){
        NvTache.setVisible(false);
        Valider.setOnAction(event ->{
            handleSaveButton();
            Stage stage =(Stage) NvTache.getScene().getWindow();
            stage.close();
        } );
        Annuler.setOnAction(event -> handleCancelButton());
    }

    public void Réinitialiser(){
        //réinitialiser les champs
        NomTache.setText("");
        simple.setSelected(false);
        decomposable.setSelected(false);
        Duree.setText("0");
        bloquee.setSelected(false);
    }
    // Other methods, event handlers, etc.
}


