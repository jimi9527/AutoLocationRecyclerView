package com.example.dengjx.verticalrecylerviewselector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dengjx on 2017/12/28.
 */

public class RadarView extends View {
    // 五边形的边长个数
    private final static int PALYGON_COUNT = 5;
    // 中心位置的坐标
    private int centerX, centerY;
    // 里面第一个五边形的长度
    private int mRadius = 300;
    // 角度
    private float angle = (float) (Math.PI*2/PALYGON_COUNT);
    //各维度分值
    private double[] data = {2,3,5,4,1};
    //数据最大值
    private float maxValue = 5;
    // 顶点的文字
    private String[] mTtile = new String[]{"用户价值","盈利模式","创始人与团队","市场与竞争","增长模式"};
    // 字体与顶点之前的距离间隔
    private final static int DETALY = 15;
    public RadarView(Context context) {
        this(context,null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        centerX = 500;
        centerY = 500;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        drawPolygon(canvas);
        drawRegion(canvas);
    }

    /**
     * 绘制正多边形 以及顶点的文字
     */
    private void drawPolygon(Canvas canvas){
        Paint  mainPaint = new Paint();
        mainPaint.setColor(Color.parseColor("#b5b5b5"));
        mainPaint.setStrokeWidth(3);
        mainPaint.setAntiAlias(true);
        mainPaint.setStyle(Paint.Style.STROKE);

        Path path = new Path();
        float r = mRadius/(PALYGON_COUNT-1);//r是蜘蛛丝之间的间距
        for(int i=1;i<=PALYGON_COUNT;i++){//中心点不用绘制
            float curR = r*i;//当前半径
            path.reset();
            for(int j=0;j<PALYGON_COUNT;j++){
                if(j==0){
                    float x = (float) (centerX+curR*Math.sin(angle));
                    float y = (float) (centerY-curR*Math.cos(angle));
                    path.moveTo(x,y);
                }else{
                   //根据半径，计算出蜘蛛丝上每个点的坐标
                    float x1 = (float) (centerX+curR*Math.sin(angle/2));
                    float y1 = (float) (centerY+curR*Math.cos(angle/2));
                    path.lineTo(x1,y1);
                    float x2 = (float) (centerX-curR*Math.sin(angle/2));
                    float y2 = (float) (centerY+curR*Math.cos(angle/2));
                    path.lineTo(x2,y2);
                    float x3 = (float) (centerX-curR*Math.sin(angle));
                    float y3 = (float) (centerY-curR*Math.cos(angle));
                    path.lineTo(x3,y3);
                    float x4 = (float) (centerX);
                    float y4 = (float) (centerY-curR);
                    path.lineTo(x4,y4);
                    float x = (float) (centerX+curR*Math.sin(angle));
                    float y = (float) (centerY-curR*Math.cos(angle));
                    path.lineTo(x, y);

                    // 连接顶点到中心的距离
                    if(i == PALYGON_COUNT){
                        canvas.drawLine(centerX,centerY,x,y,mainPaint);
                        canvas.drawLine(centerX,centerY,x1,y1,mainPaint);
                        canvas.drawLine(centerX,centerY,x2,y2,mainPaint);
                        canvas.drawLine(centerX,centerY,x3,y3,mainPaint);
                        canvas.drawLine(centerX,centerY,x4,y4,mainPaint);
                        // 绘制顶点的文字
                        Paint mTextPaint = new Paint();
                        mTextPaint.setTextSize(40);
                        mTextPaint.setColor(Color.BLACK);
                        Rect mCenterRect = new Rect();
                        mTextPaint.getTextBounds(mTtile[0],0,mTtile[0].length(),mCenterRect);
                        canvas.drawText(mTtile[0],x + DETALY , y + mCenterRect.height() / 2 ,mTextPaint);
                        mCenterRect = new Rect();
                        mTextPaint.getTextBounds(mTtile[1],0,mTtile[1].length(),mCenterRect);
                        canvas.drawText(mTtile[1],x1 - mCenterRect.width() / 2 , y1 + DETALY + mCenterRect.height() ,mTextPaint);
                        mCenterRect = new Rect();
                        mTextPaint.getTextBounds(mTtile[2],0,mTtile[2].length(),mCenterRect);
                        canvas.drawText(mTtile[2],x2 - mCenterRect.width() / 2 , y2 + DETALY + mCenterRect.height() ,mTextPaint);
                        mCenterRect = new Rect();
                        mTextPaint.getTextBounds(mTtile[3],0,mTtile[3].length(),mCenterRect);
                        canvas.drawText(mTtile[3],x3 - DETALY - mCenterRect.width() , y3 + mCenterRect.height() / 2 ,mTextPaint);
                        mCenterRect = new Rect();
                        mTextPaint.getTextBounds(mTtile[4],0,mTtile[4].length(),mCenterRect);
                        canvas.drawText(mTtile[4],x4 - mCenterRect.width() / 2 , y4 - DETALY ,mTextPaint);
                    }
                }
            }
            path.close();//闭合路径
            canvas.drawPath(path, mainPaint);
        }

    }

    /**
     * 绘制区域
     * @param canvas
     */
    private void drawRegion(Canvas canvas){

       Paint valuePaint=new Paint();
        valuePaint.setColor(Color.parseColor("#f39700"));
        valuePaint.setStrokeWidth(3);
        valuePaint.setAntiAlias(true);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);


        Path path = new Path();
        valuePaint.setAlpha(255);
        float r = mRadius/(PALYGON_COUNT-1);//r是蜘蛛丝之间的间距
        double percent1;
        if(data[0] != maxValue){
            percent1 = data[0]%maxValue;
        }else{
            percent1 = maxValue;
        }
        float x1 = (float) (centerX+r*percent1*Math.sin(angle));
        float y1= (float) (centerY-r*percent1*Math.cos(angle));
         //绘制小圆点
        path.moveTo(x1, y1);
        canvas.drawCircle(x1,y1,5,valuePaint);
        double percent2;
        if(data[1] != maxValue){
            percent2 = data[1]%maxValue;
        }else{
            percent2 = maxValue;
        }
        float x2 = (float) (centerX+r*percent2*Math.sin(angle/2));
        float y2= (float) (centerY+r*percent2*Math.cos(angle/2));
        //绘制小圆点
        path.lineTo(x2, y2);
        canvas.drawCircle(x2,y2,5,valuePaint);
        double percent3;
        if(data[2] != maxValue){
            percent3 = data[2]%maxValue;
        }else{
            percent3 = maxValue;
        }
        float x3 = (float) (centerX-r*percent3*Math.sin(angle/2));
        float y3= (float) (centerY+r*percent3*Math.cos(angle/2));
        //绘制小圆点
        path.lineTo(x3, y3);
        canvas.drawCircle(x3,y3,5,valuePaint);
        double percent4;
        if(data[3] != maxValue){
            percent4 = data[3]%maxValue;
        }else{
            percent4 = maxValue;
        }
        float x4 = (float) (centerX-r*percent4*Math.sin(angle));
        float y4= (float) (centerY-r*percent4*Math.cos(angle));
        //绘制小圆点
        path.lineTo(x4, y4);
        canvas.drawCircle(x4,y4,5,valuePaint);
        double percent5;
        if(data[4] != maxValue){
            percent5 = data[4]%maxValue;
        }else{
            percent5 = maxValue;
        }
        float x5 = (float) (centerX);
        float y5= (float) (centerY-r*percent5);
        //绘制小圆点
        path.lineTo(x5, y5);
        canvas.drawCircle(x5,y5,5,valuePaint);
        path.lineTo(x1, y1);
        path.close();
        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, valuePaint);
        valuePaint.setAlpha(90);
         //绘制填充区域
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, valuePaint);
    }
}
