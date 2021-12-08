package com.gurjeet.codeitcapstone1project.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gurjeet.codeitcapstone1project.R;
import com.gurjeet.codeitcapstone1project.model.UserRegister;

import java.util.ArrayList;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.CallAdapterViewholder> {

    Context context;
    ArrayList<UserRegister> list;
    public CallAdapter(Context context, ArrayList<UserRegister> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CallAdapterViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowcalluser,parent,false);
        return new CallAdapterViewholder(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull final CallAdapterViewholder holder, @SuppressLint("RecyclerView") int position) {
        final UserRegister Mn = list.get(position);
        holder.textname.setText(Mn.getName());
        final String ns = Mn.getId();
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegister ur = list.get(position);
                String  username = ur.getName();
                String phone = ur.getPhone();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel: "+phone));
                context.startActivity(callIntent);
                //Intent i=new Intent(Intent.ACTION_DIAL,Uri.parse(phno));
                //context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CallAdapterViewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textname;
        Button call;
        public CallAdapterViewholder(@NonNull View itemView) {
            super(itemView);
            call = itemView.findViewById(R.id.cbutton);
            imageView =itemView.findViewById(R.id.cimageuser);
            textname=itemView.findViewById(R.id.ctextuser);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

}
