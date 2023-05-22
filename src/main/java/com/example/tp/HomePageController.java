package com.example.tp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {
    private User user;
    @FXML
    private VBox VboxFixe;
    @FXML
    private Label MoisAnnee;
    @FXML
    private Button SetTache;
    @FXML
    private Button ajouterTache;
    @FXML
    private GridPane mois;
    @FXML
    private Button suiv;
    @FXML
    private Button back;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user=UserManager.getUser();
        System.out.println("passward="+user.getPassward()+"     name="+user.getPseudo());
        ajouterTache.setOnAction(event -> {
            try {
                Creetache(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        SetTache.setOnAction(event -> {
            try {
                Creetache(false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


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


        // Obtenir la date actuelle (mois et année)
        LocalDate currentDate = LocalDate.now();
        int numeroMois = currentDate.getMonthValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM - yyyy");
        String moisAnnee = currentDate.format(formatter);
        MoisAnnee.setText(moisAnnee);

        try {
            remplirGrille(user.getCalendar(currentDate.getYear()).getMois(numeroMois));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void Creetache(Boolean verifie) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("Tache.fxml"));
        Parent root1 = fxmlLoader.load();
        TacheController tacheController= fxmlLoader.getController();
        if(verifie){
        VBox newVbox= new VBox();
        Button ajouterManu = new Button("Manuellement");
        Button ajouterAuto = new Button("Automatiquement");
        newVbox.getChildren().addAll(ajouterManu,ajouterAuto);
        newVbox.setAlignment(Pos.TOP_CENTER);
        VboxFixe.getChildren().add(3,newVbox);
            ajouterAuto.setOnAction(event -> {
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();
                tacheController.AjouterUneSeulTache();
            });

            ajouterManu.setOnAction(event -> System.out.println("Ajouter manuellement"));
            /**  ici il faut lui ouvrir le calendrier et lui demander de choisir un des creneaux libre qui seront highlited then on ouvre le fxml Tache to fill the informations **/

        }
        else {
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            tacheController.NvSetTaches();
        }
    }
    public void VisualiserTache(Taches Tache) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("Tache.fxml"));
        fxmlLoader.setControllerFactory(obj -> new TacheController(Tache));
        Parent root1 = fxmlLoader.load();
        TacheController tacheController= fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
        tacheController.VisualiserTache();

    }
    public void remplirGrille(Mois month) throws IOException {
        List<Journee> journees = month.getJournees(); // Obtient la liste des journées du mois
        int premierJourMois = journees.get(0).getDate().getDayOfWeek().getValue(); // Obtention du premier jour du mois
        mois.getChildren().clear(); //vider la grille avant de la remplir de nouveau

        // Boucle pour parcourir toutes les journées du mois
        for (int i = 1; i <= journees.size(); i++) {
            System.out.println("journee"+i);
            Journee journee = journees.get(i-1);
            // Création du jour container
            VBox jour = new VBox();
            jour.setAlignment(Pos.TOP_CENTER);
            jour.setPrefSize(250, 250);
            Label dateLabel = new Label(Integer.toString(journee.getDate().getDayOfMonth()));
            dateLabel.setFont(new Font("Calibri", 16));

            VBox creneaucontainer = new VBox();
            creneaucontainer.setStyle("-fx-background-color:pink;");
            creneaucontainer.setSpacing(3);
            setCreneau(journee,creneaucontainer);
            //creneaucontainer.getChildren().addAll(new Button(), new Button(), new Button(), new Button());

            jour.getChildren().add(dateLabel);
            jour.getChildren().add(creneaucontainer);
            //jour.setPadding(new Insets(15, 10, 10, 10));
            VBox.setVgrow(dateLabel, Priority.ALWAYS);
            VBox.setVgrow(creneaucontainer, Priority.ALWAYS);
            VBox.setMargin(creneaucontainer,new Insets(0,0,0,0));

            if (journees.get(0).getDate().getDayOfWeek() == DayOfWeek.SUNDAY ) {
                premierJourMois=0; // Incrémenter la ligne si la journée se situe entre dimanche et le premier jour du mois
            }
            int colonne = journee.getDate().getDayOfWeek().getValue() % 7;
            int ligne = (i+premierJourMois) / 7 ; // Calcule la ligne en fonction de l'indice de la journée dans le mois

            if (journee.getDate().getDayOfWeek() == DayOfWeek.SATURDAY ) {
                ligne--; // Incrémenter la ligne si la journée se situe entre dimanche et le premier jour du mois
            }

            mois.add(jour,colonne ,ligne);
        }
    }

    public void setCreneau(Journee jour, VBox creneaucontainer) {
        List<Creneau> listetemp = new ArrayList<>();
        listetemp.addAll(jour.getCreneauxLibres());
        listetemp.addAll(jour.getCreneauxPris());

        listetemp.sort(Comparator.comparing(Creneau::getHeureDebut));
        String texteBouton = null;
        if (listetemp.isEmpty())
            System.out.println("LISTE CRENEAUX VIDE");

        for (Creneau creneau : listetemp) {
            System.out.println("creneau i TEST TEST");
            if (jour.getCreneauxLibres().contains(creneau)) {
                texteBouton = creneau.getHeureDebut().toString() + " - " + creneau.getHeureFin().toString();
                Button boutonCreneau = new Button(texteBouton);
                boutonCreneau.getStyleClass().add("creneaulibre");

                // Ajouter le bouton à la vbox creneaucontainer
                creneaucontainer.getChildren().add(boutonCreneau);
                boutonCreneau.setMaxWidth(Double.MAX_VALUE);
                VBox.setVgrow(boutonCreneau,Priority.ALWAYS);
            } else
            {   //c'est automatiquement un créneau pris
                List<Taches> taches = jour.getTacheList();
                for (Taches tache : taches) {
                    if (tache.getCreneau() == creneau) {
                        texteBouton = tache.getName();
                        break;
                    }
                }
                Button boutonTache = new Button(texteBouton);
                boutonTache.getStyleClass().add("creneaupris");
                // Ajouter le bouton à la vbox creneaucontainer
                creneaucontainer.getChildren().add(boutonTache);
                creneaucontainer.setStyle("-fx-background-color:pink;");
                boutonTache.setMaxWidth(Double.MAX_VALUE);
                VBox.setVgrow(boutonTache,Priority.ALWAYS);
            }
        }
    }

    public void handleSuivButton() {
        String moisAnnee = MoisAnnee.getText();
        String[] parties = moisAnnee.split(" - ");

        DateTimeFormatter moisAnneeFormatter = DateTimeFormatter.ofPattern("MMMM - yyyy");
        YearMonth moisAnnee_ = YearMonth.parse(moisAnnee, moisAnneeFormatter);
        int annee = Integer.parseInt(parties[1]);

        // Obtenir le mois précédent en soustrayant un mois à la date actuelle
        YearMonth moisSuivant = moisAnnee_.plusMonths(1);

        // Vérifier si le mois précédent est dans une année différente
        if (moisSuivant.getYear() > annee) {
            annee++; // Ajuster l'année en conséquence
        }

        int numeroMoisSuivant = moisSuivant.getMonthValue();


        try {
            // Vérifier si le calendrier pour la nouvelle année existe déjà
            if (user.getCalendar(annee)==null) {
                // Créer un nouveau calendrier pour la nouvelle année
                user.newCalender(new Calendrier(annee));
            }

            remplirGrille(user.getCalendar(annee).getMois(numeroMoisSuivant));

            // Mettre à jour la valeur du label MoisAnnee avec le nouveau mois et l'année
            String moisSuivantTexte = moisSuivant.format(DateTimeFormatter.ofPattern("MMMM"));
            String anneeTexte = Integer.toString(annee);
            MoisAnnee.setText(moisSuivantTexte + " - " + anneeTexte);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleBackButton(){
        String moisAnnee = MoisAnnee.getText();
        String[] parties = moisAnnee.split(" - ");

        DateTimeFormatter moisAnneeFormatter = DateTimeFormatter.ofPattern("MMMM - yyyy");
        YearMonth moisAnnee_ = YearMonth.parse(moisAnnee, moisAnneeFormatter);
        int annee = Integer.parseInt(parties[1]);

        // Obtenir le mois précédent en soustrayant un mois à la date actuelle
        YearMonth moisPrecedent = moisAnnee_.minusMonths(1);

        // Vérifier si le mois précédent est dans une année différente
        if (moisPrecedent.getYear() < annee) {
            annee--; // Ajuster l'année en conséquence
        }

        int numeroMoisPrecedent = moisPrecedent.getMonthValue();


        try {
            // Vérifier si le calendrier pour la nouvelle année existe déjà
            if (user.getCalendar(annee)==null) {
                // Créer un nouveau calendrier pour la nouvelle année
                user.newCalender(new Calendrier(annee));
            }

            remplirGrille(user.getCalendar(annee).getMois(numeroMoisPrecedent));

            // Mettre à jour la valeur du label MoisAnnee avec le nouveau mois et l'année
            String moisPrecedentTexte = moisPrecedent.format(DateTimeFormatter.ofPattern("MMMM"));
            String anneeTexte = Integer.toString(annee);
            MoisAnnee.setText(moisPrecedentTexte + " - " + anneeTexte);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void CreerCreneau() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("CreerCreneau.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }
}
