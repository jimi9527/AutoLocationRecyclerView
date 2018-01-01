package com.example.dengjx.verticalrecylerviewselector;

import android.content.Context;
import android.content.Loader;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by dengjx on 2017/12/29.
 */

public class RulerRecylercView extends RecyclerView {
    private static final String TAG = "RulerRecylercView";
    // 偏移
    private int deltay;
    private RulerView mRulerView;
    private int avagerNum;
    private int leftMargin = 50;
    private int revleftMargin = -50;
    // 屏幕的宽度
    private int mScreenWidth;
    // 当前定位的数字刻度值
    private int mCurNum;
    private onReturnNum onreturnNum;

    public RulerRecylercView(Context context) {
        this(context,null);
    }
    boolean isFinish;
    public RulerRecylercView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        Log.d(TAG,"mScreenWidth:"+ mScreenWidth);
        avagerNum = (mScreenWidth-leftMargin *2 ) / 9 ;
        Log.d(TAG,"avagerNum:"+ avagerNum);

    }
    public void setOnReturnNum(onReturnNum onReturnNum){
        this.onreturnNum = onReturnNum;
    }


    public interface onReturnNum{
        void onNum(int num);
    }

    // 在setAdapter 之后调用
     public void initScoll(){
         scrollBy(5 * avagerNum + leftMargin,0);
         getAdapter().notifyDataSetChanged();
     }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if(state == SCROLL_STATE_DRAGGING){
            isFinish = true;
        }

        if(state == SCROLL_STATE_IDLE ){
            if(isFinish) {
                int curDeltay = deltay;
                Log.d(TAG, "delaty:" + deltay);
                mCurNum = ((deltay - leftMargin )/ avagerNum +1 );
                Log.d(TAG, "(deltay - leftMargin )/ avagerNum :" +((deltay - leftMargin )/ avagerNum +1 ));
                Log.d(TAG, "(deltay - leftMargin )/ avagerNum :" +((deltay - leftMargin )/ avagerNum +1 ) * avagerNum);
                int mscroll = ((deltay - leftMargin )/ avagerNum + 1 ) * avagerNum - curDeltay - leftMargin;
                if(Math.abs(mscroll) > (avagerNum /2)){
                    mCurNum = mCurNum + 1;
                    mscroll = avagerNum - Math.abs(mscroll);
                }
                Log.d(TAG, "mscroll:" + mscroll);
                onreturnNum.onNum(mCurNum);

                scrollBy(mscroll,0);
                 getAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        deltay = deltay + dx;
    }


}
