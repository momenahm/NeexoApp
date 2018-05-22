package com.example.me.neexoapp;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by m.elshaeir on 1/29/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {
    Context context ;
    List<DataModel> list ;
    AlertDialog.Builder builder;



    public RecyclerAdapter(Context ctx, List<DataModel> list) {
        this.context = ctx;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_layout,parent,false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }
    @Override
    public int getItemCount() {

        return list.size();
    }
    private  boolean mPorcessLike = false;
    private DatabaseReference databaseReference;

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String Uid =mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Likes");
        final DatabaseReference mdata = FirebaseDatabase.getInstance().getReference().child("Comments");
        databaseReference.keepSynced(true);
        final DataModel dataModel = list.get(position);
        Picasso.with(context).load(dataModel.getUserimage()).placeholder(R.mipmap.profil).into(holder.mprofileimg);
        Glide.with(context).load(dataModel.getImage()).dontAnimate().into(holder.mMainimg);
        holder.musername.setText(dataModel.getName());
        final String postkey = dataModel.getPostkey();
        holder.mHead.setText(dataModel.getHead());
        holder.mPost.setText(dataModel.getPost());

        holder.mlikebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               mPorcessLike = true;

                  databaseReference.addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(DataSnapshot dataSnapshot) {
                          if (mPorcessLike) {
                              if (dataSnapshot.child(postkey).hasChild(mAuth.getCurrentUser().getUid())) {
                                  databaseReference.child(postkey).child(mAuth.getCurrentUser().getUid()).removeValue();
                                  holder.mlikebtn.setImageResource(R.mipmap.likeeee);
                                  mPorcessLike = false;
                              } else
                              {
                                  holder.mlikebtn.setImageResource(R.mipmap.redlikeeee);
                                  databaseReference.child(postkey).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
                                  mPorcessLike = false;
                              }

                          }
                      }

                      @Override
                      public void onCancelled(DatabaseError databaseError) {

                      }
                  });
                }


        });
        holder.mbtncomment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Add Comment ........");

                //alert.setMessage("Message");

             // Set an EditText view to get user input
                final EditText input = new EditText(context);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String value = input.getText().toString();
                        // Do something with value;

                        final DatabaseReference mdatacomment = FirebaseDatabase.getInstance().getReference().child("Comments").child(postkey).child(Uid).push();
                        DatabaseReference muserdata = FirebaseDatabase.getInstance().getReference().child("Users").child(Uid);
                        muserdata.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final String username = dataSnapshot.child("name").getValue().toString();
                                final String userimage = dataSnapshot.child("userimage").getValue().toString();
                                mdatacomment.child("comment").setValue(value);
                                mdatacomment.child("Username").setValue(username);
                                mdatacomment.child("Userimage").setValue(userimage);
                                Toast.makeText(context,"Comment Added...",Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();

            }
        });

        DatabaseReference mdatacomment =mdata.child(postkey).child(Uid);
        mdatacomment.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String data = dataSnapshot.getValue().toString();
               // Toast.makeText(context,data.toString(),Toast.LENGTH_LONG).show();
                List<DataComment> commentlist = new ArrayList<DataComment>();
                DataComment dataComment = dataSnapshot.getValue(DataComment.class);
                commentlist.add(dataComment);
                CommentAdapter commentAdapter = new CommentAdapter(context,commentlist);
                holder.mlistcomment.setAdapter(commentAdapter);

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



    public class  MyHolder extends RecyclerView.ViewHolder
    {
         ImageView mMainimg;
        CircleImageView mprofileimg;
        TextView mHead,mPost,musername;
        Button mbtncomment;
        ImageButton mlikebtn;
        ListView mlistcomment;

        public MyHolder(View itemView) {
            super(itemView);
            mMainimg    = (ImageView)itemView.findViewById(R.id.mainimg);
            mHead       = (TextView) itemView.findViewById(R.id.txthead);
            mPost       = (TextView)itemView.findViewById(R.id.txtpost);
            mlikebtn    = (ImageButton)itemView.findViewById(R.id.likebtn);
            mbtncomment = (Button) itemView.findViewById(R.id.comment);
            mprofileimg = (CircleImageView) itemView.findViewById(R.id.profileimg);
            musername = (TextView) itemView.findViewById(R.id.mainusername);
            mlistcomment = (ListView)itemView.findViewById(R.id.listcomment);

          //  recyclerView = (RecyclerView)itemView.findViewById(R.id.recclercomment);
        }
    }



}



