package com.example.programavimotechnologijosprif.model.consoleCourseWork;

import com.example.programavimotechnologijosprif.model.FoodOrder;
import com.example.programavimotechnologijosprif.model.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class Wolt implements Serializable {
    private List<User> allSysUsers;
    private List<FoodOrder>allOrders;

    public Wolt() {
        this.allSysUsers = new ArrayList<>();
        this.allOrders = new ArrayList<>();
    }
}
