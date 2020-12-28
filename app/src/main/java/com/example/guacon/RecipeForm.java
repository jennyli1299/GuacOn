package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

//enter the details for your recipe and add them to our database
public class RecipeForm extends AppCompatActivity {

    private ViewFlipper simpleViewFlipper;
    TextView t;
    Spinner spinner, spinner2;
    TextView set_time;
    Button btn_set;

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
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        spinner2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("g");
        categories.add("mg");
        categories.add("mcg");
        categories.add("mL");
        categories.add("%");
        categories.add("IU");

        List<String> freq = new ArrayList<String>();
        freq.add("Everyday");
        freq.add("6 days a week");
        freq.add("5 days a week");
        freq.add("4 days a week");
        freq.add("3 days a week");
        freq.add("2 days a week");
        freq.add("Every 2 days");
        freq.add("Once a week");
        freq.add("Every 28 days");
        freq.add("Only when needed");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, freq);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter2);

        set_time = (TextView)findViewById(R.id.set_time);
        btn_set = (Button)findViewById(R.id.btn_set);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    public void checked(View v){
        ((RadioButton)findViewById(v.getId())).setBackgroundResource(R.color.colorAccent);
        a.add(((RadioButton)findViewById(v.getId())).getText().toString());
        simpleViewFlipper.showNext();
        ((TextView)findViewById(R.id.ques)).setText("What is the dosage taken once?");
    }

    ArrayList<String> a = new ArrayList<String>();

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonNext1:  a.add(((EditText)findViewById(R.id.ans1)).getText().toString());
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("What is the form of medicine?");
                break;
            case R.id.buttonNext3:  a.add(((EditText)findViewById(R.id.ans3)).getText().toString());
                a.add(String.valueOf(spinner.getSelectedItem()));
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("At what time you wish to take medicine?");
                break;
            case R.id.buttonNext4:  simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("When do you wish to repeat this schedule?");
                break;
            case R.id.buttonNext5:  a.add(String.valueOf(spinner2.getSelectedItem()));
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("For how long you wish to take this medicine?");
                break;
            case R.id.buttonNext6:  a.add(((EditText)findViewById(R.id.ans6)).getText().toString());
                startActivity(new Intent(RecipeForm.this, MainActivity.class));
                break;
        }
    }
}