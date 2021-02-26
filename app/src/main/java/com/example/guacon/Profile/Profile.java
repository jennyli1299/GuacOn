package com.example.guacon.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.guacon.Login.Launcher;
import com.example.guacon.MainActivity;
import com.example.guacon.ProfileAdapter;
import com.example.guacon.R;
import com.example.guacon.Recipe;
import com.example.guacon.RecipeAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//user profile displaying user data along with saved recipes and recipes added by user
public class Profile extends AppCompatActivity {

    TextView t;
    String TAG ="main";
    RecyclerView saved_recipes, your_recipes;
    ProfileAdapter savedRecipeAdapter, yourRecipeAdapter;
    Query base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        t = (TextView) findViewById(R.id.name);
        Button delete = (Button) findViewById(R.id.btn_delete);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
        t.setText(sharedPreferences.getString("user_name",""));
        ((TextView) findViewById(R.id.age)).setText(String.valueOf(sharedPreferences.getInt("user_age",0)));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RecipeForm.class));
            }
        });

        base = FirebaseFirestore.getInstance().collection("recipes");

        saved_recipes = findViewById(R.id.saved_recipes);
        saved_recipes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        your_recipes = findViewById(R.id.your_recipes);
        your_recipes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        FirestoreRecyclerOptions<Recipe> options = new FirestoreRecyclerOptions.Builder<Recipe>().setQuery(base, Recipe.class).build();

        savedRecipeAdapter = new ProfileAdapter(getApplicationContext(), options);
        yourRecipeAdapter = new ProfileAdapter(getApplicationContext(), options);

        saved_recipes.setAdapter(savedRecipeAdapter);
        your_recipes.setAdapter(yourRecipeAdapter);
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        savedRecipeAdapter.startListening();
        yourRecipeAdapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        savedRecipeAdapter.stopListening();
        yourRecipeAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
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