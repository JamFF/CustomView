package com.ff.view.svg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ff.view.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MapView extends View {

    private static final String TAG = "MapView";
    private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFFFFFFFF};// 省份颜色
    private Context mContext;
    private List<ProvinceItem> itemList;// 省份集合
    private Paint paint;
    private ProvinceItem selectProvinceItem;// 手指选中的省份
    private RectF totalRect;// 可装下SVG的最小矩形
    private float scale = 1.0f;// SVG缩放系数

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        itemList = new CopyOnWriteArrayList<>();// 涉及到自线程修改
        loadThread.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (totalRect != null) {
            scale = width / totalRect.width();// 获取缩放系数
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 在这里完成绘制
        if (itemList != null) {
            canvas.scale(scale, scale);// 对画布进行缩放
            for (ProvinceItem provinceItem : itemList) {
                provinceItem.drawItem(canvas, paint, provinceItem == selectProvinceItem);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 触摸点也需要进行缩放
        handleTouch(event.getX() / scale, event.getY() / scale);
        return super.onTouchEvent(event);
    }

    private Thread loadThread = new Thread() {
        @Override
        public void run() {
            final InputStream inputStream = mContext.getResources().openRawResource(R.raw.china);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();// 取得DocumentBuilderFactory实例
            DocumentBuilder builder; // 从factory获取DocumentBuilder实例
            try {
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse(inputStream);   //解析输入流 得到Document实例
                Element rootElement = doc.getDocumentElement();
                NodeList items = rootElement.getElementsByTagName("path");// 取到path标签
                float left = -1;
                float right = -1;
                float top = -1;
                float bottom = -1;
                for (int i = 0; i < items.getLength(); i++) {
                    Element element = (Element) items.item(i);
                    String pathData = element.getAttribute("android:pathData");// 取到
                    @SuppressLint("RestrictedApi") Path path = PathParser.createPathFromPathData(pathData);
                    ProvinceItem provinceItem = new ProvinceItem(path, colorArray[i % 4]);
                    RectF rect = new RectF();
                    path.computeBounds(rect, true);// 将path转换为矩形
                    // 找到整个SVG的位置
                    left = left == -1 ? rect.left : Math.min(left, rect.left);
                    right = right == -1 ? rect.right : Math.max(right, rect.right);
                    top = top == -1 ? rect.top : Math.min(top, rect.top);
                    bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);
                    itemList.add(provinceItem);
                }
                // 得到可装下SVG的最小矩形
                totalRect = new RectF(left, top, right, bottom);
                // 刷新界面
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestLayout();
                        invalidate();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 根据点击坐标，找到选中的省份，然后刷新布局
     *
     * @param x x坐标
     * @param y y坐标
     */
    @UiThread
    private void handleTouch(float x, float y) {
        if (itemList == null) {
            return;
        }
        for (ProvinceItem provinceItem : itemList) {
            if (provinceItem != null && provinceItem.isTouch(x, y)) {
                selectProvinceItem = provinceItem;
                invalidate();
            }
        }
    }
}
