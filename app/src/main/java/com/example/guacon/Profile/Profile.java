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
import com.example.guacon.R;
import com.example.guacon.Recipe;
import com.example.guacon.RecipeAdapter;
import com.example.guacon.Recipe_Detail;
import com.example.guacon.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashSet;

//user profile displaying user data along with saved recipes and recipes added by user
public class Profile extends AppCompatActivity {

    TextView t;
    String TAG ="main";
    private RecyclerView recipe;
    User userInfo;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        RecipeAdapter ad;


        t = (TextView) findViewById(R.id.txt_profile_header);
        Button delete = (Button) findViewById(R.id.btn_delete);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
        t.setText(sharedPreferences.getString("user_name","") + ", " + sharedPreferences.getInt("user_age", 0));
        //TODO: set saved_recipes and your_recipes from shared preferences to respective views

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RecipeForm.class));
            }
        });


        // Creates the user object
        userInfo = new User();
        recipe = (RecyclerView) findViewById(R.id.savedRecipe);
        recipe.setLayoutManager(new LinearLayoutManager(this));

        // Figure out how saved recipes are going to be stored in firestore
        FirestoreRecyclerOptions<Recipe> options;
        Query base;

        ArrayList<String> savedRecipes = new ArrayList<String>( sharedPreferences.getStringSet("user_saved_recipes", new HashSet<String>()));
        for (int i = 0; i < savedRecipes.size(); i++) {
            base = FirebaseFirestore.getInstance().collection("recipes").whereEqualTo("name", savedRecipes.get(i));
            options = new FirestoreRecyclerOptions.Builder<Recipe>().setQuery(base, Recipe.class).build();
            ad = new RecipeAdapter(options);
            recipe.setAdapter(ad);
            ad.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    String path = documentSnapshot.getReference().getPath();
                    showCustomDialog(path);
                }
            });
        }
    }

            public void showCustomDialog(String path) {
                FirebaseFirestore.getInstance().document(path).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            @NonNull Recipe model = new Recipe();
                            DocumentSnapshot documentSnapshot = task.getResult();
                            //TODO: send recipe name along with Intent
                            Intent intent = new Intent(getApplicationContext(), Recipe_Detail.class);
                            startActivity(intent);
                        }
                    }
                });
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