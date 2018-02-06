package com.tkk.eventprocessor;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkk.api.RxEventProcessor;
import com.tkk.api.ThreadType;
import com.tkk.api.annotation.Recipient;

/**
 * Created  on 2018/2/5
 *
 * @author 唐开阔
 * @describe
 */

public class FragmentObserver extends Fragment {
    TextView textView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxEventProcessor.get().bind(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_observer,container,true);
        textView = rootView.findViewById(R.id.tv);
        return rootView;
    }

    /**
     * 接收无参数的事件
     */
    @Recipient(tag = RxEventProcessorTag.NO_PAR,observeOn = ThreadType.MAIN,subscribeOn = ThreadType.NEW)
    public void onSendEventNoPar(){
        textView.append("\n onSendEventNoPar ");
    }

    /**
     * 接收一个参数的事件
     */
    @Recipient(tag = RxEventProcessorTag.ONE_PAR,observeOn = ThreadType.MAIN,subscribeOn = ThreadType.NEW)
    public void onSendEventOnePar(int size){
        textView.append("\n onSendEventOnePar size = " + size);
    }
    /**
     * 接收两个个参数的事件
     */
    @Recipient(tag = RxEventProcessorTag.TWO_PAR,observeOn = ThreadType.MAIN,subscribeOn = ThreadType.NEW)
    public void onSendEventTwoPar(int size,String name){
        textView.append("\n onSendEventTwoPar size = " + size + "  name = " + name);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        RxEventProcessor.get().unBind(this);
    }
}
