package com.example.programavimotechnologijosprif.model;
// !BasicUser!

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class AppUser extends User implements Serializable {
    protected String address;
    protected List<FoodOrder> myOrders;
    protected List<Review> myReviews;
    protected List<Review>feedback;
    protected double rating;

    public AppUser(String login, String password, String name, String surname, String phoneNumber, String address) {
        super(login, password, name, surname, phoneNumber);
        this.address = address;
        this.myOrders = new ArrayList<>();
        this.myReviews = new ArrayList<>();
        this.feedback = new ArrayList<>();
    }

    public AppUser(String login, String password){
        super(login, password);
    }
    @Override
    public String toString() {
        return login + " " + password + " " + name + " " + surname + " " + phoneNumber + " " + address;
    }
}
