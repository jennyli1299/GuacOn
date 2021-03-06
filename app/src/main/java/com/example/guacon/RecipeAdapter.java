package com.example.guacon;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Locale;

//manages recyclerview for recipe data class and put data in recipe_card.xml
public class RecipeAdapter extends FirestoreRecyclerAdapter<Recipe, RecipeAdapter.RecipesViewHolder> {

    private OnItemClickListener listener;
    Context context;

    public RecipeAdapter(Context context, @NonNull FirestoreRecyclerOptions<Recipe> options){
        super(options);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onBindViewHolder(@NonNull RecipesViewHolder holder, final int position, @NonNull final Recipe model){
        holder.name.setText(String.join(" ", model.getName()));
        //Loading image from Glide library.
        Glide.with(context).load(model.getFinal_photo()).into(holder.media_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = new Recipe();
                recipe.setName(model.getName());
                recipe.setPrep_time(model.getPrep_time());
                recipe.setCook_time(model.getCook_time());
                recipe.setTags(model.getTags());
                recipe.setFinal_photo(model.getFinal_photo());
                recipe.setInstructions(model.getInstructions());
                recipe.setIngredients(model.getIngredients());
                recipe.setOwner(model.getOwner());
                recipe.setNo_of_stars(model.getNo_of_stars());
                recipe.setTimestamp(model.getTimestamp());
                recipe.setDoc_id(getSnapshots().getSnapshot(position).getId());
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
        TextView name, link;
        ImageView media_image;
        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            media_image = itemView.findViewById(R.id.media_image);
            link = itemView.findViewById(R.id.seerecipe);
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
