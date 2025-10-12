package com.example.programavimotechnologijosprif.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Admin extends User {
    public Admin(String login, String password, String name, String surname, String phoneNumber) {
        super(login, password, name, surname, phoneNumber);
    }
    public Admin() {
        super();
    }
}
