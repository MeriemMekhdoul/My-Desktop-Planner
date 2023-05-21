package com.example.tp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {
    @FXML
    private VBox VboxFixe;

    @FXML
    private Button SetTache;
    @FXML
    private Button ajouterTache;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            /**  ici il faut lui ouvrir le calendrier et lui demander de choisir un des creneaux libre qui seron highlited then on ouvre le fxml Tache to fill the informations
**/

        }
        else {
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            tacheController.NvSetTaches();
        }




    }
}
