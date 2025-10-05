package com.example.programavimotechnologijosprif.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Chat {
    private int id;
    private List<Message> messages;

    public Chat() {
        this.messages = new ArrayList<>();
        // default message??
    }
}
