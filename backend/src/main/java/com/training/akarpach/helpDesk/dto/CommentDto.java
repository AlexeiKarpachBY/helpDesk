package com.training.akarpach.helpDesk.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
public class CommentDto {

    @NotBlank(message = "Comment must be filled.")
    @Size(max = 500, message = "Comment must be no more than 500 characters.")
    @Pattern(regexp = "[[a-zA-Z0-9]~.(),\\s:;<>@\\[\\]!#$%&'*+\\-/=?^_`{|}]+", message = "The Comment uses a forbidden character.")
    private String text;

    private String date;

    private String user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDto that = (CommentDto) o;
        return Objects.equals(text, that.text) && Objects.equals(date, that.date) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, date, user);
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "text='" + text + '\'' +
                ", date='" + date + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
