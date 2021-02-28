package com.example.guacon.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.guacon.Profile.Profile;
import com.example.guacon.R;
import com.example.guacon.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

//enter the details for your recipe and add them to our database
public class RecipeForm extends AppCompatActivity implements View.OnClickListener{

    ViewFlipper simpleViewFlipper;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;
    Recipe newRecipe = new Recipe();
    Button add_inst, add_ing;
    ArrayList<String> inst = new ArrayList<>(), ing = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        // get The references of Button and ViewFlipper
        simpleViewFlipper = (ViewFlipper) findViewById(R.id.simpleViewFlipper); // get the reference of ViewFlipper

        // set the animation type to ViewFlipper
        simpleViewFlipper.setInAnimation(this, R.anim.slide_right);
        simpleViewFlipper.setOutAnimation(this, R.anim.slide_left);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        add_inst = findViewById(R.id.add);
        add_ing = findViewById(R.id.add2);

        add_inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.activity_listview2, (LinearLayout) findViewById(R.id.container), false);

                final TextView text = (TextView) addView.findViewById(R.id.inst1);
                text.setText(((EditText) findViewById(R.id.inst)).getText().toString());

                Button delete_inst = (Button) addView.findViewById(R.id.delete_inst);
                final View.OnClickListener thisListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        inst.remove(text.getText().toString());
                        ((LinearLayout)addView.getParent()).removeView(addView);
                    }
                };

                delete_inst.setOnClickListener(thisListener);

                if(inst.contains(text.getText().toString()))
                    Toast.makeText(RecipeForm.this, "Instruction already added", Toast.LENGTH_SHORT).show();
                else {
                    inst.add(text.getText().toString());
                    ((LinearLayout) findViewById(R.id.container)).addView(addView);
                }
            }
        });

        add_ing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.activity_listview2, (LinearLayout) findViewById(R.id.container2), false);

                final TextView text = (TextView) addView.findViewById(R.id.inst1);
                text.setText(((EditText) findViewById(R.id.ing)).getText().toString());

                Button delete_inst = (Button) addView.findViewById(R.id.delete_inst);
                final View.OnClickListener thisListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ing.remove(text.getText().toString());
                        ((LinearLayout)addView.getParent()).removeView(addView);
                    }
                };

                delete_inst.setOnClickListener(thisListener);

                if(ing.contains(text.getText().toString()))
                    Toast.makeText(RecipeForm.this, "Ingredient already added", Toast.LENGTH_SHORT).show();
                else {
                    ing.add(text.getText().toString());
                    ((LinearLayout) findViewById(R.id.container2)).addView(addView);
                }
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()){

            //add recipe name to Hashmap
            case R.id.buttonNext1:
                newRecipe.setName(((EditText)findViewById(R.id.ans1)).getText().toString());
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("How much time is required for preparation?");
                break;

            //add recipe preparation time to Hashmap
            case R.id.buttonNext2:
                newRecipe.setPrep_time(((EditText)findViewById(R.id.ans2)).getText().toString());
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("How much time is required for cooking it?");
                break;

            //add recipe cooking time to Hashmap
            case R.id.buttonNext3:
                newRecipe.setCook_time(((EditText)findViewById(R.id.ans3)).getText().toString());
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("List all the ingredients for your recipe?");
                break;

            //add recipe ingredients to HashMap
            case R.id.buttonNext4: newRecipe.setIngredients(ing);
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("Provide the instructions for your recipe?");
                break;

            //add recipe instructions to Hashmap
            case R.id.buttonNext5:  newRecipe.setInstructions(inst);
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("What tag(s) suits your recipe?");
                break;

            //add recipe tags to HashMap
            case R.id.buttonNext6:
                ArrayList<String> tags = new ArrayList<>();
                if(((CheckBox) findViewById(R.id.vegan)).isChecked())
                    tags.add("Vegan");
                if(((CheckBox) findViewById(R.id.vegetarian)).isChecked())
                    tags.add("Vegetarian");
                if(((CheckBox) findViewById(R.id.gluten_free)).isChecked())
                    tags.add("Gluten Free");
                if(((CheckBox) findViewById(R.id.dairy_free)).isChecked())
                    tags.add("Dairy Free");
                if(((CheckBox) findViewById(R.id.naturally_sweetened)).isChecked())
                    tags.add("Naturally Sweetened");
                newRecipe.setTags(tags);
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("What time does your recipe tastes best?");
                break;

            case R.id.buttonNext8:
                ArrayList<String> meal_time = new ArrayList<>();
                if(((CheckBox) findViewById(R.id.breakfast)).isChecked())
                    meal_time.add("Breakfast");
                if(((CheckBox) findViewById(R.id.dinner)).isChecked())
                    meal_time.add("Dinner");
                if(((CheckBox) findViewById(R.id.lunch)).isChecked())
                    meal_time.add("Lunch");
                if(((CheckBox) findViewById(R.id.snack)).isChecked())
                    meal_time.add("Snack");
                newRecipe.setMeal_time(meal_time);
                simpleViewFlipper.showNext();
                ((TextView)findViewById(R.id.ques)).setText("Show us how your creation looks?");
                break;

            //Upload Image to Firebase storage and userRecipe hashmap to Firestore
            case R.id.buttonNext7:
                uploadImage();
                if(((TextView) findViewById(R.id.image_name)).getText().toString().equals(""))
                    Toast.makeText(this, "Please upload an Image", Toast.LENGTH_SHORT).show();
                else {
                    newRecipe.setFinal_photo("https://firebasestorage.googleapis.com/v0/b/guacon-65c8f.appspot.com/o/images%2F" + ((TextView) findViewById(R.id.image_name)).getText().toString() + "?alt=media");
                    FirebaseFirestore.getInstance().collection("recipes").document().set(newRecipe);
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                }
                break;

            //select Image button
            case R.id.select:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ((ImageView) findViewById(R.id.image)).setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.image)).setImageBitmap(bitmap);
                ((TextView) findViewById(R.id.image_name)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.image_name)).setText(UUID.randomUUID().toString());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ ((TextView) findViewById(R.id.image_name)).getText().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(RecipeForm.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RecipeForm.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public void isChecked(View v) {
        if( ((CheckBox) findViewById(v.getId())).isChecked() )
            findViewById(v.getId()).setBackgroundResource(R.color.gray);
        if( !((CheckBox) findViewById(v.getId())).isChecked() )
            findViewById(v.getId()).setBackgroundResource(R.color.white);
    }

}
