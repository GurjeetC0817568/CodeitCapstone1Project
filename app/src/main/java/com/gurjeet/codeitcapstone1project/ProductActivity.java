package com.gurjeet.codeitcapstone1project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    //RecyclerView.LayoutManager layoutManager;
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
    TextView userName,sellerName,productName;
    String userId,prodSelected;
    Button btnPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        userId=getIntent().getStringExtra("userId");
        prodSelected=getIntent().getStringExtra("prodSelected");
        userName = findViewById(R.id.userName);
        sellerName = findViewById(R.id.sellerName);
        productName = findViewById(R.id.productName);
        btnPay = findViewById(R.id.btnPay);
        getUser();


       /* btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: here will write paypal code
            }
        });*/

    }

    public void getUser() {
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot npsnapshot : snapshot.getChildren()) {
                        UserRegister u = npsnapshot.getValue(UserRegister.class);
                        Log.d("result", "Name: " + u.getName()+" Email: "+ u.getEmail()+" Phone: "+ u.getPhone());
                        firebaseAuth = FirebaseAuth.getInstance();
                        user = firebaseAuth.getCurrentUser();
                        userName.setText(user.getUid()+"-"+user.getDisplayName());

                        if(userId.equals(u.getId())){

                            sellerName.setText("Name: "+u.getName()+"\nPhone: "+u.getPhone()+"\nEmail: "+u.getEmail()+"\nAddress"+u.getAddress());

                            //ast.makeText(ProductActivity.this, "Seller Email: " +u.getEmail() + "\nContact No.:" + u.getPhone() + "\nAddress: "+u.getAddress(), Toast.LENGTH_LONG).show();
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