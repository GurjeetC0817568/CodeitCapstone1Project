package com.gurjeet.codeitcapstone1project;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
import com.gurjeet.codeitcapstone1project.adapter.UserAdapter;
import com.gurjeet.codeitcapstone1project.model.UserRegister;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;
    Button btnLogout;
    TextView welcome,detail;
    //UserAdapter adapter;  //TODO: add useradapter class
    final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("Register");



    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth  = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        getuser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

       /*View view = inflater.inflate(R.layout.fragment_profile,container,false);
        recycleruser = view.findViewById(R.id.userRecycler);// may use recycler view to copy same code for listing
        UserList = new ArrayList<>();
        firebaseAuth  = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        adapter=new UserAdapter(UserList,getActivity());
        getuser();
        return view;*/
        btnLogout = view.findViewById(R.id.btnLogout);
        welcome = view.findViewById(R.id.welcome);
        detail = view.findViewById(R.id.detail);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logout();
            }
        });

        return  view;
    }


    private void logout(){
        if (user!=null&&firebaseAuth!=null){
        firebaseAuth.signOut();
        SharedPreferences preferences = getActivity().getSharedPreferences("mypref",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.apply();
        startActivity(new Intent(getActivity(),LoginActivity.class));
         }
    }

    private void getuser() {
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot npsnapshot : snapshot.getChildren()) {
                        UserRegister l = npsnapshot.getValue(UserRegister.class);
                        String name = l.getName();
                        Log.d("nam", "onDataChange: "+name);
                        if(user.getUid().equals(l.getId())){
                            welcome.setText("Welcome " + l.getName());
                            detail.setText(l.getEmail()+"\n"+l.getPhone());
                        }
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error"+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }





}
