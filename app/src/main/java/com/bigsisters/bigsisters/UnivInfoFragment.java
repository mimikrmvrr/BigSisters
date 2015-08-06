package com.bigsisters.bigsisters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


/**
 * Fragment that shows info on university
 */
public class UnivInfoFragment extends Fragment {

    public TextView mText;
    int id;

    public UnivInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_univ_info, null);
        /*id = getArguments().getInt("info");
        Log.d("stefania", "argument id is " +id);
        mText = (TextView) view.findViewById(R.id.uniInfo);
        if (mText != null) {
            mText.setText("Loading ... ");
        }*/

        UniversityActivity parent = (UniversityActivity) getActivity();
        id = parent.university.id;

        mText = (TextView) view.findViewById(R.id.uniInfo);
        if (mText != null) {
            mText.setText("Loading ... ");
        }
        String fbUrl = "https://blazing-torch-4222.firebaseio.com/Universities/" + id;
        Firebase uniRoot = new Firebase(fbUrl);
        Log.d("stefania", "Firebase: " + fbUrl);
        uniRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mText.setText(dataSnapshot.child("info").getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        return view;
    }

    public void updateText(String update) {
        mText.setText(update);
    }


}
