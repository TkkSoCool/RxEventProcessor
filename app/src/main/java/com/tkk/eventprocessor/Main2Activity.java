package com.tkk.eventprocessor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tkk.api.RxEventProcessor;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    RxEventProcessor.get().bind(this);

    }

        public void send(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.b1:
                RxEventProcessor.get().post("metood1");
                break;
            case R.id.b2:
                List<Integer> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add(i);
                }
                RxEventProcessor.get().post("metood2",list);

                break;
            case R.id.b3:
                RxEventProcessor.get().post("metood3",new TestData("tkk"));

                break;
            case R.id.b4:
                RxEventProcessor.get().post("metood4",true);

                break;
            case R.id.b5:
                RxEventProcessor.get().post("metood5",1,"2");

                break;
            default:
                return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEventProcessor.get().unBind(this);
    }
}
