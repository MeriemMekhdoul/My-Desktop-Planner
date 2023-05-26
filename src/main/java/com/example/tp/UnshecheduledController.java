package com.example.tp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UnshecheduledController implements Initializable {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox tacheContainer;
    private User user;
    @FXML
    private Button annuler;

    @FXML
    private Button close;

    @FXML
    private Button replanifier;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user=UserManager.getUser();
        Image image = new Image(getClass().getResourceAsStream("/image/recycle-bin.png"));
        ImageView imageView = new ImageView(image);

        for(Taches tache: user.getUnsheduledTaches()){
            System.out.println("controller unshecheduled");
            Button tacheButton = new Button();
            tacheButton.setText(tache.getName());
            Button suppTache= new Button();
            suppTache.setGraphic(imageView);
            HBox hBoxcontainer= new HBox(20);
            tacheContainer.getChildren().add(hBoxcontainer);
            suppTache.setOnAction(event -> {
                tacheContainer.getChildren().remove(hBoxcontainer);

            });
            tacheButton.setOnAction(event -> {
                FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("Tache.fxml"));
                fxmlLoader.setControllerFactory(obj -> new TacheController(tache));
                Parent root1 = null;
                try {
                    root1 = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                TacheController tacheController= fxmlLoader.getController();
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();
                tacheController.VisualiserTache();
            });


        }
        close.setOnAction(event -> {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
        });
        annuler.setOnAction(event ->{
            Stage stage = (Stage) annuler.getScene().getWindow();
            stage.close();
        });

    }

}
