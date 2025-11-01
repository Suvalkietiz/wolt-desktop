package com.example.programavimotechnologijosprif.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Restaurant extends User {
    private String address;
    private String workHours;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY) private List<Chat> chats;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY) private List<FoodOrder> myOrders;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY) private List<Review> reviews;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY) private List<Food> dishes;


    public Restaurant(String login, String password, String name, String surname, String phoneNumber,
                      String address,String workHours, List<Food> dishes) {
        super(login, password, name, surname, phoneNumber);
        this.address = address;
        this.workHours = workHours;
        this.dishes = dishes;
    }

    public Restaurant(String login, String password, String name, String surname, String phoneNumber,
                      String address, String workHours, List<Food> dishes, LocalDate dateCreated) {
        super(login, password, name, surname, phoneNumber, dateCreated);
        this.address = address;
        this.workHours = workHours;
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return name;
    }
}
