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
import android.widget.ListView;
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
    private ArrayList<String> dunis;
    private ArrayList<String> funis;
    private ArrayList<String> cunis;
    private ArrayList<University> currentUniversities;
    private ArrayList<University> pastUniversities;
    private ArrayList<University> favouriteUniversities;

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
            dunis =  new ArrayList<String>();
            funis =  new ArrayList<String>();
            cunis = new ArrayList<String>();
            favouriteUniversities = new ArrayList<University>();
            currentUniversities = new ArrayList<University>();
            pastUniversities = new ArrayList<University>();

            userRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mUser.setName((String) dataSnapshot.child("name").getValue());
                    mUser.setPhotoUrl((String) dataSnapshot.child("photoUrl").getValue());
                    TextView tv = (TextView) findViewById(R.id.name);
                    tv.setText(mUser.getName());
                    ImageView imageView = (ImageView) findViewById(R.id.userimg);
                    Picasso.with(UserProfile.this).load(mUser.getPhotoUrl()).into(imageView);

                    Log.d("Silvia", dataSnapshot.child("currents").getChildrenCount() + "");
                    DataSnapshot cds = dataSnapshot.child("currents");
                    for(DataSnapshot curr : cds.getChildren()){
                        String currentUni = curr.getValue(String.class);
                        cunis.add(currentUni);
                    }


                    DataSnapshot fds = dataSnapshot.child("favorites");
                    for(DataSnapshot fave : cds.getChildren()){
                        String f = fave.getValue(String.class);
                        funis.add(f);
                    }


                    DataSnapshot pds = dataSnapshot.child("pasts");
                    for(DataSnapshot pst : pds.getChildren()){
                        String p = pst.getValue(String.class);
                        dunis.add(p);
                    }



                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // TODO: specific error handling
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });}

            for(String f: funis){
                String fbUrl = "https://blazing-torch-4222.firebaseio.com/Universities/" + f;
                Firebase uniRoot = new Firebase(fbUrl);
                //addig favourite universities
                final University university = new University();
                uniRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        university.setName((String) dataSnapshot.child("univName").getValue());
                        university.setLocation((String) dataSnapshot.child("location").getValue());
                        university.setWebsiteUrl((String) dataSnapshot.child("website").getValue());
                        university.setPhotoUrl((String) dataSnapshot.child("univPhotoUrl").getValue());
                        university.setInfo((String) dataSnapshot.child("info").getValue());
                    favouriteUniversities.add(university);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {


                    }});}

                for(String d: dunis){
                    String fbUrl = "https://blazing-torch-4222.firebaseio.com/Universities/" + d;
                    Firebase duniRoot = new Firebase(fbUrl);
                    //adding past universities
                    final University duniversity = new University();
                    duniRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            duniversity.setName((String) dataSnapshot.child("univName").getValue());
                            duniversity.setLocation((String) dataSnapshot.child("location").getValue());
                            duniversity.setWebsiteUrl((String) dataSnapshot.child("website").getValue());
                            duniversity.setPhotoUrl((String) dataSnapshot.child("univPhotoUrl").getValue());
                            duniversity.setInfo((String) dataSnapshot.child("info").getValue());
                            pastUniversities.add(duniversity);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {


                        }});
            }

                for(String c: cunis){
                    String fbUrl = "https://blazing-torch-4222.firebaseio.com/Universities/" + c;
                    Firebase cuniRoot = new Firebase(fbUrl);
                    //adding current universities
                    final University cuniversity = new University();
                    cuniRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            cuniversity.setName((String) dataSnapshot.child("univName").getValue());
                            cuniversity.setLocation((String) dataSnapshot.child("location").getValue());
                            cuniversity.setWebsiteUrl((String) dataSnapshot.child("website").getValue());
                            cuniversity.setPhotoUrl((String) dataSnapshot.child("univPhotoUrl").getValue());
                            cuniversity.setInfo((String) dataSnapshot.child("info").getValue());
                            currentUniversities.add(cuniversity);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {


                        }});
                }
        ListView currentUniView = (ListView) findViewById(R.id.current_uni_list);
        currentUniAdapter = new UniAdapter();
        currentUniAdapter.unis =  currentUniversities;
        currentUniView.setAdapter(currentUniAdapter);

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

        public ArrayList<University> unis;
        @Override
        public int getCount() {
            return unis.size();
        }

        @Override
        public Object getItem(int position) {
            //TODO make this get objects
            University university = unis.get(position);
            return university;

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            University university = unis.get(position);
            TextView tv ;
            if (null == convertView) {
                //no reuse - create new textView
                tv = new TextView(UserProfile.this);
                tv.setText(university.getName());
                tv.setTextSize(22);
                tv.setPadding(16, 16, 16, 16);
            }
            else {
                //we have an old textView to work with
                tv = (TextView) convertView;
            }
            //University uni = this.getItem(position);
            tv.setText(university.getName());

            return tv;
        }
    }
}
