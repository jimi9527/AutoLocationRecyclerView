package com.example.dengjx.verticalrecylerviewselector;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by dengjx on 2017/12/26.
 */

public class RulerActivity extends Activity {
    RecyclerView mRulerView;
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
        mRulerView.smoothScrollToPosition(1);

    }

}
