package com.example.guacon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//data class for collection "Recipes"
//inflate data received from firestore in an object of type 'Recipe'
public class Recipe implements Serializable {
    //variable name must match the firestore key names
    private String prep_time, cook_time, final_photo, owner, doc_id;
    long timestamp, no_of_stars;
    private List<String> ingredients = new ArrayList<>(), instructions = new ArrayList<>(), tags = new ArrayList<>(), name = new ArrayList<>();

    public Recipe() {}

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public List<String> getName(){ return name; }
    public void setName(List<String> name){
        this.name = name;
    }

    public long getNo_of_stars() {
        return no_of_stars;
    }

    public void setNo_of_stars(long no_of_stars) {
        this.no_of_stars = no_of_stars;
    }

    public long getTimestamp() {
        Calendar c = Calendar.getInstance();
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
