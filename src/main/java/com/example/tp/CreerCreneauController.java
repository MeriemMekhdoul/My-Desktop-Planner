package com.example.tp;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class CreerCreneauController implements Initializable {

    private Creneau nvCreneau;
    @FXML
    private TextField HD;
    @FXML
    private TextField HF;
    @FXML
    private Button ajoutcreneau;
    @FXML
    private RadioButton ajr;
    @FXML
    private VBox defineperiode;
    @FXML
    private RadioButton periode;
    @FXML
    private TextField periodicite;
    @FXML
    private DatePicker pickDebut;
    @FXML
    private DatePicker pickFin;
    @FXML
    private ToggleGroup test;
    @FXML
    private Button annuler;
    @FXML
    private Button confirmer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //afficher le choix de periode si option périodicité choisie
        defineperiode.visibleProperty().bind(periode.selectedProperty());

    }

    @FXML
    private void handleConfirmButton(){
        //s'il confirme les données saisies on crée un nv créneau
        this.nvCreneau = new Creneau();
                    //get les infos entrées et remplir le créneau crée
        String heure = HD.getText();
        LocalTime time = LocalTime.parse(heure);  //récup la donnée entrée sous le format HH:mm
        nvCreneau.setHeureDebut(time);
        heure = HF.getText();
        time = LocalTime.parse(heure);  //récup la donnée entrée sous le format HH:mm
        nvCreneau.setHeureFin(time);

        if (ajr.isSelected()){
            //créer nvl journee, lui attribuer ce créneau comme creneau libre
            Date date = new Date();
            nvCreneau.setDate(date);
            Journee journee = new Journee(date);
            journee.addCreneauLibre(nvCreneau);
        }

        Réinitialiser();
    }
    @FXML
    private void handleCancelButton(){
        this.nvCreneau = null;
        Réinitialiser();
    }
    public void Réinitialiser(){
        //réinitialiser les champs
        HD.setText(":");
        HF.setText(":");
        ajr.setSelected(false);
        periode.setSelected(false);
        periodicite.setText("0");
    }
    public Creneau getNvCreneau() {
        return nvCreneau;
    }

    public void setNvCreneau(Creneau nvCreneau) {  //inutile
        this.nvCreneau = nvCreneau;
    }
}
