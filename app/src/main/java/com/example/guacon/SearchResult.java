package com.example.guacon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.guacon.Login.Launcher;
import com.example.guacon.Profile.Profile;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SearchResult extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecipeAdapter adapter;
    Query base;
    SharedPreferences sharedPreferences;
    ArrayList<String> pref;
    SharedPreferences.Editor editor;
    FirestoreRecyclerOptions<Recipe> options;
    ShimmerRecyclerView shimmerRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result3);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("For You");
        setSupportActionBar(toolbar);

        shimmerRecycler = findViewById(R.id.shimmer_recycler_view);
        shimmerRecycler.showShimmerAdapter();

        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        inflateView(new ArrayList<>(sharedPreferences.getStringSet("preferences", new HashSet<String>())));
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
        menu.findItem(R.id.action_home).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refine) {
            openRefine();
            //startActivity(new Intent(getApplicationContext(), Refine.class));
            return true;
        }

        if (id == R.id.action_profile) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
            return true;
        }

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(getApplicationContext(), Launcher.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void inflateView(ArrayList<String> arrayList) {
        pref = arrayList;
        if (pref.isEmpty()){
            //show all recipes
            base = FirebaseFirestore.getInstance().collection("recipes");
        }
        else {
            //show all recipes with preferences
            base = FirebaseFirestore.getInstance().collection("recipes").whereArrayContainsAny("tags", pref);
        }

        options = new FirestoreRecyclerOptions.Builder<Recipe>().setQuery(base, Recipe.class).build();
        adapter = new RecipeAdapter(getApplicationContext(), options);
        recyclerView.setAdapter(adapter);

        base.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    shimmerRecycler.hideShimmerAdapter();
                    (findViewById(R.id.default_text)).setVisibility(View.VISIBLE);
                }
                else
                    shimmerRecycler.hideShimmerAdapter();
                    recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void openRefine() {
        editor = sharedPreferences.edit();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_refine);

        if(pref.contains("Vegan"))
            ((CheckBox) dialog.findViewById(R.id.vegan)).setChecked(true);
        if(pref.contains("Vegetarian"))
            ((CheckBox) dialog.findViewById(R.id.vegetarian)).setChecked(true);
        if(pref.contains("Gluten Free"))
            ((CheckBox) dialog.findViewById(R.id.gluten_free)).setChecked(true);
        if(pref.contains("Dairy Free"))
            ((CheckBox) dialog.findViewById(R.id.dairy_free)).setChecked(true);
        if(pref.contains("Naturally Sweetened"))
            ((CheckBox) dialog.findViewById(R.id.naturally_sweetened)).setChecked(true);

        ((Button) dialog.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> preferences = new HashSet<>();
                if(((CheckBox) dialog.findViewById(R.id.vegan)).isChecked())
                    preferences.add("Vegan");
                if(((CheckBox) dialog.findViewById(R.id.vegetarian)).isChecked())
                    preferences.add("Vegetarian");
                if(((CheckBox) dialog.findViewById(R.id.gluten_free)).isChecked())
                    preferences.add("Gluten Free");
                if(((CheckBox) dialog.findViewById(R.id.dairy_free)).isChecked())
                    preferences.add("Dairy Free");
                if(((CheckBox) dialog.findViewById(R.id.naturally_sweetened)).isChecked())
                    preferences.add("Naturally Sweetened");

                editor.putStringSet("preferences", preferences);
                editor.commit();

                adapter.stopListening();
                (findViewById(R.id.default_text)).setVisibility(View.INVISIBLE);
                inflateView(new ArrayList<>(preferences));
                adapter.startListening();
                dialog.cancel();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}