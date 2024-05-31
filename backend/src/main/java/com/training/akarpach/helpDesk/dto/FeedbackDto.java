package com.training.akarpach.helpDesk.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class FeedbackDto {

    private Long id;

    private String text;

    private String rate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackDto that = (FeedbackDto) o;
        return Objects.equals(text, that.text) && Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, rate);
    }

    @Override
    public String toString() {
        return "FeedbackDto{" +
                "text='" + text + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
