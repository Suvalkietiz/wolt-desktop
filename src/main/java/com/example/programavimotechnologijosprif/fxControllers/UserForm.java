package com.example.programavimotechnologijosprif.fxControllers;
//TODO

import com.example.programavimotechnologijosprif.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;

import java.util.ArrayList;

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
    public TextField addressField;
    public TextField workHoursField;
    public ListView<Food> dishesField;
    public TextField driverLicenseField;
    public DatePicker birthDateFIeld;
    public RadioButton userRadio;
    public RadioButton restaurantRadio;
    public RadioButton driverRadio;
    public RadioButton clientRadio;
    public ToggleGroup userType;



    public void createUser(ActionEvent actionEvent) {
        if(userRadio.isSelected()){
            User user = new User(
                    loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText());
            System.out.println(user);
        } else if(restaurantRadio.isSelected()){
            Restaurant restaurant = new Restaurant(
                    loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText(),
                    workHoursField.getText(),
                    new ArrayList<>());
            System.out.println(restaurant);
        } else if(driverRadio.isSelected()){
            Driver driver = new Driver(loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText(),
                    driverLicenseField.getText(),
                    birthDateFIeld.getValue());
            System.out.println(driver);
        } else if(clientRadio.isSelected()){
            AppUser appUser = new AppUser(loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText());
            System.out.println(appUser);
        }
    }

    public void disableFields(ActionEvent actionEvent) {
        if(userRadio.isSelected()) {
            addressField.setDisable(true);
            workHoursField.setDisable(true);
            dishesField.setDisable(true);
            driverLicenseField.setDisable(true);
            birthDateFIeld.setDisable(true);
        } else if(restaurantRadio.isSelected()) {
            addressField.setDisable(false);
            workHoursField.setDisable(false);
            dishesField.setDisable(false);
            driverLicenseField.setDisable(true);
            birthDateFIeld.setDisable(true);

        } else if(driverRadio.isSelected()) {
            addressField.setDisable(false);
            workHoursField.setDisable(true);
            dishesField.setDisable(true);
            driverLicenseField.setDisable(false);
            birthDateFIeld.setDisable(false);
        } else if(clientRadio.isSelected()) {
            addressField.setDisable(false);
            workHoursField.setDisable(true);
            dishesField.setDisable(true);
            driverLicenseField.setDisable(true);
            birthDateFIeld.setDisable(true);
        }
    }
}
