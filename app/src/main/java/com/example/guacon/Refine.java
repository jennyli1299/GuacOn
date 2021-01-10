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
import java.util.HashMap;

//TODO: add preferences in the form of Bundle to be used throughout the application lifecycle
public class Refine extends AppCompatActivity {

    Button ok;

    CheckBox veg;
    CheckBox v;
    CheckBox gf;
    CheckBox df;
    CheckBox ns;

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

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("pref", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("vegan", v.isChecked());
                editor.putBoolean("vegetarian", veg.isChecked());
                editor.putBoolean("gluten_free", gf.isChecked());
                editor.putBoolean("dairy_free", df.isChecked());
                editor.putBoolean("naturally_sweetened", ns.isChecked());
                editor.apply();
                Toast.makeText(Refine.this, "Preferenecs Saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Refine.this, MainActivity.class));
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}