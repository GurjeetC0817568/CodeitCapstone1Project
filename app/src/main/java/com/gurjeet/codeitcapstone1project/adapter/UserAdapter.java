package com.gurjeet.codeitcapstone1project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gurjeet.codeitcapstone1project.R;
import com.gurjeet.codeitcapstone1project.model.UserRegister;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<UserRegister> UserList;
    private List<UserRegister> names;
    private Context context;


    public UserAdapter(ArrayList<UserRegister> userList, Context context) {
        UserList = userList;
        this.context = context;
    }
    public UserAdapter() {}


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user,parent,false);
        return new ViewHolder(view);

    }




    public class ViewHolder extends RecyclerView.ViewHolder {
       //TODO: view holder data to be add here
    }
}
