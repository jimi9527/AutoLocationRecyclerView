package com.example.dengjx.verticalrecylerviewselector;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by dengjx on 2017/12/29.
 */

public class RulerRecylercView extends RecyclerView {
    // 偏移
    private int deltay;
    private RulerView mRulerView;
    public RulerRecylercView(Context context) {
        this(context,null);
    }

    public RulerRecylercView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void getRulerView(RulerView rulerView){
        this.mRulerView = mRulerView;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        deltay = deltay + dx;
    }
}
