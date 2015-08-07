package com.bigsisters.bigsisters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by demouser on 8/6/15.
 */
public class QuestionActivity extends ActionBarActivity {
    public static String QUESTION_ID = "QuestionID";
    public class AnswerAdapter extends BaseAdapter {

        private class ViewHolder {
            public TextView text;
            public TextView time;
            public TextView from;
        }

        private List<Answer> answers;

        @Override
        public int getCount() {
            return answers.size();
        }

        @Override
        public Answer getItem(int position) {
            return answers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public LinearLayout getView(int position, View convertView, ViewGroup parent) {
            LinearLayout layout;
            ViewHolder viewHolder;

            if (convertView != null) {
                layout = (LinearLayout) convertView;
                viewHolder = (ViewHolder) layout.getTag();
            } else {
                LayoutInflater postInflater = LayoutInflater.from(QuestionActivity.this);
                layout = (LinearLayout) postInflater.inflate(R.layout.answ, null);

                //create ViewHolder
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) layout.findViewById(R.id.text);
                viewHolder.time = (TextView) layout.findViewById(R.id.time);
                viewHolder.from = (TextView) layout.findViewById(R.id.from);
                layout.setTag(viewHolder);
            }

            Answer answer = getItem(position);
            viewHolder.text.setText(answer.getText());
            viewHolder.time.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(answer.getTime())));
            //viewHolder.time.setText(answer.getTime());
            loadUserName(answer.getFrom(), viewHolder.from);
           // viewHolder.from.setText(answer.getFrom());

            return layout;
        }

        public void setAnswers(Collection<Answer> answers) {
            this.answers = new ArrayList<>(answers);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        final TextView title = (TextView) findViewById(R.id.title);
        final TextView about = (TextView) findViewById(R.id.about);
        final TextView text = (TextView) findViewById(R.id.text);
        final TextView from = (TextView) findViewById(R.id.from);
        final ListView answer = (ListView) findViewById(R.id.answer);
        final TextView time = (TextView) findViewById(R.id.time);
        final TextView answers_count = (TextView) findViewById(R.id.answers_count);

        Firebase.setAndroidContext(QuestionActivity.this);
        Intent intent = getIntent();
        final String questionId = intent.getStringExtra(QUESTION_ID);
        final Firebase questionRef = new Firebase("https://blazing-torch-4222.firebaseio.com/Questions/" + "question1");
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Answer> answers = new ArrayList<>();
                for(DataSnapshot answerSnapshot : snapshot.child("answer").getChildren()) {
                    answers.add(answerSnapshot.getValue(Answer.class));
                }
                Question question = new Question();
                question.setAbout(snapshot.child("about").getValue(String.class));
                question.setFrom(snapshot.child("from").getValue(String.class));
                question.setText(snapshot.child("text").getValue(String.class));
                question.setTime(snapshot.child("time").getValue(String.class));
                question.setTitle(snapshot.child("title").getValue(String.class));
                question.setAnswer(answers);
                //Question question = snapshot.getValue(Question.class);
                title.setText(question.getTitle());
                loadUniversityName(question.getAbout(), about);
                text.setText(question.getText());
                loadUserName(question.getFrom(), from);
              //  time.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(question.getTime())));
                answers_count.setText("" + question.getAnswer().size() + " Answers");

                AnswerAdapter adapter = new AnswerAdapter();
                adapter.setAnswers(question.getAnswer());
                answer.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


        final Button addAnswerButton = (Button) findViewById(R.id.add_answer);
        addAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this, WriteAnswerActivity.class);
                intent.putExtra(WriteAnswerActivity.QUESTION_ID, questionId);
                startActivity(intent);
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

        return super.onOptionsItemSelected(item);
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

    private void loadUserName(final String id, final TextView name) {
        final Firebase userRef = new Firebase("https://blazing-torch-4222.firebaseio.com/Users/" + id + "/name");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);
                name.setText(username);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
