package com.example.whatsappclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsappclone.Adapter.ChatListAdapter;
import com.example.whatsappclone.Adapter.UserListAdapter;
import com.example.whatsappclone.Models.MessageModel;
import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ChatsFragment extends Fragment {

    public ChatsFragment() {
        // Required empty public constructor
    }

    private FragmentChatsBinding binding;
    private ArrayList<Users> usersArrayList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private ChatListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentChatsBinding.inflate(inflater, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        adapter = new ChatListAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Get all user that not me
        firebaseDatabase.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Users user = dataSnapshot.getValue(Users.class);

                    assert user != null;
                    user.setUserID(dataSnapshot.getKey());

                    if (!user.getUserID().equals(firebaseUser.getUid())) {
                        usersArrayList.add(user);
                    }
                }
                scanChatList(usersArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    private void scanChatList(ArrayList<Users> usersArrayList) {
        // get All message to me now!!
        ArrayList<MessageModel> messageModels = new ArrayList<>();

        ArrayList<Users> finalUser = new ArrayList<>();

        firebaseDatabase.getReference().child("ChatsConnection").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel model = dataSnapshot.getValue(MessageModel.class);
                    if (model.getReceiverID().equals(FirebaseAuth.getInstance().getUid()) || model.getSenderID().equals(FirebaseAuth.getInstance().getUid())) {
                        messageModels.add(model);
                    }
                }

                Collections.sort(messageModels, new Comparator<MessageModel>() {
                    @Override
                    public int compare(MessageModel o1, MessageModel o2) {
                        return o2.getTimeStamp().compareTo(o1.getTimeStamp());
                    }
                });

                for (MessageModel m : messageModels) {
                    for (Users u : usersArrayList) {
                        if (u.getUserID().equals(m.getReceiverID()) && m.getSenderID().equals(firebaseUser.getUid())
                                || u.getUserID().equals(m.getSenderID()) && m.getReceiverID().equals(firebaseUser.getUid())) {

                            if (finalUser.size() == 0) {
                                finalUser.add(u);
                            } else {
                                boolean check = false;
                                for (int i = 0; i < finalUser.size(); i++) {
                                    if (finalUser.get(i).getUserID().equals(u.getUserID())) {
                                        check = true;
                                    }
                                }
                                if (!check) {
                                    finalUser.add(u);
                                }
                            }
                        }
                    }
                }
                adapter.setUserList(finalUser);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}