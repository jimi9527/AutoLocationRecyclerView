package com.example.dengjx.verticalrecylerviewselector;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Scroller;

/**
 * Created by dengjx on 2017/12/26.
 */

public class RulerViewScroller {
    //滚动的时间
    private static final int SCROLLING_DURATION = 400;
    // 用于滚动的最小增量
    private static final  int MIN_DELTA_FOR_SCROLLING = 1;
    private ScrollingListener listener;
    private Context context;

    private GestureDetector gestureDetector;
    private Scroller scroller;
    private int lastScrollX;
    private float lastTouchedX;
    private boolean isScrollingPerformed;

    private final int MESSAGE_SCROLL = 0;
    private final int MESSAGE_JUSTIFY = 1;

    public RulerViewScroller(Context context , ScrollingListener listener){
        this.context = context;
        this.listener = listener;
        gestureDetector = new GestureDetector(context,gestureListener);
        scroller = new Scroller(context);
        scroller.setFriction(0.05f);
    }

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            lastScrollX = 0;
            scroller.fling(0, lastScrollX, (int) -velocityX, 0, -0x7FFFFFFF, 0x7FFFFFFF, 0, 0);
            setNextMessage(MESSAGE_SCROLL);
            return true;
        }
    };

    // 手势处理
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastTouchedX = event.getX();
                scroller.forceFinished(true);
                clearMessages();
                break;
            case MotionEvent.ACTION_MOVE:
                int distaceX = (int) (event.getX() - lastTouchedX);
                if(distaceX != 0){
                    startScrolling();
                    listener.onScroll(distaceX);
                    lastTouchedX = event.getX();
                }
                break;
        }
        if(!gestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP){
            justify();
        }

        return true;
    }

    // 滚动停止的校验
    private void justify(){
        listener.onJustify();
        setNextMessage(MESSAGE_JUSTIFY);
    }

    // 开始滚动
    private void startScrolling(){
        if(!isScrollingPerformed){
            listener.onStarted();
            isScrollingPerformed = true;
        }
    }
    // 结束滚动
    private void finishScrolling(){
        if(isScrollingPerformed){
            listener.onFinished();
            isScrollingPerformed = false;
        }
    }

    // 发送消息
    private void setNextMessage(int message){
        clearMessages();
        animationHandler.sendEmptyMessage(message);
    }


    // 清除所有消息列表
    public void clearMessages(){
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
        animationHandler.removeMessages(MESSAGE_SCROLL);
    }

    // 动画处理
    private Handler animationHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            scroller.computeScrollOffset();
            int currx = scroller.getCurrX();
            int delta = lastScrollX - currx;
            lastScrollX = currx;
            if(delta != 0){
                listener.onScroll(delta);
            }
            if(Math.abs(currx - scroller.getFinalX()) < MIN_DELTA_FOR_SCROLLING){
                lastScrollX = scroller.getFinalX();
                scroller.forceFinished(true);
            }
            if(!scroller.isFinished()){
                animationHandler.sendEmptyMessage(message.what);
            }
            else if(message.what == MESSAGE_SCROLL){
                justify();
            }else {
                finishScrolling();
            }

            return true;
        }
    });

    public interface  ScrollingListener{
        /**
         * 正在滚动
         */
        void onScroll(int distance);
        /**
         * 滚动开始
         */
        void onStarted();
        /**
         * 滚动结束
         */
        void onFinished();
        /**
         *  滚动停止校验
         */
        void onJustify();

    }
}
