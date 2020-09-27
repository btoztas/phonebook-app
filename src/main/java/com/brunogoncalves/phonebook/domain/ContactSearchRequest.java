package com.brunogoncalves.phonebook.domain;

import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ContactSearchRequest {

    @NotNull(message = "The search token must not be null")
    @NotBlank(message = "The search token must not be null")
    private String token;

    public ContactSearchRequest() {}

    public ContactSearchRequest(final String token) {
        this.token = token;
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ContactSearchRequest that = (ContactSearchRequest) o;
        return token.equals(that.token);
    }

    @Override
    public String toString() {
        return "ContactSearchRequest{" +
               "token='" + token + '\'' +
               '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }
}
