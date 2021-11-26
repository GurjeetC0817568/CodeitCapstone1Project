package com.gurjeet.codeitcapstone1project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.gurjeet.codeitcapstone1project.adapter.CallAdapter;
import com.gurjeet.codeitcapstone1project.adapter.PostAdapter;
import com.gurjeet.codeitcapstone1project.model.PostModel;
import com.gurjeet.codeitcapstone1project.model.UserRegister;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    PostAdapter postAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PostModel> postList;
    FragmentManager fragmentManager;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;
    DatabaseReference reference;
    final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("Register");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference;
    CollectionReference collectionReference = db.collection("data");
    Uri imageUri;
    TextView userName;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        userName = findViewById(R.id.userName);
    }

    public void getUser() {
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot npsnapshot : snapshot.getChildren()) {
                        UserRegister u = npsnapshot.getValue(UserRegister.class);
                        userId=getIntent().getStringExtra("userId");

                        if(userId.equals(u.getId())){
                            userName.setText("Seller Name: " + u.getName() + "\nContact No.:" + u.getPhone() + "\nAddress: "+u.getAddress());
                            Toast.makeText(ProductActivity.this, "Seller Email: " + u.getEmail() + "\nContact No.:" + u.getPhone() + "\nAddress: "+u.getAddress(), Toast.LENGTH_LONG).show();

                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }






}