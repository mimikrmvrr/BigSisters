package com.bigsisters.bigsisters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demouser on 8/6/15.
 */
public class HomeActivity extends ActionBarActivity {

    public class PostsAdapter extends BaseAdapter {

        private class ViewHolder {
            public TextView name;
            public TextView time;
            public ImageView pic;
            public TextView post_content;
        }

        private List<Post> posts;

        @Override
        public int getCount() {
            return posts.size();
        }

        @Override
        public Post getItem(int position) {
            return posts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public LinearLayout getView(int position, View convertView, ViewGroup parent) {
            LinearLayout postLayout;
            RelativeLayout postHeaderLayout;
            ViewHolder viewHolder;

            if (convertView != null) {
                postLayout = (LinearLayout) convertView;
                viewHolder = (ViewHolder) postLayout.getTag();
            } else {
                LayoutInflater postInflater = LayoutInflater.from(HomeActivity.this);
                postLayout = (LinearLayout) postInflater.inflate(R.layout.post, null);
                postHeaderLayout = (RelativeLayout) postLayout.findViewById(R.id.post_header);

                //create ViewHolder
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) postHeaderLayout.findViewById(R.id.name);
                viewHolder.time = (TextView) postHeaderLayout.findViewById(R.id.time);
                viewHolder.pic = (ImageView) postHeaderLayout.findViewById(R.id.pic);
                viewHolder.post_content = (TextView) postLayout.findViewById(R.id.post_content);
                postLayout.setTag(viewHolder);
            }

            Post post = getItem(position);
            if (viewHolder.name != null) {
                viewHolder.name.setText(post.getName());
            };
            viewHolder.time.setText(post.getTime());
            viewHolder.post_content.setText(post.getText());
            Picasso.with(getApplicationContext()).load(post.getPicUrl()).into(viewHolder.pic);

            return postLayout;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }


    }

    private class TabsAdapter extends FragmentStatePagerAdapter {

        private final String[] tabs = {"NEWS", "Q&A"};

        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeFragment();
            }
            return new QuestionsFragment();
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));

    }




}