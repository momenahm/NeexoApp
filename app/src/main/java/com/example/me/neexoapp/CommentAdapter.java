package com.example.me.neexoapp;

import com.squareup.picasso.Picasso;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by m.elshaeir on 2/15/2018.
 */

public class CommentAdapter extends BaseAdapter {
List<DataComment> data_list;
    Context context;

    public CommentAdapter( Context context,List<DataComment> data_list) {
        this.data_list = data_list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.data_list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView  = layoutInflater.inflate(R.layout.row_comment,null);
        CircleImageView mtxtimg = (CircleImageView) convertView.findViewById(R.id.commentimg);
        TextView mtxtusername = (TextView)convertView.findViewById(R.id.comm_username);
        TextView mtxtcomment  =(TextView)convertView.findViewById(R.id.txtcomment);
        Picasso.with(context).load(data_list.get(position).getUserimage()).into(mtxtimg);
        mtxtusername.setText(data_list.get(position).getUsername());
        mtxtcomment.setText(data_list.get(position).getComment());


        return convertView;
    }
}
