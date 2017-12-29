package com.example.dengjx.verticalrecylerviewselector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dengjx on 2017/12/29.
 */

public abstract class BaseFootViewAdapter<E> extends BaseAdapter {
    public static final int DEFAULT_SHOW_COUNT = 3;
    protected Context mContext;
    protected ListView mListView;
    protected LayoutInflater inflater;
    protected LinearLayout headView;
    protected TextView mBtnMore;
    protected ArrayList<E> mShowObjects = new ArrayList<>();
    protected ArrayList<E> mAllObjects = new ArrayList<>();
    protected boolean shrink = true;

    private  BaseFootViewAdapter(){
    }
    public BaseFootViewAdapter(Context context, ListView listView){
        this.mListView = listView;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        headView = (LinearLayout) inflater.inflate(R.layout.iv_footer_button,null);
        mBtnMore = headView.findViewById(R.id.text_show);
        mBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeShow();
            }
        });
        mListView.addFooterView(headView,null,false);
    }
    public void setAapterData(ArrayList<E> mAllObjects){
        this.mAllObjects = mAllObjects;
        mShowObjects.clear();
        if(mAllObjects != null){
            if(mAllObjects.size() <= DEFAULT_SHOW_COUNT){
                headView.setVisibility(View.GONE);
                mShowObjects.addAll(mAllObjects);
            }else {
                headView.setVisibility(View.VISIBLE);
                for(int i = 0; i < DEFAULT_SHOW_COUNT; i++){
                    mShowObjects.add(mAllObjects.get(i));
                }
            }
        }
        notifyDataSetChanged();
        setListViewHeightBaseOnChildern(mListView);
    }

    @Override
    public int getCount() {
        int showCount = 0;
        if(mShowObjects != null){
            showCount = mShowObjects.size();
        }
        return showCount;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public E getItem(int i) {
        E object = null;
        if(mShowObjects != null){
            object = mShowObjects.get(i);
        }
        return object;
    }

    private void changeShow() {
        if(headView.getVisibility() == View.GONE){
            headView.setVisibility(View.VISIBLE);
        }
        mShowObjects.clear();
        if(shrink){
            shrink = false;
            mShowObjects.addAll(mAllObjects);
            mBtnMore.setText("收起");
        }else {
            shrink = true;
            for(int i = 0; i < DEFAULT_SHOW_COUNT; i++){
                mShowObjects.add(mAllObjects.get(i));
            }
             mBtnMore.setText("查看所有的评论");
        }
       notifyDataSetChanged();
        setListViewHeightBaseOnChildern(mListView);
    }


    public void setListViewHeightBaseOnChildern(ListView listView) {
        if(listView == null){
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null){
            return;
        }
        int totalHeight = 0;
        for(int i = 0; i < listAdapter.getCount(); i++){
            View listItem = listAdapter.getView(i,null,listView);
            listItem.measure(0,0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() -1));
        listView.setLayoutParams(params);

    }
}
