package com.ff.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ff.view.R;

import java.util.Arrays;

/**
 * description:
 * author: FF
 * time: 2019-05-04 10:12
 */
public class CarView extends View {

    private static final String TAG = "CarView";

    private int width, height;// 控件的宽高

    private Bitmap carBitmap;// 小车图像
    private Path path;// 圆圈的路径
    private PathMeasure pathMeasure;// 圆圈的路径计算
    private float distanceRatio = 0;// 进度百分比，0-1
    private Paint circlePaint;// 画圆圈的画笔
    private Paint carPaint;// 画小车的画笔
    private Matrix carMatrix;// 设置小车的角度，需要针对bitmap图片操作的矩阵

    private float[] pos;// 记录小车当前坐标
    private float[] tan;// 记录小车当前切点值xy

    public CarView(Context context) {
        this(context, null);
    }

    public CarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 小车
        carBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_car);

        path = new Path();
        // 坐标为0,0，半径为200，Path.Direction.CW为顺时针的圆，在onDraw中通过平移Canvas，放在中心
        path.addCircle(0, 0, 200, Path.Direction.CW);
        // forceClosed为true，即使path未闭合，也视为闭合路径进行测量
        pathMeasure = new PathMeasure(path, false);

        circlePaint = new Paint();
        circlePaint.setStrokeWidth(5);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);// 抗锯齿
        circlePaint.setColor(Color.BLACK);

        carPaint = new Paint();
        carPaint.setColor(Color.DKGRAY);
        carPaint.setStrokeWidth(2);
        carPaint.setStyle(Paint.Style.STROKE);

        carMatrix = new Matrix();

        pos = new float[2];
        tan = new float[2];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged: width = " + w + ", height = " + h);
        width = w;
        height = h;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout: width = " + width + ", height = " + height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.translate(width / 2f, height / 2f);// 移动canvas坐标系到中心

        carMatrix.reset();// 清空上一次的设置的参数

        distanceRatio += 0.006f;// 进度百分比，设置越小，小车走的越慢越细腻
        if (distanceRatio >= 1) {
            distanceRatio = 0;
        }

        float distance = pathMeasure.getLength() * distanceRatio;// 获取距离
        // pos[0]是该点x坐标，pos[1]是该点y坐标，tan[0]是邻边边长，tan[1]是对边边长
        pathMeasure.getPosTan(distance, pos, tan);// 获取pos和tan

        Log.d(TAG, "onDraw: pos = " + Arrays.toString(pos) + ", tan = " + Arrays.toString(tan));

        // atan2返回的是弧度值，并不是角度，也就是从-PI到PI的弧度，一个半圆是180度=弧度PI
        // 所以：1度 = (PI/180)弧度，1弧度 = (180/PI)度
        // 计算小车本身要旋转的角度，就需要用弧度值乘以(180/PI)
        float degree = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);

        // 并不是一定要先旋转再平移，只不过先要旋转再平移，方便找到旋转中心点，如果先进行平移，旋转中心点不好确定，需要考虑到偏移量
        // 设置小车旋转角度，旋转中心的x坐标，旋转中心的y坐标
        carMatrix.postRotate(degree, carBitmap.getWidth() / 2f, carBitmap.getHeight() / 2f);
        // 设置小车的x坐标，y坐标，pos中记录的是小车左上坐标点，为了居中，需要将小车，向左移动宽度的一半，向上移动高度的一半
        carMatrix.postTranslate(pos[0] - carBitmap.getWidth() / 2f, pos[1] - carBitmap.getHeight() / 2f);

        canvas.drawPath(path, circlePaint);
        canvas.drawBitmap(carBitmap, carMatrix, carPaint);
        invalidate();
    }
}
