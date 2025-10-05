package com.example.programavimotechnologijosprif.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Food {
    private int id;
    private List<Ingredients> ingredients;
    private List<Allergens> allergens;
    private double price;

    public Food(List<Ingredients> ingredients, List<Allergens> allergens, double price) {
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.price = price;
    }
}
