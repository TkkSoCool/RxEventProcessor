package com.tkk.eventprocessor;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;


/**
 * Created  on 2017/11/27
 *
 * @author 唐开阔
 * @describe
 */

public class LeakApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
