package com.example.programavimotechnologijosprif.model;

import com.example.programavimotechnologijosprif.hibernateControllers.CustomHibernate;
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
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY) @OrderBy("timestamp ASC") private List<Message> messages;
    @ManyToOne private AppUser appUser;
    @ManyToOne private Driver driver;
    @ManyToOne private Restaurant restaurant;
    @OneToOne private FoodOrder foodOrder;



    public Chat() {
        this.messages = new ArrayList<>();
        // default message??
    }

    public boolean isEmpty(EntityManagerFactory entityManagerFactory){
        updateMessages(entityManagerFactory);
        if(messages.isEmpty())return true;
        return false;
    }

    private void updateMessages(EntityManagerFactory entityManagerFactory){
        CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
        messages = customHibernate.getChatMessages(this);
    }

    @Override
    public String toString() {
        return foodOrder.toString();
    }
}
