package com.example.programavimotechnologijosprif.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Chat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY) private List<Message> messages;
    @ManyToOne private AppUser appUser;
    @ManyToOne private Driver driver;
    @ManyToOne private Restaurant restaurant;
    @OneToOne private FoodOrder foodOrder;



    public Chat() {
        this.messages = new ArrayList<>();
        // default message??
    }

    @Override
    public String toString() {
        return foodOrder.getTimeCreated().toString();
    }
}
