package com.example.guacon.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.guacon.Recipe_Detail;
import com.example.guacon.SearchResult;
import com.example.guacon.User;
import com.example.guacon.UserCard;
import com.example.guacon.UserCardAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;



//user profile displaying user data along with saved recipes and recipes added by user
public class Profile extends AppCompatActivity {

    TextView name_age, followers, following;
    String TAG ="main";
    RecyclerView cards;
    Query base;
    UserCardAdapter userCardAdapter;
    SharedPreferences sharedPreferences;
    User[] user;
    Button follow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        RecipeAdapter ad;

        follow = findViewById(R.id.follow);
        follow.setText("Edit Profile");
        follow.setBackgroundResource(R.color.white);
        follow.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        name_age = findViewById(R.id.name);
        followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

        //get user basic info from sharedpreferences
        sharedPreferences = getSharedPreferences("user",0);
        String json = sharedPreferences.getString("user_info", null);
        Gson gson = new Gson();
        user = gson.fromJson(json, User[].class);

        followers.setText(Html.fromHtml("<font><b>" + user[0].getFollowers_count() + "</b></font>") + "\nfollowers");
        following.setText(Html.fromHtml("<font><b>" + user[0].getFollowing_count() + "</b></font>") + "\nfollowing");
        name_age.setText(user[0].getName() + ", " + user[0].getAge());

        //add a new recipe
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RecipeForm.class));
            }
        });

        //display user cards
        base = FirebaseFirestore.getInstance().collection("Users/" + getSharedPreferences("user",0).getString("user_email","") + "/cards");

        cards = findViewById(R.id.cards);
        cards.setLayoutManager(new GridLayoutManager(this, 2));

        FirestoreRecyclerOptions<UserCard> options = new FirestoreRecyclerOptions.Builder<UserCard>().setQuery(base, UserCard.class).build();
        userCardAdapter = new UserCardAdapter(getApplicationContext(), options);
        cards.setAdapter(userCardAdapter);

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FollowersFollowing.class);
                intent.putExtra("type", "Followers");
                startActivity(intent);
            }
        });


        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FollowersFollowing.class);
                intent.putExtra("type", "Following");
                startActivity(intent);
            }
        });
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        userCardAdapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        userCardAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        menu.findItem(R.id.action_refine).setVisible(false);
        menu.findItem(R.id.action_profile).setVisible(false);
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
    }
}