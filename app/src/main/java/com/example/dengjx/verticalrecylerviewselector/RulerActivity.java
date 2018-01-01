package com.example.dengjx.verticalrecylerviewselector;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import static com.example.dengjx.verticalrecylerviewselector.RulerRecylercView.*;

/**
 * Created by dengjx on 2017/12/26.
 */

public class RulerActivity extends Activity implements onReturnNum {
    private static final String TAG = "RulerActivity" ;
    RulerRecylercView mRulerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ruleract);
        mRulerView = findViewById(R.id.rulerview);
        RulerViewAdapter rulerViewAdapter = new RulerViewAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRulerView.setLayoutManager(linearLayoutManager);
        mRulerView.setAdapter(rulerViewAdapter);
        mRulerView.setOnReturnNum(this);
        mRulerView.initScoll();
        mRulerView.smoothScrollToPosition(1);

    }

    @Override
    public void onNum(int num) {
        Log.d(TAG, "NUM :" +num);
    }
}
