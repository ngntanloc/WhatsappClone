package com.example.whatsappclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsappclone.Adapter.UserListAdapter;
import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.FragmentUsersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UsersFragment extends Fragment {

    private FragmentUsersBinding binding;

    private FirebaseDatabase firebaseDatabase;
    private ArrayList<Users> users;

    private UserListAdapter userListAdapter;
    private RecyclerView recyclerView;
    private FirebaseUser firebaseUser;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentUsersBinding.inflate(inflater, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userListAdapter = new UserListAdapter(getContext());

        binding.recyclerView.setAdapter(userListAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        users = new ArrayList<>();
        firebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Users user = dataSnapshot.getValue(Users.class);

                    assert user != null;
                    user.setUserID(dataSnapshot.getKey());

                    if (!user.getUserID().equals(firebaseUser.getUid())) {
                        users.add(user);
                    }

                    Collections.sort(users, new Comparator<Users>() {
                        @Override
                        public int compare(Users o1, Users o2) {
                            return o1.getUserName().compareTo(o2.getUserName());
                        }
                    });

                    userListAdapter.setUsers(users);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

}