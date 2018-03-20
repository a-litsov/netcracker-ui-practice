package com.edu_netcracker.nn.adlitsov.ui.homework1;

import java.util.Objects;

public class Author {

    public enum Gender {
        MALE, FEMALE
    }

    private String firstName, lastName;
    private String email;
    private Gender gender;

    public Author(String firstName, String lastName, String email, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + firstName + " " + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(firstName, author.firstName) && Objects.equals(lastName, author.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
