package com.example.tp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.*;
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
    @FXML
    private Button AjoutPlan;
    private Projet projet;
    @FXML
    private TextField dureeMIN;
    private VBox newVbox;
    @FXML
    private  Button nvProjet;
    @FXML
    private  Button replan;
    @FXML
    private Button Unshecheduled,MesTaches;
    @FXML
    private StackPane Stack;
    @FXML
    private ScrollPane scrollPane;
    private VBox TacheContainer ;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        newVbox = new VBox(20);
        user=UserManager.getUser();
        TacheContainer= new VBox(15);
        System.out.println("passward="+user.getPassward()+"     name="+user.getPseudo());
        ajouterTache.setOnAction(event -> {
            try {
                if (!VboxFixe.getChildren().contains(newVbox))
                    VboxFixe.getChildren().add(3, newVbox);
                else{
                    VboxFixe.getChildren().remove(newVbox);
                }

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

        /*************************************************************************/

        MesTaches.setOnAction(event -> VoirTache(user.getTacheList(),false));
        Unshecheduled.setOnAction(event -> VoirTache(user.getUnsheduledTaches(),false));
        AjoutPlan.setOnAction(event -> {
            TacheContainer.getChildren().clear();
            for ( Planning plan : user.getPlanningList()){
                VisualiserPlan(plan);
            }
        });
        replan.setOnAction(event -> {
            user.PlanningActuelleJour(LocalDate.now()).replanification();
        });
        nvProjet.setOnAction(event -> {
            projet= new Projet();
            VoirTache(user.getTacheList(),true);
            Button Valider= new Button("Valider");
            Valider.setStyle("-fx-background-color :  #3F9984; -fx-text-fill: white;");
            Valider.setOnAction(event1 -> {
                user.addPoject(projet);
            });
            TacheContainer.getChildren().add(Valider);

        });
        System.out.println("duree mmin dans la homepage = "+user.getMinCreneau());
        dureeMIN.setText(user.getMinCreneau().toString());
        dureeMIN.setOnAction(event -> {
            StringConverter<LocalTime> converter = new LocalTimeStringConverter();
            LocalTime localTime = converter.fromString(dureeMIN.getText());
            if (localTime != null) {
                LocalTime referenceTime = LocalTime.MIDNIGHT;
                Duration duration = Duration.between(referenceTime, localTime);
                user.setMinCreneau(duration); }
        });

    }
    @FXML
    public  void VisualiserPlan(Planning plan){
        mois.setVisible(false);
        scrollPane.setVisible(true);
        HBox newHbox= new HBox(15);

        user.getBadges().forEach((badge, value) -> {
            Button button =new Button();
            button.setText("Badge:  " + badge.name() + ", Value: " + value);
            button.setAlignment(Pos.CENTER);
            button.setTextAlignment(TextAlignment.CENTER);
            button.setStyle("-fx-background-color: #3F9984; -fx-text-fill: white;");
            newHbox.getChildren().add(button);
            HBox.setHgrow(button, Priority.ALWAYS);
            button.setMaxWidth(Double.MAX_VALUE);
        });//elle est vide
        TacheContainer.getChildren().add(0,new Label("Debut: "+plan.getDateDebut()+"   Fin: "+plan.getDateFin())) ;
       TacheContainer.getChildren().add(1,newHbox);
        VBox.setVgrow(newHbox, Priority.ALWAYS);
        for(Taches tache1 : plan.getTacheList()){
            Button button =new Button();
            button.setText(tache1.getName()+" le  "+tache1.getCreneau().getDate()+" de "+tache1.getCreneau().getHeureDebut()+" à "+tache1.getCreneau().getHeureFin()+" etat: "+tache1.getEtat());
            button.setAlignment(Pos.CENTER);
            button.setTextAlignment(TextAlignment.CENTER);
            TacheContainer.getChildren().add(button);
            VBox.setVgrow(button, Priority.ALWAYS);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setOnAction(event -> {
                try {
                    VisualiserTache(tache1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }
        scrollPane.setContent(TacheContainer);

    }
    @FXML
    public void actualiser(){
        String moisAnnee = MoisAnnee.getText();
        String[] parties = moisAnnee.split(" - ");
        dureeMIN.setText(user.getMinCreneau().toString());
        DateTimeFormatter moisAnneeFormatter = DateTimeFormatter.ofPattern("MMMM - yyyy");
        YearMonth moisAnnee_ = YearMonth.parse(moisAnnee, moisAnneeFormatter);
        int annee = Integer.parseInt(parties[1]);

        try {
            remplirGrille(user.getCalendar(annee).getMois(moisAnnee_.getMonthValue()));
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
    @FXML
    public void RevenirCalendrier(){
        mois.setVisible(true);
        scrollPane.setVisible(false);
    }
    @FXML
    private void NvPlanning() {
        // Create a new stage for the pop-up window
        Stage popupStage = new Stage();

        // Set the modality to APPLICATION_MODAL to block input events for other windows
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Pop-up Window");
        HBox dateStartBox = new HBox(20);
        dateStartBox.setAlignment(Pos.CENTER_LEFT);
        dateStartBox.setPrefSize(631, 50);
        dateStartBox.getStyleClass().add("InfoField");
        dateStartBox.setPadding(new Insets(10, 10, 10, 20));

        Label startDateLabel = new Label("Saisissez la date de début:");
        startDateLabel.setFont(new Font(13));

        DatePicker DD = new DatePicker();

        dateStartBox.getChildren().addAll(startDateLabel, DD);

        HBox dateEndBox = new HBox(36);
        dateEndBox.setAlignment(Pos.CENTER_LEFT);
        dateEndBox.setPrefSize(631, 41);
        dateEndBox.getStyleClass().add("InfoField");
        dateEndBox.setPadding(new Insets(10, 10, 10, 20));

        Label endDateLabel = new Label("Saisissez la date de fin:");
        endDateLabel.setFont(new Font(13));

        DatePicker DF = new DatePicker();

        dateEndBox.getChildren().addAll(endDateLabel, DF);

        Button continueButton = new Button("Terminer");
        continueButton.setStyle("-fx-text-fill: white;" +
                "-fx-background-color: #3F9984;" +
                "-fx-font-family: 'Calibri';" +
                "-fx-background-radius: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-font-weight: bold;"+
                "-fx-font-size: 14px;");
        continueButton.setPrefWidth(100);

        Button AnnulerButton= new Button("Annuler");
        AnnulerButton.setStyle("-fx-text-fill: white;" +
                "-fx-background-color: lightgrey;" +
                "-fx-font-family: 'Calibri';" +
                "-fx-background-radius: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-font-weight: bold;"+
                "-fx-font-size: 14px;");
        AnnulerButton.setPrefWidth(100);

        HBox ButtonHbox = new HBox();
        ButtonHbox.setAlignment(Pos.CENTER);
        ButtonHbox.setSpacing(160);
        ButtonHbox.setFillHeight(true);
        ButtonHbox.getChildren().addAll(AnnulerButton,continueButton);
        Label label = new Label("Creer un nouveau planning");
        label.setStyle("-fx-font-family:'Calibri';"+
                "-fx-font-weight:bold;"+
                "-fx-font-size: 16px;"+
                "-fx-text-fill:#3F9984;");
        // Create the layout for the pop-up window
        VBox popupRoot = new VBox(20);
        // popupRoot.setAlignment(Pos.CENTER);
        popupRoot.setPadding(new Insets(10));
        popupRoot.setAlignment(Pos.CENTER_LEFT);
        popupRoot.getChildren().addAll(label,dateStartBox,dateEndBox, ButtonHbox);
        AnnulerButton.setOnAction(event -> popupStage.close());
        continueButton.setOnAction(event -> {
            try {
                LocalDate dateF = DF.getValue();
                LocalDate dateD = DD.getValue();
                LocalDate currentDate = LocalDate.now();

                if (dateD == null) {
                    dateD = currentDate;
                }

                if (dateD.isBefore(currentDate)) {
                    throw new IllegalArgumentException("La date de début doit être supérieure ou égale à la date d'aujourd'hui.");
                }
                else{
                    popupRoot.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("La date de début doit être supérieure ou égale à la date d'aujourd'hui."));

                }
                for (Planning p : user.getPlanningList()) {
                    System.out.println("je suis dans un planning du user de : "+p.getDateFin()+" à : "+p.getDateFin());
                    //pour chaque planning voir si la date de debut n'est pas dans sa periode
                    if ((dateD.isAfter(p.getDateDebut()) || dateD.equals(p.getDateDebut())) && (dateF.isBefore(p.getDateFin()) || dateF.equals(p.getDateFin())))
                        throw new IllegalArgumentException("La periode entrée est au milieu d'un planning existant ( "+p.getDateDebut()+ " -- "+p.getDateFin() +" ) veuillez choisir une autre période.");
                    else
                        popupRoot.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("La periode entrée est au milieu d'un planning existant ( "+p.getDateDebut()+ " -- "+p.getDateFin() +" ) veuillez choisir une autre période."));
                    if ((dateD.isBefore(p.getDateDebut())) && (dateF.equals(p.getDateFin()) || (dateF.isBefore(p.getDateFin()) && dateF.isBefore(p.getDateDebut()))))
                        throw new IllegalArgumentException("La periode entrée n'est pas valide, vous devez avancer la date de fin avant le "+p.getDateDebut());
                    else
                        popupRoot.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("La periode entrée n'est pas valide, vous devez avancer la date de fin avant le "+p.getDateDebut()));
                    if ((dateD.equals(p.getDateDebut()) || (dateD.isAfter(p.getDateDebut()) && dateD.isBefore(p.getDateFin()))) && dateF.isAfter(p.getDateFin()))
                        throw new IllegalArgumentException("La periode entrée n'est pas valide, vous devez décaler la date de début après le "+p.getDateFin());
                    else
                        popupRoot.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("La periode entrée n'est pas valide, vous devez décaler la date de début après le "+p.getDateFin()));
                }

                if (dateF == null) {
                    throw new IllegalArgumentException("Vous devez sélectionner une date de fin.");
                }
                else{
                    popupRoot.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("Vous devez sélectionner une date de fin."));

                }


                if (dateF.isBefore(dateD)) {
                    throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début.");
                }
                else{
                    popupRoot.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("La date de fin doit être postérieure à la date de début."));

                }
                user.newPlanning(new Planning(dateD, dateF));
                if (popupRoot.getChildren().stream()
                        .anyMatch(node -> node instanceof Label && ((Label) node).getStyle().equals("-fx-text-fill: red;"))){
                    throw new HnadleException("Erreur dans les remplissage des donnees ");

                }else{
                    popupStage.close();}
            }

            catch (IllegalArgumentException e) {
                // Handle the exception here
                showError(e.getMessage(),popupRoot);
            }
            catch (HnadleException e){
                showError(e.getMessage(),popupRoot);
            }
        });
        // Create the scene for the pop-up window
        Scene scene = new Scene(popupRoot, 400, 250);

        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
    public void Creetache(Boolean verifie) throws IOException {
        if (verifie) {
            Button ajouterManu = new Button("Manuellement");
            ajouterManu.setMaxWidth(Double.MAX_VALUE);
            Button ajouterAuto = new Button("Automatiquement");
            ajouterAuto.setMaxWidth(Double.MAX_VALUE);
            ajouterAuto.setStyle("-fx-background-color:#99513f;");
            if (!newVbox.getChildren().stream().anyMatch(node -> node instanceof Button) )
                 newVbox.getChildren().addAll(ajouterManu, ajouterAuto);
            newVbox.setAlignment(Pos.TOP_CENTER);

            ajouterAuto.setOnAction(event -> {
                Stage stage = new Stage();
                try {
                    FXMLLoader fxmlLoaderAuto = new FXMLLoader(getClass().getResource("Tache.fxml"));
                    Parent rootAuto = fxmlLoaderAuto.load();
                    TacheController tacheControllerAuto = fxmlLoaderAuto.getController();
                    tacheControllerAuto.setPlanning(user.PlanningActuelleJour(LocalDate.now()));
                    stage.setScene(new Scene(rootAuto));
                    stage.show();
                    tacheControllerAuto.AjouterUneSeulTache();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            ajouterManu.setOnAction(event -> {
                System.out.println("Ajouter manuellement");
                openPopup();
            });
        } else {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Tache.fxml"));
            Parent root2 = fxmlLoader.load();
            TacheController tacheController = fxmlLoader.getController();
            tacheController.setPlanning(user.PlanningActuelleJour(LocalDate.now()));
            stage.setScene(new Scene(root2));
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
            Journee journee = journees.get(i-1);
            // Création du jour container
            VBox jour = new VBox();
            jour.setAlignment(Pos.TOP_CENTER);
            jour.setPrefSize(250, 250);
            Label dateLabel = new Label(Integer.toString(journee.getDate().getDayOfMonth()));
            dateLabel.setFont(new Font("Calibri", 16));
            ScrollPane scrollPane1= new ScrollPane();
            VBox creneaucontainer = new VBox();
            scrollPane1.setFitToWidth(true);
            scrollPane1.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane1.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane1.setPadding(new Insets(2,2,2,2));
            creneaucontainer.setSpacing(3);
            scrollPane1.setContent(creneaucontainer);
            creneaucontainer.setSpacing(3);
            setCreneau(journee,creneaucontainer);
            jour.getChildren().add(dateLabel);
            jour.getChildren().add(scrollPane1);
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
    public void test(Creneau creneau, Journee jour, boolean vide) throws IOException {
        if (vide){
            Planning plan = user.PlanningActuelleJour(jour.getDate());
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("Tache.fxml"));

            fxmlLoader.setControllerFactory(obj -> new TacheController(creneau));
            Parent root1 = fxmlLoader.load();
            TacheController tacheController=fxmlLoader.getController();
            tacheController.setPlanning(plan);
            tacheController.setJour(jour);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            tacheController.AjouterTacheManu();

        }
        else{
            Taches tache = jour.getTache(creneau);// il ne reconnaît pas créneau ?? jsp prq
            if (tache == null)
                System.out.println("TACHE NULLE ://////////////////////////////");
            else
                VisualiserTache(tache);
        }
    }
    public void setCreneau(Journee jour, VBox creneaucontainer) {
        List<Creneau> listetemp = new ArrayList<>();
        listetemp.addAll(jour.getCreneauxLibres());
        listetemp.addAll(jour.getCreneauxPris());
        listetemp.sort(Comparator.comparing(Creneau::getHeureDebut));
        String texteBouton = null;

        for (Creneau creneau : listetemp) {
            if (jour.getCreneauxLibres().contains(creneau)) {
                texteBouton = creneau.getHeureDebut().toString() + " - " + creneau.getHeureFin().toString();
                Button boutonCreneau = new Button(texteBouton);
                // Ajouter le bouton à la vbox creneaucontainer
                creneaucontainer.getChildren().add(boutonCreneau);
                boutonCreneau.setMaxWidth(Double.MAX_VALUE);
                VBox.setVgrow(boutonCreneau,Priority.ALWAYS);
                boutonCreneau.setOnAction(event -> {
                    try {
                        test(creneau,jour,true);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else
            {   //c'est automatiquement un créneau pris
                List<Taches> taches = jour.getTacheList();
                System.out.println(taches.size());
                for (Taches tache : taches) {

                   System.out.println(tache.getName());
                }
                 for (Taches tache : taches) {
                    if (tache.getCreneau().equals(creneau)) {
                        texteBouton = tache.getName()+"\n"+tache.getCreneau().getHeureDebut()+"-"+tache.getCreneau().getHeureFin();/** y avait une erreur ici jsp prq?? il faisait pas tache.getCreneau == creneau ??**/
                         break;
                    }
                }
                Button boutonTache = new Button(texteBouton );
                boutonTache.setWrapText(true);
                boutonTache.setTextAlignment(TextAlignment.CENTER);
                // Ajouter le bouton à la vbox creneaucontainer
                creneaucontainer.getChildren().add(boutonTache);
                boutonTache.setStyle("-fx-background-color: #3F9984;");
                boutonTache.setMaxWidth(Double.MAX_VALUE);
                VBox.setVgrow(boutonTache,Priority.ALWAYS);
                boutonTache.setOnAction(event -> {
                    try {
                        test(creneau,jour,false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
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
    private void openPopup() {
        // Create a new stage for the pop-up window
        Stage popupStage = new Stage();

        // Set the modality to APPLICATION_MODAL to block input events for other windows
        popupStage.initModality(Modality.APPLICATION_MODAL);

        // Set the title of the pop-up window
        popupStage.setTitle("Pop-up Window");

        // Create a label to display a message in the pop-up window
        Label label = new Label("Choisisez un creneau parmis ceux disponible sur votre calndrier");
        label.setStyle("-fx-font-family: 'Calibri';" +
                "-fx-text-fill: #23574B;"+ "-fx-font-size: 16px;");
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        // Create a button to close the pop-up window
        Button continueButton = new Button("Continue");
        continueButton.setStyle("-fx-text-fill: white;" +
                "-fx-background-color: #3F9984;" +
                "-fx-font-family: 'Calibri';" +
                "-fx-background-radius: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-font-weight: bold;"+
                "-fx-font-size: 16px;");
        continueButton.setPrefWidth(200);
        continueButton.setPrefHeight(40);
        continueButton.setOnAction(event -> popupStage.close());

        // Create the layout for the pop-up window
        VBox popupRoot = new VBox(20);
        popupRoot.setAlignment(Pos.CENTER);
        popupRoot.setPadding(new Insets(10));
        popupRoot.getChildren().addAll(label, continueButton);

        // Create the scene for the pop-up window
        Scene popupScene = new Scene(popupRoot, 400, 150);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
    public void VoirTache(List<Taches> list,Boolean bool){
        mois.setVisible(false);
        scrollPane.setVisible(true);
        TacheContainer.getChildren().clear();

        for(Taches tache1 : list){
            Button button =new Button();
            button.setText(tache1.getName()+" le  "+tache1.getCreneau().getDate()+" de "+tache1.getCreneau().getHeureDebut()+" à "+tache1.getCreneau().getHeureFin()+" etat: "+tache1.getEtat());
            button.setAlignment(Pos.CENTER);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setTextAlignment(TextAlignment.CENTER);
            TacheContainer.getChildren().add(button);
            button.setOnAction(event -> {
                if (bool) {
                    projet.addTache(tache1);
                }
                try {
                    VisualiserTache(tache1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

    } scrollPane.setContent(TacheContainer);
    }
    private void showError(String message,VBox Vbox) {
        // Remove any previous error messages
        Vbox.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getStyle().equals("-fx-text-fill: red;"));

        // Display the error message in red above the elements
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red;");
        Vbox.getChildren().add(1, errorLabel);
    }

}
