package com.example.me.neexoapp;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * Created by m.elshaeir on 1/28/2018.
 */

public class Signup_Fragment extends Fragment {
    private ImageButton mprofilimg;
    private EditText mregname,mregemail,mregpass;
    private Button mbtnreg;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    StorageReference storageReference;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.signup_layout,container,false);

        mregname = (EditText)view.findViewById(R.id.editUserreg);
        mregemail = (EditText)view.findViewById(R.id.editEmailreg);
        mregpass  = (EditText)view.findViewById(R.id.editPassReg);
        mbtnreg   = (Button)view.findViewById(R.id.btnReg);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("UserImg");
        mbtnreg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

    }
    public void  Register() {
        final String username = mregname.getText().toString();
        final String email = mregemail.getText().toString();
        String password = mregpass.getText().toString();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        final String userID = mAuth.getCurrentUser().getUid();

                        DatabaseReference mdatabase = databaseReference.child(userID);
                        mdatabase.child("name").setValue(username);
                        mdatabase.child("email").setValue(email);

                        Toast.makeText(getContext(), "Register Successfully", Toast.LENGTH_LONG).show();
                        Intent ManIntent = new Intent(getContext(), UserCont.class);
                        startActivity(ManIntent);
                    } else {
                        Toast.makeText(getContext(), "Something wrong!!!!!", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            Toast.makeText(getContext(), "Please fill the fields", Toast.LENGTH_LONG).show();
        }
    }

}
