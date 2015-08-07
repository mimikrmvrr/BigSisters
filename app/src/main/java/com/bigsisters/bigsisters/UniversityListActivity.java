package com.bigsisters.bigsisters;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;


public class UniversityListActivity extends ActionBarActivity {

    public MyAdapter mAdapter;
    public ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_list);


        mList = (ListView) findViewById(R.id.listView);
        mAdapter = new MyAdapter();
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> list, View view, int position,
                                    long id) {
                // do things
                Intent i = new Intent(UniversityListActivity.this, UniversityActivity.class);
                Random r = new Random();
                i.putExtra(UniversityActivity.EXTRA_ID, Integer.toString(r.nextInt() % 3));
                startActivity(i);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_university_list, menu);

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

    public class MyAdapter extends BaseAdapter {
        String[] universities = new String[] {"Reykjavik University", "Tartu University", "Lund University", "University of Helsinki", "Stanford",
                "University of Edinburgh", "Alto University", "University of Iceland"};
        public int getCount() {
            return universities.length;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv ;
            if (null == convertView) {
                //no reuse - create new textView
                tv = new TextView(UniversityListActivity.this);

                tv.setTextSize(35);
                tv.setPadding(16, 16, 16, 16);
            }
            else {
                //we have an old textView to work with
                tv = (TextView) convertView;
            }

            tv.setText(universities[position]);

            return tv;
        }

        public Object getItem(int pos) {
            return universities[pos % universities.length];
        }

        public long getItemId(int index) {
            return index;
        }
    }
}
