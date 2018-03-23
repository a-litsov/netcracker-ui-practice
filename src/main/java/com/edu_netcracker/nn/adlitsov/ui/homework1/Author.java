package com.edu_netcracker.nn.adlitsov.ui.homework1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Author {

    public enum Gender {
        MALE, FEMALE
    }

    private String name;
    private String email;
    private Gender gender;

    public Author() {

    }

    @JsonCreator
    public Author(@JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("gender") Gender gender) {
        validateName(name);
        validateEmail(email);

        this.name = name;
        this.email = email;
        this.gender = gender;
    }

    private void validateName(String name) {
        if (name == null || !isNameValid(name)) {
            throw new IllegalArgumentException("Incorrect name!");
        }
    }

    public static boolean isNameValid(String name) {
        return name.toLowerCase().matches("^(?:[a-zа-я]+(?: |\\. ?)?)+[a-zа-я]$");
    }

    private void validateEmail(String email) {
        if (email == null || !isEmailValid(email)) {
            throw new IllegalArgumentException("Incorrect email!");
        }
    }

    public static boolean isEmailValid(String email) {
        return email.toLowerCase().matches("^[a-z0-9._%-]+@[a-z0-9.-]+\\.[a-z]{2,6}$");
    }


    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
