package com.example.programavimotechnologijosprif.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FoodOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
    @ManyToOne private AppUser appUser;
    @ManyToOne private Driver driver;
    @ManyToOne private Restaurant restaurant;
    @ManyToMany private List<Food> items;
    private double price;
    @OneToOne private Chat chat;
    private LocalDateTime timeCreated;
    private LocalDateTime timeCompleted;
    @Enumerated private Status status;
    @OneToMany(mappedBy = "foodOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY) private List<Review> reviews;

    public FoodOrder(List<Food> items) {
        this.items = items;
        this.price = 10.10; // calculate from <Food> items...
        timeCreated = LocalDateTime.now();
        status = Status.PENDING;
    }

    public FoodOrder(AppUser client, Driver driver, Restaurant restaurant,  List<Food> FoodItems) {
        this.appUser = client;
        this.driver = driver;
        this.restaurant = restaurant;
        this.items = FoodItems;
        price = 0;
        timeCreated = LocalDateTime.now();
        status = Status.PENDING;
    }

    public void calculatePrice() {
        for (Food food : items) {
            price += food.getPrice();
        }
    }
    public void completeOrder() {
        status = Status.COMPLETED;
        timeCompleted = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Created: " + timeCreated;
    }
}
