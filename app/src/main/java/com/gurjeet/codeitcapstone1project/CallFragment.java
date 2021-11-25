package com.gurjeet.codeitcapstone1project;

import androidx.fragment.app.Fragment;

public class CallFragment extends Fragment {
 /*   FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;
    CallAdapter adapter;
    String curruntuserid;
    RecyclerView recyclercall;
    Call call;
    private ArrayList<ModelRegister> UserList;
    final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("Register");
    public Call() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Call");


        View view = inflater.inflate(R.layout.fragment_call,container,false);
        recyclercall = view.findViewById(R.id.callrecyler);
        curruntuserid =FirebaseAuth.getInstance().getCurrentUser().getUid();
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

                        ModelRegister l = npsnapshot.getValue(ModelRegister.class);
                        String name = l.getName();
                        //id = l.getId();
                        Log.d("nam", "onDataChange: "+name);
                        if(!user.getUid().equals(l.getId())){
                            UserList.add(l);
                        }
                    }


                    adapter=new CallAdapter(getActivity(),UserList);
                    recyclercall.setAdapter(adapter);
                    // LayoutAnimationController animationController =
                    //       AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_down_to_up);
                    // recyclercall.setLayoutAnimation(animationController);
                    recyclercall.getAdapter().notifyDataSetChanged();
                    recyclercall.scheduleLayoutAnimation();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
*/
}