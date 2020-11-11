package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Recipe extends AppCompatActivity {

    //fields
    TextView Instructions;
    TextView Recipe;
    TextView Image_PlaceHolder;
    TextView prep_time;
    TextView cook_time;
    TextView Ingredients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Instructions = (TextView) findViewById(R.id.instructions_txt);
        Recipe = (TextView) findViewById(R.id.recipe_text);
        Image_PlaceHolder = (TextView) findViewById(R.id.img_place);
        prep_time = (TextView) findViewById(R.id.prep_txt);
        cook_time = (TextView) findViewById(R.id.cook_txt);
        Ingredients = (TextView) findViewById(R.id.ingredients_txt);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(getApplicationContext(), Category_List.class));
    }

}