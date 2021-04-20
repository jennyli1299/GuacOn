package com.example.guacon.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.guacon.Login.Launcher;
import com.example.guacon.R;
import com.example.guacon.Recipe;
import com.example.guacon.RecipeAdapter;
import com.example.guacon.SearchResult;
import com.example.guacon.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PublicProfile extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecipeAdapter adapter;
    Query base;
    SharedPreferences sharedPreferences;
    ArrayList<String> pref;
    SharedPreferences.Editor editor;
    FirestoreRecyclerOptions<Recipe> options;
    Button follow;
    User[] user;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        follow=findViewById(R.id.follow);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("user",0);
        String json = sharedPreferences.getString("user_info", null);
        Gson gson = new Gson();
        user = gson.fromJson(json, User[].class);

        recyclerView = findViewById(R.id.cards);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        email = getIntent().getStringExtra("owner_email");
        base = FirebaseFirestore.getInstance().collection("recipes").whereEqualTo("owner", email);
        options = new FirestoreRecyclerOptions.Builder<Recipe>().setQuery(base, Recipe.class).build();
        adapter = new RecipeAdapter(getApplicationContext(), options);
        recyclerView.setAdapter(adapter);

        if(user[0].getFollowing().contains(email)) {
            ((Button) findViewById(R.id.follow)).setText("Following");
            findViewById(R.id.follow).setBackgroundResource(R.color.colorPrimary);
            ((Button) findViewById(R.id.follow)).setTextColor(getResources().getColor(R.color.black));
        }
        if(!user[0].getFollowing().contains(email)) {
            ((Button) findViewById(R.id.follow)).setText("Follow");
            findViewById(R.id.follow).setBackgroundResource(R.color.colorPrimaryDark);
            ((Button) findViewById(R.id.follow)).setTextColor(getResources().getColor(R.color.white));
        }

        ((TextView) findViewById(R.id.name)).setText(getIntent().getStringExtra("owner"));
        updateActivity();
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    void follow(){
        editor=sharedPreferences.edit();
        if(follow.getText().equals("Follow")) {
            ((Button) findViewById(R.id.follow)).setText("Following");
            findViewById(R.id.follow).setBackgroundResource(R.color.colorPrimary);
            ((Button) findViewById(R.id.follow)).setTextColor(getResources().getColor(R.color.black));

            user[0].getFollowing().add(email);
            user[0].setFollowing_count(user[0].getFollowing_count()+1);
            FirebaseFirestore.getInstance().document("Users/" + FirebaseAuth.getInstance().getCurrentUser().getEmail()).update("Following", FieldValue.arrayUnion(email), "Following_count", FieldValue.increment(1));

            FirebaseFirestore.getInstance().document("Users/" + email).update("Followers_count", FieldValue.increment(1), "Followers", FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getEmail()));

            updateActivity();
        }

        else {
            ((Button) findViewById(R.id.follow)).setText("Follow");
            findViewById(R.id.follow).setBackgroundResource(R.color.colorPrimaryDark);
            ((Button) findViewById(R.id.follow)).setTextColor(getResources().getColor(R.color.white));

            user[0].getFollowing().remove(email);
            user[0].setFollowing_count(user[0].getFollowing_count()-1);
            FirebaseFirestore.getInstance().document("Users/" + FirebaseAuth.getInstance().getCurrentUser().getEmail()).update("Following", FieldValue.arrayRemove(email), "Following_count", FieldValue.increment(-1));

            //add current user to owner's followers
            FirebaseFirestore.getInstance().document("Users/" + email).update("Followers_count", FieldValue.increment(-1), "Followers", FieldValue.arrayRemove(FirebaseAuth.getInstance().getCurrentUser().getEmail()));

            updateActivity();
        }
        List<User> temp = new ArrayList<>();
        temp.add(user[0]);
        Gson gson = new Gson();
        String json = gson.toJson(temp);
        editor.putString("user_info", json);
        editor.commit();
    }

    void updateActivity(){
        FirebaseFirestore.getInstance().collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                ((TextView) findViewById(R.id.followers)).setText(Html.fromHtml("<font><b>" + documentSnapshot.getLong("Followers_count") + "</b></font>") + "\nfollowers");
                ((TextView) findViewById(R.id.following)).setText(Html.fromHtml("<font><b>" + documentSnapshot.getLong("Following_count") + "</b></font>") + "\nfollowing");

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

        int id = item.getItemId();

        if (id == R.id.action_profile) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
            return true;
        }

        if (id == R.id.action_home) {
            startActivity(new Intent(getApplicationContext(), SearchResult.class));
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
        finish();
    }
}