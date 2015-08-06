package com.bigsisters.bigsisters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by demouser on 8/6/15.
 */
public class QuestionActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        final TextView title = (TextView) findViewById(R.id.title);
        final TextView about = (TextView) findViewById(R.id.about);
        final TextView text = (TextView) findViewById(R.id.text);
        final TextView from = (TextView) findViewById(R.id.from);
        final TextView answer = (TextView) findViewById(R.id.answer);

        Firebase.setAndroidContext(QuestionActivity.this);
        final Firebase questionRef = new Firebase("https://blazing-torch-4222.firebaseio.com/Questions/question1");
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Question question = snapshot.getValue(Question.class);
                title.setText(question.getTitle());
                about.setText(question.getText());
                text.setText(question.getText());
                from.setText(question.getFrom());
                answer.setText(question.getAnswer());
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

        return super.onOptionsItemSelected(item);
    }
}
