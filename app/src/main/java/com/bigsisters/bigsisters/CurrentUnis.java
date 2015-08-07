package com.bigsisters.bigsisters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentUnis.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentUnis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentUnis extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TYPE = "current|past|favourite";


    // TODO: Rename and change types of parameters
    private String mType;
    private ArrayList<String> mUniversityIdList;
    private ArrayList<University> mUniversityList;
    private UniAdapter mUniAdapter;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment CurrentUnis.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentUnis newInstance(String param1) {
        CurrentUnis fragment = new CurrentUnis();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param1);

        fragment.setArguments(args);
        return fragment;
    }

    public CurrentUnis() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Firebase.setAndroidContext(getActivity());
        View v = inflater.inflate(R.layout.fragment_current_unis, null);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userid = sharedPref.getString(getString(R.string.login_id), "");
        String fbUrl = "https://blazing-torch-4222.firebaseio.com/Users/" + userid+"/currents/";
        Firebase userRoot = new Firebase(fbUrl);
        mUniversityIdList = new ArrayList<String>();
        mUniversityList = new ArrayList<University>();

        // Inflate the layout for this fragment
        userRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String u = ds.getValue(String.class);
                    mUniversityIdList.add(u);
                    Log.d("silvia", "I'm getting a uni in here yay" + u);
                }

                for (String uniId : mUniversityIdList) {

                    String unisUrl = "https://blazing-torch-4222.firebaseio.com/Universities/" + uniId;
                    Firebase unisRoot = new Firebase(unisUrl);
                    unisRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            University university = new University();
                            university.setName((String) dataSnapshot.child("univName").getValue());
                            university.setLocation((String) dataSnapshot.child("location").getValue());
                            university.setWebsiteUrl((String) dataSnapshot.child("website").getValue());
                            university.setPhotoUrl((String) dataSnapshot.child("univPhotoUrl").getValue());
                            university.setInfo((String) dataSnapshot.child("info").getValue());
                            mUniversityList.add(university);

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
            public void onCancelled(FirebaseError firebaseError) {
                // TODO: specific error handling
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        UniAdapter m

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class UniAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mUniversityIdList.size();
        }

        @Override
        public Object getItem(int position) {
            //TODO make this get objects
            University university = mUniversityList.get(position);
            return university;

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            University university = mUniversityList.get(position);
            TextView tv ;
            if (null == convertView) {
                //no reuse - create new textView
                tv = new TextView(getActivity());
                tv.setText(university.getName());
                tv.setTextSize(22);
                tv.setPadding(16, 16, 16, 16);
            }
            else {
                //we have an old textView to work with
                tv = (TextView) convertView;
                Log.d("Silvia", "i even make the view");
            }
            //University uni = this.getItem(position);
            tv.setText(university.getName());

            return tv;
        }
    }

}
