package com.example.me.neexoapp;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.theartofdev.edmodo.cropper.CropImage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class UserCont extends AppCompatActivity {
    ImageButton mimgprofile;
    EditText meditphone;
    Button mbtnstart;
    DatabaseReference mdatabase;
    StorageReference mstorage;
    FirebaseAuth mAuth;
    Uri mimguri = null;
    private static final int GALLERY_REQUEST=1;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cont);
        mAuth = FirebaseAuth.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mstorage = FirebaseStorage.getInstance().getReference();
        dialog = new ProgressDialog(UserCont.this);
        mimgprofile = (ImageButton)findViewById(R.id.profileimgg);
        meditphone  = (EditText)findViewById(R.id.phonenumber);
        mbtnstart   = (Button)findViewById(R.id.btnstart);
        mimgprofile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GallIntent = new Intent(Intent.ACTION_GET_CONTENT);
                GallIntent.setType("image/*");
                startActivityForResult(GallIntent,GALLERY_REQUEST);
            }
        });
        mbtnstart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 startMain();


            }
        });



    }

    private void startMain()
    {
        dialog.setMessage("Waiting......");
        dialog.show();
        final String phone = meditphone.getText().toString();
        final String userId = mAuth.getCurrentUser().getUid();
        StorageReference filepath = mstorage.child("UserImage").child(userId).child(mimguri.getLastPathSegment());
        filepath.putFile(mimguri).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
            @Override
            public void onSuccess(TaskSnapshot taskSnapshot) {

                Uri userimg = taskSnapshot.getDownloadUrl();
                DatabaseReference datauser = mdatabase.child(userId);
                datauser.child("userimage").setValue(userimg.toString());
                datauser.child("phonenumber").setValue(phone);
                dialog.dismiss();
                Toast.makeText(UserCont.this,"You Are Welcome.....",Toast.LENGTH_LONG).show();
                Intent mainPageIntent = new Intent(UserCont.this,MainPage.class);
                startActivity(mainPageIntent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
        {
            mimguri = data.getData();
            CropImage.activity(mimguri)
                    .setAspectRatio(1,1)
                    .start(this);
            mimgprofile.setImageURI(mimguri);

        }
    }
}
