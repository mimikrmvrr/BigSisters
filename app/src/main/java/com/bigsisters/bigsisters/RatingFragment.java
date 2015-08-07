package com.bigsisters.bigsisters;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {

    private TextView mText;
    int uniId;
    String userId;
    public RatingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, null);
        UniversityActivity parent = (UniversityActivity) getActivity();
        uniId = parent.university.id;
        userId = parent.userId;
        String fbUrl = "https://blazing-torch-4222.firebaseio.com/Universities/" + uniId + "/rating";
        Firebase uniRoot = new Firebase(fbUrl);
        uniRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double[] ratings = new double[4];
                //ratings[0] = Double.parseDouble((String) dataSnapshot.child("att0").child("grade").getValue());
                ratings[0] = dataSnapshot.child("att0").child("grade").getValue(Double.class);
                ratings[1] = dataSnapshot.child("att1").child("grade").getValue(Double.class);
                ratings[2] = dataSnapshot.child("att2").child("grade").getValue(Double.class);
                ratings[3] = dataSnapshot.child("att3").child("grade").getValue(Double.class);
                TextView grade = (TextView) getView().findViewById(R.id.grade0);
                grade.setText(Double.toString(ratings[0]));
                grade = (TextView) getView().findViewById(R.id.grade1);
                grade.setText(Double.toString(ratings[1]));
                grade = (TextView) getView().findViewById(R.id.grade2);
                grade.setText(Double.toString(ratings[2]));
                grade = (TextView) getView().findViewById(R.id.grade3);
                grade.setText(Double.toString(ratings[3]));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        return view;
    }



}
