package com.example.tp;

import com.dlsc.formsfx.model.structure.PasswordField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class authentification {
    @FXML
    private PasswordField Password;

    @FXML
    private Button SeConnecter;

    @FXML
    private TextField UserName;

    private com.example.tp.system system;

    public void setSystem(com.example.tp.system system) {
        this.system = system;
    }

    @FXML
    private void handleLoginButton() {
        String username = UserName.getText();
        String password = Password.getValue();
    }
}