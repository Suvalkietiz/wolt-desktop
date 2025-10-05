package com.example.programavimotechnologijosprif.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin extends User {
    public Admin(String login, String password, String name, String surname, String phoneNumber) {
        super(login, password, name, surname, phoneNumber);
    }
}
