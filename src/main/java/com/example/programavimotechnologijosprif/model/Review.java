package com.example.programavimotechnologijosprif.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review extends Message {
    private int rate;
    @ManyToOne private FoodOrder foodOrder;
    @ManyToOne private Restaurant restaurant;
    @ManyToOne private Driver driver;

    public Review(String text, int rate) {
        super(text);
        this.rate = rate;
    }
}
