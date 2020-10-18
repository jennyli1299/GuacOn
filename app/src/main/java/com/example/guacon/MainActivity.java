package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button refine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadioGroup rg = (RadioGroup)findViewById(R.id.rg);
        RadioButton rb = (RadioButton)findViewById(rg.getCheckedRadioButtonId());
        rb.setBackgroundResource(R.color.white);
        rb.setChecked(false);
    }

    public void onClick(View view){
        startActivity(new Intent(MainActivity.this, Refine.class));
        finish();
    }

    public void isChecked(View view){
        ((RadioButton)findViewById(view.getId())).setBackgroundResource(R.color.gray);
        startActivity(new Intent(MainActivity.this, Recipe.class));
        finish();
    }
}