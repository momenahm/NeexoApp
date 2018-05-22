package com.example.me.neexoapp;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettings extends AppCompatActivity {
   private CircleImageView   mimgview;
    private TextView mtxtname;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();
        String userid = mAuth.getCurrentUser().getUid();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        mimgview = (CircleImageView)findViewById(R.id.settingImg);
        mtxtname = (TextView)findViewById(R.id.settingDisplayName);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imgstr = dataSnapshot.child("userimage").getValue().toString();
                String username= dataSnapshot.child("name").getValue().toString();
                Glide.with(getApplicationContext()).load(imgstr).dontAnimate().into(mimgview);
                mtxtname.setText(username);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void ToMain(View view) {
    }

    public void ToChat(View view) {
        Intent chatIntent = new Intent(AccountSettings.this,ChatActivity.class);
        chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(chatIntent);
    }
}
