package com.bigsisters.bigsisters;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {

    private TextView mText;
    public RatingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, null);
        /*mText = (TextView) view.findViewById(R.id.uniRating);
        if (mText != null) {

            mText.setText("This is the rating area. ");
        }*/
        return view;
    }

}
