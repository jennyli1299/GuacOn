package com.example.guacon.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guacon.MainActivity;
import com.example.guacon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button b1;
    private TextView t;
    private EditText e1,e2;
    private RadioButton r;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    String email, password, type;
    private FirebaseFirestore medtrack;
    HashMap<String, String> m = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1 = (EditText) findViewById(R.id.email_text);
        e2 = (EditText) findViewById(R.id.password_text);
        t = (TextView) findViewById(R.id.signup_link);
        b1 = (Button) findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
    }

    private void loginUserAccount(){
        progressBar.setVisibility(View.VISIBLE);
        email = e1.getText().toString();
        password = e2.getText().toString();

        if(TextUtils.isEmpty(email)){
            e1.setError("required");
            return;
        }

        if(TextUtils.isEmpty(password)){
            e2.setError("required");
            return;
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
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
