package com.bigsisters.bigsisters;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditRatingFragment extends Fragment {

    int uniId;
    String userId;
    String userUrl;
    String uniUrl;
    final int[] oldRating = new int[4];
    final double[] oldUniRating = new double[4];
    final long[] oldVotes = new long[4];

    public EditRatingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_edit_rating, container, false);
        UniversityActivity parent = (UniversityActivity) getActivity();

        uniId = parent.university.id;
        userId = parent.userId;

        userUrl = "https://blazing-torch-4222.firebaseio.com/Users/"+userId+"/universities/"+uniId;
        uniUrl = "https://blazing-torch-4222.firebaseio.com/Universities/" + uniId + "/rating";
        Log.d("stefania", userUrl);
        Log.d("stefania", uniUrl);

        Firebase userRating = new Firebase(userUrl);
        userRating.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EditText et = (EditText) view.findViewById(R.id.myGrade0);
                et.setText(dataSnapshot.child("att0").getValue().toString());
                oldRating[0] = Integer.parseInt(et.getText().toString());
                et = (EditText) view.findViewById(R.id.myGrade1);
                et.setText(dataSnapshot.child("att1").getValue().toString());
                oldRating[1] = Integer.parseInt(et.getText().toString());
                et = (EditText) view.findViewById(R.id.myGrade2);
                et.setText(dataSnapshot.child("att2").getValue().toString());
                oldRating[2] = Integer.parseInt(et.getText().toString());
                et = (EditText) view.findViewById(R.id.myGrade3);
                et.setText(dataSnapshot.child("att3").getValue().toString());
                oldRating[3] = Integer.parseInt(et.getText().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        final Firebase uniRating = new Firebase(uniUrl);
        uniRating.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 0; i < 4; i++) {
                    //oldUniRating[i] = Double.parseDouble((String) dataSnapshot.child("att" + i).child("grade").getValue());
                    oldUniRating[i] = dataSnapshot.child("att" + i).child("grade").getValue(Double.class);
                    //oldVotes[i] = Integer.parseInt((String) dataSnapshot.child("att"+i).child("votes").getValue());
                    oldVotes[i] = (Long) dataSnapshot.child("att"+i).child("votes").getValue(Long.class);
                    Log.d("stefania", Double.toString(oldUniRating[i]));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Button submitBtn = (Button) view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] newRating = new int[4];
                EditText et = (EditText) view.findViewById(R.id.myGrade0);
                newRating[0] = Integer.parseInt(et.getText().toString());
                et = (EditText) view.findViewById(R.id.myGrade1);
                newRating[1] = Integer.parseInt(et.getText().toString());
                et = (EditText) view.findViewById(R.id.myGrade2);
                newRating[2] = Integer.parseInt(et.getText().toString());
                et = (EditText) view.findViewById(R.id.myGrade3);
                newRating[3] = Integer.parseInt(et.getText().toString());
                for (int i = 0; i < 4; i++) {
                    if (newRating[i] > 5 || newRating[i] < 1) newRating[i] = oldRating[i];
                }

                // First upgrade user
                Firebase userRatings = new Firebase(userUrl);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("att0", newRating[0]);
                map.put("att1", newRating[1]);
                map.put("att2", newRating[2]);
                map.put("att3", newRating[3]);
                userRatings.updateChildren(map);

                // Then upgrade university
                Firebase uniRatings = new Firebase(uniUrl);
                for (int i = 0; i < 4; i++) {
                    Log.d("stefania", "grades updating at " + uniUrl);
                    if (newRating[i] == oldRating[i]) {}
                    else if (oldRating[0] == 0) {
                        Log.d("stefania", "new Vote " +newRating[i]);
                        if (oldVotes[i] == 0) uniRating.child("att"+i).child("grade").setValue(newRating[i]);
                        else uniRating.child("att"+i).child("grade").setValue((oldUniRating[i] * oldVotes[i] + newRating[i]) / (1.0 * (1+oldVotes[i])));
                        uniRating.child("att"+i).child("votes").setValue(oldVotes[i] + 1);
                    }
                    else if (newRating[i] == 0) {
                        Log.d("stefania", "vote removed");
                        if(oldVotes[i] == 1) uniRating.child("att"+i).child("grade").setValue(0);
                        else uniRating.child("att"+i).child("grade").setValue((oldUniRating[i] * oldVotes[i] - newRating[i]) / (1+oldVotes[i]));
                        uniRating.child("att"+i).child("votes").setValue(oldVotes[i] - 1);
                    }
                    else {
                        Log.d("stefania", "modified vote");
                        uniRating.child("att"+i).child("grade").setValue(oldUniRating[i] + (newRating[i] - oldRating[i]) / oldVotes[i]);
                    }

                    UniversityActivity parent = (UniversityActivity) getActivity();
                    //parents.switchTabs(1);
                }


            }
        });


        return view;
    }


}
