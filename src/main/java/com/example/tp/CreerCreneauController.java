package com.example.tp;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.Calendar;
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
    private Button confirmer;
    private User user= new User();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //afficher le choix de periode si option périodicité choisie
        defineperiode.visibleProperty().bind(periode.selectedProperty());

        /** *************************************************************** **/
        //test créer calender user à mettre par la suite dans l'authentification
        boolean calendrierExistant = false;
        for (Calendrier calendrier : user.getCalendriers()) {
            if (calendrier.getAnnee() == Year.now().getValue()) {
                calendrierExistant = true;
                // Utilisez l'objet calendrier existant
                break;
            }
        }
        if (!calendrierExistant) {
            System.out.println("nv calender crée");
            Calendrier nouveauCalendrier = new Calendrier(Year.now().getValue());
            user.getCalendriers().add(nouveauCalendrier);
            user.getCalendriers().get(0).afficherCalendrier();
        }
        /** *************************************************************** **/

    }

    @FXML
    private void handleConfirmButton(){
        if (!Objects.equals(HD.getText(), ":") && !Objects.equals(HF.getText(), ":")){

        //s'il confirme les données saisies on crée un nv créneau
        this.nvCreneau = new Creneau();
                    //get les infos entrées et remplir le créneau crée
        String heure = HD.getText();
        LocalTime time = LocalTime.parse(heure);  //récup la donnée entrée sous le format HH:mm
        nvCreneau.setHeureDebut(time);
        heure = HF.getText();
        time = LocalTime.parse(heure);  //récup la donnée entrée sous le format HH:mm
        nvCreneau.setHeureFin(time);

        if (ajr.isSelected() || (!ajr.isSelected() && !periode.isSelected())){
            //get la date d'aujourd'hui
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            LocalDate date = currentDate.toInstant().atZone(calendar.getTimeZone().toZoneId()).toLocalDate();

            /*UserManager.getUser()*/user.getCalendar(Year.now().getValue()).getJournee(date).addCreneauLibre(nvCreneau);
            //nvCreneau.setDate(date);
            }
        else if (periode.isSelected()) {
            // Si le radio bouton "periode" est sélectionné
            LocalDate debut = pickDebut.getValue();
            LocalDate fin = pickFin.getValue();
            int periodicite_ = Integer.parseInt(periodicite.getText());

            if (debut != null && fin != null) {  //si la periode est définie
                Periode periode = new Periode(debut, fin);
                setCreneauPeriodique(nvCreneau,periode,periodicite_);
            }
            else {  //si la période n'est pas définie

                //get date actuelle :
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                LocalDate today = currentDate.toInstant().atZone(calendar.getTimeZone().toZoneId()).toLocalDate();
                Periode periode_;

                if (debut != null){  //il a spécifié que le debut de la periode
                    Year year_ = Year.of(debut.getYear());
                    LocalDate endOfYear_ = year_.atMonth(12).atEndOfMonth();
                    periode_ = new Periode(debut,endOfYear_);
                    setCreneauPeriodique(nvCreneau,periode_,periodicite_);
                }
                else if (fin != null)
                    {  //il a spécifié que la fin de la période
                    periode_ = new Periode(today,fin);
                    setCreneauPeriodique(nvCreneau,periode_,periodicite_);
                    } else //il n'a rien spécifié ni début ni fin
                        {  //période par défaut : aujourd'hui jusqu'à fin d'année
                            Year year = Year.of(today.getYear());    //get date fin d'année
                            LocalDate endOfYear = year.atMonth(12).atEndOfMonth();
                            periode_ = new Periode(today,endOfYear);
                            setCreneauPeriodique(nvCreneau,periode_,periodicite_);
                        }
                }
            }
        } else   //s'il n'a entré aucune donnée
            System.out.println("Aucune donnée saisie");

        Réinitialiser();
    }

    public void setCreneauPeriodique(Creneau creneau, Periode periode, int periodicite){
        LocalDate debut = periode.getDateDebut();
        LocalDate fin = periode.getDatefin();

        Calendrier calendrier;
        LocalDate date = debut;
        while (!date.isAfter(fin)) {
            Creneau creneau_ = new Creneau(creneau.getHeureDebut(), creneau.getHeureFin());
            int year = date.getYear();
            calendrier = /*UserManager.getUser()*/ user.getCalendar(year);

            if (calendrier != null) {  //le calendrier existe
                calendrier.getJournee(date).addCreneauLibre(creneau_);
            } else {   //on a dépassé l'année courante, on crée un nouveau calendrier pour la nouvelle année
                calendrier = new Calendrier(year);
                /*UserManager.getUser()*/ user.newCalender(calendrier);
                calendrier.getJournee(date).addCreneauLibre(creneau_);
            }
            date = date.plusDays(periodicite);
        }
    }
    @FXML
    private void handleCancelButton(){
        this.nvCreneau = null;
        Réinitialiser();
        //afficher tous les calendriers du user
        System.out.println("AFFICHAGE DES CALENDRIERS");
        if (user.getCalendriers()!=null){
            for (Calendrier cal: user.getCalendriers()) {
                System.out.println("cal 1 year : "+cal.getAnnee());
                cal.afficherCalendrier();
            }
        }else
            System.out.println("liste nulle, aucun calendrier existant");
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
