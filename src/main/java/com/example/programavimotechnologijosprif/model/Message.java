package com.example.programavimotechnologijosprif.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Message {
    protected int id;
    protected String text;

    public Message(String text) {
        this.text = text;
    }
}
