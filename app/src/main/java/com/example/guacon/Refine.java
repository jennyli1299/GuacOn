package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.HashMap;

//TODO: add preferences in the form of Bundle to be used throughout the application lifecycle
public class Refine extends AppCompatActivity {

    Button ok;

    CheckBox veg;
    CheckBox v;
    CheckBox gf;
    CheckBox df;
    CheckBox naturallysweet;

    public HashMap<String, Boolean> preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refine);

        ok = (Button) findViewById(R.id.btn_ok);
        veg = (CheckBox) findViewById(R.id.vegetarian);
        v = (CheckBox) findViewById(R.id.vegan);
        gf = (CheckBox) findViewById(R.id.gluten_free);
        df = (CheckBox) findViewById(R.id.dairy_free);
        naturallysweet = (CheckBox) findViewById(R.id.naturally_sweetened);

        preferences = new HashMap<String, Boolean>();
        Bundle bundle = getIntent().getExtras();
        preferences = MainActivity.getPreferences(bundle);

        setAppropriateCheckBoxes();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onCheck(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.vegan:
//                if (checked) {
                    preferences.put("Vegan", checked);
                    MainActivity.updatePreferences("Vegan", checked);
//                }
//                else {
//                    preferences.put("Vegan", false);
//                }
//                break;
            case R.id.vegetarian:
//                if (checked) {
                    preferences.put("Vegetarian", checked);
                MainActivity.updatePreferences("Vegetarian", checked);
//                }
//                break;
            case R.id.gluten_free:
//                if (checked) {
                    preferences.put("GF", checked);
                MainActivity.updatePreferences("GF", checked);
//                }
//                break;
            case R.id.dairy_free:
//                if (checked) {
                    preferences.put("DF", checked);
                MainActivity.updatePreferences("DF", checked);
//                }
//                break;
            case R.id.naturally_sweetened:
//                if (checked) {
                    preferences.put("Naturally Sweetened", checked);
                MainActivity.updatePreferences("Naturally Sweetened", checked);
//                }
//                break;
        }
    }

    public void okay(View v) {
        Intent mainPgIntent = new Intent(getBaseContext(), MainActivity.class);
//        mainPgIntent.putExtra("veg",true);
        startActivity(mainPgIntent);
        finish();
    }

    public void setAppropriateCheckBoxes() {
//        for (String s: MainActivity.Preferences) {
//            if (MainActivity.refinePreferences.containsKey(s)) {
//                setCheckBox(s);
//            }
//        }
        veg.setChecked(MainActivity.refinePreferences.get("Vegetarian"));
        v.setChecked(MainActivity.refinePreferences.get("Vegan"));
        gf.setChecked(MainActivity.refinePreferences.get("GF"));
        df.setChecked(MainActivity.refinePreferences.get("DF"));
        naturallysweet.setChecked(MainActivity.refinePreferences.get("Naturally Sweetened"));
    }

//    private void setCheckBox(String s) {
//        switch (s) {
//            case "Vegetarian":
//                veg.setChecked(true);
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}