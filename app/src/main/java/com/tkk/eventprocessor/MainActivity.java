package com.tkk.eventprocessor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tkk.api.RxEventProcessor;
import com.tkk.api.annotation.Recipient;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        long l1 =  System.currentTimeMillis();
        RxEventProcessor.get().bind(this);
        long l2 =  System.currentTimeMillis();
        Log.d("tag", ">>>注册耗时---" + (l2-l1));

    }
    
    public void to(View view){
        startActivity(new Intent(this,Main2Activity.class));
    }
    public void toNew(View view){
        startActivity(new Intent(this,MainActivity.class));
    }

    @Recipient(tag = "metood1")
    void metood1(){
        showToast("方法1"+hashCode());
        Log.d(TAG, ">>>metood1---" +hashCode());

    }
    @Recipient(tag = "metood2")
    void metood2(List<Integer> list){
        Log.d(TAG, ">>>metood2---" + list.size()+"  "+ hashCode());
    }    @Recipient(tag = "metood3")
    void metood3(TestData data){
        showToast("方法3" + data.name);

    }
    @Recipient(tag = "metood4")
    void metood4(boolean isHave){
        showToast("方法4" + isHave);

    }

    @Recipient(tag = "metood5")
    void metood5(int i,String s){
        showToast("方法5" + i+"   "+s);

    }
    void showToast(String s){
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
