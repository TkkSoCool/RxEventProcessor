package com.tkk.eventprocessor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tkk.api.RxEventProcessor;

public class DemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        RxEventProcessor.get().bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEventProcessor.get().unBind(this);
    }
}
