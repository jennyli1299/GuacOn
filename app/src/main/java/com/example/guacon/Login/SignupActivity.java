package com.example.guacon.Login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guacon.MainActivity;
import com.example.guacon.R;
import com.example.guacon.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
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

        //creating a new user
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //checking if success
                if(task.isSuccessful()){
                    //enter in Starting Gradle Daemon...database
                    getDataFromUser();
                }
                else{
                    //display some message here
                    Toast.makeText(SignupActivity.this,"Registration Error"+task.getException(),Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private int mYear, mMonth, mDay;

    public void getDataFromUser() {
        final Map<String, Object> newUser = new HashMap<>();
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_profile);

        final Calendar cal = Calendar.getInstance();
        ((ImageButton) dialog.findViewById(R.id.date_icon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = cal.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String mm = String.format("%02d", monthOfYear+1);
                        String dd = String.format("%02d", dayOfMonth);
                        ((TextView) dialog.findViewById(R.id.age)).setText(year + "-" + mm + "-" + dd);
                        mYear = year;
                        mMonth = Integer.parseInt(mm) - 1;
                        mDay = Integer.parseInt(dd);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        ((Button) dialog.findViewById(R.id.buttonOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.set(mYear, mMonth, mDay);
                if(TextUtils.isEmpty(((EditText)dialog.findViewById(R.id.first_name)).getText().toString())){
                    ((EditText)dialog.findViewById(R.id.first_name)).setError("Required");
                }
                if(TextUtils.isEmpty(((EditText)dialog.findViewById(R.id.last_name)).getText().toString())){
                    ((EditText)dialog.findViewById(R.id.last_name)).setError("Required");
                }
                if(TextUtils.isEmpty(((EditText)dialog.findViewById(R.id.age)).getText().toString())){
                    ((EditText)dialog.findViewById(R.id.age)).setError("Required");
                }
                else {
                    newUser.put("First_Name", ((EditText) dialog.findViewById(R.id.first_name)).getText().toString());
                    newUser.put("Last_Name", ((EditText) dialog.findViewById(R.id.last_name)).getText().toString());
                    newUser.put("Age", cal.getTimeInMillis());
                    newUser.put("Followers", new ArrayList<String>());
                    newUser.put("Following", new ArrayList<String>());
                    newUser.put("Followers_count", 0);
                    newUser.put("Following_count", 0);
                    //add data to collection 'Users'
                    guacon.collection("Users").document(email).set(newUser);
                    dialog.cancel();
                    saveDataToLocal(email);
                }
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void saveDataToLocal(String email) {
        final SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_email", email);
        editor.commit();
        FirebaseFirestore.getInstance().collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    user.setName(documentSnapshot.getString("First_Name"), documentSnapshot.getString("Last_Name"));
                    user.setAge(documentSnapshot.getLong("Age"));
                    user.setFollowers((ArrayList<String>)documentSnapshot.get("Followers"));
                    user.setFollowing((ArrayList<String>)documentSnapshot.get("Following"));
                    user.setFollowers_count(documentSnapshot.getLong("Followers_count"));
                    user.setFollowing_count(documentSnapshot.getLong("Following_count"));
                }
            }
        });
        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("user_info", user));
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