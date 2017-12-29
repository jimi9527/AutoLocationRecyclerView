package com.example.dengjx.verticalrecylerviewselector;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by dengjx on 2017/12/29.
 */

public class FootViewAdapter extends BaseFootViewAdapter<StationImage>{

    public FootViewAdapter(Context context, ListView listView) {
        super(context, listView);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemHoder itemHoder = null;
        if(view == null){
            itemHoder = new ItemHoder();
            view = inflater.inflate(R.layout.item_list,viewGroup,false);
            itemHoder.mName = view.findViewById(R.id.name);
            itemHoder.mContent = view.findViewById(R.id.content);
            view.setTag(itemHoder);
        }else {
            itemHoder = (ItemHoder) view.getTag();
        }
        StationImage stationImage = getItem(i);
        Log.d("test","stationImage.getName():" +stationImage.getName());
        Log.d("test","itemHoder.mName:"+itemHoder.mName);
        itemHoder.mName.setText(stationImage.getName());
        itemHoder.mContent.setText(stationImage.getContent());
        return view;
    }

    class ItemHoder{
        TextView mName;
        TextView mContent;
    }
}
