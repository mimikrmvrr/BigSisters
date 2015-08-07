package com.bigsisters.bigsisters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;


public class UserProfile extends ActionBarActivity {
    private User mUser;
    private String userId;
    private UniAdapter pastUniAdapter;
    private UniAdapter currentUniAdapter;
    private UniAdapter favouriteUniAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Firebase.setAndroidContext(this);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String userid = sharedPref.getString(getString(R.string.login_id),"");
        if(userid.equals("")){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            mUser=new User();
            userId=userid;
            String fbUrl = "https://blazing-torch-4222.firebaseio.com/Users/" + userId;
            Firebase userRoot = new Firebase(fbUrl);

            mUser.setId(userId);
            userRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mUser.setName((String) dataSnapshot.child("name").getValue());
                    mUser.setPhotoUrl((String) dataSnapshot.child("photoUrl").getValue());
                    TextView tv = (TextView) findViewById(R.id.name);
                    tv.setText(mUser.getName());
                    ImageView imageView = (ImageView) findViewById(R.id.userimg);
                    Picasso.with(UserProfile.this).load(mUser.getPhotoUrl()).into(imageView);
                    ArrayList<String> unis = new ArrayList<String>();
                    Log.d("Silvia", dataSnapshot.child("currents").getChildrenCount() + "");
                    DataSnapshot cds = dataSnapshot.child("currents");
                    for(DataSnapshot curr : cds.getChildren()){
                        String currentUni = curr.getValue(String.class);
                        unis.add(currentUni);
                    }
                    mUser.setCurrentUnis(unis);
                    unis =  new ArrayList<String>();
                    DataSnapshot fds = dataSnapshot.child("favorites");
                    for(DataSnapshot fave : cds.getChildren()){
                        String f = fave.getValue(String.class);
                        unis.add(f);
                    }
                    mUser.setFaveUnis(unis);
                    unis =  new ArrayList<String>();
                    DataSnapshot pds = dataSnapshot.child("favorites");
                    for(DataSnapshot pst : pds.getChildren()){
                        String p = pst.getValue(String.class);
                        unis.add(p);
                    }
                    mUser.setFaveUnis(unis);


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // TODO: specific error handling
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });


        }







    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String userid = sharedPref.getString(getString(R.string.login_id),"");
        if(userid.equals("")){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            userId=userid;


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public class UniAdapter extends BaseAdapter{

        public ArrayList<String> unis;
        public University university;
        @Override
        public int getCount() {
            return unis.size();
        }

        @Override
        public Object getItem(int position) {
            //TODO make this get objects
            String fbUrl = "https://blazing-torch-4222.firebaseio.com/Universities/" + unis.get(position);
            Firebase uniRoot = new Firebase(fbUrl);
            university = new University();
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


                }});
            return university;

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return null;
        }
    }
}
