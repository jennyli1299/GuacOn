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

import com.example.guacon.R;
import com.example.guacon.SearchResult;
import com.example.guacon.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button b1;
    private TextView t;
    private EditText e1,e2;
    private RadioButton r;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    String email, password;
    HashMap<String, String> m = new HashMap<>();
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1 = findViewById(R.id.email_text);
        e2 = findViewById(R.id.password_text);
        t = findViewById(R.id.signup_link);
        b1 = findViewById(R.id.login_button);
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
                    progressBar.setVisibility(View.GONE);

                    //save user info in sharedpreferences for local usage
                    saveData(email);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void saveData(String email) {
        //save data in sharedpreferences 'user' --> 'user_email' and 'User' type object
        final SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("user_email", email);
        FirebaseFirestore.getInstance().collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    //save data to User class object
                    user.setName(documentSnapshot.getString("First_Name"), documentSnapshot.getString("Last_Name"));
                    user.setAge(documentSnapshot.getLong("Age"));
                    user.setFollowers((ArrayList<String>)documentSnapshot.get("Followers"));
                    user.setFollowing((ArrayList<String>)documentSnapshot.get("Following"));
                    user.setFollowers_count(documentSnapshot.getLong("Followers_count"));
                    user.setFollowing_count(documentSnapshot.getLong("Following_count"));

                    //convert to User class object to Json type for storing in sharedpreferences
                    List<User> temp = new ArrayList<>();
                    temp.add(user);
                    Intent intent = new Intent(getApplicationContext(), SearchResult.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(temp);

                    editor.putString("user_info", json);
                    editor.commit();

                    startActivity(intent);
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
