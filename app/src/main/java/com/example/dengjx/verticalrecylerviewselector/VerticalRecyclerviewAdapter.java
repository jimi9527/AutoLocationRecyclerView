package com.example.dengjx.verticalrecylerviewselector;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

/**
 * Created by dengjx on 2017/12/25.
 */

public class VerticalRecyclerviewAdapter extends RecyclerView.Adapter {
    private Context context;
    private String[] mStringTitle = new String[]{"篮球","网球","足球"};
    public VerticalRecyclerviewAdapter(Context context){
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(getItemView(parent));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        itemHolder.mTitle.setText(mStringTitle[position]);
    }
    public View getItemView(ViewGroup parent){
        View view = LayoutInflater.from(context).inflate(R.layout.layou_item,null);
        int headandfootWidth = parent.getMeasuredWidth() / getItemCount();
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams
                (headandfootWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
        return view;
    }

    @Override
    public int getItemCount() {
        return mStringTitle.length;
    }
    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_text);
        }
    }
}
