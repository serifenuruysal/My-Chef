package com.soulkitchen.serifenuruysal.mychef.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

/**
 * Created by S.Nur Uysal on 22.12.2018.
 */
public class Meal implements Parcelable {
    String name;
    String description;
    GeoPoint geoLocation;
    String mealOwner;
    double price;
    Date date;
    int count;


    public Meal(Parcel in) {
        name = in.readString();
        description = in.readString();
        mealOwner = in.readString();
        price = in.readDouble();
        count = in.readInt();
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    public Meal() {

    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(mealOwner);
        dest.writeDouble(price);
        dest.writeInt(count);
    }
}
