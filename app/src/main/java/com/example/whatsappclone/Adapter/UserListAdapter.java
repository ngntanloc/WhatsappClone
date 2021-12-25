package com.example.whatsappclone.Adapter;

import static com.example.whatsappclone.Adapter.ChatListAdapter.PROFILE_PIC;
import static com.example.whatsappclone.Adapter.ChatListAdapter.USER_ID;
import static com.example.whatsappclone.Adapter.ChatListAdapter.USER_NAME;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.ChatActivity;
import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Users> users = new ArrayList<>();

    public UserListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Users myUser = users.get(position);
        holder.userName.setText(myUser.getUserName());

        if (myUser.getStatus() == null) {
            holder.status.setText("Today is beautiful");
        } else {
            holder.status.setText(myUser.getStatus());
        }

        if (myUser.getAvailable().equals("online")) {
            holder.onlineStatus.setVisibility(View.VISIBLE);
        } else {
            holder.onlineStatus.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(myUser.getProfilePic())
                .into(holder.imgProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(USER_ID, myUser.getUserID());
                intent.putExtra(USER_NAME, myUser.getUserName());
                intent.putExtra(PROFILE_PIC, myUser.getProfilePic());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(ArrayList<Users> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imgProfile;
        private TextView userName, status;
        private CircleImageView onlineStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.profile_image);
            onlineStatus = itemView.findViewById(R.id.onlineStatus);
            userName = itemView.findViewById(R.id.userName);
            status = itemView.findViewById(R.id.status);

        }
    }
}
