package com.example.tp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class FirstUtilisation implements Initializable {

    @FXML
    private DatePicker DD;

    @FXML
    private DatePicker DF;
    private User user;
    @FXML
    private Button AjoutTache;
    @FXML
    private TextField MinCreneau;

    @FXML
    private TextField MinTache;

    @FXML
    private HBox Hbox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user=UserManager.getUser();
        AjoutTache.setOnAction(event -> {
            VBox newVbox= new VBox();
            Button ajouterManu = new Button("Manuellement");
            Button ajouterAuto = new Button("Automatiquement");
            Hbox.getChildren().addAll(ajouterManu,ajouterAuto);
            ajouterAuto.setOnAction(event1 -> {
                try {
                    CreeTache();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });



    }
    @FXML
    public void Terminer(){
        StringConverter<LocalTime> converter = new LocalTimeStringConverter();
        String durationText = MinCreneau.getText();
        LocalTime localTime = converter.fromString(durationText);
        if (localTime != null) {
            LocalTime referenceTime = LocalTime.MIDNIGHT;
            Duration duration = Duration.between(referenceTime, localTime);
            user.setMinCreneau(duration);
            // Use the duration as needed
            System.out.println("Duration: " + duration);
        } else {
            // Invalid input, handle accordingly
            System.out.println("Invalid duration input");
        }
        if (!MinTache.getText().isEmpty()){
        user.setMinTaskDaily(Integer.parseInt(MinTache.getText()));}
        LocalDate dateF= DF.getValue();
        LocalDate dateD= DD.getValue();
        Planning plan= new Planning(dateD,dateF);
        user.newPlanning(plan);
        Stage stage = (Stage) DF.getScene().getWindow();
        stage.close();
        /**ici remplir dans le calendrier de home page**/
    }
    @FXML
    public void CreeCreneau() throws IOException {
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("CreerCreneau.fxml"));
        Parent root1 = fxmlLoader.load();
      // CreerCreneauController creneauController= fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
        /**fermer la page de first utilisation?**/
    }
    public void CreeTache() throws IOException {
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("tache.fxml"));
       // fxmlLoader.setControllerFactory(obj -> new TacheController());
        Parent root1 = fxmlLoader.load();
        TacheController tacheController= fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
        tacheController.NvSetTaches();
        /**fermer la page de first utilisation?**/
    }

}
