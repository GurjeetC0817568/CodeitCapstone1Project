package com.gurjeet.codeitcapstone1project.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;




public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PostModel> postList;

    public PostAdapter(Context context, ArrayList<PostModel> postList) {
        this.context = context;
        this.postList = postList;
    }


    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.post2item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        final PostModel L = postList.get(position);
        String imageUrl;
        //TODO: fields add here


    }



    @Override
    public int getItemCount() {
        return postList == null? 0:postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView PostImage;
        public TextView tvTime,tvCondition,tvname, Tphone,TPrice;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //TODO: add variables

        }

    }
}