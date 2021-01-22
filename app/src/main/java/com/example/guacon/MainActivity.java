package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.guacon.Profile.Profile;

import java.util.HashMap;

//TODO: In alignment with Refine.class
public class MainActivity extends AppCompatActivity {

    private Button refine;
    private Button[] options= new Button[5];

    public static HashMap<String, Boolean> refinePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup rg = (RadioGroup)findViewById(R.id.rg);
        RadioButton rb = (RadioButton)findViewById(rg.getCheckedRadioButtonId());

        rb.setBackgroundResource(R.color.white);
        rb.setChecked(false);
        refinePreferences = new HashMap<String, Boolean>();
        loadInitialPref();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void isChecked(View view){
        ((RadioButton)findViewById(view.getId())).setBackgroundResource(R.color.gray);
        Intent intent = new Intent(MainActivity.this, SearchResult.class);
        intent.putExtra("refinePreferences", refinePreferences);
        startActivity(intent);
    }

    public static HashMap<String, Boolean> getPreferences(Bundle b) {
        String[] preferences = {"Vegetarian", "Vegan", "GF", "DF", "Naturally Sweetened"};
        HashMap<String, Boolean> pref = new HashMap<String, Boolean>();
        if (b != null) {
            for (String s: preferences) {
                pref.put(s,b.getBoolean(s));
            }
        }
        return pref;
    }

    public static boolean updatePreferences(String p, Boolean b) {
        refinePreferences.put(p,b);
        return true;
    }

    public void loadInitialPref() {
        refinePreferences.put("Vegan", false);
        refinePreferences.put("Vegetarian", false);
        refinePreferences.put("GF", false);
        refinePreferences.put("DF", false);
        refinePreferences.put("Naturally Sweetened", false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refine) {
            startActivity(new Intent(getApplicationContext(), Refine.class));
            return true;
        }

        if (id == R.id.action_profile) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}