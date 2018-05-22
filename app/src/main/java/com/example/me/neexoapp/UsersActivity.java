package com.example.me.neexoapp;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {


    RecyclerView muserlist;
    DatabaseReference mdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mdata = FirebaseDatabase.getInstance().getReference().child("Users");
        getSupportActionBar().setTitle("All Users");
        muserlist = (RecyclerView) findViewById(R.id.userrecycler);
        muserlist.setHasFixedSize(true);
        muserlist.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<DataModel,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataModel, UserViewHolder>(
                DataModel.class,
                R.layout.user_singlelayout,
                UserViewHolder.class,
                mdata

        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, DataModel model, int position) {
                viewHolder.SetName(model.getName());
                Glide.with(getApplicationContext()).load(model.getUserimage()).placeholder(R.mipmap.profil).dontAnimate().into(viewHolder.mcimgview);
              final String user_id = getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent profileInent = new Intent(UsersActivity.this,ProfileActivity.class);
                      profileInent.putExtra("user_id",user_id);
                      startActivity(profileInent);
                  }
              });


            }



        };
        muserlist.setAdapter(firebaseRecyclerAdapter);
    }
    public static  class UserViewHolder extends RecyclerView.ViewHolder
    {
      View mView;
        CircleImageView mcimgview;
        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mcimgview = (CircleImageView)mView.findViewById(R.id.userprofileimg);
        }

        public void SetName(String name) {
            TextView mtxtname = (TextView)mView.findViewById(R.id.textViewuserid);
            mtxtname.setText(name);
        }


    }
}
