package com.example.guacon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
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

public class CollectionListAdapter extends FirestoreRecyclerAdapter<UserCard, CollectionListAdapter.RecipesViewHolder> {

    private CollectionListAdapter.OnItemClickListener listener;
    Context context;

    public CollectionListAdapter(Context context, @NonNull FirestoreRecyclerOptions<UserCard> options){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final CollectionListAdapter.RecipesViewHolder holder, int position, @NonNull final UserCard model){
        final ArrayList<String> recipeArrayList = model.getRecipe();
        holder.collection_name.setText(model.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: onClick doesn't work
                Toast.makeText(context, "button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to tell the class about the Card view (here
    // "profile_card.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public CollectionListAdapter.RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_option, parent, false);
        return new CollectionListAdapter.RecipesViewHolder(view);
    }

    // Sub Class to create references of the views in Card
    // view (here "collection_option.xml")
    class RecipesViewHolder extends RecyclerView.ViewHolder {
        Button collection_name;
        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            collection_name = itemView.findViewById(R.id.option);
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
    public void setOnItemClickListener(CollectionListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
