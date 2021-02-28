package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Refine extends AppCompatActivity {

    Button ok;

    CheckBox veg;
    CheckBox v;
    CheckBox gf;
    CheckBox df;
    CheckBox ns;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refine);

        ok = (Button) findViewById(R.id.btn_ok);
        veg = (CheckBox) findViewById(R.id.vegetarian);
        v = (CheckBox) findViewById(R.id.vegan);
        gf = (CheckBox) findViewById(R.id.gluten_free);
        df = (CheckBox) findViewById(R.id.dairy_free);
        ns = (CheckBox) findViewById(R.id.naturally_sweetened);

        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ArrayList<String> pref = new ArrayList<String>(sharedPreferences.getStringSet("preferences", new HashSet<String>()));
        markPref(pref);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> preferences = new HashSet<>();
                if(v.isChecked())
                    preferences.add("Vegan");
                if(veg.isChecked())
                    preferences.add("Vegetarian");
                if(gf.isChecked())
                    preferences.add("Gluten Free");
                if(df.isChecked())
                    preferences.add("Dairy Free");
                if(ns.isChecked())
                    preferences.add("Naturally Sweetened");

                editor.putStringSet("preferences", preferences);
                editor.commit();
                Toast.makeText(Refine.this, "Preferenecs Saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Refine.this, MainActivity.class));
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    void markPref(ArrayList<String> pref){
        if(pref.contains("Vegan"))
            v.setChecked(true);
        if(pref.contains("Vegetarian"))
            veg.setChecked(true);
        if(pref.contains("Gluten Free"))
            gf.setChecked(true);
        if(pref.contains("Dairy Free"))
            df.setChecked(true);
        if(pref.contains("Naturally Sweetened"))
            ns.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}