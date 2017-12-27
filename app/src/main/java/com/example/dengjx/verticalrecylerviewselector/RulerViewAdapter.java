package com.example.dengjx.verticalrecylerviewselector;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dengjx on 2017/12/27.
 */

public class RulerViewAdapter extends RecyclerView.Adapter {
    private static final String TAG ="RulerViewAdapter";
    private Context context;
    private final static int TYPE_ITEM = 1;
    private final static int TYPE_HEAD_FOOT =2;

    public RulerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 || position == getItemCount() -1){
            return TYPE_HEAD_FOOT;
        }else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEAD_FOOT){
            View view = new View(context);
            int headerFooterWidth = parent.getMeasuredWidth() / 2  ;
            Log.d(TAG,"headerFooterWidth:"+headerFooterWidth);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(headerFooterWidth, 100);
            view.setLayoutParams(params);
            return new HeadViewHodler(view);
        }else {
            RulerView rulerview = new RulerView(context);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    200);
            rulerview.setLayoutParams(params);
            Log.d(TAG,"params.width:"+params.width);
            Log.d(TAG,"params.height:"+params.height);
            return new itemViewHolder(rulerview);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class HeadViewHodler extends RecyclerView.ViewHolder{

        public HeadViewHodler(View itemView) {
            super(itemView);
        }
    }
    class itemViewHolder extends RecyclerView.ViewHolder{

        public itemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
