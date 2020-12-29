package com.example.guacon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

//enter the details for your recipe and add them to our database
public class RecipeForm extends AppCompatActivity {

    Button cancel;
    Button addRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);
        cancel = (Button) findViewById(R.id.button2);
        addRecipe = (Button) findViewById(R.id.button3);

        // Goes back to the profile page if the user hits cancel
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }
        });
        HashMap<String, String> userRecipe = new HashMap<String, String>();
        // TODO- add items to the hashmap
        //the keys are the names of the fields on Firebase and the values are the user's input
        EditText mEdit;
        mEdit = (EditText)findViewById(R.id.editTextTextPersonName);
        userRecipe.put("name", mEdit.getText().toString());
        mEdit = (EditText)findViewById(R.id.editTextTextPersonName);
        userRecipe.put("cook_time", mEdit.getText().toString());
        userRecipe.put("dairy_free", mEdit.getText().toString());
        userRecipe.put("name", mEdit.getText().toString());
        userRecipe.put("name", mEdit.getText().toString());
        userRecipe.put("name", mEdit.getText().toString());
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("recipes").
                        document().set(userRecipe);
            }
        });
    }

}