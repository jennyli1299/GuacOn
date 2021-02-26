package com.example.guacon;

import android.content.Context;
import android.content.Intent;
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

//manages recyclerview for recipe data class and put data in profile_card.xml
public class ProfileAdapter extends FirestoreRecyclerAdapter<Recipe, ProfileAdapter.RecipesViewHolder> {

    private OnItemClickListener listener;
    Context context;

    public ProfileAdapter(Context context, @NonNull FirestoreRecyclerOptions<Recipe> options){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecipesViewHolder holder, int position, @NonNull final Recipe model){
        holder.name.setText(model.getName());
        //Loading image from Glide library.
        Glide.with(context).load(model.getFinal_photo()).into(holder.media_image);
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
                Intent intent = new Intent(context, Recipe_Detail.class);
                intent.putExtra("Recipe", recipe);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    // Function to tell the class about the Card view (here
    // "profile_card.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card, parent, false);
        return new ProfileAdapter.RecipesViewHolder(view);
    }

    // Sub Class to create references of the views in Card
    // view (here "profile_card.xml")
    class RecipesViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView media_image;
        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recipe_name);
            media_image = itemView.findViewById(R.id.profile_media_image);
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