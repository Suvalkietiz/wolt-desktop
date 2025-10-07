package com.example.programavimotechnologijosprif.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
public class Client extends AppUser implements Serializable {

    public Client(String login, String password, String name, String surname,
                  String phoneNumber, String address) {
        super(login, password, name, surname, phoneNumber, address);
    }
}
