package com.hassanmashraful.myapplication.activity;

/**
 * Created by Hassan M.Ashraful on 4/12/2017.
 */

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.hassanmashraful.myapplication.R;
import com.hassanmashraful.myapplication.adapter.SMSAdapter;
import com.hassanmashraful.myapplication.content.UserInfo;

import java.util.ArrayList;

public class SMSDetails extends AppCompatActivity {

    private SMSAdapter mAdapter;
    private String smsLayout;
    private TextView titleSMS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sms_details);
        titleSMS = (TextView) findViewById(R.id.titleSMS);

        Intent intent = getIntent();
        smsLayout = intent.getStringExtra("smslayout");

        titleSMS.setText(smsLayout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecyclerViews();
    }

    private void initRecyclerViews(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        ArrayList<UserInfo> userInfos = prepareData();
        mAdapter = new SMSAdapter(getApplicationContext(), userInfos);
        recyclerView.setAdapter(mAdapter);

    }

    private ArrayList<UserInfo> prepareData(){

        ArrayList<UserInfo> userInfos = new ArrayList<>();


        if (smsLayout.equals("Delivered")){
            userInfos.add(new UserInfo("0152120758", "adjhdjasjdgfk"));
            userInfos.add(new UserInfo("01887695911", "hello h cool"));
            userInfos.add(new UserInfo("01934628221", "kill the guy"));
        }

        if (smsLayout.equals("Pending")){
            userInfos.add(new UserInfo("017333222", "qqqqqqqqqqq"));
            userInfos.add(new UserInfo("017899222", "aaaaaaaaaaa"));
            userInfos.add(new UserInfo("017445221", "hhhhhhhhhhhh"));
        }


        // public static final String INBOX = "content://sms/inbox";
        // public static final String SENT = "content://sms/sent";
        // public static final String DRAFT = "content://sms/draft";

        if (smsLayout.equals("Inbox")){
            Uri uri = Uri.parse("content://sms/inbox");
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                while (cursor.moveToNext()){
                    userInfos.add(new UserInfo(cursor.getString(cursor.getColumnIndexOrThrow("address")), cursor.getString(cursor.getColumnIndexOrThrow("body"))));
                }
                cursor.close();
            }

        }


        return userInfos;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SMSDetails.this, MainMenu.class);
        startActivity(intent);
        Log.v("abck", "main menu");
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mAdapter != null) mAdapter.getFilter().filter(newText);
                return true;
            }
        });

    }
}
