package com.example.me.neexoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ChatActivity extends AppCompatActivity {
    private ViewPager mviewpager;
    private SectionPagerAdapter msectionAdapter;
    private TabLayout mtablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setTitle("Chat");
        mviewpager = (ViewPager) findViewById(R.id.tab_pager);
        msectionAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mviewpager.setAdapter(msectionAdapter);
        mtablayout = (TabLayout) findViewById(R.id.main_tabs);
        mtablayout.setupWithViewPager(mviewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatmenu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.AllUsers)
        {
            Intent alluserintent = new Intent(ChatActivity.this,UsersActivity.class);
            startActivity(alluserintent);

        }
        return super.onOptionsItemSelected(item);


    }
}