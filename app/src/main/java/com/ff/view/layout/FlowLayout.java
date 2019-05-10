package com.ff.view.layout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ff.view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author: FF
 * time: 2019-05-04 20:20
 */
public class FlowLayout extends RelativeLayout {

    private static final String TAG = "FlowLayout";

    @NonNull
    private List<String> mTags = new ArrayList<>();

    private boolean mInitialized;
    private LayoutInflater mInflater;
    private int mWidth;// FlowLayout的宽度

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void addTag(@NonNull List<String> tags) {
        Log.d(TAG, "addTag: ");
        mTags = tags;
        drawTags();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged: ");
        mWidth = w;
    }

    private void init(Context context) {

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d(TAG, "onGlobalLayout: ");
                if (!mInitialized) {
                    mInitialized = true;
                    drawTags();
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    private void drawTags() {

        if (!mInitialized) {
            return;
        }
        removeAllViews();
        float total = 0;
        int index = 1;// 现在的位置
        int pindex = index;// 相对起点位置
        View tagLayout;
        LayoutParams tagParams;
        for (String item : mTags) {
            tagLayout = mInflater.inflate(R.layout.layout_tag, this, false);
            tagLayout.setId(index);
            TextView tagView = tagLayout.findViewById(R.id.tag_txt);
            tagView.setText(item);// 设置标签view显示的文字
            tagView.setPadding(10, 5, 10, 5);
            float tagWidth = tagView.getPaint().measureText(item) + 10 * 2;
            tagParams = new LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            if (total + tagWidth < mWidth) {
                tagParams.addRule(RelativeLayout.ALIGN_TOP, pindex);
                tagParams.addRule(RelativeLayout.RIGHT_OF, index - 1);
                if (index > 1) {
                    tagParams.leftMargin = 10;
                    total += 10;

                }
            } else {
                tagParams.addRule(RelativeLayout.BELOW, pindex);
                tagParams.topMargin = 10;
                total = 0;
                pindex = index;
            }
            total += tagWidth;
            addView(tagLayout, tagParams);// 添加到相对布局中
            index++;
        }
    }
}
