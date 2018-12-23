package com.soulkitchen.serifenuruysal.mychef.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by S.Nur Uysal on 22.12.2018.
 */
public class ChefMenu {
    HashMap<Integer,Meal> meals;

    public HashMap<Integer, Meal> getMeals() {
        return meals;
    }

    public void setMeals(HashMap<Integer, Meal> meals) {
        this.meals = meals;
    }
}
