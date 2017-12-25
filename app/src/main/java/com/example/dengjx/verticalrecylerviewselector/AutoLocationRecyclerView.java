package com.example.dengjx.verticalrecylerviewselector;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by dengjx on 2017/12/25.
 */

public class AutoLocationRecyclerView extends RecyclerView {
    private static final String TAG = "AutoLocation";
    private Context context;

    //总item 数量
    private int mSum;
    // 滚动的偏移值
    private int deltaX;

    //用户自定义的Adapter
    private RecyclerView.Adapter adpater;
    private WrapperAdapter wrapperAdapter;
    private LinearLayoutManager linearLayoutManager;
    //初始化的位置
    private int initPos;

    // 当前被选中的位置
    private int selection;

    public AutoLocationRecyclerView(Context context) {
        this(context,null);
    }

    public AutoLocationRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if(state == SCROLL_STATE_IDLE){
           int itemWidth = wrapperAdapter.getItemWidth();
           int headFootWidth = wrapperAdapter.getHeadFootWidth();
           if(itemWidth == 0 || headFootWidth == 0){
               return;
           }
            int overLastPosOffest = deltaX % itemWidth;
            if(overLastPosOffest == 0){

            }else if(overLastPosOffest < 0 && Math.abs(overLastPosOffest) <= itemWidth / 2){
                scrollBy(-overLastPosOffest , 0);
            }else if(overLastPosOffest > 0){
                scrollBy((itemWidth - overLastPosOffest),0);
            }

        }

    }
    public void setInitPos(int initPos){
        this.initPos = initPos;
        selection = initPos;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        deltaX = deltaX + dx;
        calculateSelectedPos();
    }

    private void calculateSelectedPos(){
        int itemWidth = wrapperAdapter.getItemWidth();
        selection = deltaX / itemWidth + initPos;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        this.adpater = adapter;
        this.wrapperAdapter = new WrapperAdapter(context,adapter);
        deltaX = 0;
        if(linearLayoutManager == null){
            linearLayoutManager = new LinearLayoutManager(context);
        }
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        super.setLayoutManager(linearLayoutManager);
        super.setAdapter(this.wrapperAdapter);
    }



    class WrapperAdapter extends RecyclerView.Adapter {
        private Context context;
        // 用户自己的适配器
        private RecyclerView.Adapter adapter;
        private final static int HEADER_FOOTER_TYPE = 1;
        // 每个item 的宽度
        private int itemWidth;
        // 顶部和底部的高度
        private  int headandfootWidth;

        public WrapperAdapter(Context context, RecyclerView.Adapter adapter){
            this.context = context;
            this.adapter = adapter;

        }
        @Override
        public int getItemViewType(int position) {
            if((position == 0) || (position == getItemCount() - 1)){
                return HEADER_FOOTER_TYPE;
            }
            return adapter.getItemViewType(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            itemWidth = parent.getMeasuredWidth() / getItemCount();
            if(viewType == HEADER_FOOTER_TYPE){
                View view = new View(context);
                 headandfootWidth = parent.getMeasuredWidth() / 2 -
                        (parent.getMeasuredWidth() / getItemCount()-2) / 2;
                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams
                        (headandfootWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(layoutParams);
                return new WrapperAdapter.headAndFootViewHoder(view);
            }
            RecyclerView.ViewHolder holder = adapter.onCreateViewHolder(parent, viewType);
            // View itemView = ((VerticalRecyclerviewAdapter)adapter).getItemView();
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(position == 0 || position == getItemCount() -1){
                return;
            }
            adapter.onBindViewHolder(holder, position-1);
        }
        public int getItemWidth() {
            return itemWidth;
        }
        public int getHeadFootWidth(){
            return headandfootWidth;
        }
        @Override
        public int getItemCount() {
            return adapter.getItemCount() + 2;
        }
        class headAndFootViewHoder extends RecyclerView.ViewHolder {
            public headAndFootViewHoder(View itemView) {
                super(itemView);
            }
        }

        }
}
