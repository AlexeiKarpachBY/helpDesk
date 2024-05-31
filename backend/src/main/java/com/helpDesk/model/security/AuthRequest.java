package com.helpDesk.model.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
public class AuthRequest {

    @Email(message = "Please make sure you are using a valid email or password")
    @Size(max = 100, message = "<Login> field must be no more than 100 characters.")
    private String login;

    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~.\"(),:;<>@\\[\\]!#$%&'*+\\-/=?^_`{|} ]).{6,20}",
            message = "Please make sure you are using a valid email or password")
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthRequest that = (AuthRequest) o;
        return Objects.equals(login, that.login) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
