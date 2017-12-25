package com.example.dengjx.verticalrecylerviewselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    AutoLocationRecyclerView mRecycleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycleView = (AutoLocationRecyclerView) findViewById(R.id.recyclerview);
        VerticalRecyclerviewAdapter verticalRecyclerviewAdapter = new VerticalRecyclerviewAdapter(this);
        if(null != verticalRecyclerviewAdapter){
            mRecycleView.setAdapter(verticalRecyclerviewAdapter);
        }
    }
}
