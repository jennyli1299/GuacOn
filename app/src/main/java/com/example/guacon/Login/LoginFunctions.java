package com.example.guacon.Login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guacon.R;
import com.example.guacon.SearchResult;
import com.example.guacon.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginFunctions extends AppCompatActivity {

    private int mYear, mMonth, mDay;
    User user = new User();

    public void getDataFromUser(final String email, final Context context) {
        final Map<String, Object> newUser = new HashMap<>();
        final Dialog dialog = new Dialog(context);

        //form in a dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_profile);

        final Calendar cal = Calendar.getInstance();
        //get DOB for age
        ((ImageButton) dialog.findViewById(R.id.date_icon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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

        //save data in db
        ((Button) dialog.findViewById(R.id.buttonOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.set(mYear, mMonth, mDay);

                //check for empty fields
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
                    FirebaseFirestore.getInstance().collection("Users").document(email).set(newUser);
                    dialog.cancel();

                    //save data for local usage
                    saveData(email, context);
                }
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void saveData(String email, final Context context) {
        //save data in sharedpreferences 'user' --> 'user_email' and 'User' type object
        final SharedPreferences sharedPreferences = context.getSharedPreferences("user", 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();

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
                    Gson gson = new Gson();
                    String json = gson.toJson(temp);

                    editor.putString("user_info", json);
                    editor.apply();
                    Toast.makeText(context, "Changes made", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, SearchResult.class));
                }
            }
        });
    }
}
