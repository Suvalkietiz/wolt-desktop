package com.example.programavimotechnologijosprif.fxControllers;

import com.example.programavimotechnologijosprif.HelloApplication;
import com.example.programavimotechnologijosprif.Utils.FxUtils;
import com.example.programavimotechnologijosprif.hibernateControllers.CustomHibernate;
import com.example.programavimotechnologijosprif.model.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

public class LoginForm {
    @FXML public TextField loginField;
    @FXML public PasswordField passwordField;


    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("main_database");

    public void validateAndLoad(ActionEvent actionEvent) throws IOException {
        // istraukti data is formos lauku ir patikrinti ar duomenu bazeje yra atitikmuo, ar teisingi
        // sekmes atveju pereinama prie main-form
        CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
        User user = customHibernate.getUserByCrediantials(loginField.getText(), passwordField.getText());
        if (user != null) {
            //sekmes atvejis
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-form.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setTitle("Main Form");
            stage.setScene(scene);
            stage.show();
        } else {
            //cia noriu naudoti alertus
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "User login", "No such user");
        }
    }

    public void registerNewUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));

        // jei nori pasiaiskink papildomai sitas tris eilutes
        Parent parent = fxmlLoader.load();
        UserForm userForm = (UserForm) fxmlLoader.<UserForm>getController();
        userForm.setData(entityManagerFactory);

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("User Form");
        stage.setScene(scene);
        stage.show();
    }
}
