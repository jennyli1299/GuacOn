package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Refine extends AppCompatActivity {

    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refine);

        ok = (Button) findViewById(R.id.btn_ok);
    }

    public void okay(View v) {
        Intent mainPgIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(mainPgIntent);
    }
}