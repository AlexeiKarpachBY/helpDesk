package com.training.akarpach.helpDesk.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateFormatter {

    public String getDateForDto(LocalDate date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return date.format(formatter);
    }

    public LocalDate getDateFromDto(String date) {

        return LocalDate.parse(date);

    }

}
