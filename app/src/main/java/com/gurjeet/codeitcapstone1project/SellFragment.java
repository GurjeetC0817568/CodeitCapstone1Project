package com.gurjeet.codeitcapstone1project;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gurjeet.codeitcapstone1project.model.PostModel;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;



import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class SellFragment extends Fragment {
    android.widget.Spinner spinner;

    SpinnerAdapter spinnerAdapter;
    private double latitude,longitude;
    ImageView imageView;
    Button next,save;
    RadioButton rbtnnew,rbtnused;
    private static final int PICK_IMAGE  = 1;
    private static final int PICK_MULTIPLE_IMAGE  = 2;
    LocationManager locationManager;
    TextInputLayout productname, location,detail,pric;
    RadioGroup radioGroup;
    Bitmap bitmaps;
    int Postion;
    String ne,us,price,details,nam,n,p;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference = db.collection("data");
    private Uri imageUri;

    FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //   ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Sell");
        View view = connectViews(inflater, container);
        final RadioGroup radio = (RadioGroup) getActivity().findViewById(R.id.radioG);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: // first button
                        ne = rbtnnew.getText().toString();
                        Toast.makeText(getActivity(), ""+ne,Toast.LENGTH_LONG).show();

                        break;
                    case 1: // secondbutton
                        us = rbtnused.getText().toString();
                        Toast.makeText(getActivity(), ""+us , Toast.LENGTH_LONG).show();

                        break;
                }
            }
        });

        return view;

    }

    private View connectViews(LayoutInflater inflater, ViewGroup container) {
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth =FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_sell, container, false);
        // spinner = view.findViewById(R.id.spinner);
     //   location = view.findViewById(R.id.location);
        imageView = view.findViewById(R.id.PimageView);
     //   next = view.findViewById(R.id.btnnext);
        save = view.findViewById(R.id.btnsave);

        productname = view.findViewById(R.id.productname);
        rbtnnew = view.findViewById(R.id.rbtnnew);
        rbtnused = view.findViewById(R.id.rbtnused);
        radioGroup = view.findViewById(R.id.radioG);

        detail = view.findViewById(R.id.details);
        pric  = view.findViewById(R.id.price);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
        authStateListener= new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user!=null){

                }else {

                }
            }
        };

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            //upload single image
            public void onClick(View v) {
                Intent Gallery = new Intent(Intent.ACTION_PICK);
                Gallery.setType("image/*");
                Gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(Gallery, "Select Image"), PICK_IMAGE);

            }

        });






        //Save part
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                details = detail.getEditText().getText().toString();
                price = pric.getEditText().getText().toString();
                nam = productname.getEditText().getText().toString();
                final String cm = ne;
                final String um =us;
                if (!TextUtils.isEmpty(nam)&&imageUri!=null){
                    final StorageReference filepath = storageReference
                            .child("Item_Images")
                            .child("myimage"+ Timestamp.now().getSeconds());
                    filepath.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUri =  uri.toString();
                                            final PostModel mod = new PostModel();
                                            mod.setUserid(user.getUid());
                                            mod.setName(nam);
                                            if (rbtnnew.isChecked()){
                                                mod.setCondition(ne);
                                            }else{
                                                mod.setCondition(us);
                                            }
                                            mod.setDetails(details);
                                            mod.setNumber(user.getPhoneNumber());
                                            //   mod.setPrice(Integer.parseInt(price));
                                            try{
                                                mod.setPrice(price);
                                            } catch(NumberFormatException ex){ // handle your exception
                                                Toast.makeText(getActivity(), "" + ex, Toast.LENGTH_SHORT).show();
                                            }
                                            mod.setImageURi(imageUri);
                                            mod.setTimeAdd(new Timestamp(new Date()));
                                            collectionReference.add(mod).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(getActivity(), "Successfully Added!", Toast.LENGTH_SHORT).show();
                                                  //  Log.d("DocReference gk:",documentReference.toString());
                                                    HomeFragment home = new HomeFragment();
                                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                    transaction.replace(R.id.fragmentMainPart,home);
                                                    transaction.commit();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                                                    Log.d("FailureMsg gk:",e.toString());

                                                }
                                            });
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("Error",e.toString());

                        }

                    });

                }
            }
        });

        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  final Uri uri = data.getData();
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            if (data!=null){
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        user =firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
//        Log.d("user",user.toString());

    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseAuth!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

}
