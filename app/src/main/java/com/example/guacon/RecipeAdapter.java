package com.example.guacon;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

//manages recyclerview for recipe data class and put data in recipe_card.xml
public class RecipeAdapter extends FirestoreRecyclerAdapter<Recipe, RecipeAdapter.RecipesViewHolder> {

    private OnItemClickListener listener;
    Context context;

    public RecipeAdapter(Context context, @NonNull FirestoreRecyclerOptions<Recipe> options){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecipesViewHolder holder, int position, @NonNull final Recipe model){
        holder.name.setText(model.getName());
        holder.prep_time.setText("Prep Time: " + model.getPrep_time() + " min");
        holder.supporting_text.setText(model.tags());
        //Loading image from Glide library.
        Glide.with(context).load(model.getFinal_photo()).into(holder.media_image);
        // https://firebasestorage.googleapis.com/v0/b/guacon-65c8f.appspot.com/o/images%2F2634cd89-f286-437b-9716-1ed4cb8d1596?alt=media&token=c3fd46d2-d2f6-42ad-bf8d-ae3233ecee2d
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = new Recipe();
                recipe.setName(model.getName());
                recipe.setPrep_time(model.getPrep_time());
                recipe.setCook_time(model.getCook_time());
                recipe.setVegan(model.isVegan());                               recipe.setVegetarian(model.isVegetarian());
                recipe.setGluten_free(model.isGluten_free());                   recipe.setDairy_free(model.isDairy_free());
                recipe.setNaturally_sweetened(model.isNaturally_sweetened());   recipe.setFinal_photo(model.getFinal_photo());
                recipe.setInstructions(model.getInstructions());                recipe.setIngredients(model.getIngredients());
                recipe.setBreakfast(model.isBreakfast());                       recipe.setBreakfast(model.isLunch());
                recipe.setBreakfast(model.isDinner());                       recipe.setBreakfast(model.isSnacks());
                Intent intent = new Intent(context, Recipe_Detail.class);
                intent.putExtra("Recipe", recipe);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    // Function to tell the class about the Card view (here
    // "recipe_card.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new RecipeAdapter.RecipesViewHolder(view);
    }

    // Sub Class to create references of the views in Card
    // view (here "recipe_card.xml")
    class RecipesViewHolder extends RecyclerView.ViewHolder {
        TextView name, prep_time, supporting_text;
        ImageView media_image;
        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            prep_time = itemView.findViewById(R.id.prep_time);
            supporting_text = itemView.findViewById(R.id.supporting_text);
            media_image = itemView.findViewById(R.id.media_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
