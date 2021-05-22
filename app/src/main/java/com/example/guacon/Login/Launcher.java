package com.example.guacon.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guacon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Launcher extends AppCompatActivity implements View.OnClickListener{

    Button b1;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        t = findViewById(R.id.login_link);
        b1 = findViewById(R.id.signup_button);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //check if user is logged in or not
        final FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser!=null) {
            //if logged in, check if it has a entry in 'User' collection
            FirebaseFirestore.getInstance().document("Users/"+ currentUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(!task.getResult().exists()) {
                        //get Data for collection if no entry is found
                        Toast.makeText(Launcher.this, String.valueOf(task.getResult().get("First_Name")), Toast.LENGTH_SHORT).show();
                        new LoginFunctions().getDataFromUser(currentUser.getEmail(), Launcher.this);
                    }
                }
            });
        }
        else{
            startActivity(new Intent(Launcher.this, LoginActivity.class));
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
