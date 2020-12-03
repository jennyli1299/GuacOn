package com.example.guacon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class SearchResult extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecipeAdapter adapter;
    Query base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result3);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        HashMap<String, Boolean> recipePreferences = (HashMap<String, Boolean>)intent.getSerializableExtra("recipePreferences");
        base = FirebaseFirestore.getInstance().collection("recipes");
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirestoreRecyclerOptions<Recipe> options = new FirestoreRecyclerOptions.Builder<Recipe>().setQuery(base, Recipe.class).build();
        adapter = new RecipeAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String path = documentSnapshot.getReference().getPath();
                showCustomDialog(path);
            }
        });
    }

    public void showCustomDialog(String path) {
        FirebaseFirestore.getInstance().document(path).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    @NonNull Recipe model = new Recipe();
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Intent intent = new Intent(getApplicationContext(), Recipe_Detail.class);
                    startActivity(intent);
                }
            }
        });
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
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
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}