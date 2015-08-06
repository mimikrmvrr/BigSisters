package com.bigsisters.bigsisters;

import android.app.ActionBar;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Toolbar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ToolBar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ToolBar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToolBar extends Fragment {
    private Toolbar mToolBar;


    public static ToolBar newInstance(String param1, String param2) {
        ToolBar fragment = new ToolBar();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public ToolBar() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_tool_bar, null);


        mToolBar = (Toolbar) v.findViewById(R.id.my_toolbar);
        // Set an OnMenuItemClickListener to handle menu item clicks
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                return true;
            }
        });

        // Inflate a menu to be displayed in the toolbar
        mToolBar.inflateMenu(R.menu.my_toolbar_menu);
        ActionBarActivity activity = (ActionBarActivity) this.getActivity();
        activity.setSupportActionBar(mToolBar);
        //GetActivity().setSupportActionBar(toolbar);
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        mToolBar.setTitle("");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

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

}
