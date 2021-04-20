package com.example.guacon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.guacon.Profile.CardResult;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

//manages recyclerview for recipe data class and put data in profile_card.xml
public class UserCardAdapter extends FirestoreRecyclerAdapter<UserCard, UserCardAdapter.RecipesViewHolder> {

    private OnItemClickListener listener;
    Context context;

    public UserCardAdapter(Context context, @NonNull FirestoreRecyclerOptions<UserCard> options){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final UserCardAdapter.RecipesViewHolder holder, int position, @NonNull final UserCard model){
        final ArrayList<String> recipeArrayList = model.getRecipe();
        final float scale = context.getResources().getDisplayMetrics().density;
        int height = (int) (100 * scale + 0.5f);
        holder.name.setText(model.getName());
        if(model.getName().equals("Your Recipes")){
            holder.media_image.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, height));
            holder.media_image2.setVisibility(View.GONE);
            holder.media_image3.setVisibility(View.GONE);
            holder.media_image.setImageResource(R.drawable.guacon);
        }
        if (model.getCount() == 1) {
            holder.media_image.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, height));
            holder.media_image2.setVisibility(View.GONE);
            holder.media_image3.setVisibility(View.GONE);
        }
        if (model.getCount() == 2)
            holder.media_image3.setVisibility(View.GONE);

        final Recipe r = new Recipe();
        for(int i=0; i< (model.getCount()>3 ? 3 : model.getCount());i++) {
            final int finalI = i;
            FirebaseFirestore.getInstance().document("recipes/" + recipeArrayList.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    r.setFinal_photo(documentSnapshot.getString("final_photo"));
                    if(finalI == 0){
                        Glide.with(context).load(r.getFinal_photo()).into(holder.media_image);
                    }
                    if(finalI == 1){
                        Glide.with(context).load(r.getFinal_photo()).into(holder.media_image2);
                    }
                    if(finalI == 2){
                        Glide.with(context).load(r.getFinal_photo()).into(holder.media_image3);
                    }
                }
            });
        }
            //Loading image from Glide library.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CardResult.class);
                intent.putExtra("Card", model.getName());
                intent.putStringArrayListExtra("Recipe List", recipeArrayList);
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
        return new UserCardAdapter.RecipesViewHolder(view);
    }

    // Sub Class to create references of the views in Card
    // view (here "profile_card.xml")
    class RecipesViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView media_image, media_image2, media_image3;
        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.card_name);
            media_image = itemView.findViewById(R.id.profile_media_image);
            media_image2 = itemView.findViewById(R.id.profile_media_image2);
            media_image3 = itemView.findViewById(R.id.profile_media_image3);
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