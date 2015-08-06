package com.bigsisters.bigsisters;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;

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
public class UniversityActivity extends FragmentActivity {

    int currentFragment = 0;
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
        Log.d("stefania", "Firebase: " + fbUrl);
        university.setId(Integer.parseInt(id));
        // TODO: make sure university exists
        uniRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                university.setName((String) dataSnapshot.child("univName").getValue());
                university.setLocation((String) dataSnapshot.child("location").getValue());
                university.setWebsiteUrl((String) dataSnapshot.child("website").getValue());
                university.setPhotoUrl((String) dataSnapshot.child("univPhotoUrl").getValue());
                university.setInfo((String) dataSnapshot.child("info").getValue());

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

        // Setting up the first fragment
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) { return;}
            Bundle args = new Bundle();
            Log.d("stefania", "argument id is " +university.id);
            args.putInt("id", university.id);
            UnivInfoFragment startFragment = new UnivInfoFragment();
            startFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, startFragment).commit();
        }

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



        // Setting up the fragment switching buttons
        Button btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {switchTabs(0);}
        });
        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {switchTabs(1);}
        });
        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {switchTabs(2);}
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

    private void switchTabs(int pos) {
        Log.d("stefania", "running fragment switcher " + pos);
        // check what fragment is currently active
        if (pos == currentFragment) return;
        // if it is different from the current one, switch
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(pos) {
            case 0:
                UnivInfoFragment iFragment = new UnivInfoFragment();
                transaction.replace(R.id.fragment_container, iFragment);
                currentFragment = 0;
                break;
            case 1:
                RatingFragment rFragment = new RatingFragment();
                transaction.replace(R.id.fragment_container, rFragment);
                currentFragment = 1;
                break;
            default: return;

        }
        transaction.addToBackStack(null);
        transaction.commit();
        Log.d("stefania", "fragments have been switched");


    }

}
