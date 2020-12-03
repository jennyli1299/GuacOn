package com.example.guacon;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name, prep_time;
    private List<String> ingredients = new ArrayList<>();
    private boolean vegan, vegetarian, gluten_free, dairy_free, naturally_sweetened;

    public Recipe() {}

    public String getName(){ return name; }
    public void setName(String name){
        this.name = name;
    }
    public String getPrep_time(){
        return "Prep Time: " + prep_time + " min";
    }
    public void setPrep_time(String prep_time){
        this.prep_time = prep_time;
    }
    public List<String> getIngredients(){
        return ingredients;
    }
    public void setIngredients(List<String> ingredients){
        this.ingredients = ingredients;
    }
    public boolean isVegan(){
        return vegan;
    }
    public void setVegan(boolean vegan){
        this.vegan = vegan;
    }
    public boolean isVegetarian(){
        return vegetarian;
    }
    public void setVegetarian(boolean vegetarian){
        this.vegetarian = vegetarian;
    }
    public boolean isGluten_free(){
        return gluten_free;
    }
    public void setGluten_free(boolean gluten_free){
        this.gluten_free = gluten_free;
    }
    public boolean isDairy_free(){
        return dairy_free;
    }
    public void setDairy_free(boolean dairy_free){
        this.dairy_free = dairy_free;
    }
    public boolean isNaturally_sweetened(){
        return naturally_sweetened;
    }
    public void setNaturally_sweetened(boolean naturally_sweetened){
        this.naturally_sweetened = naturally_sweetened;
    }
    public String tags() {
        String tag = "";
        if(isVegan())
            tag = tag + " Vegan ";
        if(isVegetarian())
            tag = tag + " Vegetarian ";
        if(isGluten_free())
            tag = tag + " Gluten-Free ";
        if(isDairy_free())
            tag = tag + " Dairy-Free ";
        if(isNaturally_sweetened())
            tag = tag + " Naturally Sweetened ";
        return tag;
    }
}
