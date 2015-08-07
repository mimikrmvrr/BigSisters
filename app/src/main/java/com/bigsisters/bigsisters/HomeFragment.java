package com.bigsisters.bigsisters;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
public class HomeFragment extends android.support.v4.app.Fragment {
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
                LayoutInflater postInflater = LayoutInflater.from(getActivity());
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
            loadUniversityName(post.getName(), viewHolder.name);
            viewHolder.time.setText(post.getTime());
            viewHolder.post_content.setText(post.getText());
            Picasso.with(getActivity().getApplicationContext()).load(post.getPicUrl()).into(viewHolder.pic);
            viewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(getActivity(), UniversityActivity.class);
                    intent.putExtra(UniversityActivity.EXTRA_ID, "1");
                    startActivity(intent);
                }
            });

            return postLayout;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }


        private void loadUniversityName(String id, final TextView name) {
          //  final String universityName = "";
            final Firebase universityRef = new Firebase("https://blazing-torch-4222.firebaseio.com/Universities/" + id + "/univName");
            universityRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ListView list = (ListView) inflater.inflate(R.layout.posts_list, container, false);
        Firebase.setAndroidContext(getActivity());
        final Firebase postsRef = new Firebase("https://blazing-torch-4222.firebaseio.com/Posts");
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

        return list;
    }
}
