package com.bigsisters.bigsisters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/*
* Displays info on university
* Started by an intent that includes the univeristy ID
* */
public class UniversityActivity extends FragmentActivity {

    int currentFragment = 0;
    University university = new University();
    boolean isStudent = false;
    String userId = null;

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
                tvLocation.setText(university.getLocation());

                setupImage(university.getPhotoUrl());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // TODO: specific error handling
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        // TODO: read from shared preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPref.getString("userId", null);

        handleFaveButton(userId, id);

        // Setting up the first fragment
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState == null) {
            Bundle args = new Bundle();
            Log.d("stefania", "argument id is " + university.id);
            args.putInt("id", university.id);
            UnivInfoFragment startFragment = new UnivInfoFragment();
            startFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, startFragment).commit();
        }
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

        checkAttendingUniversities(userId, id);

        setupRateBtn(userId, id);
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

        Button rate = (Button) findViewById(R.id.giveRatingBtn);
        rate.setVisibility(View.VISIBLE);
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
            case 3:
                EditRatingFragment eFragment = new EditRatingFragment();
                transaction.replace(R.id.fragment_container, eFragment);
                currentFragment = 3;
                break;
            default: return;

        }
        transaction.addToBackStack(null);
        transaction.commit();
        Log.d("stefania", "fragments have been switched");
    }

    public void setupImage(String url) {
        Log.d("stefania", "image url " + url);
        String url2 = "http://www.educationabroadnetwork.org/site/galleries/8_458.jpg";
        ImageView imageView = (ImageView) findViewById(R.id.uniPic);
        //Picasso.with(this).load(url).into(imageView);
        Picasso.with(this).load(url).placeholder(R.id.uniPic).into(imageView);
    }

    public void attendedThisUni(final int studentID, final int uniID) {
        Firebase studentRoot = new Firebase("https://blazing-torch-4222.firebaseio.com/Users/" + studentID);
        studentRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("current").hasChild(Integer.toString(uniID)) ||
                        dataSnapshot.child("past").hasChild(Integer.toString(uniID))) {
                    isStudent = true;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void handleFaveButton(final String userId, final String uniId) {
        Log.d("stefania", "user uni " + userId + " " + uniId);
        final String fbUrl = "https://blazing-torch-4222.firebaseio.com/Users/" + userId + "/favorites";
        Log.d("stefania", "Firebase: " + fbUrl);
        Firebase userFaves = new Firebase(fbUrl);
        userFaves.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Button faveButton = (Button) findViewById(R.id.faveButton);
                Log.d("stefania", "id " + uniId);
                //Button rateButton = (Button) findViewById(R.id.giveRatingBtn);
                if (dataSnapshot.hasChild(uniId)) {
                    // already faved

                    faveButton.setText("Faved");
                    faveButton.setBackgroundColor(android.graphics.Color.RED);
                    // setup rate button
                    //rateButton.setVisibility(View.VISIBLE);
                } else {
                    // activate fave button
                    faveButton.setText("Fave");
                    faveButton.setBackgroundColor(android.graphics.Color.GREEN);
                    // deactivate rate button
                    //rateButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // create button handler - button handler calls handleFaveButton?
        Button faveButton = (Button) findViewById(R.id.faveButton);
        faveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Firebase favRoot = new Firebase(fbUrl);
                favRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(uniId)) {
                            // remove
                            Log.d("stefania", "remove from database");
                            favRoot.child(uniId).setValue(null);

                        } else {
                            // add
                            Log.d("stefania", "write to database");
                            favRoot.child(uniId).setValue(uniId);
                        }
                        handleFaveButton(userId, uniId);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });

    }

    public void checkAttendingUniversities(final String userId, final String uniId) {
        String fbUrl = "https://blazing-torch-4222.firebaseio.com/Users/" + userId +"/universities";
        Firebase attendRoot = new Firebase(fbUrl);
        attendRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Button rateButton = (Button) findViewById(R.id.giveRatingBtn);
                if (dataSnapshot.hasChild(uniId)) {
                    // button is here
                    rateButton.setVisibility(View.VISIBLE);
                } else {
                    // button disappears
                    rateButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void setupRateBtn(final String userId, final String uniId) {
        final Button rateBtn = (Button) findViewById(R.id.giveRatingBtn);
        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rateBtn.setVisibility(View.INVISIBLE);
                Log.d("stefania", "Rate button");
                switchTabs(3);
            }
        });
    }


}
