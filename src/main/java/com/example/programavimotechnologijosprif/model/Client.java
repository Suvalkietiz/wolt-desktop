package com.example.programavimotechnologijosprif.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Setter
@Getter
public class Client extends AppUser implements Serializable {
    public Client(String login, String password, String name, String surname,
                  String phoneNumber, String address) {
        super(login, password, name, surname, phoneNumber, address);
    }
}
