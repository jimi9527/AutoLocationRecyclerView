package com.example.dengjx.verticalrecylerviewselector;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by dengjx on 2017/12/27.
 */

public class RulerRecyclerView extends RecyclerView {
    //偏移的距离
    private int delta;
    public RulerRecyclerView(Context context) {
        this(context,null);
    }

    public RulerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
         delta = delta + dx;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        // 手抬起的动作
        if(state == SCROLL_STATE_IDLE){

        }

    }
}
