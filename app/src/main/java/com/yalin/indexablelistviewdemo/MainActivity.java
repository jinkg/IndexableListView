package com.yalin.indexablelistviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yalin.indexablelistview.IndexableListView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IndexableListView listView = (IndexableListView) findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);
        mAdapter.setIndexables(Arrays.asList(IndexableDatas.ITEMS));
    }

    private IndexableListView.IndexableAdapter<IndexableItem> mAdapter =
            new IndexableListView.IndexableAdapter<IndexableItem>() {

                @Override
                public int getCount() {
                    return indexables == null ? 0 : indexables.size();
                }

                @Override
                public IndexableItem getItem(int position) {
                    return indexables.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = LayoutInflater.from(MainActivity.this).
                                inflate(android.R.layout.simple_list_item_1, parent, false);
                    }
                    TextView textView = (TextView) convertView;
                    textView.setText(getItem(position).name);
                    return convertView;
                }
            };
}
