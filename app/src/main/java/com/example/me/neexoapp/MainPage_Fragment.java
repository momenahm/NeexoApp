package com.example.me.neexoapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by m.elshaeir on 1/28/2018.
 */

public class MainPage_Fragment extends Fragment {
    private Button mbtnAddpost;
     RecyclerView mrecyclerview;
     RecyclerAdapter recyclerAdapter;
     LayoutManager layoutManager;
     DatabaseReference mdatabase;
    FirebaseAuth mAuth;
    List<DataModel> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerpage_layout,container,false);
        mbtnAddpost = (Button)view.findViewById(R.id.btnAddPost);
        mrecyclerview = (RecyclerView)view.findViewById(R.id.recyclerdata);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
           mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mbtnAddpost.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              getActivity()
                      .getSupportFragmentManager()
                      .beginTransaction()
                      .replace(R.id.MainPageContainer,new AddPost_Fragment())
                      .commit();
            }
        });
        layoutManager = new LinearLayoutManager(getContext());
        mrecyclerview.setLayoutManager(layoutManager);
        mrecyclerview.setHasFixedSize(true);
        getData();


    }

    private void getData() {
        mdatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataModel dataModel = dataSnapshot.getValue(DataModel.class);
                String key = dataSnapshot.getKey().toString();
                dataModel.setPostkey(key);
                list.add(dataModel);
                recyclerAdapter = new RecyclerAdapter(getContext(), list);
                mrecyclerview.setAdapter(recyclerAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}


