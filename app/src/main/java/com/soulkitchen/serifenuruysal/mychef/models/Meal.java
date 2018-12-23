package com.soulkitchen.serifenuruysal.mychef.models;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by S.Nur Uysal on 22.12.2018.
 */
public class Meal implements Serializable {
    String name;
    String description;
    GeoPoint geoLocation;
    String mealOwner;
    double price;
    Date date;
    int count;


    public GeoPoint getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getMealOwner() {
        return mealOwner;
    }

    public void setMealOwner(String mealOwner) {
        this.mealOwner = mealOwner;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getImage() {
        return "ic_meal_photo";
    }
}
