package com.training.akarpach.helpDesk.model.security;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
public class SecurityToken {

    private String token;

    public SecurityToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityToken that = (SecurityToken) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public String toString() {
        return "SecurityToken{" +
                "token='" + token + '\'' +
                '}';
    }
}
