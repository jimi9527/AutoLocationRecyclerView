package com.example.dengjx.verticalrecylerviewselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements VerticalRecyclerviewAdapter.OnItemClick {
    AutoLocateHorizontalView mRecycleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycleView = (AutoLocateHorizontalView) findViewById(R.id.recyclerview);
        VerticalRecyclerviewAdapter verticalRecyclerviewAdapter = new VerticalRecyclerviewAdapter(this);
        verticalRecyclerviewAdapter.setOnItemClick(this);
        if(null != verticalRecyclerviewAdapter){
            mRecycleView.setInitPos(1);
            mRecycleView.setAdapter(verticalRecyclerviewAdapter);
        }
    }

    @Override
    public void onItemClickListner(int postion) {
            mRecycleView.updateData(postion);
    }
}
