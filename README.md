# IndexableListView

<img src="https://github.com/jinkg/Screenshots/blob/master/IndexableListView/indexable_listview.gif" width="180" height="320">

## Usage

```xml
<com.yalin.indexablelistview.IndexableListView
        android:id="@+id/list_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@android:color/white"
        app:indexBarBackground="#33000000"
        app:indexListDivider="@drawable/list_divider" />
```

```java
 IndexableListView listView = (IndexableListView) findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);
        mAdapter.setIndexables(Arrays.asList(IndexableDatas.ITEMS));
```

You can see a complete usage in the demo app.

## Feedback

nilaynij@gmail.com.
