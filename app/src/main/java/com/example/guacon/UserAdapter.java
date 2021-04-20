package com.example.guacon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.guacon.Profile.CardResult;
import com.example.guacon.Profile.PublicProfile;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.UserViewHolder> {

    private OnItemClickListener listener;
    Context context;

    public UserAdapter(Context context, @NonNull FirestoreRecyclerOptions<User> options){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final UserAdapter.UserViewHolder holder, final int position, @NonNull final User model){
        holder.name.setText(model.getName());
        if(model.getFollowers().contains(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            holder.follow.setText("Following");
            holder.follow.setBackgroundResource(R.color.colorPrimary);
            holder.follow.setTextColor(context.getResources().getColor(R.color.black));
        }
        else {
            holder.follow.setText("Follow");
            holder.follow.setBackgroundResource(R.color.colorPrimaryDark);
            holder.follow.setTextColor(context.getResources().getColor(R.color.white));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PublicProfile.class);
                intent.putExtra("owner", model.getName());
                intent.putExtra("owner_email", getSnapshots().getSnapshot(position).getId());
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
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_following_card, parent, false);
        return new UserAdapter.UserViewHolder(view);
    }

    // Sub Class to create references of the views in Card
    // view (here "profile_card.xml")
    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView dp;
        Button follow;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name1);
            dp = itemView.findViewById(R.id.dp1);
            follow = itemView.findViewById(R.id.follow);
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
