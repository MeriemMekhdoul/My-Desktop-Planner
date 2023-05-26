package com.example.tp;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
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
    private Button terminer;
    @FXML
    private VBox structure;
    private User user;
    private Label erreurduree;
    private final Label erreurdateanterieure = new Label("Vous ne pouvez pas programmer un créneau pendant une date antérieure à celle d'aujourd'hui");
    private final Label erreurHeure = new Label("Heure invalide, veuillez entrer une heure sous ce format : HH:mm");
    private final Label erreurCreneauVide = new Label("Creneau vide, vous ne pouvez rien enregistrer");



    public Creneau getNvCreneau() {
        return nvCreneau;
    }
    public void setNvCreneau(Creneau nvCreneau) {  //inutile
        this.nvCreneau = nvCreneau;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = UserManager.getUser();

        erreurduree = new Label("Vous ne pouvez pas programmer un créneau d'une durée inférieure à "+ user.getMinCreneau());


        //afficher le choix de periode si option périodicité choisie
        defineperiode.visibleProperty().bind(periode.selectedProperty());

        erreurdateanterieure.setStyle("-fx-text-fill:#FF0000");
        erreurduree.setStyle("-fx-text-fill:#FF0000");
        erreurHeure.setStyle("-fx-text-fill:#FF0000");
        erreurCreneauVide.setStyle("-fx-text-fill:#FF0000");

        periodicite.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("0")) {
                periodicite.setStyle("-fx-text-fill: red; -fx-border-color: red;");
            } else {
                periodicite.setStyle(""); // Rétablit le style initial
            }
        });
    }

    @FXML
    private void handleConfirmButton(){
        handleAddCreneauButton(); //pour ajouter le créneau dernier créneau saisi

        if (erreursPresentes()) {
            return; // Ne pas fermer le stage si des erreurs sont présentes
        }
        Stage stage = (Stage) terminer.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void handleAddCreneauButton() {
        if (!isCreneauVide()) {
            afficherErreurCreneauVide();
            return;
        }
        enleverErreur();
        if (!isHeureValide()) {
            afficherErreurHeure();
            return;
        }
        enleverErreur();
        if (!isDureeValide()) {
            afficherErreurDuree();
            return;
        }
        // La condition est vérifiée, enlever le message d'erreur s'il est présent
        enleverErreur();

        // Continuer avec la création du créneau
        this.nvCreneau = creerCreneau();

        if (ajr.isSelected() || (!ajr.isSelected() && !periode.isSelected())) {
            {
                ajouterCreneauLibreAujourdhui();
                Réinitialiser();
            }
        } else if (periode.isSelected()) {
            traiterPeriode();
        }
    }
    @FXML
    private void handleCancelButton(){
        this.nvCreneau = null;
        Réinitialiser();
        enleverErreur();
        Stage stage = (Stage) terminer.getScene().getWindow();
        stage.close();
        //afficher tous les calendriers du user
        if (user.getCalendriers()!=null){
            for (Calendrier cal: user.getCalendriers()) {
                System.out.println("cal 1 year : "+cal.getAnnee());
                cal.afficherCalendrier();
            }
        }else
            System.out.println("liste nulle, aucun calendrier existant");
    }
    public void setCreneauPeriodique(Creneau creneau, Periode periode, int periodicite){
        LocalDate debut = periode.getDateDebut();
        LocalDate fin = periode.getDatefin();

        Calendrier calendrier;
        LocalDate date = debut;
        while (!date.isAfter(fin)) {
            Creneau creneau_ = new Creneau(creneau.getHeureDebut(), creneau.getHeureFin());
            int year = date.getYear();
            calendrier = user.getCalendar(year);

            if (calendrier != null) {  //le calendrier existe
                calendrier.getJournee(date).addCreneauLibre(creneau_);
            } else {   //on a dépassé l'année courante, on crée un nouveau calendrier pour la nouvelle année
                calendrier = new Calendrier(year);
                user.newCalender(calendrier);
                calendrier.getJournee(date).addCreneauLibre(creneau_);
            }
            creneau_.setDate(date);
            date = date.plusDays(periodicite);
        }
    }
    private Creneau creerCreneau() {
        Creneau creneau = new Creneau();
        String heure = HD.getText();
        LocalTime time = LocalTime.parse(heure);
        creneau.setHeureDebut(time);
        heure = HF.getText();
        time = LocalTime.parse(heure);
        creneau.setHeureFin(time);
        return creneau;
    }

    private boolean isHeureValide() {
        String heureD = HD.getText();
        String heureF = HF.getText();

        // Vérifier si les heures sont au format HH:mm
        boolean heureDValide = heureD.matches("\\d{2}:\\d{2}");
        boolean heureFValide = heureF.matches("\\d{2}:\\d{2}");

        return heureDValide && heureFValide;
    }

    private boolean isCreneauVide() {
        String heureDebut = HD.getText();
        String heureFin = HF.getText();
        return !Objects.equals(heureDebut, ":") && !Objects.equals(heureFin, ":") && !heureFin.isEmpty() && !heureDebut.isEmpty();
    }

    private boolean isDureeValide() {
        LocalTime timeD = LocalTime.parse(HD.getText());
        LocalTime timeF = LocalTime.parse(HF.getText());
        Duration duree = Duration.between(timeD, timeF);
        return duree.compareTo(user.getMinCreneau()) >= 0;
    }

    private void afficherErreurHeure() {
        if (!structure.getChildren().contains(erreurHeure)) {
            enleverErreur();
            structure.getChildren().add(3, erreurHeure);
        }
    }

    private void afficherErreurDuree() {
        if (!structure.getChildren().contains(erreurduree)) {
            enleverErreur();
            structure.getChildren().add(3, erreurduree);
        }
    }
    private void afficherErreurCreneauVide() {
        if (!structure.getChildren().contains(erreurCreneauVide)) {
            enleverErreur();
            structure.getChildren().add(5, erreurCreneauVide);
        }
    }

    private void enleverErreur() {
        if (structure.getChildren().contains(erreurduree)) {
            structure.getChildren().remove(erreurduree);

        } else if (structure.getChildren().contains(erreurHeure)) {
            structure.getChildren().remove(erreurHeure);

        } else if (structure.getChildren().contains(erreurdateanterieure)) {
            structure.getChildren().remove(erreurdateanterieure);

        } else if (structure.getChildren().contains(erreurCreneauVide)) {
            structure.getChildren().remove(erreurCreneauVide);
        }
    }

    private void ajouterCreneauLibreAujourdhui() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        LocalDate date = currentDate.toInstant().atZone(calendar.getTimeZone().toZoneId()).toLocalDate();
        nvCreneau.setDate(date);
        user.getCalendar(Year.now().getValue()).getJournee(date).addCreneauLibre(nvCreneau);
    }

    private void traiterPeriode() {
        LocalDate debut = pickDebut.getValue();
        LocalDate fin = pickFin.getValue();

        //get date actuelle :
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        LocalDate today = currentDate.toInstant().atZone(calendar.getTimeZone().toZoneId()).toLocalDate();

        // Continuer l'exécution
        int periodicite_ = Integer.parseInt(periodicite.getText());
        if (periodicite_ != 0) {
            if (debut != null && fin != null) {
                Periode periode = new Periode(debut, fin);
                setCreneauPeriodique(nvCreneau, periode, periodicite_);
            } else {
                Periode periode;
                if (debut != null) {
                    periode = new Periode(debut, user.PlanningActuelleJour(LocalDate.now()).getDateFin());
                    setCreneauPeriodique(nvCreneau, periode, periodicite_);
                } else if (fin != null) {
                    periode = new Periode(today, fin);
                    setCreneauPeriodique(nvCreneau, periode, periodicite_);
                } else {
                    if (user.PlanningActuelleJour(LocalDate.now())!=null){
                    periode = new Periode(today, user.PlanningActuelleJour(LocalDate.now()).getDateFin());
                    setCreneauPeriodique(nvCreneau, periode, periodicite_);
                    }else{
                        Year year = Year.of(today.getYear());
                        LocalDate endOfYear = year.atMonth(12).atEndOfMonth();
                        periode = new Periode(today, endOfYear);
                        setCreneauPeriodique(nvCreneau, periode, periodicite_);
                    }
                }
            }
            Réinitialiser();
        } else {
            System.out.println("periodicite nulle");
        }
    }

    private void afficherErreurPeriode() {
        if (!structure.getChildren().contains(erreurdateanterieure)) {
            enleverErreur();
            structure.getChildren().add(5, erreurdateanterieure);
        }
    }

    private void enleverErreurPeriode() {
        if (structure.getChildren().contains(erreurdateanterieure)) {
            structure.getChildren().remove(erreurdateanterieure);
        }
    }
    private Boolean erreursPresentes(){
        return structure.getChildren().contains(erreurdateanterieure) || structure.getChildren().contains(erreurCreneauVide) || structure.getChildren().contains(erreurduree) || structure.getChildren().contains(erreurHeure);
    }

    public void Réinitialiser(){
        //réinitialiser les champs
        HD.setText(":");
        HF.setText(":");
        ajr.setSelected(false);
        periode.setSelected(false);
        periodicite.setText("1");
        periodicite.setStyle("");
    }
}
