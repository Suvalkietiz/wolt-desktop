package com.example.programavimotechnologijosprif.model;
// !BasicUser!

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AppUser extends User implements Serializable {
    protected String address;
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY) protected List<Chat> chats;
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY) protected List<FoodOrder> myOrders;
    //protected List<Review> myReviews;
    //protected List<Review> feedback;
    @Transient protected double rating;

    public AppUser(String login, String password, String name, String surname, String phoneNumber, String address) {
        super(login, password, name, surname, phoneNumber);
        this.address = address;
        this.myOrders = new ArrayList<>();
        //this.myReviews = new ArrayList<>();
        //this.feedback = new ArrayList<>();
    }

    public AppUser(String login, String password, String name, String surname, String phoneNumber, String address, LocalDate dateCreated) {
        super(login, password, name, surname, phoneNumber, dateCreated);
        this.address = address;
        this.myOrders = new ArrayList<>();
        //this.myReviews = new ArrayList<>();
        //this.feedback = new ArrayList<>();
    }

    public AppUser(String login, String password){
        super(login, password);
    }

    public void addOrder(FoodOrder foodOrder){
        this.myOrders.add(foodOrder);
    }
}
