package com.gurjeet.codeitcapstone1project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gurjeet.codeitcapstone1project.adapter.CallAdapter;
import com.gurjeet.codeitcapstone1project.model.UserRegister;


import java.util.ArrayList;


public class CallFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;
    CallAdapter adapter;
    RecyclerView recyclercall;
    private ArrayList<UserRegister> UserList;
    final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("Register");
    //String currentUserId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //currentUserId =FirebaseAuth.getInstance().getCurrentUser().getUid();
        View view = inflater.inflate(R.layout.fragment_call,container,false);
        recyclercall = view.findViewById(R.id.callerList);
        UserList = new ArrayList<>();
        recyclercall.setLayoutManager(new LinearLayoutManager(view.getContext()));
        firebaseAuth  = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        adapter=new CallAdapter(getActivity(),UserList);
        getuser();
        return view;
    }

    private void getuser() {
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot npsnapshot : snapshot.getChildren()) {
                        UserRegister l = npsnapshot.getValue(UserRegister.class);
                        String name = l.getName();
                        if(!user.getUid().equals(l.getId())){
                            UserList.add(l);
                        }
                    }
                    adapter=new CallAdapter(getActivity(),UserList);
                    recyclercall.setAdapter(adapter);
                    recyclercall.getAdapter().notifyDataSetChanged();
                    recyclercall.scheduleLayoutAnimation();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}