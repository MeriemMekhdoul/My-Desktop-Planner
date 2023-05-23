package com.example.tp;
import javafx.scene.control.Alert;


public class HnadleException extends Exception{

        public  HnadleException(String message) {
            super(message);
        }

        public void displayErrorMessage() {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(getMessage());
            alert.showAndWait();
        }
    }


