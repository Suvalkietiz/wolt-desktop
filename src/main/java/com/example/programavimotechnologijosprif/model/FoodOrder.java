package com.example.programavimotechnologijosprif.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter

public class FoodOrder {
   private int id;
   private List<Food> items;
   private double price;
   private List<Chat> chat;
   private LocalDateTime timeCreated;
   private LocalDateTime timeCompleted;
   private Status status;

    public FoodOrder(List<Food> items) {
        this.items = items;
        this.price = 10.10; // TEMPORARY
        timeCreated = LocalDateTime.now();
        status = Status.PENDING;
    }
}
