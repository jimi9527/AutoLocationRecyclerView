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
    int curDeltay;

    public RulerRecylercView(Context context) {
        this(context, null);
    }

    boolean isFinish;
    private Context context;
    public RulerRecylercView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        Log.d(TAG, "mScreenWidth:" + mScreenWidth);
        avagerNum = (mScreenWidth - leftMargin * 2) / 9;
        Log.d(TAG, "avagerNum:" + avagerNum);
        curDeltay = mScreenWidth / 2;
    }

    public void setOnReturnNum(onReturnNum onReturnNum) {
        this.onreturnNum = onReturnNum;
    }


    public interface onReturnNum {
        void onNum(int num);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == SCROLL_STATE_IDLE) {

            Log.d(TAG, "delaty:" + deltay);
            mCurNum = ((deltay - leftMargin) / avagerNum );
            Log.d(TAG, "(deltay - leftMargin )/ avagerNum :" + ((deltay - leftMargin) / avagerNum ) * avagerNum);
            int mscroll = ((deltay - leftMargin) / avagerNum ) * avagerNum - deltay + leftMargin + dptopx()/2 ;
            Log.d(TAG, "mscroll:" + mscroll);
            Log.d(TAG, "mCurNum:" + mCurNum);
            //判断是否大于3分之2则刻度直接加1
            if(mCurNum > 0 && mCurNum < 9) {
                if (Math.abs(mscroll) >= (avagerNum * 2 / 3)) {
                    mCurNum = mCurNum + 1;
                    mscroll = avagerNum - Math.abs(mscroll);
                }
            }
            Log.d(TAG, "mCurNum:" + mCurNum);
            onreturnNum.onNum(mCurNum);
            scrollBy(mscroll, 0);
            getAdapter().notifyDataSetChanged();

        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        deltay = deltay + dx;
    }
    // dp转px 4为中间指针的宽度
    public int dptopx(){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (4 * scale + 0.5f);
    }


}
