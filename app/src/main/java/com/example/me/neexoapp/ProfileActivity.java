package com.example.me.neexoapp;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    ImageView mprofileactimg;
    TextView mprofileactusername,mprofileuseremail,mprofileacttotalfriend;
    Button mbtnsendrequest;
    DatabaseReference mDataUsers;
    DatabaseReference mDataRequest;
    FirebaseUser mCurrentUser;
    private String current_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        final String user_id = getIntent().getStringExtra("user_id");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDataUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mDataRequest = FirebaseDatabase.getInstance().getReference().child("FriendRequest");
        mprofileactimg = (ImageView)findViewById(R.id.profileactuserimg);
        mprofileactusername = (TextView)findViewById(R.id.profileactusername);
        mprofileuseremail = (TextView)findViewById(R.id.profileactuseremail);
        mprofileacttotalfriend = (TextView)findViewById(R.id.profilesendrequest);
        mbtnsendrequest = (Button)findViewById(R.id.profilesendrequest);
        current_state ="not_friend";
        mDataUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               String display_name = dataSnapshot.child("name").getValue().toString();
                String display_email = dataSnapshot.child("email").getValue().toString();
                String display_img = dataSnapshot.child("userimage").getValue().toString();
                mprofileactusername.setText(display_name);
                mprofileuseremail.setText(display_email);
                Glide.with(getApplicationContext()).load(display_img).placeholder(R.mipmap.profileimg).dontAnimate().into(mprofileactimg);
                //______________friend request_________________

                mDataRequest.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(user_id))
                        {
                            String request_typee = dataSnapshot.child(user_id).child("request_type").getValue().toString();
                            if (request_typee.equals("recieved"))
                            {
                                current_state = "request_recieved";
                                mbtnsendrequest.setText("Accept Friend Request");

                            }
                            else if (request_typee.equals("sent")){
                                current_state ="request_send";
                                mbtnsendrequest.setText("Cancel Friend Request");
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
     mbtnsendrequest.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {

             if(current_state.equals("not_friend"))
             {
                 mbtnsendrequest.setEnabled(false);
                mDataRequest.child(mCurrentUser.getUid()).child(user_id).child("reguest_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            mDataRequest.child(user_id).child(mCurrentUser.getUid()).child("request_type").setValue("recieved")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mbtnsendrequest.setEnabled(true);
                                            current_state = "reguest_send";
                                            mbtnsendrequest.setText("Cancel Friend Request");
                                            mbtnsendrequest.setBackgroundColor(Color.GRAY);

                                            Toast.makeText(getApplicationContext()," Sending Request...",Toast.LENGTH_LONG).show();
                                        }
                                    });


                        }else
                        {
                            Toast.makeText(getApplicationContext(),"Failed Sending Request...",Toast.LENGTH_LONG).show();
                        }
                    }
                });
             }
             if (current_state.equals("reguest_send"))
             {
                 mDataRequest.child(mCurrentUser.getUid()).child(user_id).removeValue()
                         .addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                               mDataRequest.child(user_id).child(mCurrentUser.getUid()).removeValue()
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {
                                               mbtnsendrequest.setEnabled(true);
                                               current_state.equals("not_friend");
                                               mbtnsendrequest.setText("Sending Friend Request");
                                              mbtnsendrequest.setBackgroundColor(Color.parseColor("#FF5722"));
                                           }
                                       });
                             }
                         });
             }
         }
     });

    }
}
