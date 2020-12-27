package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//displays data of a particular recipe
//TODO: recieve recipe selected from SearchResult.class
//display link for checking the author of the recipe
public class Recipe_Detail extends AppCompatActivity {

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

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refine) {
            startActivity(new Intent(getApplicationContext(), Refine.class));
            return true;
        }

        if (id == R.id.action_home) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        if (id == R.id.action_profile) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(getApplicationContext(), Category_List.class));
    }

}