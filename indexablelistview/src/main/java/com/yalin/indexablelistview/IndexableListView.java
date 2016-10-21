package com.yalin.indexablelistview;

import android.app.Service;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：YaLin
 * 日期：2016/7/15.
 */
public class IndexableListView extends FrameLayout implements IndexBar.OnLetterChangeListener {
    private ListView mListView;

    private IndexBar mIndexBar;

    private TextView mTextView;

    private IndexableAdapter mListViewAdapter;

    private boolean mIsContainedIndexOnly = false;

    public IndexableListView(Context context) {
        super(context);
        init(context, null);
    }

    public IndexableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IndexableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View content = layoutInflater.inflate(R.layout.layout_indexable_list, this, false);
        mListView = (ListView) content.findViewById(R.id.order_list_datas);
        mIndexBar = (IndexBar) content.findViewById(R.id.order_list_ob_index);
        mTextView = (TextView) content.findViewById(R.id.order_list_tv_select);

        addView(content);

        mIndexBar.setLetterChangeListener(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndexableListView);
        int typedCount = typedArray.getIndexCount();

        for (int i = 0; i < typedCount; i++) {
            int index = typedArray.getIndex(i);
            if (index == R.styleable.IndexableListView_indexBarBackground) {
                int indexBackground = typedArray.getColor(R.styleable.IndexableListView_indexBarBackground, 0);
                mIndexBar.setBackgroundColor(indexBackground);
            } else if (index == R.styleable.IndexableListView_indexTextBackground) {
                int textBackground = typedArray.getColor(R.styleable.IndexableListView_indexTextBackground, 0);
                mTextView.setBackgroundColor(textBackground);
            } else if (index == R.styleable.IndexableListView_indexTextColor) {
                int textColor = typedArray.getColor(R.styleable.IndexableListView_indexTextColor, 0);
                mTextView.setTextColor(textColor);
            } else if (index == R.styleable.IndexableListView_indexListDivider) {
                Drawable divider = typedArray.getDrawable(R.styleable.IndexableListView_indexListDivider);
                mListView.setDivider(divider);
                mListView.setDividerHeight(1);
            }
        }
        typedArray.recycle();
    }

    @Override
    public void onLetterChange(Character letter, int y) {
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(letter.toString());
        mTextView.setY(y - mTextView.getHeight() / 2);

        localListView(letter);
    }

    @Override
    public void onCancel() {
        mTextView.setVisibility(View.GONE);
    }

    private void localListView(Character letter) {
        if (mListViewAdapter == null || !mListViewAdapter.mIndexMap.containsKey(letter)) {
            return;
        }
        int index = (int) mListViewAdapter.mIndexMap.get(letter);
        mListView.setSelection(index);
    }

    public void setAdapter(IndexableAdapter adapter) {
        mListView.setAdapter(adapter);
        mListViewAdapter = adapter;
        if (mIsContainedIndexOnly) {
            List<Character> letters = new ArrayList<>();
            letters.addAll(mListViewAdapter.mIndexMap.keySet());
            mIndexBar.setLetters(letters);
        }
    }

    public IndexableAdapter getAdapter() {
        return mListViewAdapter;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        mListView.setOnItemClickListener(itemClickListener);
    }

    public static abstract class IndexableAdapter<T extends Indexable> extends BaseAdapter {
        protected List<T> indexables;

        private Map<Character, Integer> mIndexMap = new HashMap<>();

        public void setIndexables(List<T> indexables) {
            if (indexables == null || indexables.size() == 0) {
                return;
            }
            this.indexables = new ArrayList<>();
            this.indexables.addAll(indexables);

            traverseList();
        }

        private void traverseList() {
            Collections.sort(indexables, new Comparator<Indexable>() {
                @Override
                public int compare(Indexable lhs, Indexable rhs) {
                    if (TextUtils.isEmpty(lhs.getIndex())) {
                        return -1;
                    }
                    if (TextUtils.isEmpty(rhs.getIndex())) {
                        return 1;
                    }
                    return lhs.getIndex().toUpperCase().compareTo(rhs.getIndex().toUpperCase());
                }
            });
            mIndexMap.put('#', 0);
            int size = indexables.size();
            for (int i = 0; i < size; i++) {
                Indexable indexable = indexables.get(i);
                if (TextUtils.isEmpty(indexable.getIndex())) {
                    break;
                }
                char first = Character.toUpperCase(indexable.getIndex().charAt(0));
                if ((first >= 'a' && first <= 'z') || (first >= 'A' && first <= 'Z')) {
                    if (!mIndexMap.containsKey(first)) {
                        mIndexMap.put(first, i);
                    }
                } else {
                    mIndexMap.put('#', i);
                }
            }
        }
    }
}
