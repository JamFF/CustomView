package com.ff.view.svg;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

public class ProvinceItem {

    private Path mPath;

    private int mColor;// 绘制颜色

    public ProvinceItem(Path path, int color) {
        mPath = path;
        mColor = color;
    }

    /**
     * 绘制
     *
     * @param canvas   画布
     * @param paint    画笔
     * @param isSelect 手指是否选中
     */
    public void drawItem(Canvas canvas, Paint paint, boolean isSelect) {
        if (isSelect) {
            // 绘制内部的颜色
            paint.clearShadowLayer();
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mColor);
            canvas.drawPath(mPath, paint);

            // 绘制边界
            paint.setStyle(Paint.Style.STROKE);
            int strokeColor = 0xFFD0E8F4;
            paint.setColor(strokeColor);
            canvas.drawPath(mPath, paint);
        } else {
            // 绘制内部的颜色
            paint.setStrokeWidth(2);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(8, 0, 0, 0xffffff);
            canvas.drawPath(mPath, paint);

            // 绘制边界
            paint.clearShadowLayer();
            paint.setColor(mColor);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(2);
            canvas.drawPath(mPath, paint);
        }
    }

    /**
     * 判断是否为选中的省份
     *
     * @param x x坐标
     * @param y y坐标
     */
    public boolean isTouch(float x, float y) {
        RectF rectF = new RectF();
        // 得到可装下path的最小矩形
        mPath.computeBounds(rectF, true);
        Region region = new Region();
        // 设置一个path的区域与一个矩形的交集产生的连续性区域
        region.setPath(mPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        // 区域中是否包含该坐标
        return region.contains((int) x, (int) y);
    }
}
