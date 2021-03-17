package com.example.guacon.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guacon.MainActivity;
import com.example.guacon.R;
import com.example.guacon.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Launcher extends AppCompatActivity implements View.OnClickListener{

    Button b1;
    TextView t;
    private FirebaseAuth auth;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        t = (TextView) findViewById(R.id.login_link);
        b1 = (Button) findViewById(R.id.signup_button);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser!=null) {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(getSharedPreferences("user", 0).getString("user_email","")).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        user.setName(documentSnapshot.getString("First_Name"), documentSnapshot.getString("Last_Name"));
                        user.setAge(documentSnapshot.getLong("Age"));
                        user.setFollowers((ArrayList<String>) documentSnapshot.get("Followers"));
                        user.setFollowing((ArrayList<String>) documentSnapshot.get("Following"));
                        user.setFollowers_count(documentSnapshot.getLong("Followers_count"));
                        user.setFollowing_count(documentSnapshot.getLong("Following_count"));
                    }
                }
            });
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("user_info", user);
            startActivity(intent);
        }
    }

    public void onClick(View v){
        if(v.getId()==R.id.signup_button){
            Intent intent = new Intent(Launcher.this, SignupActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.login_link){
            Intent intent = new Intent(Launcher.this, LoginActivity.class);
            startActivity(intent);
        }
    }

}
