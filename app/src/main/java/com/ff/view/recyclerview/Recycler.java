package com.ff.view.recyclerview;

import android.util.Log;
import android.view.View;

import java.util.Stack;

/**
 * description: 回收池
 * author: FF
 * time: 2019-05-05 10:27
 */
public class Recycler {

    private static final String TAG = "Recycler";

    // 使用栈结构，使用数组是为了存储多个type
    private Stack<View>[] views;

    public Recycler(int typeNumber) {
        views = new Stack[typeNumber];

        for (int i = 0; i < typeNumber; i++) {
            views[i] = new Stack<>();
        }
    }

    /**
     * 存View到指定到type下
     */
    public void put(View view, int type) {
        views[type].push(view);
    }

    /**
     * 根据Item的type取View
     */
    public View get(int type) {
        try {
            // views没数据时，取会报错
            return views[type].pop();
        } catch (Exception e) {
            Log.d(TAG, "get: " + e);
            return null;
        }
    }
}
