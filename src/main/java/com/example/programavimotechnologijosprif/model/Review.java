package com.example.programavimotechnologijosprif.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Review extends Message {
    private int rate;

    public Review(String text, int rate) {
        super(text);
        this.rate = rate;
    }
}
