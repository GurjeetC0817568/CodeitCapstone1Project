package com.gurjeet.codeitcapstone1project.adapter;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.List;

import com.gurjeet.codeitcapstone1project.Message;
import com.gurjeet.codeitcapstone1project.R;
import com.gurjeet.codeitcapstone1project.model.UserRegister;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<UserRegister> UserList;
    private List<UserRegister> names;
    private Context context;

    public UserAdapter() {
    }

    public UserAdapter(ArrayList<UserRegister> userList, Context context) {
        UserList = userList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user,parent,false);
        return new ViewHolder(vi);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        UserRegister MO = UserList.get(position);

        //userimage...........
        //User name........
        holder.textView.setText(MO.getName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserRegister MO = UserList.get(position);

                final String ns = MO.getId();
                String  username = MO.getName();
                String  phone = MO.getPhone();
                //Toast.makeText(context, ""+ns, Toast.LENGTH_SHORT).show();
               // Toast.makeText(context, "Username = "+username, Toast.LENGTH_LONG).show();

                Intent intennt = new Intent(v.getContext(), Message.class);
                intennt.putExtra("id",ns);
                intennt.putExtra("title",username);
                intennt.putExtra("phone",phone);
                context.startActivity(intennt);

            }
        });

    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }
    /*@Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    UserList = names;
                } else {
                    List<Name> filteredList = new ArrayList<>();
                    for (Name name : nameList) {
                        if (name.getName().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(name);
                        }
                        filteredNameList = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredNameList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredNameList = (List<Name>) results.values;
                notifyDataSetChanged();
            }
        };
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageuser);
            textView = itemView.findViewById(R.id.textuser);
            cardView =itemView.findViewById(R.id.cardview);

        }
    }
}
