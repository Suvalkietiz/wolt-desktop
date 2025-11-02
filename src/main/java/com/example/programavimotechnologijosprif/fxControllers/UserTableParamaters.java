package com.example.programavimotechnologijosprif.fxControllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserTableParamaters {
    private SimpleIntegerProperty id =  new SimpleIntegerProperty();
    private SimpleStringProperty userType =  new SimpleStringProperty();
    private SimpleStringProperty login =  new SimpleStringProperty();
    private SimpleStringProperty password =  new SimpleStringProperty();
    private SimpleStringProperty name =  new SimpleStringProperty();
    private SimpleStringProperty surname =  new SimpleStringProperty();
    private SimpleStringProperty phoneNumber =  new SimpleStringProperty();
    private SimpleStringProperty dateCreated = new SimpleStringProperty();
    private SimpleStringProperty address =  new SimpleStringProperty();
    private SimpleStringProperty workHours =  new SimpleStringProperty();
    private SimpleStringProperty birthDate =  new SimpleStringProperty();
    private SimpleStringProperty driverLicense =  new SimpleStringProperty();

    public String getBirthDate() {
        return birthDate.get();
    }

    public SimpleStringProperty birthDateProperty() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate.set(birthDate);
    }

    public String getDriverLicense() {
        return driverLicense.get();
    }

    public SimpleStringProperty driverLicenseProperty() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense.set(driverLicense);
    }

    public String getWorkHours() {
        return workHours.get();
    }

    public SimpleStringProperty workHoursProperty() {
        return workHours;
    }

    public void setWorkHours(String workHours) {
        this.workHours.set(workHours);
    }

    public String getDateCreated() {
        return dateCreated.get();
    }

    public SimpleStringProperty dateCreatedProperty() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getUserType() {
        return userType.get();
    }

    public SimpleStringProperty userTypeProperty() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType.set(userType);
    }

    public String getLogin() {
        return login.get();
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }
}
