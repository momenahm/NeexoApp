package com.example.me.neexoapp;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by m.elshaeir on 1/28/2018.
 */


public class SignIn_Fragment extends Fragment {
    private EditText mloginemail,mloginpass;
    private Button mbtnlog;
    TextView mtxtreg;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.signin_layout,container,false);
        mloginemail = (EditText) view.findViewById(R.id.editEmailLogin);
        mloginpass  = (EditText) view.findViewById(R.id.editPassLogin);
        mtxtreg     = (TextView) view.findViewById(R.id.Rrgister);
        mbtnlog     = (Button)   view.findViewById(R.id.btnLogin);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mtxtreg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.FragmentContainer,new Signup_Fragment())
                        .commit();
            }
        });
        mbtnlog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });

    }

    private void SignIn() {
        String email    = mloginemail.getText().toString();
        String password = mloginpass.getText().toString();
        if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password))
        {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getContext(), "Login Successfully", Toast.LENGTH_LONG).show();
                        Intent mainIntent = new Intent(getContext(),MainPage.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Something Wrong!!!!!", Toast.LENGTH_LONG).show();
                    }

                }
            });

        }
        else
        {
            Toast.makeText(getContext(),"Please Fill The Fields",Toast.LENGTH_LONG).show();
        }

    }
}
