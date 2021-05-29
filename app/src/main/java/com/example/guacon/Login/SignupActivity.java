package com.example.guacon.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guacon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    Button b1;
    RadioButton r;
    EditText e1,e2;
    TextView t1;
    private FirebaseAuth auth;
    String email, password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        e1 = (EditText) findViewById(R.id.email_text);
        e2 = (EditText) findViewById(R.id.password_text);
        t1 = (TextView) findViewById(R.id.login_link);
        b1 = (Button) findViewById(R.id.signup_button);
        r = (RadioButton) findViewById(R.id.privacy_policy);
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
    }

    private void registerUser(){
        //getting email and password from edit texts
        email = e1.getText().toString().trim();
        password  = e2.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            e1.setError("required");
            return;
        }
        if(TextUtils.isEmpty(password)){
            e2.setError("required");
            return;
        }
        if(!r.isChecked()){
            Toast.makeText(this,"Please agree to Privacy Policy.",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog
        progressDialog.setMessage("Registering" + "\n" + "Please Wait...");
        progressDialog.show();

        //creating a new user
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //checking if success
                if(task.isSuccessful()){
                    //get basic info for user for creating document in collection 'User'
                    progressDialog.dismiss();
                    new LoginFunctions().getDataFromUser(email, SignupActivity.this);
                }
                else{
                    //display some message here
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this,"Registration Error"+task.getException(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onClick(View view) {
        if(view.getId()==R.id.signup_button)
            //calling register method on click
            registerUser();
        else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
}