package com.example.guacon.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.guacon.Profile.Profile;
import com.example.guacon.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//enter the details for your recipe and add them to our database
public class RecipeForm extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    HashMap<String, String> userRecipe = new HashMap<String, String>();
    ViewFlipper simpleViewFlipper;
    Spinner spinner, spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        // get The references of Button and ViewFlipper
        simpleViewFlipper = (ViewFlipper) findViewById(R.id.simpleViewFlipper); // get the reference of ViewFlipper

        // set the animation type to ViewFlipper
        simpleViewFlipper.setInAnimation(this, R.anim.slide_right);
        simpleViewFlipper.setOutAnimation(this, R.anim.slide_left);

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> time_unit = new ArrayList<>();
        time_unit.add("min");
        time_unit.add("hr");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time_unit);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter);

        /*findViewById(R.id.buttonNext7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("recipes").
                        document().set(userRecipe);
            }
        });*/
    }


    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonNext1:  userRecipe.put("Name", ((EditText)findViewById(R.id.ans1)).getText().toString());
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("How much time is required for preparation?");
                break;
            case R.id.buttonNext2:  userRecipe.put("Prep_Time", ((EditText)findViewById(R.id.ans1)).getText().toString());
                //TODO: get unit from spinner
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("How much time is required for cooking it?");
                break;
            case R.id.buttonNext3:  userRecipe.put("Cook_Time", ((EditText)findViewById(R.id.ans3)).getText().toString());
                //TODO: get unit from spinner
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("List all the ingredients for your recipe?");
                break;
            case R.id.buttonNext4:  String ing[];
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("Provide the instructions for your recipe?");
                break;
            case R.id.buttonNext5:  simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("What tag(s) suits your recipe?");
                break;
            case R.id.buttonNext6:  simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("Show us how your creation looks like?");
                break;
            case R.id.buttonNext7:  startActivity(new Intent(getApplicationContext(), Profile.class));
                break;
        }
    }

    public void isChecked(View v) {
        if( ((CheckBox) findViewById(v.getId())).isChecked() )
            findViewById(v.getId()).setBackgroundResource(R.color.gray);
        if( !((CheckBox) findViewById(v.getId())).isChecked() )
            findViewById(v.getId()).setBackgroundResource(R.color.white);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        String item = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}