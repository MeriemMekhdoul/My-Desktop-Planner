package com.example.tp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FirstUtilisation implements Initializable {

    @FXML
    private DatePicker DD;

    @FXML
    private DatePicker DF;
    private User user;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user=UserManager.getUser();

    }
    @FXML
    public void Terminer(){
        LocalDate dateF= DF.getValue();
        LocalDate dateD= DD.getValue();
        Planning plan= new Planning(dateD,dateF);
        user.newPlanning(plan);
        /**ici remplir dans le calendrier de home page**/
    }
    @FXML
    public void CreeCreneau(){

    }
}
