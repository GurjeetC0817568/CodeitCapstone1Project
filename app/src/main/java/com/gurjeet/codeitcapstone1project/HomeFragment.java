package com.gurjeet.codeitcapstone1project;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.gurjeet.codeitcapstone1project.adapter.PostAdapter;
import com.gurjeet.codeitcapstone1project.model.PostModel;

import java.util.ArrayList;
import java.util.List;


import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    PostAdapter postAdapter;
    // private ArrayList<Products> PostProduct;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PostModel> postList;
    FragmentManager fragmentManager;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    //get data from users or data collection in firebase
    private CollectionReference collectionReference = db.collection("data");
    private Uri imageUri;
    private String ARG_SECTION_NUMBER;


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.logout,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //Not using this part now because hide the TitleBar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.LogOut:
                if (user!=null&&firebaseAuth!=null){
                    firebaseAuth.signOut();
                    SharedPreferences preferences = getActivity().getSharedPreferences("mypref",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    startActivity(new Intent(getActivity(),LoginActivity.class));

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrimaryNavigationFragmentChanged(boolean isPrimaryNavigationFragment) {
        super.onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      //  ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView = view.findViewById(R.id.postRecycler);
        postList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        firebaseAuth  = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();

        getData();
        setAdapter();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //   postAdapter.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        // postAdapter.stopListening();

    }


    private void getData(){
        //to get data from firebase collection to recycler view .
        db.collection("data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (task.isSuccessful()) {
                                PostModel types = document.toObject(PostModel.class);
                                // Add all products except which are sold
                                if(!types.getPaymentdone().equals("done")) { // checking product sold or not via paypal
                                    postList.add(types);
                                }
                                setAdapter();
                                Log.d("tag", document.getId() + " => " + document.getData());
                            } else {
                                Log.d("tag", "Error getting documents: ", task.getException());
                            }
                        }
                    }
                });

    }



    private void setAdapter(){
        PostAdapter myadapter = new PostAdapter(getActivity(),postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myadapter);
        myadapter.notifyDataSetChanged();
    }
}