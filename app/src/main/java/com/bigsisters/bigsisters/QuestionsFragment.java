package com.bigsisters.bigsisters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
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
public class QuestionsFragment extends Fragment {

    public class QuestionsAdapter extends BaseAdapter {

        private class ViewHolder {
            public TextView title;
            public TextView time;
            public TextView about;
        }

        private List<Question> questions;

        @Override
        public int getCount() {
            return questions.size();
        }

        @Override
        public Question getItem(int position) {
            return questions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RelativeLayout getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout layout;
            ViewHolder viewHolder;

            if (convertView != null) {
                layout = (RelativeLayout) convertView;
                viewHolder = (ViewHolder) layout.getTag();
            } else {
                LayoutInflater postInflater = LayoutInflater.from(getActivity());
                layout = (RelativeLayout) postInflater.inflate(R.layout.question, null);

                //create ViewHolder
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) layout.findViewById(R.id.title);
                viewHolder.time = (TextView) layout.findViewById(R.id.time);
                viewHolder.about = (TextView) layout.findViewById(R.id.about);
                layout.setTag(viewHolder);
            }

            Question question = getItem(position);
            viewHolder.title.setText(question.getTitle());
            loadUniversityName(question.getAbout(), viewHolder.about);
            viewHolder.time.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(question.getTime())));

            layout.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("mimi", "Click on question is made");
                            Intent intent = new Intent(getActivity(), QuestionActivity.class);
                            startActivity(intent);
                        }
                    }
            );

            return layout;
        }

        public void setQuestions(List<Question> questions) {
            this.questions = questions;
        }

        private void loadUniversityName(final String id, final TextView name) {
            final Firebase universityRef = new Firebase("https://blazing-torch-4222.firebaseio.com/Universities/" + id + "/univName");
            universityRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String universityName = dataSnapshot.getValue(String.class);

                    if (universityName == null || universityName.isEmpty()) {
                        universityName = "General";
                    }
                    name.setText(universityName);
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
        final ListView list = (ListView) inflater.inflate(R.layout.questions_list, container, false);
        Firebase.setAndroidContext(getActivity());
        final Firebase questionsRef = new Firebase("https://blazing-torch-4222.firebaseio.com/Questions");
        questionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<Question> questions = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    questions.add(child.getValue(Question.class));
                }

                QuestionsAdapter adapter = new QuestionsAdapter();
                adapter.setQuestions(questions);
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
