package com.Handson.Versioning.model;

public class StudentV2
{
    private String firstName;
    private String lastName;

    public StudentV2(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
