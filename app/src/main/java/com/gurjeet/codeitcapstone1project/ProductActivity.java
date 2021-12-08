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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    TextView userName,sellerName,productName,productCondition,productPrice,productDetails;
    String userId,prodSelectedId;
    Button btnPay,btnContact;
    public ImageView productImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        userId=getIntent().getStringExtra("userId");
        prodSelectedId=getIntent().getStringExtra("prodSelectedId");

        productImage = findViewById(R.id.productImage);
        userName = findViewById(R.id.userName);
        sellerName = findViewById(R.id.sellerName);
        productName = findViewById(R.id.productName);
        productCondition = findViewById(R.id.productCondition);
        productPrice = findViewById(R.id.productPrice);
        productDetails = findViewById(R.id.productDetails);
        btnPay = findViewById(R.id.btnPay);
        btnContact = findViewById(R.id.btnContact);
        getProduct();
        getUser();





    }

    public void getUser() {

        /*
        //Working code when fields name not matches in model and database
        nm.child("Register").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String email = ds.child("Email").getValue(String.class);
                    String phone = ds.child("Phone").getValue(String.class);
                    Log.d("Result 2", "onDataChange: " + name + " " + email + " " + phone);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/



        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot npsnapshot : snapshot.getChildren()) {
                        UserRegister u = npsnapshot.getValue(UserRegister.class);



                        Log.d("result", "Name: " + u.getName()+" Email: "+ u.getEmail()+" Id: "+ u.getId());
                        firebaseAuth = FirebaseAuth.getInstance();
                        user = firebaseAuth.getCurrentUser();
                       // userName.setText(user.getUid()+"-"+user.getDisplayName());


                        if(userId.equals(user.getUid())) {
                            btnContact.setVisibility(View.GONE);btnPay.setVisibility(View.GONE);
                            sellerName.setText("My Own Listing");
                            userName.setText("");
                        }else if(userId.equals(u.getId())){
                            sellerName.setText("Name: "+u.getName()+"\nPhone: "+u.getPhone()+"\nEmail: "+u.getEmail()+"\nAddress: "+u.getAddress());
                            final String ns = u.getId();
                            final String  username = u.getName();
                            final String  phone = u.getPhone();

                            btnContact.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intennt = new Intent(v.getContext(), Message.class);
                                    intennt.putExtra("id",ns);
                                    intennt.putExtra("title",username);
                                    intennt.putExtra("phone",phone);
                                    startActivity(intennt);
                                }
                            });
                            btnContact.setVisibility(View.VISIBLE);btnPay.setVisibility(View.VISIBLE);


                        }




                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void getProduct(){
        db.collection("data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (task.isSuccessful()) {
                                PostModel p = document.toObject(PostModel.class);
                                if(prodSelectedId.equals(p.getPid())){
                                    productName.setText("Name: "+p.getName());
                                    productCondition.setText("Condition: "+p.getCondition());
                                    productPrice.setText("Price: $"+p.getPrice());
                                    productDetails.setText(p.getDetails());
                                    String imageUrl;
                                    imageUrl= p.getImageURi();

                                    RequestOptions options = new RequestOptions();
                                    int corner_size = 40;
                                    options.placeholder(R.drawable.ic_launcher_background)
                                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                            .error(R.drawable.ic_launcher_foreground)
                                            .transform(new CenterCrop())
                                            .transform(new RoundedCorners(corner_size));
                                    Glide.with(getApplicationContext()).load(imageUrl)
                                            .apply(options)
                                            .into(productImage);


                                    btnPay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent PaymentLinkIntent = new Intent(ProductActivity.this, PayPalActivity.class);
                                            PaymentLinkIntent.putExtra("price",p.getPrice());
                                            PaymentLinkIntent.putExtra("pname",p.getName());
                                            PaymentLinkIntent.putExtra("pdetails",p.getDetails());
                                            PaymentLinkIntent.putExtra("pid",p.getPid());
                                            ProductActivity.this.startActivity(PaymentLinkIntent);
                                        }
                                    });

                                }
                            } else {
                                Log.d("tag", "Error getting documents: ", task.getException());
                            }
                        }
                    }
                });
    }



}