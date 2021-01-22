package com.example.guacon.Login;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.guacon.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
    User user = new User();

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

                    saveData(email);

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

    public void saveData(String email) {
        final SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_email", email);
        FirebaseFirestore.getInstance().collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    user.setName(documentSnapshot.getString("First_Name"), documentSnapshot.getString("Last_Name"));
                    user.setAge(Integer.parseInt(String.valueOf(documentSnapshot.getLong("Age"))));
                    user.setSaved_recipes((ArrayList<String>)documentSnapshot.get("saved_recipes"));
                    user.setYour_recipes((ArrayList<String>)documentSnapshot.get("your_recipes"));

                    editor.putString("user_name", user.getName());
                    editor.putInt("user_age", user.getAge());
                    editor.putStringSet("user_saved_recipes", new HashSet<String>(user.getSaved_recipes()));
                    editor.putStringSet("user_recipes", new HashSet<String>(user.getYour_recipes()));
                    editor.apply();
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
