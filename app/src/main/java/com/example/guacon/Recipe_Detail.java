package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guacon.Profile.Profile;

import java.io.Serializable;

//displays data of a particular recipe
//TODO: recieve recipe selected from SearchResult.class
//display link for checking the author of the recipe
public class Recipe_Detail extends AppCompatActivity {

    //fields
    ListView Instructions;
    TextView Recipe;
    TextView prep_time;
    TextView cook_time;
    ListView Ingredients;
    ImageView imageView;
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Instructions = (ListView) findViewById(R.id.instructions_txt);
        Recipe = (TextView) findViewById(R.id.recipe_text);
        prep_time = (TextView) findViewById(R.id.prep_txt);
        cook_time = (TextView) findViewById(R.id.cook_txt);
        Ingredients = (ListView) findViewById(R.id.ingredients_txt);
        imageView = (ImageView) findViewById(R.id.imageView7);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recipe = (Recipe) getIntent().getSerializableExtra("Recipe");
        Recipe.setText(recipe.getName());
        prep_time.setText("Prep Time: " + recipe.getPrep_time() + " min");
        cook_time.setText("Cook Time: " + recipe.getCook_time() + " min");

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, recipe.getIngredients());
        Ingredients.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, recipe.getInstructions());
        Instructions.setAdapter(adapter);

        Glide.with(getApplicationContext()).load(recipe.getFinal_photo()).into(imageView);
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