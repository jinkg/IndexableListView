package com.yalin.indexablelistviewdemo;

import com.yalin.indexablelistview.Indexable;

/**
 * 作者：YaLin
 * 日期：2016/10/21.
 */

public class IndexableItem implements Indexable {
    public String name;
    public int code;

    public IndexableItem(String name, int code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String getIndex() {
        return name;
    }
}
