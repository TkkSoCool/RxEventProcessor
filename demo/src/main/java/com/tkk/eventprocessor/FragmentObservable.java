package com.tkk.eventprocessor;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkk.api.RxEventProcessor;

/**
 * Created  on 2018/2/5
 *
 * @author 唐开阔
 * @describe
 */

public class FragmentObservable extends Fragment {
    int twoSize,oneSize = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_observable,container,true);
        rootView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxEventProcessor.get().post(RxEventProcessorTag.NO_PAR);
            }
        });
        rootView.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxEventProcessor.get().post(RxEventProcessorTag.ONE_PAR,oneSize);
                oneSize++;
            }
        });
        rootView.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxEventProcessor.get().post(RxEventProcessorTag.TWO_PAR,twoSize,"name");
                twoSize++;
            }
        });
        return rootView;
    }
}
