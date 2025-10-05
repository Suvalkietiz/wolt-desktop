package com.example.programavimotechnologijosprif.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Client extends User {

    public Client(String login, String password, String name, String surname, String phoneNumber) {
        super(login, password, name, surname, phoneNumber);
    }
}
