package com.ff.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.ff.view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author: FF
 * time: 2019-05-05 10:04
 */
public class RecyclerView extends ViewGroup {

    private static final String TAG = "RecyclerView";

    private Adapter mAdapter;

    // 当前显示的View
    private List<View> viewList = new ArrayList<>();
    // 上一次事件的y值
    private float lastY;
    // 行数
    private int rowCount;
    // view的第一行 是占内容的几行
    private int firstRow;
    // 第一个可见Item的左上顶点 距离屏幕的左上顶点的距离
    private int scrollY;
    // 初始化  第一屏最慢，控制layout的执行
    private boolean needRelayout;

    // RecyclerView的宽高
    private int width, height;

    private int[] heights;//item  高度
    private Recycler recycler;
    // 最小滑动距离，小于该值代表点击事件，大于该值代表滑动事件，不同手机值不同
    private int touchSlop;

    public RecyclerView(Context context) {
        this(context, null);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        ViewConfiguration configuration = ViewConfiguration.get(context);
        if (configuration != null) {
            touchSlop = configuration.getScaledDoubleTapSlop();
        }
        needRelayout = true;
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        if (adapter != null) {
            // 每次setAdapter的时候，要使用新的回收池
            recycler = new Recycler(adapter.getViewTypeCount());
            scrollY = 0;
            firstRow = 0;
            needRelayout = true;
            requestLayout();// 会触发 1.onMeasure 2.onLayout
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: ");
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (mAdapter != null) {
            rowCount = mAdapter.getCount();
            heights = new int[rowCount];
            for (int i = 0; i < rowCount; i++) {
                heights[i] = mAdapter.getHeight(i);
            }
        }
        // Item数据的总高度
        int tmpH = sumArray(heights, 0, heights.length);
        // 由于RecyclerView高度可能设置为wrap_content，所以取总高度和控件高度的最小值
        int h = Math.min(heightSize, tmpH);
        // 设置宽高
        setMeasuredDimension(widthSize, h);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 返回数组中某一部分的累加值
     *
     * @param array      数组
     * @param firstIndex 起始位置
     * @param count      个数
     * @return 从firstIndex开始到firstIndex+count结束的累加值
     */
    private int sumArray(int array[], int firstIndex, int count) {
        int sum = 0;
        count += firstIndex;
        for (int i = firstIndex; i < count; i++) {
            sum += array[i];
        }
        return sum;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (needRelayout || changed) {
            needRelayout = false;
            // 由于onLayout耗时，使用needRelayout标记
            Log.d(TAG, "onLayout: ");
            viewList.clear();
            removeAllViews();
            if (mAdapter != null) {
                // 摆放
                width = r - l;
                height = b - t;
                int top = 0;
                int bottom;
                // 判断控件的top要小于RecyclerView的高度，才进行添加
                for (int i = 0; i < rowCount && top < height; i++) {
                    bottom = top + heights[i];
                    // 生成一个View
                    View view = makeAndStep(i, 0, top, width, bottom);
                    viewList.add(view);
                    top = bottom;// 循环摆放
                }
            }
        }
    }

    private View makeAndStep(int row, int left, int top, int right, int bottom) {
        View view = obtainView(row, right - left, bottom - top);
        // 摆放
        view.layout(left, top, right, bottom);
        return view;
    }

    /**
     * 从回收池中获取，添加到RecyclerView中
     *
     * @param row    位置索引
     * @param width  Item的宽
     * @param height Item的高
     * @return
     */
    private View obtainView(int row, int width, int height) {

        int itemType = mAdapter.getItemViewType(row);
        View convertView = recycler.get(itemType);// 从回收池取
        View view;
        if (convertView == null) {// 取不到
            view = mAdapter.onCreateViewHolder(row, null, this);
            if (view == null) {
                throw new RuntimeException("onCreateViewHolder  必须填充布局");
            }
        } else {// 取到了
            view = mAdapter.onBinderViewHolder(row, convertView, this);
        }
        // 设置一个Tag，方便后面取到type，因为并不是每次都知道位置，不能通过getItemViewType方法取
        view.setTag(R.id.tag_type_view, itemType);

        // 测量
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        addView(view, 0);// 添加到RecyclerView里面
        return view;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent: ");
        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                lastY = ev.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float y2 = Math.abs(lastY - ev.getRawY());
                if (y2 > touchSlop) {
                    intercept = true;// 滑动事件
                }
            }
        }
        // 只拦截触摸事件，不拦截点击事件
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // 移动的距离y方向
            float y2 = event.getRawY();
            // 因为scrollBy的y方向：上滑正，下滑负，所以这里用上一次y值减去当前y值
            int diffY = (int) (lastY - y2);
            // 画布移动，并不影响子控件的位置
            scrollBy(0, diffY);// y方向：上滑正，下滑负
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void scrollBy(int x, int y) {
        // 对每一个子控件进行摆放
        // scrollY表示 第一个可见Item的左上顶点 距离屏幕的左上顶点的距离
        scrollY += y;
        scrollY = scrollBounds(scrollY);
        if (scrollY > 0) {// 上滑正
            // 使用while，是因为在快速滑动，onTouchEvent被调用时，可能会出现移除/添加多个的现象
            while (scrollY > heights[firstRow]) {// 当上滑的距离大于第一个展示的Item高度
                // 上滑移除
                removeView(viewList.remove(0));
                scrollY -= heights[firstRow];
                firstRow++;
            }
            while (getFillInHeight() < height) {// 最后一个可见Item滑入屏幕
                // 上滑添加
                int addLast = firstRow + viewList.size();
                View view = obtainView(addLast, width, heights[addLast]);
                viewList.add(viewList.size(), view);
            }


        } else if (scrollY < 0) {// 下滑负
            while (scrollY < 0) {
                // 下滑添加
                int firstAddRow = firstRow - 1;
                View view = obtainView(firstAddRow, width, heights[firstAddRow]);
                viewList.add(0, view);
                firstRow--;
                scrollY += heights[firstRow + 1];
            }
            while (getFillOutHeight() >= height) {// 最后一个可见Item滑出屏幕
                // 下滑移除
                removeView(viewList.remove(viewList.size() - 1));
            }

        } else {
            Log.d(TAG, "scrollBy: scrollY == 0");
        }
        repositionViews();
    }

    /**
     * 修正边界值，如果在首个元素，不让下滑，在最后一个元素，不让上滑
     *
     * @param scrollY
     * @return
     */
    private int scrollBounds(int scrollY) {
        if (scrollY > 0) {// 上滑
            // 第一个可见的Item到最后一个元素的的距离再减去RecyclerView的高度与scrollY比较取小值
            // 保证最后一个元素不被再拉出去
            scrollY = Math.min(scrollY, sumArray(heights, firstRow, heights.length - firstRow) - height);
        } else { // 下滑
            // 不能简单的在scrollY < 0时返回0，那样会出现不能下滑
            // 只有当第一个可见的Item是首个元素时，第二个参数才为0，用该值与scrollY进行比对
            scrollY = Math.max(scrollY, -sumArray(heights, 0, firstRow));

        }
        return scrollY;
    }

    /**
     * 第一个可见Item到最后一个可见Item的高度，减去第一个可见Item的超出屏幕的距离
     */
    private int getFillInHeight() {
        return sumArray(heights, firstRow, viewList.size()) - scrollY;
    }

    /**
     * 第一个可见Item到最后一个可见Item的高度，减去第一个可见Item的超出屏幕的距离，再减去最后一个Item的高度
     */
    private int getFillOutHeight() {
        return getFillInHeight() - heights[firstRow + viewList.size() - 1];
    }

    /**
     * 摆放所有的View
     */
    private void repositionViews() {
        int top, bottom, i;
        top = -scrollY;
        i = firstRow;
        for (View view : viewList) {
            bottom = top + heights[i++];
            view.layout(0, top, width, bottom);
            top = bottom;
        }
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        // 重写removeView，增加回收池逻辑
        int type = (int) view.getTag(R.id.tag_type_view);
        recycler.put(view, type);
    }

    interface Adapter {

        View onCreateViewHolder(int position, View convertView, ViewGroup parent);

        View onBinderViewHolder(int position, View convertView, ViewGroup parent);

        int getItemViewType(int row);// Item的类型

        int getViewTypeCount();// Item的类型数量

        int getCount();

        int getHeight(int index);
    }
}
