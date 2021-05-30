package com.example.guacon.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guacon.Login.Launcher;
import com.example.guacon.R;
import com.example.guacon.SearchResult;
import com.example.guacon.User;
import com.example.guacon.UserCard;
import com.example.guacon.UserCardAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import java.util.ArrayList;


//user profile displaying user data along with saved recipes and recipes added by user
public class Profile extends AppCompatActivity {

    TextView name_age, followers, following;
    RecyclerView cards;
    Query base;
    UserCardAdapter userCardAdapter;
    SharedPreferences sharedPreferences;
    User[] user;
    Button follow;
    TextView your_recipe_head;
    FirestoreRecyclerOptions<UserCard> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        float scale = this.getResources().getDisplayMetrics().density;
        int height = (int) (100 * scale + 0.5f);
        your_recipe_head = findViewById(R.id.card_name);
        your_recipe_head.setText("Your Recipes");
        (findViewById(R.id.profile_media_image)).setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, height));
        (findViewById(R.id.profile_media_image2)).setVisibility(View.GONE);
        (findViewById(R.id.profile_media_image3)).setVisibility(View.GONE);
        ((ImageView)findViewById(R.id.profile_media_image)).setImageResource(R.drawable.guacon);
        (findViewById(R.id.your_recipe_card)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CardResult.class);
                intent.putExtra("Card", "Your Recipes");
                intent.putStringArrayListExtra("Recipe List", new ArrayList<String>());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

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
        getUserData();

        updateActivity();

        name_age.setText(user[0].getName() + ", " + user[0].getAge());

        //add a new recipe
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RecipeForm.class));
            }
        });

        setRecyclerView();

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

    @Override
    protected void onResume(){
        super.onResume();
        updateActivity();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        userCardAdapter.stopListening();
    }

    public void getUserData(){
        sharedPreferences = getSharedPreferences("user",0);
        String json = sharedPreferences.getString("user_info", null);
        Gson gson = new Gson();
        user = gson.fromJson(json, User[].class);
    }

    //update followers/following in layout
    public void updateActivity(){
        followers.setText(Html.fromHtml("<font><b>" + user[0].getFollowers_count() + "</b></font>"));
        followers.append("\nfollowers");
        following.setText(Html.fromHtml("<font><b>" + user[0].getFollowing_count() + "</b></font>"));
        following.append("\nfollowing");
    }

    public void setRecyclerView(){
        //display user cards
        base = FirebaseFirestore.getInstance()
                .collection("Users/" + getSharedPreferences("user",0).getString("user_email","") + "/cards");

        cards = findViewById(R.id.cards);
        cards.setLayoutManager(new GridLayoutManager(this, 2));

        options = new FirestoreRecyclerOptions.Builder<UserCard>().setQuery(base, UserCard.class).build();
        userCardAdapter = new UserCardAdapter(getApplicationContext(), options);
        cards.setAdapter(userCardAdapter);
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
            SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
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