package com.example.tp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class TacheValidation implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox tacheContainer;
    private User user;
    private List<Taches> tachesList;
    @FXML
    private Button Unshecheduled;
    @FXML
    private Button Valider;
    public TacheValidation(List<Taches> t) {
        this.tachesList = t;
    }
    @FXML
    private Button annuler;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = UserManager.getUser();
        List<Taches> TempList =new ArrayList<>();
        TempList.addAll(tachesList);
        for (Taches tache : tachesList) {
            Button tacheButton = new Button();
            tacheButton.setText(tache.getName() + " " + tache.getCreneau().getDate() + "  " + tache.getCreneau().getHeureDebut() + "  " + tache.getCreneau().getHeureFin());
            Button suppTache = new Button("Supprimer");
            HBox hBoxcontainer = new HBox(20);
            hBoxcontainer.getChildren().addAll(tacheButton,suppTache);
            tacheContainer.getChildren().addAll(hBoxcontainer);
            suppTache.setOnAction(event -> {
                tacheContainer.getChildren().remove(hBoxcontainer);
                TempList.remove(tache);
                RefuserTache(tache);
                for (Taches t: user.getUnsheduledTaches()){
                    System.out.println("Unscheduled="+t.getName());
                }
                for (Taches t1: user.TrouverPlanning(tache).getTacheList()){
                    System.out.println("tache du planning ="+t1.getName());
                }
            });
            tacheButton.setOnAction(event -> {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Tache.fxml"));
                fxmlLoader.setControllerFactory(obj -> new TacheController(tache));
                Parent root1 = null;
                try {
                    root1 = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                TacheController tacheController = fxmlLoader.getController();
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();
                tacheController.VisualiserTache();
            });
            Valider.setOnAction(event -> {
                Stage stage = (Stage) Valider.getScene().getWindow();
                stage.close();
            });
            Unshecheduled.setOnAction(event -> {
                System.out.println("liste de taches non planifiées");
                FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("ListeTacheUnshecheduled.fxml"));
                Parent root1 = null;
                try {
                    root1 = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();
            });

        }
        annuler.setOnAction(event ->{
            for (Taches taches : TempList) {
                RefuserTache(taches);
            }
            Stage stage = (Stage) Valider.getScene().getWindow();
            stage.close();
        });

    }

    public List<Taches> getTachesList() {
        return tachesList;
    }

    public void setTachesList(List<Taches> tachesList) {
        this.tachesList = tachesList;
    }
    public  void RefuserTache(Taches tache){

        user.SuppTache(tache);
        if (!user.getUnsheduledTaches().contains(tache))
             user.getUnsheduledTaches().add(tache);
        LocalDate date = tache.getCreneau().getDate();
        Journee journee= user.getCalendar(date.getYear()).getJournee(date);
        journee.suppCreneauPris(tache.getCreneau());
        journee.addCreneauLibre(tache.getCreneau());
        Planning plan= user.TrouverPlanning(tache);
        plan.getTacheList().remove(tache);
        tache.setCreneau(null);
    }
}