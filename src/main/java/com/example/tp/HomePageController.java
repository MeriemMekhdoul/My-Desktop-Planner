package com.example.tp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        // Obtenir la date actuelle (mois et année)
        LocalDate currentDate = LocalDate.now();
        int numeroMois = currentDate.getMonthValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM - yyyy");
        String moisAnnee = currentDate.format(formatter);
        MoisAnnee.setText(moisAnnee);

        try {
            remplirGrille(new Mois(numeroMois));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Planning plan= new Planning();
        plan.TrieListeTache(user.getTacheList());
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

        // Boucle pour parcourir toutes les journées du mois
        for (int i = 1; i <= journees.size(); i++) {
          //  System.out.println("journee"+i);
            Journee journee = journees.get(i-1);

            // Crée une instance de JourneeController et appelle la méthode setJournee avec la journée correspondante
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Journee.fxml"));
            VBox jour = fxmlLoader.load();
            JourneeController journeeController = fxmlLoader.getController();
            //JourneeController journeeController = new JourneeController();
            journeeController.setDate(journee.getDate());
            journeeController.setCreneau(journee);

            // Ajoute la journée à la grille en respectant les critères de positionnement
            Button btn = new Button("test "+i);
            int colonne = journee.getDate().getDayOfWeek().getValue() % 7;
            int ligne = i / 7; // Calcule la ligne en fonction de l'indice de la journée dans le mois

            mois.add(journeeController,colonne ,ligne);
        }
    }
}
