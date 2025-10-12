package com.example.programavimotechnologijosprif.fxControllers;

import com.example.programavimotechnologijosprif.Utils.FxUtils;
import com.example.programavimotechnologijosprif.hibernateControllers.GenericHibernate;
import com.example.programavimotechnologijosprif.model.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class UserForm {
    @FXML public RadioButton userRadio;
    @FXML public RadioButton restaurantRadio;
    @FXML public RadioButton driverRadio;
    @FXML public RadioButton clientRadio;

    @FXML public AnchorPane clientBox;
    @FXML public VBox restaurantBox;
    @FXML public VBox driverBox;

    @FXML public TextField loginField;
    @FXML public PasswordField passwordField;
    @FXML public TextField nameField;
    @FXML public TextField surnameField;
    @FXML public TextField phoneNumberField;
    @FXML public TextField addressField;
    @FXML public TextField workHoursField;
    @FXML public DatePicker birthDateField;
    @FXML public TextField drivingLicenseID;

    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;


    public void setData(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
    }

    @FXML public void createUser(ActionEvent actionEvent) {
        final String alertTitle = "Netinkamas login";
        final String alertMessage = "Toks login jau egzistuoja, prasome ivesti kitoki login";
        if(userRadio.isSelected()){
            Admin admin = new Admin(
                    loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText());
            if(countEmptyFields() != 0) generateEmptyInputAlert();
            else genericHibernate.createEntity(admin, alertTitle, alertMessage);
        } else if(restaurantRadio.isSelected()){
            Restaurant restaurant = new Restaurant(
                    loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText(),
                    workHoursField.getText(),
                    new ArrayList<>()); // CHANGE ME
            if(countEmptyFields() != 0) generateEmptyInputAlert();
            else genericHibernate.createEntity(restaurant, alertTitle, alertMessage);
        } else if(driverRadio.isSelected()){
            Driver driver = new Driver(
                    loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText(),
                    drivingLicenseID.getText(),
                    birthDateField.getValue());
            if(countEmptyFields() != 0) generateEmptyInputAlert();
            else genericHibernate.createEntity(driver, alertTitle, alertMessage);
        } else if(clientRadio.isSelected()){
            AppUser appUser = new AppUser(
                    loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText());
            if(countEmptyFields() != 0) generateEmptyInputAlert();
            else genericHibernate.createEntity(appUser, alertTitle, alertMessage);
        }
    }

    @FXML public void disableFields(ActionEvent actionEvent) {
        if(userRadio.isSelected()) {
            clientBox.setVisible(false);
            driverBox.setVisible(false);
            restaurantBox.setVisible(false);
        } else if(restaurantRadio.isSelected()) {
            clientBox.setVisible(true);
            driverBox.setVisible(false);
            restaurantBox.setVisible(true);
        } else if(driverRadio.isSelected()) {
            clientBox.setVisible(true);
            driverBox.setVisible(true);
            restaurantBox.setVisible(false);
        } else if(clientRadio.isSelected()) {
            clientBox.setVisible(true);
            driverBox.setVisible(false);
            restaurantBox.setVisible(false);
        }
        
    }

    //================================================================
    //====================FUNCTIONAL METHODS==========================
    //================================================================
    private boolean isFieldEmpty(TextField textField) {
        if(textField.getText().isEmpty()) return true;
        else return false;
    }

    private String getEmptyFieldsMessage(){
        String emptyFieldsMessage = "";
        int emptyFields = countEmptyFields();
        if(emptyFields == 0)return emptyFieldsMessage;
        if(isFieldEmpty(loginField)){
            emptyFields--;
            emptyFieldsMessage += (emptyFields == 0 ? " login" : " login,");
        }
        if(isFieldEmpty(passwordField)){
            emptyFields--;
            emptyFieldsMessage += (emptyFields == 0 ? " password" : " password,");
        }
        if(isFieldEmpty(nameField)){
            emptyFields--;
            emptyFieldsMessage += (emptyFields == 0 ? " name" : " name,");
        }
        if(isFieldEmpty(surnameField)) {
            emptyFields--;
            emptyFieldsMessage += (emptyFields == 0 ? " surname" : " surname,");
        }
        if(isFieldEmpty(phoneNumberField)) {
            emptyFields--;
            emptyFieldsMessage += (emptyFields == 0 ? " phone number" : " phone number,");
        }
        return emptyFieldsMessage;
    }
    private int countEmptyFields(){
        int emptyFields = 0;
        if(isFieldEmpty(loginField))emptyFields++;
        if(isFieldEmpty(passwordField))emptyFields++;
        if(isFieldEmpty(nameField))emptyFields++;
        if(isFieldEmpty(surnameField))emptyFields++;
        if(isFieldEmpty(phoneNumberField))emptyFields++;
        return emptyFields;
    }
    private void generateEmptyInputAlert(){
        FxUtils.generateHeaderAlert(Alert.AlertType.ERROR, "Input Error", "Netinkama ivestis",
                "Prasome ivesti: " + getEmptyFieldsMessage() + " laukus");
    }
}
