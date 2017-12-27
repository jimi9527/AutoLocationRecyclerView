package com.example.dengjx.verticalrecylerviewselector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created by dengjx on 2017/12/26.
 */

public class RulerView extends View{

    private static final String TAG ="RulerView";
    //中间标准刻度的颜色
    private int mCenterColor = Color.RED;
    // 中间刻度文字的颜色
    private int mCenterTextColor = Color.BLUE;
    // 中间刻度线的高大小
    private int mCenterHigh = 80;
    // 中间线条的宽度
    private int mCenterWidth = 6;
    private int leftMargin = 50;
    private int mCenterTextHeight ;
    // 刻度的起点X
    private int ruleX;

    // 显示刻度
    private String mCenterText = "5";
    // 偏移值
    private int offset = 5;
    // 平均刻度的值
    private int avagerNum;

    // 刻度的高度
    private int ruleHeigt;
    // 滚动器
    private RulerViewScroller scroller;

    // 是否执行滚动
    private boolean isScrollingPerformed;
    // 屏幕的宽度
    private int mScreenWidth;

    public RulerView(Context context) {
        this(context,null);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.BLUE);
      //  scroller = new RulerViewScroller(context,scrollinglistener);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        Log.d(TAG,"mScreenWidth:"+ mScreenWidth);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidthSize(widthMeasureSpec),measureHeightSize(heightMeasureSpec));
    }

    private int measureHeightSize(int heightMeasureSpec) {
        int result;
        int mode=MeasureSpec.getMode(heightMeasureSpec);
        int size=MeasureSpec.getSize(heightMeasureSpec);
        if(mode==MeasureSpec.EXACTLY){
            result=size;
        }else{
            result = getHeight() + getPaddingTop() + getPaddingBottom();
            if(mode==MeasureSpec.AT_MOST){
                result=Math.min(result,size);
            }
        }
        Log.d(TAG,"result---HEIGHT:"+result);
        return  result;
    }

    private int measureWidthSize(int widthMeasureSpec) {
        int result;
        int mode=MeasureSpec.getMode(widthMeasureSpec);
        int size=MeasureSpec.getSize(widthMeasureSpec);
        if(mode==MeasureSpec.EXACTLY){
            result=size;
        }else{
            result=size + getPaddingLeft() + getPaddingRight();
            if(mode==MeasureSpec.AT_MOST){
                result=Math.min(result,size);
            }
        }
        Log.d(TAG,"result:"+result);
        return  result;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //drawCenterLine(canvas);
        drwacenterBottonLine(canvas);


    }
    // 绘制底部直线
    public void drwacenterBottonLine(Canvas canvas){
        // 底部直线
        Paint paint = new Paint();
        paint.setColor(mCenterColor);
        paint.setStrokeWidth(mCenterWidth / 2);
        canvas.drawLine(leftMargin+mCenterWidth/2 ,getMeasuredHeight() / 2  +mCenterHigh + offset / 2,
                mScreenWidth - leftMargin ,getMeasuredHeight() / 2  + mCenterHigh + offset / 2,paint);


        // 绘制底部数字
        for(int i = 1; i <= 10; i++){
            Paint mTextPaint = new Paint();
            mTextPaint.setTextSize(40);
            mTextPaint.setColor(Color.BLACK);
            Rect mCenterRect = new Rect();

            mTextPaint.getTextBounds(String.valueOf(i),0,1,mCenterRect);
            Log.d(TAG,"mCenterRect.width:"+mCenterRect.width());
            mCenterTextHeight = mCenterRect.height();
            avagerNum = (mScreenWidth-leftMargin *2 ) / 9 ;
            ruleX = leftMargin - mCenterRect.width() / 2;
            Log.d(TAG,"ruleX.width:"+ ruleX);
            canvas.drawText(String.valueOf(i),ruleX + avagerNum * (i-1) ,
                    getMeasuredHeight() / 2 + mCenterHigh + 50   ,mTextPaint);
        }


        // 绘制刻度
        for(int j = 1; j <= 10; j++){
            Paint linepaint = new Paint();
            linepaint.setColor(mCenterColor);
            linepaint.setStrokeWidth(mCenterWidth /2 );
            if(j == 1 || j == 10){
                ruleHeigt = 50;
            }else {
                ruleHeigt = 30;
            }

            canvas.drawLine(leftMargin+mCenterWidth/2 + avagerNum * ( j - 1) ,
                    getMeasuredHeight() / 2  + mCenterHigh + offset / 2 - ruleHeigt,
                    leftMargin+mCenterWidth/2  + avagerNum *(j - 1) ,
                    getMeasuredHeight() / 2  + mCenterHigh + offset / 2 + mCenterWidth /2  ,linepaint);
        }

    }



    // 绘制中间的线和数字
    public void drawCenterLine(Canvas canvas){
        Paint mTextPaint = new Paint();
        mTextPaint.setTextSize(50);
        mTextPaint.setColor(mCenterTextColor);
        Rect mCenterRect = new Rect();

        mTextPaint.getTextBounds(mCenterText,0,1,mCenterRect);
        Log.d(TAG,"mCenterRect.height:"+mCenterRect.height());
        mCenterTextHeight = mCenterRect.height();
        canvas.drawText(mCenterText,getMeasuredWidth()/2 - mCenterRect.width()/2 ,getMeasuredHeight()/2,mTextPaint);

        Paint paint = new Paint();
        paint.setColor(mCenterColor);
        paint.setStrokeWidth(mCenterWidth);
        canvas.drawLine(getMeasuredWidth()/2 + mCenterWidth/2 ,getMeasuredHeight() / 2 + mCenterRect.height()+offset ,
                getMeasuredWidth()/2 + mCenterWidth / 2 ,getMeasuredHeight() / 2 +
                        mCenterRect.height() + offset + mCenterHigh,paint);

    }

   /* RulerViewScroller.ScrollingListener scrollinglistener = new RulerViewScroller.ScrollingListener() {
        @Override
        public void onScroll(int distance) {

        }

        @Override
        public void onStarted() {
            isScrollingPerformed = true;
            if(null != onWeelListener){
                onWeelListener.onScrollingStarted(RulerView.this);
            }
        }

        @Override
        public void onFinished() {

            if (isScrollingPerformed) {
                //滚动结束
                if (null != onWeelListener) {
                    onWeelListener.onScrollingFinished(RulerView.this);
                }
                isScrollingPerformed = false;
            }
        }

        @Override
        public void onJustify() {

        }
    };

    private OnRulerViewScrollListener onWeelListener;

    public void setOnRulerScrollListener(OnRulerViewScrollListener onWeelListener){
        this.onWeelListener = onWeelListener;
    }

    public interface OnRulerViewScrollListener<T> {
        *//**
         * 当更改选择的时候回调方法
         * @param rulerView 状态更改的view
         * @param oldValue  当前item的旧值
         * @param newValue  当前item的新值
         *//*
        void onChanged(RulerView rulerView, T oldValue, T newValue);

        *//**
         * 滚动启动时调用的回调方法
         * @param rulerView
         *//*
        void onScrollingStarted(RulerView rulerView);

        *//**
         * 滚动结束时调用的回调方法
         * @param rulerView
         *//*
        void onScrollingFinished(RulerView rulerView);

    }*/

}
