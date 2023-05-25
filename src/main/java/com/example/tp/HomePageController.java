package com.example.tp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
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
        for (Taches t: user.getTacheList()){
            System.out.println("name="+t.getName());
            System.out.println("Etat="+t.getEtat());
            System.out.println("Duree="+t.getDuree());
            System.out.println("prio="+t.getPriorite());
            System.out.println("deadline="+t.getDeadline());
        }

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
       // Planning plan = user.PlanningActuelleJour(LocalDate.now());
       // plan.TrieListeTache(user.getTacheList());
        MesTaches.setOnAction(event -> VoirTache(user.getTacheList()));
        Unshecheduled.setOnAction(event -> VoirTache(user.getUnsheduledTaches()));


    }
    @FXML
    public void actualiser(){
        String moisAnnee = MoisAnnee.getText();
        String[] parties = moisAnnee.split(" - ");

        DateTimeFormatter moisAnneeFormatter = DateTimeFormatter.ofPattern("MMMM - yyyy");
        YearMonth moisAnnee_ = YearMonth.parse(moisAnnee, moisAnneeFormatter);
        int annee = Integer.parseInt(parties[1]);

        try {
            remplirGrille(user.getCalendar(annee).getMois(moisAnnee_.getMonthValue()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
   public void Creetache(Boolean verifie) throws IOException {
        if (verifie) {
            VBox newVbox = new VBox(20);
            Button ajouterManu = new Button("Manuellement");
            ajouterManu.setMaxWidth(Double.MAX_VALUE);
            Button ajouterAuto = new Button("Automatiquement");
            ajouterAuto.setMaxWidth(Double.MAX_VALUE);
            newVbox.getChildren().addAll(ajouterManu, ajouterAuto);
            newVbox.setAlignment(Pos.TOP_CENTER);
            VboxFixe.getChildren().add(4, newVbox);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Tache.fxml"));
            Parent root1 = fxmlLoader.load();
            TacheController tacheController = fxmlLoader.getController();

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
          //  System.out.println("journee"+i);
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
            creneaucontainer.setStyle("-fx-background-color:pink;");
            creneaucontainer.setSpacing(3);
            setCreneau(journee,creneaucontainer);
            //creneaucontainer.getChildren().addAll(new Button(), new Button(), new Button(), new Button());

            jour.getChildren().add(dateLabel);
            jour.getChildren().add(scrollPane1);
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
    public void test(Creneau creneau, Journee jour, boolean vide) throws IOException {
        if (vide){
            Planning plan = user.PlanningActuelleJour(jour.getDate());
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("Tache.fxml"));

            if (creneau!= null){
                System.out.println(creneau.getDate());

                System.out.println("_______________************__________");
            }
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
                boutonCreneau.getStyleClass().add("creneaulibre");

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
                    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
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
                boutonTache.getStyleClass().add("creneaupris");
                // Ajouter le bouton à la vbox creneaucontainer
                creneaucontainer.getChildren().add(boutonTache);
                creneaucontainer.setStyle("-fx-background-color:pink;");
                boutonTache.setMaxWidth(Double.MAX_VALUE);
                boutonTache.setStyle("-fx-background-color:red;");
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
    @FXML
    public void CreerCreneau() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("CreerCreneau.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
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
    @FXML
    private Button Unshecheduled,MesTaches;
    @FXML
    private StackPane Stack;
    private ScrollPane scrollPane;
    public void VoirTache(List<Taches> list){
        mois.setVisible(false);
        scrollPane=new ScrollPane();
        VBox TacheContainer= new VBox(10);

       // user.getTacheList(); Je dois sort mais en fonction de quoi ?? j'ai pas access au journees
        for(Taches tache1 : list){
            Button button =new Button();
            button.setText(tache1.getName());
            button.setAlignment(Pos.CENTER);
            button.setTextAlignment(TextAlignment.CENTER);
            TacheContainer.getChildren().add(button);
            button.setOnAction(event -> {
                try {
                    VisualiserTache(tache1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
    }scrollPane.setContent(TacheContainer);
        Stack.getChildren().add(scrollPane);
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
    private void showError(String message,VBox Vbox) {
        // Remove any previous error messages
        Vbox.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getStyle().equals("-fx-text-fill: red;"));

        // Display the error message in red above the elements
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red;");
        Vbox.getChildren().add(1, errorLabel);
    }

}
