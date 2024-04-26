package com.example.myapplication;

import android.os.Parcel;

import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String username;
    private String photo;
    private ArrayList<String> orders;
    public User() {
    }
    public User(String firstName, String lastName, String username, String phone, String address, String photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;

        this.photo = photo;
        this.orders = new ArrayList<>();
    }

    public User(Parcel in) {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ArrayList<String> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<String> favourits) {
        this.orders = favourits;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", Photo='" + photo + '\'' +
                '}';
    }
}
