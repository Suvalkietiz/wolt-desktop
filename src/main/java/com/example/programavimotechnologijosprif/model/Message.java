package com.example.programavimotechnologijosprif.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) protected int id;
    protected String text;
    @ManyToOne private Chat chat;

    public Message(String text) {
        this.text = text;
    }
}
