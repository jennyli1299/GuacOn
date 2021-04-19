package com.example.guacon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guacon.Login.Launcher;
import com.example.guacon.Profile.Profile;
import com.example.guacon.Profile.PublicProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    Button startcooking;

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
        startcooking = findViewById(R.id.imageButton2);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Recipe");
        setSupportActionBar(toolbar);

        recipe = (Recipe) getIntent().getSerializableExtra("Recipe");

        FirebaseFirestore.getInstance().document("Users/" + recipe.getOwner()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                owner.setText("Recipe by " + task.getResult().getString("First_Name") + " " + task.getResult().getString("Last_Name"));
                owner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(recipe.getOwner().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                            startActivity(new Intent(getApplicationContext(), Profile.class));
                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(), PublicProfile.class);
                            intent.putExtra("owner_email", recipe.getOwner());
                            intent.putExtra("owner", task.getResult().getString("First_Name") + " " + task.getResult().getString("Last_Name").charAt(0));
                            startActivity(intent);
                        }
                    }
                });
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
            Instructions.append(Html.fromHtml("<font><b>Step " + (i+1) + "</b></font>"));
            Instructions.setTypeface(null, Typeface.NORMAL);
            Instructions.append("\n" + recipe.getInstructions().get(i) + "\n\n");
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

        startcooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), StartCooking.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        menu.findItem(R.id.action_refine).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_home) {
            startActivity(new Intent(getApplicationContext(), SearchResult.class));
        }
        if (id == R.id.action_profile) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
            return true;
        }
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Launcher.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}