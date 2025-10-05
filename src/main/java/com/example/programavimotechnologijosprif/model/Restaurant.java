package com.example.programavimotechnologijosprif.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Restaurant extends AppUser {
    protected List<Food> dishes;
    protected String workHours;

    public Restaurant(String login, String password, String name, String surname, String phoneNumber,
                      String address,String workHours, List<Food> dishes) {
        super(login, password, name, surname, phoneNumber, address);
        this.workHours = workHours;
        this.dishes = dishes;
    }
}
