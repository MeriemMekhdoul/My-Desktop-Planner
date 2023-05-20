package com.example.tp;

import com.example.tp.utilities.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
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
    private void handleSaveButton() {
        boolean isBlocked = bloquee.isSelected();
        if (tache==null) {
            // Check the state of the checkboxes
            boolean isSimple = simple.isSelected();

            boolean isDecomposable = decomposable.isSelected();
            Categorie catg = categorie.getValue();
            Priorite prio = priorite.getValue();
            Etat etat1 = etat.getValue();
            LocalDate selectedDate = deadline.getValue();
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
                    tacheSimple.setDuree(Duree.getText());
                }
                tacheSimple.setName(NomTache.getText());
                tacheSimple.setCategorie(catg);
                tacheSimple.setPriorite(prio);
                tacheSimple.setEtat(etat1);
                tacheSimple.creneau.setBloque(isBlocked);
                user.addTache(tacheSimple);
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
                tacheDecomposee.setDuree(Duree.getText());
                System.out.println(tacheDecomposee.getDuree());
                }
                System.out.println(Duree.getText());
                tacheDecomposee.setCategorie(catg);
                tacheDecomposee.setPriorite(prio);
                System.out.println(tacheDecomposee.getPriorite());
                tacheDecomposee.setEtat(etat1);
                System.out.println(tacheDecomposee.getEtat());
                tacheDecomposee.creneau.setBloque(isBlocked);

                user.addTache(tacheDecomposee);
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
            tache.setDuree(Duree.getText());
            tache.setCategorie(categorie.getValue());
            tache.setPriorite(priorite.getValue());
            tache.setEtat(etat.getValue());
            LocalDate selectedDate = deadline.getValue();
            if (selectedDate != null) {
                tache.setDeadline(selectedDate);
            }
            simple.setDisable(true);
            decomposable.setDisable(true);
            tache.creneau.setBloque(isBlocked);
        }
    }
    User user= new User();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            Duree.setText(tache.duree);
            priorite.setValue(tache.getPriorite());
            etat.setValue(tache.getEtat());
            categorie.setValue(tache.getCategorie());
            deadline.setValue(tache.getDeadline());
        }
    }

    public Taches getTache() {
        return tache;
    }

    public void setTache(Taches tache) {
        this.tache = tache;
    }

    // Other methods, event handlers, etc.
}


