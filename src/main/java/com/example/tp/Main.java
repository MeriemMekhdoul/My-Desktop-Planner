package com.example.tp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;

public class Main extends Application {
    private  system s;
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        FileMyDestcktopPlanner DP= new FileMyDestcktopPlanner();
        s= new system();
        File file= new File(System.getProperty("user.home")+"\\MyDesktopPlanner\\Systeme"+"\\Systeme-info.bin");
        if (!file.exists()){
            DP.CreerDossierDescktopPlanner();
        }else {
            s.LoadListUsers();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CreerCreneau.fxml"));
        //fxmlLoader.setControllerFactory(obj -> new authentification(s));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("My Desktop Planner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}