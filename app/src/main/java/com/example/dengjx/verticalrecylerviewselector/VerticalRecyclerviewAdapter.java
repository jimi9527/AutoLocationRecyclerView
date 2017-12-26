package com.example.dengjx.verticalrecylerviewselector;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.example.dengjx.verticalrecylerviewselector.AutoLocateHorizontalView.IAutoLocateHorizontalView;

/**
 * Created by dengjx on 2017/12/25.
 */

public class VerticalRecyclerviewAdapter extends RecyclerView.Adapter implements
        IAutoLocateHorizontalView{
    private Context context;
    private View itemView;
    private String[] mStringTitle = new String[]{"篮球","网球","足球"};
    private OnItemClick onItemClick;
    public interface OnItemClick{
       void onItemClickListner(int postion);
    }
    public VerticalRecyclerviewAdapter(Context context){
        this.context = context;
    }
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layou_item,null);

        this.itemView = view;
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        itemHolder.mTitle.setText(mStringTitle[position]);
    }

    @Override
    public int getItemCount() {
        return mStringTitle.length;
    }

    @Override
    public View getItemView() {
        return itemView;
    }

    @Override
    public void onViewSelected(boolean isSelected, final int pos, RecyclerView.ViewHolder holder, int itemWidth) {
        ItemViewHolder viewHolder =(ItemViewHolder)holder;
        if(isSelected) {
            viewHolder.mTitle.setTextSize(22);
            viewHolder.mTitle.setTextColor(Color.WHITE);
        }else {
            viewHolder.mTitle.setTextSize(16);
            viewHolder.mTitle.setTextColor(Color.BLACK);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClickListner(pos);
            }
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_text);
        }
    }
}
