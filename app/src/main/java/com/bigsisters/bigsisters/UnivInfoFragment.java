package com.bigsisters.bigsisters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Fragment that shows info on university
 */
public class UnivInfoFragment extends Fragment {

    private TextView mText;

    public UnivInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_univ_info, null);
        mText = (TextView) view.findViewById(R.id.uniInfo);
        if (mText != null) {

            mText.setText("This is a university. ");
        }
        return view;
    }


}
