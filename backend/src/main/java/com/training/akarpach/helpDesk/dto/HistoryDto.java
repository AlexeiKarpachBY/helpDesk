package com.training.akarpach.helpDesk.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class HistoryDto {

    private String date;

    private String user;

    private String action;

    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryDto that = (HistoryDto) o;
        return Objects.equals(date, that.date) && Objects.equals(user, that.user) && Objects.equals(action, that.action) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, user, action, description);
    }

    @Override
    public String toString() {
        return "HistoryDto{" +
                "date='" + date + '\'' +
                ", user='" + user + '\'' +
                ", action='" + action + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
