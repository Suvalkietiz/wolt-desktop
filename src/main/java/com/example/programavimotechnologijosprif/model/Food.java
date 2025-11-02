package com.example.programavimotechnologijosprif.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
// CUISINE
public class Food {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
    private String name;
    @Enumerated private List<Ingredients> ingredients;
    @Enumerated private List<Allergens> allergens;
    private double price;
    @ManyToMany(mappedBy = "items", cascade = CascadeType.ALL, fetch = FetchType.LAZY) private List<FoodOrder> orders;
    @ManyToOne private Restaurant restaurant;

    public Food(String name, List<Ingredients> ingredients, List<Allergens> allergens, double price) {
        this.name = name;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.price = price;
    }




    @Override
    public String toString() {
        return name + "   " + price + "$";
    }
}
