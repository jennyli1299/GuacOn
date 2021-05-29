package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.guacon.Login.Launcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser() !=null) {
                    startActivity(new Intent(SplashScreen.this, SearchResult.class));
                    finish();
                }
                else{
                    startActivity(new Intent(SplashScreen.this, Launcher.class));
                    finish();
                }
            }
        }, 2000);
    }
}