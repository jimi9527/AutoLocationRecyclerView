package com.example.dengjx.verticalrecylerviewselector;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by dengjx on 2017/12/29.
 */

public class ExpandActivity extends Activity{
    ListView mListView;
    ArrayList<StationImage> mStationImages = new ArrayList<StationImage>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);
        mListView = findViewById(R.id.listview);
        for(int i = 0; i < 6; i++){
                StationImage stationImage = new StationImage();
                stationImage.setName("大雄"+ i);
                stationImage.setContent("帅帅");
                 mStationImages.add(stationImage);
        }
        FootViewAdapter footViewAdapter = new FootViewAdapter(this,mListView);
        mListView.setAdapter(footViewAdapter);
        footViewAdapter.setAapterData(mStationImages);
    }
}
