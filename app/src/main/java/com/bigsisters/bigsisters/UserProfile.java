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
                    Log.d("Silvia" , dataSnapshot.child("currents").getChildrenCount()+"");
                    DataSnapshot cds = dataSnapshot.child("currents");
                    for(DataSnapshot curr : cds.getChildren()){
                        String fave = curr.getValue(String.class);
                        unis.add(fave);
                        Log.d("silvia",fave +"aaah");
                    }





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
}
