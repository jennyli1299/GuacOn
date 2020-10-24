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
    Button[] options= new Button[5];

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
        refine = (Button) findViewById(R.id.btn_refine);

        refine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refineIntent = new Intent(getBaseContext(), Refine.class);
                startActivity(refineIntent);
            }
        });

        options[0]=(Button) findViewById(R.id.btn_Breakfast);
        options[1]=(Button) findViewById(R.id.btn_lunch);
        options[2]=(Button) findViewById(R.id.btn_dinner);
        options[3]=(Button) findViewById(R.id.btn_snack);
        options[4]=(Button) findViewById(R.id.btn_searchAll);
        //direct the buttons to search results
        for(int i=0;i<5;i++){
            options[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searchIndent = new Intent(getBaseContext(), SearchResult.class);
                    startActivity(searchIndent);
                }
            });
        }

    }

    public void isChecked(View view){
        ((RadioButton)findViewById(view.getId())).setBackgroundResource(R.color.gray);
        startActivity(new Intent(MainActivity.this, Category_List.class));
        finish();
    }
}