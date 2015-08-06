package com.bigsisters.bigsisters;

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

        private static final int TABS_COUNT = 2;

        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new Fragment();
        }

        @Override
        public int getCount() {
            return TABS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "NEWS";
            }
            return "Q&A";
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));

        Firebase.setAndroidContext(this);
        final Firebase postsRef = new Firebase("https://blazing-torch-4222.firebaseio.com/Posts");

        final ListView list = (ListView) findViewById(R.id.posts);

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<Post> posts = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    posts.add(child.getValue(Post.class));
                }

                PostsAdapter adapter = new PostsAdapter();
                adapter.setPosts(posts);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}