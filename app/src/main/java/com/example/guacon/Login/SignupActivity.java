package com.example.guacon.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Map;
import java.util.Set;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    Button b1;
    RadioButton r;
    EditText e1,e2;
    TextView t1,t2;
    private FirebaseAuth auth;
    private FirebaseFirestore guacon;
    String email, password;
    private ProgressDialog progressDialog;
    User user = new User();

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
        guacon = FirebaseFirestore.getInstance();
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

        final Map<String, Object> newUser = new HashMap<>();
        newUser.put("First_Name", "");
        newUser.put("Last_Name", "");
        newUser.put("Age", 18);
        newUser.put("saved_recipes", new ArrayList<String>());
        newUser.put("your_recipes", new ArrayList<String>());

        //creating a new user
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //checking if success
                if(task.isSuccessful()){
                    //enter in Starting Gradle Daemon...database
                    guacon.collection("Users").document(email).set(newUser);

                    saveData(email);

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else{
                    //display some message here
                    Toast.makeText(SignupActivity.this,"Registration Error"+task.getException(),Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
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

    public void onClick(View view) {
        if(view.getId()==R.id.signup_button)
            //calling register method on click
            registerUser();
        else{
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}