package com.example.tp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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



    }
    @FXML
    private HBox HboxTache;
    @FXML
    public void AjoutTache(){
            HBox hbox= new HBox(10);
            Button ajouterManu = new Button("Manuellement");
            Button ajouterAuto = new Button("Automatiquement");
            hbox.getChildren().addAll(ajouterManu,ajouterAuto);
            HboxTache.setSpacing(22);
            HboxTache.getChildren().remove(AjoutTache);
            HboxTache.getChildren().add(hbox);
            ajouterAuto.setOnAction(event1 -> {
                try {
                    CreeTache();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }
    @FXML
    private VBox Vbox;
    @FXML
    public void Terminer() {
        try {
            StringConverter<LocalTime> converter = new LocalTimeStringConverter();
            String durationText = MinCreneau.getText();
            LocalTime localTime = converter.fromString(durationText);
            if (localTime != null) {
                LocalTime referenceTime = LocalTime.MIDNIGHT;
                Duration duration = Duration.between(referenceTime, localTime);
                if (duration.toMinutes() <= 0) {
                    throw new IllegalArgumentException("La durée minimale doit être supérieure à zéro.");
                }
                user.setMinCreneau(duration);
                // Use the duration as needed
                System.out.println("Duration: " + duration);
            } else {
                throw new IllegalArgumentException("Invalid duration input");
            }

            LocalDate dateF = DF.getValue();
            LocalDate dateD = DD.getValue();
            LocalDate currentDate = LocalDate.now();

            if (dateD == null) {
                dateD = currentDate;
            }

            if (dateD.isBefore(currentDate)) {
                throw new IllegalArgumentException("La date de début doit être supérieure ou égale à la date d'aujourd'hui.");
            }

            if (dateF == null) {
                throw new IllegalArgumentException("Vous devez sélectionner une date de fin.");
            }

            if (dateF.isBefore(dateD)) {
                throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début.");
            }

            user.newPlanning(new Planning(dateD, dateF));

            if (MinTache.getText().isEmpty()) {
                user.setMinTaskDaily(2);
            } else {
                user.setMinTaskDaily(Integer.parseInt(MinTache.getText()));
            }

            Stage stage = (Stage) DF.getScene().getWindow();
            stage.close();

            System.out.println(user.getMinCreneau() + " " + user.getMinTaskDaily());

            /**ici remplir dans le calendrier de home page**/
        } catch (IllegalArgumentException e) {
            // Handle the exception here
            showError(e.getMessage(),1);
        }
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
    private void showError(String message,int i) {
        // Remove any previous error messages
        clearErrorMessages();

        // Display the error message in red above the elements
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red;");
        Vbox.getChildren().add(i, errorLabel);
    }



    private void clearErrorMessages() {
        // Remove any previous error messages from the VBox
        Vbox.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getStyle().equals("-fx-text-fill: red;"));
    }
}
