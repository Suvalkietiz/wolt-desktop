package com.example.programavimotechnologijosprif.fxControllers;

import com.example.programavimotechnologijosprif.Utils.FxUtils;
import com.example.programavimotechnologijosprif.hibernateControllers.CustomHibernate;
import com.example.programavimotechnologijosprif.hibernateControllers.GenericHibernate;
import com.example.programavimotechnologijosprif.model.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
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
    @FXML public Button updateUserButton;
    @FXML public Button createUserButton;
    @FXML public TextField discountPercentageField;
    @FXML public AnchorPane discountPane;

    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;

    private User userForUpdate;

    public void setData(EntityManagerFactory entityManagerFactory, User updateUser) {
        this.entityManagerFactory = entityManagerFactory;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
        this.userForUpdate = updateUser;
        fillUserDataForUpdate();
    }

    private void fillUserDataForUpdate() {
        if(userForUpdate != null) {
            System.out.println("Client is updating a user");
            createUserButton.setVisible(false);
            updateUserButton.setVisible(true);
            discountPane.setVisible(false);

            loginField.setText(userForUpdate.getLogin());
            passwordField.setText(userForUpdate.getPassword());
            nameField.setText(userForUpdate.getName());
            surnameField.setText(userForUpdate.getSurname());
            phoneNumberField.setText(userForUpdate.getPhoneNumber());
            if(userForUpdate.isAdmin()){ // userForUpdate instanceof User
                setUserRadio("Admin");
                disableFields();
            } else if(userForUpdate instanceof Restaurant) {
                setUserRadio("Restaurant");
                disableFields();
                addressField.setText(((Restaurant) userForUpdate).getAddress());
                workHoursField.setText(((Restaurant) userForUpdate).getWorkHours());
                discountPane.setVisible(true);
            } else if(userForUpdate instanceof Driver) {
                setUserRadio("Driver");
                disableFields();
                addressField.setText(((Driver) userForUpdate).getAddress());
                birthDateField.setValue(((Driver) userForUpdate).getBirthDate());
                drivingLicenseID.setText(((Driver) userForUpdate).getDriverLicense());
            } else if(userForUpdate instanceof AppUser){
                setUserRadio("AppUser");
                disableFields();
                addressField.setText(((AppUser) userForUpdate).getAddress());
            } else {
                System.out.println("What is going on...??");
            }
            System.out.println("Selected User: " + userForUpdate);

        } else {
            System.out.println("Client is registing a user");
            createUserButton.setVisible(true);
            updateUserButton.setVisible(false);
            discountPane.setVisible(false);
        }

    }

    @FXML public void createUser(ActionEvent actionEvent) {
        final String alertTitle = "Netinkamas login";
        final String alertMessage = "Toks login jau egzistuoja, prasome ivesti kitoki login";
        if(userRadio.isSelected()){
            User admin = new User(
                    loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    LocalDate.now());
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
                    new ArrayList<>(), // CHANGE ME
                    LocalDate.now());
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
                    birthDateField.getValue(),
                    LocalDate.now());
            if(countEmptyFields() != 0) generateEmptyInputAlert();
            else genericHibernate.createEntity(driver, alertTitle, alertMessage);
        } else if(clientRadio.isSelected()){
            AppUser appUser = new AppUser(
                    loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText(),
                    LocalDate.now());
            if(countEmptyFields() != 0) generateEmptyInputAlert();
            else genericHibernate.createEntity(appUser, alertTitle, alertMessage);
        }
    }

    @FXML public void disableFields() {
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
    private void setUserRadio(String UserType){
        switch(UserType){
            case "Admin":
                userRadio.setSelected(true);
                driverRadio.setSelected(false);
                restaurantRadio.setSelected(false);
                clientRadio.setSelected(false);
                break;
            case "Driver":
                userRadio.setSelected(false);
                driverRadio.setSelected(true);
                restaurantRadio.setSelected(false);
                clientRadio.setSelected(false);
                break;
            case "Restaurant":
                userRadio.setSelected(false);
                driverRadio.setSelected(false);
                restaurantRadio.setSelected(true);
                clientRadio.setSelected(false);
                break;
            case "AppUser":
                userRadio.setSelected(false);
                driverRadio.setSelected(false);
                restaurantRadio.setSelected(false);
                clientRadio.setSelected(true);
                break;
            default:
                userRadio.setSelected(false);
                driverRadio.setSelected(false);
                restaurantRadio.setSelected(false);
                clientRadio.setSelected(false);
        }
    }


    // code dublicaiton of createUser to make updateUser
    @FXML public void updateUser(ActionEvent actionEvent) {
        if(userRadio.isSelected()){
            User admin = new User(
                    loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    userForUpdate.getDateCreated());
            admin.setId(userForUpdate.getId());
            admin.setDateUpdated(LocalDate.now());

            if(countEmptyFields() != 0) generateEmptyInputAlert();
            else genericHibernate.updateEntity(admin);
        } else if(restaurantRadio.isSelected()){
            Restaurant restaurant = new Restaurant(
                    loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText(),
                    workHoursField.getText(),
                    new ArrayList<>(), // tikriausiai istrinsiu tiesiog ... CHANGE ME!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    userForUpdate.getDateCreated());
            restaurant.setId(userForUpdate.getId());
            restaurant.setDateUpdated(LocalDate.now());

            if(countEmptyFields() != 0) generateEmptyInputAlert();
            else genericHibernate.updateEntity(restaurant);
        } else if(driverRadio.isSelected()){
            Driver driver = new Driver(
                    loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText(),
                    drivingLicenseID.getText(),
                    birthDateField.getValue(),
                    userForUpdate.getDateCreated());
            driver.setId(userForUpdate.getId());
            driver.setDateUpdated(LocalDate.now());

            if(countEmptyFields() != 0) generateEmptyInputAlert();
            else genericHibernate.updateEntity(driver);
        } else if(clientRadio.isSelected()){
            AppUser appUser = new AppUser(
                    loginField.getText(),
                    passwordField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumberField.getText(),
                    addressField.getText(),
                    userForUpdate.getDateCreated());
            appUser.setId(userForUpdate.getId());
            appUser.setDateUpdated(LocalDate.now());

            if(countEmptyFields() != 0) generateEmptyInputAlert();
            else genericHibernate.updateEntity(appUser);
        }
    }

    @FXML public void applyDiscount(ActionEvent actionEvent) {
        double percentage = parseStringToDouble(discountPercentageField.getText());
        if(percentage == Integer.MIN_VALUE){
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Tai neskaicius", "Irasei neskaiciu;?");
            return;
        }
        if(percentage<-1.0){
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Kodel??", "Nori moketi uz tai kad uzisako maista?");
            return;
        }
        if(percentage == 0.0){
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Kodel??", "Pakelti kainas 0 procentu?XD");
            return;
        }
        CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
        List<Food> foodList = customHibernate.getRestaurantFood((Restaurant) userForUpdate);
        StringBuilder dataForConfirmation = new StringBuilder();
        System.out.println("");
        for(Food food : foodList){
            double foodPrice = food.getPrice();
            food.setPrice(foodPrice + (foodPrice*percentage));
            System.out.println("food " + food.getName() + " new price " + food.getPrice());
            dataForConfirmation
                    .append("Food '")
                    .append(food.getName())
                    .append("' was calculated to ")
                    .append(food.getPrice())
                    .append("€\n");
        }


        if(FxUtils.generateDataConfirmationAlert(Alert.AlertType.INFORMATION, dataForConfirmation.toString())){
            for(Food food: foodList)
                genericHibernate.updateEntity(food);
        } else System.out.println("Canceled");



    }

    private double parseStringToDouble(String str){
        try{
            return Double.parseDouble(str);
        } catch(NumberFormatException ex){
            ex.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }
}
