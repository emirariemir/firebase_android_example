package com.emirari.mytestapplication.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.emirari.mytestapplication.R;
import com.emirari.mytestapplication.adopter.FeedRecyclerAdapter;
import com.emirari.mytestapplication.databinding.ActivityFeedBinding;
import com.emirari.mytestapplication.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private ActivityFeedBinding binding;

    FeedRecyclerAdapter feedRecyclerAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        posts = new ArrayList<Post>();

        getPosts();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedRecyclerAdapter = new FeedRecyclerAdapter(posts);
        binding.recyclerView.setAdapter(feedRecyclerAdapter);
    }

    private void getPosts() {
        firestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(FeedActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                if (value != null) {
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Map<String, Object> post = doc.getData();
                        String userMail = (String) post.get("userMail");
                        String caption = (String) post.get("caption");
                        String downloadUrl = (String) post.get("downloadUrl");
                        Post postObject = new Post(userMail, caption, downloadUrl);
                        posts.add(postObject);
                        System.out.println(doc.getData());

                    }
                    posts.add(new Post("emir", "test caption", "https://firebasestorage.googleapis.com/v0/b/insta-clone-java-88947.appspot.com/o/images%2F29605741-f181-45d3-9da1-8627c5a607d8.jpg?alt=media&token=f0348dda-18be-4c88-b3d4-72d2b6bf91ff"));
                    feedRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_option) {
            Intent intent = new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.signOut_option) {
            mAuth.signOut();
            Intent intent = new Intent(FeedActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}