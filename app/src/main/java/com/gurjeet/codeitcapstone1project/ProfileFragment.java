package com.gurjeet.codeitcapstone1project;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

  /*  private void getuser() {
    //TODO: need to use DataSnapshot code with onDataChange and fetch user using adapter and userRegister model
    }
   */
}
