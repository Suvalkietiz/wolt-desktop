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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Driver extends User {
    private String address;
    private String driverLicense;
    private LocalDate birthDate;
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY) private List<Chat> chats;
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY) private List<FoodOrder> myOrders;

    public Driver(String login, String password, String name, String surname, String phoneNumber,
                  String address, String driverLicense, LocalDate birthDate) {
        super(login, password, name, surname, phoneNumber);
        this.address = address;
        this.driverLicense = driverLicense;
        this.birthDate = birthDate;
    }

    public Driver(String login, String password, String name, String surname, String phoneNumber,
                  String address, String driverLicense, LocalDate birthDate, LocalDate dateCreated) {
        super(login, password, name, surname, phoneNumber, dateCreated);
        this.address = address;
        this.driverLicense = driverLicense;
        this.birthDate = birthDate;
    }
    @Override
    public String toString() {
        return name;
    }
}
