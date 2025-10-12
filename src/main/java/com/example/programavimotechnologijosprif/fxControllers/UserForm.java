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
        final String alertTitle = "Netinkamas login";
        final String alertMessage = "Toks login jau egzistuoja, prasome ivesti kitoki login";
        if(userRadio.isSelected()){
            Admin admin = new Admin(loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText());
            if(countEmptyFields() != 0) generateEmptyInputAlert();
            else genericHibernate.createEntity(admin, alertTitle, alertMessage);
        } else if(restaurantRadio.isSelected()){
            Restaurant restaurant = new Restaurant(); // update me
            genericHibernate.createEntity(restaurant, alertTitle, alertMessage);
        } else if(driverRadio.isSelected()){
            Driver driver = new Driver(); // update me
            genericHibernate.createEntity(driver, alertTitle, alertMessage);
        } else if(clientRadio.isSelected()){
            AppUser appUser = new AppUser(); // update me
            genericHibernate.createEntity(appUser, alertTitle, alertMessage);
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

    //================================================================
    //====================FUNCTIONAL METHODS==========================
    //================================================================
    private boolean isLoginEmpty(){
        if(loginField.getText().isEmpty()){
            //FxUtils.generateAlert(Alert.AlertType.ERROR, "Error Window", "Prasome ivesti reikiamus laukus");
            return true;
        }
        return false;
    }
    private boolean isPasswordEmpty(){
        if(passwordField.getText().isEmpty()){
            return true;
        }
        return false;
    }
    private boolean isNameEmpty(){
        if(nameField.getText().isEmpty()){
            return true;
        }
        return false;
    }
    private boolean isSurnameEmpty(){
        if(surnameField.getText().isEmpty()){
            return true;
        }
        return false;
    }
    private boolean isPhoneNumberEmpty(){
        if(phoneNumberField.getText().isEmpty()){
            return true;
        }
        return false;
    }
    private String getEmptyFieldsMessage(){
        String emptyFieldsMessage = "";
        int emptyFields = countEmptyFields();
        if(emptyFields == 0)return emptyFieldsMessage;
        if(isLoginEmpty()){
            emptyFields--;
            emptyFieldsMessage += (emptyFields == 0 ? " login" : " login,");
        }
        if(isPasswordEmpty()){
            emptyFields--;
            emptyFieldsMessage += (emptyFields == 0 ? " password" : " password,");
        }
        if(isNameEmpty()){
            emptyFields--;
            emptyFieldsMessage += (emptyFields == 0 ? " name" : " name,");
        }
        if(isSurnameEmpty()) {
            emptyFields--;
            emptyFieldsMessage += (emptyFields == 0 ? " surname" : " surname,");
        }
        if(isPhoneNumberEmpty()) {
            emptyFields--;
            emptyFieldsMessage += (emptyFields == 0 ? " phone number" : " phone number,");
        }
        return emptyFieldsMessage;
    }
    private int countEmptyFields(){
        int emptyFields = 0;
        if(isLoginEmpty())emptyFields++;
        if(isPasswordEmpty())emptyFields++;
        if(isNameEmpty())emptyFields++;
        if(isSurnameEmpty())emptyFields++;
        if(isPhoneNumberEmpty())emptyFields++;
        return emptyFields;
    }
    private void generateEmptyInputAlert(){
        FxUtils.generateHeaderAlert(Alert.AlertType.ERROR, "Input Error", "Netinkama ivestis",
                "Prasome ivesti: " + getEmptyFieldsMessage() + " laukus");
    }
}
