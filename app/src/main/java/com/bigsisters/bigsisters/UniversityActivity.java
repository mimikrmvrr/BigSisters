package com.bigsisters.bigsisters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/*
* Displays info on university
* Started by an intent that includes the univeristy ID
* */
public class UniversityActivity extends ActionBarActivity {

    University university = new University();
    static final String EXTRA_ID = "com.bigsister.EXTRA_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_university);

        // Obtaining the university data
        Intent i = getIntent();
        String id = i.getStringExtra(UniversityActivity.EXTRA_ID);
        university.setId(Integer.parseInt(id));
        String fbUrl = "https://blazing-torch-4222.firebaseio.com/Universities/" + id;
        Firebase uniRoot = new Firebase(fbUrl);
        Log.d("stefania", fbUrl);
        // TODO: make sure university exists
        uniRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                university.setName((String) dataSnapshot.child("univName").getValue());
                university.setLocation((String) dataSnapshot.child("location").getValue());
                university.setWebsiteUrl((String) dataSnapshot.child("website").getValue());
                university.setPhotoUrl((String) dataSnapshot.child("univPhotoUrl").getValue());
                Log.d("stefania", "University: " + university.toString());

                // Showing the data
                TextView tvName = (TextView) findViewById(R.id.uniName);
                tvName.setText(university.getName());
                TextView tvLocation = (TextView) findViewById(R.id.uniLocation);
                tvLocation.setText(university.getName());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // TODO: specific error handling
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });



        // Opening the website
        Button webButton = (Button) findViewById(R.id.uniUrl);
        webButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if null, do nothing
                if (university.getWebsiteUrl() == null) return;
                // if not, send intent
                Uri webpage = Uri.parse(university.getWebsiteUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_university, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
