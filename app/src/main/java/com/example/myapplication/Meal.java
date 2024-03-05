package com.example.myapplication;

public class Meal {
    private String name;
    private Double price;
    private String ingredients;
    private String picture;

    @Override
    public String toString() {
        return "Meal{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", ingredients='" + ingredients + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }

    public Meal(String name, Double price, String ingredients, String picture){
        this.name = name;
        this.price = price;
        this.ingredients =ingredients;
        this.picture = picture;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
