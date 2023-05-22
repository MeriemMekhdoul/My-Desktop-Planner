package com.example.tp;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class authentification implements Initializable {
    @FXML
    private PasswordField MoPass;

    @FXML
    private Button SeConnecter;

    @FXML
    private TextField UserName;

    private system system1;
    @FXML
    private Label statusLabel;

    public void setSystem(com.example.tp.system system) {
        this.system1 = system;
    }
    public authentification(system st){
        this.system1=st;
    }
    public String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    @FXML
    private void handleLoginButton() throws IOException, ClassNotFoundException {
        String username = UserName.getText();
        String password = MoPass.getText();
        System.out.println("pass i entered"+password);
        boolean authenticated = system1.authenticate(username, password);
        if (authenticated) {
            FileMyDestcktopPlanner DP= new FileMyDestcktopPlanner();
            statusLabel.setText("Authentication successful");
            system1.SaveListUsers();
            //i need une redirection iciiiiii vers homepage
            User user= new User();
            File file= new File(System.getProperty("user.home")+"\\MyDesktopPlanner\\UserInfo"+"\\User-info.bin");
            if (!file.exists()){
                DP.CreerDossierDescktopPlanner();
            }else {
                user.LoadUtilisateur();
            }

            UserManager.setUser(user);
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("HomePage.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) SeConnecter.getScene().getWindow();
            stage.setOnCloseRequest((WindowEvent e) -> {
                System.out.println("Le bouton de fermeture a été cliqué !");
                try {
                    UserManager.getUser().SaveInfoUtilisateur();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            // Close the stage
            stage.close();
            Screen screen = Screen.getPrimary();
            stage.setScene(new Scene(root,screen.getBounds().getWidth(),screen.getBounds().getHeight()));
            stage.setFullScreen(true);
            stage.show();
            if (premierUtilisation){
                FXMLLoader fxmlLoader1=new FXMLLoader(getClass().getResource("HomePage.fxml"));
                Parent root1 = fxmlLoader1.load();
                stage.setScene(new Scene(root1));
                stage.show();
            }
        } else {
            statusLabel.setText("Authentication failed");
        }
    }
    @FXML
    private Label label;
    @FXML
    private Button inscriptionButton;
    private  boolean premierUtilisation;

    @FXML
    public void handleRegisterButton() throws IOException {
        label.setText("Inscrivez-vous");
        inscriptionButton.setVisible(false);
        SeConnecter.setText("s'inscrire");
        premierUtilisation=true;
        String username = UserName.getText();
        String password = MoPass.getText();

        // Validate form inputs (e.g., check for empty fields, password complexity)

        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Create a new user object
        User newUser = new User(username, hashedPassword);
        system1.addUser(newUser);
        system1.SaveListUsers();
        // Save the user object to the user repository or database

        // Provide feedback to the user confirming the account creation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Successful");
        alert.setHeaderText("Account Created");
        alert.setContentText("Your account has been successfully created.");
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // system1= new system();
    }
}