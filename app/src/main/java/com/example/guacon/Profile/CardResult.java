  package com.example.guacon.Profile;

  import android.content.Intent;
  import android.os.Bundle;
  import android.os.Handler;
  import android.view.Menu;
  import android.view.MenuItem;
  import android.view.View;

  import androidx.appcompat.app.AppCompatActivity;
  import androidx.appcompat.widget.Toolbar;
  import androidx.recyclerview.widget.DefaultItemAnimator;
  import androidx.recyclerview.widget.GridLayoutManager;
  import androidx.recyclerview.widget.RecyclerView;

  import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
  import com.example.guacon.Login.Launcher;
  import com.example.guacon.R;
  import com.example.guacon.Recipe;
  import com.example.guacon.RecipeAdapter;
  import com.firebase.ui.firestore.FirestoreRecyclerOptions;
  import com.google.android.gms.tasks.OnSuccessListener;
  import com.google.firebase.auth.FirebaseAuth;
  import com.google.firebase.firestore.FieldPath;
  import com.google.firebase.firestore.FirebaseFirestore;
  import com.google.firebase.firestore.Query;
  import com.google.firebase.firestore.QuerySnapshot;

  import java.util.ArrayList;

public class CardResult extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecipeAdapter adapter;
    Query base;
    FirestoreRecyclerOptions<Recipe> options;
    ShimmerRecyclerView shimmerRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result3);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("Card"));
        setSupportActionBar(toolbar);

        findViewById(R.id.search_bar).setVisibility(View.GONE);

        shimmerRecyclerView = findViewById(R.id.shimmer_recycler_view);
        shimmerRecyclerView.showShimmerAdapter();

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<String> arrayList = getIntent().getStringArrayListExtra("Recipe List");

        if(getIntent().getStringExtra("Card").equals("Your Recipes"))
            base = FirebaseFirestore.getInstance().collection("recipes").whereEqualTo("owner", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        else
            base = FirebaseFirestore.getInstance().collection("recipes").whereIn(FieldPath.documentId(), arrayList);

        options = new FirestoreRecyclerOptions.Builder<Recipe>().setQuery(base, Recipe.class).build();
        adapter = new RecipeAdapter(getApplicationContext(), options);
        recyclerView.setAdapter(adapter);
        base.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    shimmerRecyclerView.hideShimmerAdapter();
                    (findViewById(R.id.default_text)).setVisibility(View.VISIBLE);
                }
                else
                    shimmerRecyclerView.hideShimmerAdapter();
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        menu.findItem(R.id.action_home).setVisible(false);
        menu.findItem(R.id.action_refine).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_profile) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
            return true;
        }

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Launcher.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}