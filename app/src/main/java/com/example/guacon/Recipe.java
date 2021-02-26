package com.example.guacon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//data class for collection "Recipes"
//inflate data received from firestore in an object of type 'Recipe'
public class Recipe implements Serializable {
    //variable name must match the firestore key names
    private String name, prep_time, cook_time, final_photo;
    private List<String> ingredients = new ArrayList<>();
    private List<String> instructions = new ArrayList<>();
    private List<String> meal_time = new ArrayList<>();
    private boolean vegan, vegetarian, gluten_free, dairy_free, naturally_sweetened;

    public Recipe() {}

    public String getName(){ return name; }
    public void setName(String name){
        this.name = name;
    }

    public List<String> getMeal_time() {
        return meal_time;
    }
    public void setMeal_time(List<String> meal_time) {
        this.meal_time = meal_time;
    }

    public String getFinal_photo(){ return final_photo; }
    public void setFinal_photo(String final_photo){
        this.final_photo = final_photo;
    }

    public String getPrep_time(){
        return prep_time;
    }
    public void setPrep_time(String prep_time){
        this.prep_time = prep_time;
    }

    public String getCook_time(){
        return cook_time;
    }
    public void setCook_time(String cook_time){
        this.cook_time = cook_time;
    }

    public List<String> getIngredients(){
        return ingredients;
    }
    public void setIngredients(List<String> ingredients){
        this.ingredients = ingredients;
    }

    public List<String> getInstructions(){
        return instructions;
    }
    public void setInstructions(List<String> instructions){
        this.instructions = instructions;
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
