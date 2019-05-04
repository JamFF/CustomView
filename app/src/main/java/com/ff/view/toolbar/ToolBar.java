package com.ff.view.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ff.view.R;

/**
 * description: 组合控件实现ToolBar
 * author: FF
 * time: 2019-05-04 15:25
 */
public class ToolBar extends RelativeLayout {

    private ImageView iv_left;
    private ImageView iv_right;

    public ToolBar(Context context) {
        this(context, null);
    }

    public ToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolBar);
        int textColor = mTypedArray.getColor(R.styleable.ToolBar_title_text_color, Color.WHITE);
        String titleName = mTypedArray.getString(R.styleable.ToolBar_title_text);
        mTypedArray.recycle();

        LayoutInflater.from(context).inflate(R.layout.titlebar, this, true);
        iv_left = findViewById(R.id.iv_left);
        iv_right = findViewById(R.id.iv_right);

        TextView tv_title = findViewById(R.id.tv_title);

        if (!TextUtils.isEmpty(titleName)) {
            tv_title.setTextColor(textColor);
            tv_title.setText(titleName);
        }
    }

    public void setLeftListener(OnClickListener onClickListener) {
        iv_left.setOnClickListener(onClickListener);
    }

    public void setRightListener(OnClickListener onClickListener) {
        iv_right.setOnClickListener(onClickListener);
    }
}
