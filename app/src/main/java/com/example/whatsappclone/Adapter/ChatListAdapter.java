package com.example.whatsappclone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.ChatActivity;
import com.example.whatsappclone.Models.MessageModel;
import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private ArrayList<Users> userList = new ArrayList<>();
    private Context context;


    public static final String USER_ID = "userID";
    public static final String PROFILE_PIC = "profilePic";
    public static final String USER_NAME = "userName";

    public ChatListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Users user = userList.get(position);

        holder.userName.setText(user.getUserName());

        if (user.getProfilePic() == null) {
            holder.image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context)
                    .load(user.getProfilePic())
                    .into(holder.image);
        }

        if (user.getAvailable().equals("online")) {
            holder.onlineStatus.setVisibility(View.VISIBLE);
        } else {
            holder.onlineStatus.setVisibility(View.GONE);
        }

        ArrayList<MessageModel> lastMessage = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("ChatsConnection").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel model = dataSnapshot.getValue(MessageModel.class);
                    if (model.getReceiverID().equals(user.getUserID()) && model.getSenderID().equals(FirebaseAuth.getInstance().getUid())
                    || model.getSenderID().equals(user.getUserID()) && model.getReceiverID().equals(FirebaseAuth.getInstance().getUid())) {
                        lastMessage.add(model);
                    }
                }

                MessageModel model = lastMessage.get(lastMessage.size() - 1);
                String message = model.getMessage();
                if (!model.isSeen() && !model.getSenderID().equals(FirebaseAuth.getInstance().getUid())) {
                    holder.lastMessage.setText(message);
                    holder.lastMessage.setTextColor(Color.BLACK);
                    holder.lastMessage.setTypeface(Typeface.DEFAULT_BOLD);

                    Date date = new Date(model.getTimeStamp());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
                    String strDate = simpleDateFormat.format(date);
                    holder.timeStamp.setText(strDate);
                } else {
                    holder.lastMessage.setText(message);

                    holder.lastMessage.setTypeface(null);
                    Date date = new Date(model.getTimeStamp());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
                    String strDate = simpleDateFormat.format(date);
                    holder.timeStamp.setText(strDate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(USER_ID, user.getUserID());
                intent.putExtra(PROFILE_PIC, user.getProfilePic());
                intent.putExtra(USER_NAME, user.getUserName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setUserList(ArrayList<Users> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image, onlineStatus;
        private TextView userName, lastMessage;
        private TextView timeStamp;
        private LinearLayout parent;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userNameList);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            onlineStatus = itemView.findViewById(R.id.onlineStatus);
            parent = itemView.findViewById(R.id.parent);
        }
    }

}
