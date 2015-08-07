package com.bigsisters.bigsisters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity {
    Animation animFadeIn;
    Button b;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);


        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //send the info to firebase
                        LoginManager.getInstance().logInWithReadPermissions( //logInWithPublishPermissions(
                                MainActivity.this,
                                Arrays.asList("public_profile"));

                        GraphRequest g =   new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/me",
                                null,
                                HttpMethod.GET,
                                 new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        Firebase ref = new Firebase("https://blazing-torch-4222.firebaseio.com/Users");
                                        try {

                                            Log.d("response", response.getJSONObject().toString());

                                            final String id = (String) response.getJSONObject().get("id");
                                            new GraphRequest(
                                                    AccessToken.getCurrentAccessToken(),
                                                    "/"+id+"/picture",
                                                    null,
                                                    HttpMethod.GET,
                                                    new GraphRequest.Callback() {
                                                        public void onCompleted(GraphResponse response) {
                                                            Firebase ref = new Firebase("https://blazing-torch-4222.firebaseio.com/Users");
                                                           // String image = (String)response.getJSONObject().toString();
                                                            URL url = response.getConnection().getURL();
                                                            String photoUrl = "photoUrl";
                                                            ref.child(id).child(photoUrl).setValue(url);

                                                        }
                                                    }
                                            ).executeAsync();


                                            ref.child(id).setValue("");
                                            //sharedPreferences(id);

                                            SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString(getString(R.string.login_id), id);
                                            editor.commit();


                                            String ename = "name";
                                            String name = (String) response.getJSONObject().get("name");
                                            ref.child(id).child(ename).setValue(name);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        );
                                g.executeAsync();




                        //go to homepage
                        Intent loginIntent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(loginIntent);
                        Log.d("fb", "onsuccess");

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d("fb", "oncancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d("fb", "onerror", exception);
                    }
                });

        logoAnimation();
        goHome();

    }

    void sharedPreferences(String loginId) {
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.login_id), Integer.parseInt(loginId));
        editor.commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    //navigate from conuinue without login to home screen
    void goHome(){
        b = (Button)findViewById(R.id.nologin);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(loginIntent);

            }
        });
    }
    //animate logo, animation is in anim fade_in.xml
    void logoAnimation() {
        ImageView image;
        image = (ImageView)findViewById(R.id.logo);
        // load the animation
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        image.startAnimation(animFadeIn);
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
