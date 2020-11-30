package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;

public class Recipe extends AppCompatActivity {

    //fields
    TextView Instructions;
    TextView Recipe;
    TextView Image_PlaceHolder;
    TextView prep_time;
    TextView cook_time;
    TextView Ingredients;

    ImageView ic_vegetarian;
    ImageView ic_gluten_free;
    ImageView ic_vegan;
    ImageView ic_dairy_free;
    ImageView ic_naturally_sweetened;

    String recipe_name = "Guacamole";
    String prep_t = "10 min";
    String cook_t = "5 min";
    String ingredients = "avocado, salt, onion, tomato, lemon";
    String instuctions = "MIX IT ALL TOGETHER!";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Instructions = (TextView) findViewById(R.id.instructions_txt);
        Recipe = (TextView) findViewById(R.id.recipe_text);
        prep_time = (TextView) findViewById(R.id.prep_txt);
        cook_time = (TextView) findViewById(R.id.cook_txt);
        Ingredients = (TextView) findViewById(R.id.ingredients_txt);

        ic_vegetarian = (ImageView) findViewById(R.id.imageView);
        ic_gluten_free = (ImageView) findViewById(R.id.imageView2);
        ic_vegan = (ImageView) findViewById(R.id.imageView3);
        ic_dairy_free = (ImageView) findViewById(R.id.imageView4);
        ic_naturally_sweetened = (ImageView) findViewById(R.id.imageView5);

//        FirebaseDatabase database = FirebaseApp.getInstance()


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(getApplicationContext(), Category_List.class));
    }



}