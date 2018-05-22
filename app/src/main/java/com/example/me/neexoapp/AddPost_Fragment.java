package com.example.me.neexoapp;

import static android.app.Activity.RESULT_OK;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class AddPost_Fragment extends Fragment{
    private ImageButton mimgpost,mprofilimg;
    private EditText medithead,meditpost;
    private Button   mbtnaddpost;
    Uri imguri = null;
    FirebaseAuth mAuth;
    private static final int GALLERY_REQUEST =1;
    private DatabaseReference mdatabase;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addpost_layout,container,false);
        mimgpost    = (ImageButton) view.findViewById(R.id.imgpost);
        medithead   = (EditText)   view.findViewById(R.id.editHead);
        meditpost   = (EditText)  view.findViewById(R.id.editpost);
        mbtnaddpost = (Button)    view.findViewById(R.id.btnaddpost);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuth= FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        progressDialog = new ProgressDialog(getContext());
        mimgpost.setOnClickListener(new OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                getImage.setType("image/*");
                startActivityForResult(getImage,GALLERY_REQUEST);
            }
        });
        mbtnaddpost.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPost();

            }
        });


    }

    public void AddPost()
    {
        final String headofpost = medithead.getText().toString();
        final String  post      = meditpost.getText().toString();
        final  String userId    = mAuth.getCurrentUser().getUid();
        if (!TextUtils.isEmpty(headofpost)&&!TextUtils.isEmpty(post)&& imguri !=null)
        {

            progressDialog.setMessage("Waiting to Upload.......");
            progressDialog.show();
            DatabaseReference mdatauser = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                       mdatauser.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {

                               final String username = dataSnapshot.child("name").getValue().toString();
                               final String userimage = dataSnapshot.child("userimage").getValue().toString();
                               Toast.makeText(getContext(),username,Toast.LENGTH_LONG).show();
                               Toast.makeText(getContext(),userimage,Toast.LENGTH_LONG).show();
                               StorageReference filepath= storageReference.child("postimage").child(userId).child(imguri.getLastPathSegment());
                               filepath.putFile(imguri).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                                @Override
                                public void onSuccess(TaskSnapshot taskSnapshot) {
                                    Uri downloaduri = taskSnapshot.getDownloadUrl();
                                    DatabaseReference mputpost = mdatabase.push();
                                    mputpost.child("Head").setValue(headofpost);
                                    mputpost.child("post").setValue(post);
                                    mputpost.child("image").setValue(downloaduri.toString());
                                    mputpost.child("userimage").setValue(userimage);
                                    mputpost.child("name").setValue(username);
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(),"Done!!!!",Toast.LENGTH_LONG).show();
                                    getActivity()
                                            .getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.MainPageContainer,new MainPage_Fragment())
                                            .commit();
                                }
                            });

                           }

                           @Override
                           public void onCancelled(DatabaseError databaseError) {
                               Toast.makeText(getContext(),"Error........",Toast.LENGTH_LONG).show();
                           }
                       });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
    {
        imguri = data.getData();
        mimgpost.setImageURI(imguri);

    }
    }
}
