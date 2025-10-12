package com.example.programavimotechnologijosprif.fxControllers;

import com.example.programavimotechnologijosprif.hibernateControllers.GenericHibernate;
import com.example.programavimotechnologijosprif.model.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class UserForm {
    @FXML public TextField loginField;
    @FXML public PasswordField passwordField;
    @FXML public TextField nameField;
    @FXML public TextField surnameField;
    @FXML public TextField phoneNumberField;
    @FXML public RadioButton userRadio;
    @FXML public RadioButton restaurantRadio;
    @FXML public RadioButton driverRadio;
    @FXML public RadioButton clientRadio;
    @FXML public AnchorPane restaurantPane;
    @FXML public AnchorPane driverPane;
    @FXML public AnchorPane clientPane;
    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;

    public void setData(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
    }

    public void createUser(ActionEvent actionEvent) {
        if(userRadio.isSelected()){
            Admin admin = new Admin(loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText());
            genericHibernate.createEntity(admin);
        } else if(restaurantRadio.isSelected()){
            Restaurant restaurant = new Restaurant();
            genericHibernate.createEntity(restaurant);
        } else if(driverRadio.isSelected()){
            Driver driver = new Driver();
            genericHibernate.createEntity(driver);
        } else if(clientRadio.isSelected()){
            AppUser appUser = new AppUser();
            genericHibernate.createEntity(appUser);
        }
    }

    public void disableFields(ActionEvent actionEvent) {
        if(userRadio.isSelected()) {
            restaurantPane.setVisible(false);
            driverPane.setVisible(false);
            clientPane.setVisible(false);
        } else if(restaurantRadio.isSelected()) {
            restaurantPane.setVisible(true);
            driverPane.setVisible(false);
            clientPane.setVisible(false);
        } else if(driverRadio.isSelected()) {
            restaurantPane.setVisible(false);
            driverPane.setVisible(true);
            clientPane.setVisible(false);
        } else if(clientRadio.isSelected()) {
            restaurantPane.setVisible(false);
            driverPane.setVisible(false);
            clientPane.setVisible(true);
        }
        
    }
}
