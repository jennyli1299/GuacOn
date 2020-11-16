package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button refine;
    private Button[] options= new Button[5];

    public static HashMap<String, Boolean> refinePreferences;
//    public static String[] Preferences = {"Vegetarian", "Vegan", "GF", "DF", "Naturally Sweetened"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadioGroup rg = (RadioGroup)findViewById(R.id.rg);
        RadioButton rb = (RadioButton)findViewById(rg.getCheckedRadioButtonId());
        rb.setBackgroundResource(R.color.white);
        rb.setChecked(false);
        refinePreferences = new HashMap<String, Boolean>();

    }

    public void onClick(View view){
        refine = (Button) findViewById(R.id.btn_refine);

        refine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refineIntent = new Intent(getBaseContext(), Refine.class);
                String[] preferences = {"Vegetarian", "Vegan", "GF", "DF", "Naturally Sweetened"};
                for (String s: preferences) {
                        refineIntent.putExtra(s,refinePreferences.get(s));
                    }
                startActivity(refineIntent);
            }
        });

        options[0]=(Button) findViewById(R.id.btn_breakfast);
        options[1]=(Button) findViewById(R.id.btn_lunch);
        options[2]=(Button) findViewById(R.id.btn_dinner);
        options[3]=(Button) findViewById(R.id.btn_snacks);
        options[4]=(Button) findViewById(R.id.btn_all);
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
//        startActivity(new Intent(MainActivity.this, Category_List.class));
//        finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}