package com.example.programavimotechnologijosprif.fxControllers;

import com.example.programavimotechnologijosprif.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserForm {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public TextField phoneNumberField;
    public void createUser(ActionEvent actionEvent) {
        User user = new User(loginField.getText(),  passwordField.getText(), nameField.getText(),
                surnameField.getText(), phoneNumberField.getText());
        System.out.println(user);
    }
}
