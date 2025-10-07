package com.example.programavimotechnologijosprif.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
public class Driver extends AppUser {
    protected String driverLicense;
    protected LocalDate birthDate;

    public Driver(String login, String password, String name, String surname, String phoneNumber,
                  String address, String driverLicense, LocalDate birthDate) {
        super(login, password, name, surname, phoneNumber, address);
        this.driverLicense = driverLicense;
        this.birthDate = birthDate;
    }
}
