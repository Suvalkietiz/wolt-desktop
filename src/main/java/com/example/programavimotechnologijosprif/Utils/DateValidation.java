package com.example.programavimotechnologijosprif.Utils;

import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class DateValidation {
    private String dateFormat;

    public DateValidation(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateValidation() {
        dateFormat = "yyyy-MM-dd";
    }

    public boolean isValid(String dateStr){
        try{
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(this.dateFormat));
            return true;
        } catch (DateTimeException e){
            return false;
        }
    }


}
