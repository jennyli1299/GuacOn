package com.example.guacon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guacon.Profile.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

//displays data of a particular recipe
//TODO: recieve recipe selected from SearchResult.class
//display link for checking the author of the recipe
public class Recipe_Detail extends AppCompatActivity {

    //fields
    TextView Instructions, Recipe, prep_time, cook_time, Ingredients, owner;
    ImageView imageView, v, veg, gf, df, ns;
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Instructions = findViewById(R.id.instructions_txt);
        Recipe = findViewById(R.id.recipe_text);
        prep_time = findViewById(R.id.prep_time);
        cook_time = findViewById(R.id.cook_time);
        Ingredients = findViewById(R.id.ingredients_txt);
        imageView = findViewById(R.id.imageView7);
        owner = findViewById(R.id.owner_name);
        v = findViewById(R.id.vegan);
        veg = findViewById(R.id.vegetarian);
        gf = findViewById(R.id.gluten_free);
        df = findViewById(R.id.dairy_free);
        ns = findViewById(R.id.naturally_sweetened);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recipe = (Recipe) getIntent().getSerializableExtra("Recipe");

        FirebaseFirestore.getInstance().document("Users/" + recipe.getOwner()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                owner.setText("Recipe by " + task.getResult().getString("First_Name") + " " + task.getResult().getString("Last_Name"));
            }
        });

        Recipe.setText(recipe.getName());
        prep_time.setText("Prep Time: " + recipe.getPrep_time() + " min");
        cook_time.setText("Cook Time: " + recipe.getCook_time() + " min");

        Ingredients.setText(recipe.getIngredients().get(0) + "\n");
        for(int i=1;i<recipe.getIngredients().size();i++){
            Ingredients.append(recipe.getIngredients().get(i) + "\n");
        }

        for(int i=0;i<recipe.getInstructions().size();i++){
            Instructions.append("Step " + i+1 + "\n");
            Instructions.setTypeface(null, Typeface.NORMAL);
            Instructions.append(recipe.getInstructions().get(i) + "\n\n");
        }

        if(recipe.getTags().contains("Vegan"))
            v.setVisibility(View.VISIBLE);
        if(recipe.getTags().contains("Vegetarian"))
            veg.setVisibility(View.VISIBLE);
        if(recipe.getTags().contains("Gluten Free"))
            gf.setVisibility(View.VISIBLE);
        if(recipe.getTags().contains("Dairy Free"))
            df.setVisibility(View.VISIBLE);
        if(recipe.getTags().contains("Naturally Sweetened"))
            ns.setVisibility(View.VISIBLE);

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
    }

}