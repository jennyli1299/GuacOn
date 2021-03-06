package com.example.guacon.Profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guacon.R;
import com.example.guacon.User;
import com.example.guacon.UserAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

public class FollowersFollowing extends AppCompatActivity {

    UserAdapter adapter;
    Query base;
    User[] user;
    SharedPreferences sharedPreferences;
    FirestoreRecyclerOptions<User> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_following);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("type"));
        setSupportActionBar(toolbar);

        //get user basic info from sharedpreferences
        sharedPreferences = getSharedPreferences("user",0);
        String json = sharedPreferences.getString("user_info", null);
        Gson gson = new Gson();
        user = gson.fromJson(json, User[].class);

        RecyclerView recyclerView = findViewById(R.id.cards);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if(getIntent().getStringExtra("type").equals("Followers")) {
            if (user[0].getFollowing_count() != 0)
                base = FirebaseFirestore.getInstance().collection("Users").whereIn(FieldPath.documentId(), user[0].getFollowers());
        }
        if(getIntent().getStringExtra("type").equals("Following")) {
            if (user[0].getFollowing_count() == 0)
                base = FirebaseFirestore.getInstance().collection("Users").whereNotEqualTo(FieldPath.documentId(), FirebaseAuth.getInstance().getCurrentUser().getEmail());
            else
                base = FirebaseFirestore.getInstance().collection("Users").whereIn(FieldPath.documentId(), user[0].getFollowing());
        }

        options = new FirestoreRecyclerOptions.Builder<User>().setQuery(base, User.class).build();
        adapter = new UserAdapter(getApplicationContext(), options);
        recyclerView.setAdapter(adapter);
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
        menu.findItem(R.id.action_refine).setVisible(false);
        menu.findItem(R.id.action_profile).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
