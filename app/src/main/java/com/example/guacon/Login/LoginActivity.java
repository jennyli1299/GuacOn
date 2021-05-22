package com.example.guacon.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guacon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText e1,e2;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1 = findViewById(R.id.email_text);
        e2 = findViewById(R.id.password_text);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
    }

    private void loginUserAccount(){
        progressBar.setVisibility(View.VISIBLE);

        //get data from edittext fields
        email = e1.getText().toString();
        password = e2.getText().toString();

        //check for empty fields
        if(TextUtils.isEmpty(email)){
            e1.setError("required");
            return;
        }
        if(TextUtils.isEmpty(password)){
            e2.setError("required");
            return;
        }

        //log user in
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                    FirebaseFirestore.getInstance().document("Users/"+ email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(!task.getResult().exists()) {
                                //get Data for collection if no entry is found
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, String.valueOf(task.getResult().get("First_Name")), Toast.LENGTH_SHORT).show();
                                new LoginFunctions().getDataFromUser(email, LoginActivity.this);
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                new LoginFunctions().saveData(email, LoginActivity.this);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void onClick(View v){
        if(v.getId()==R.id.signup_link){
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        }
        else
            loginUserAccount();
    }
}
