package com.example.guacon;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeObj {
    private String recipeName;
    private Meal meal;
    private int servings;
    private int prepTime;
    private int cookTime;

    private HashMap<String, Integer> ingredients;
    private ArrayList<String> instructions;
    private ArrayList<String> tags;

    public RecipeObj() {
        recipeName = "Recipe";
        meal = Meal.Breakfast;
        servings = 1;
        prepTime = 0;
        cookTime = 0;
        ingredients = new HashMap<String, Integer>();
//        instructions

    }
}
