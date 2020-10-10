package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button refine;
    Button[] options= new Button[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

}