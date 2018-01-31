package com.tkk.api;


import com.tkk.api.entity.Event;

import io.reactivex.processors.FlowableProcessor;

/**
 * Created  on 2018/1/24
 *
 * @author 唐开阔
 * @describe 用于绑定
 */

public interface Binder<T> {
    /**
     * 绑定事件，注册观察者
     * @param mFlowableProcessor 事件发射器
     * @param target 绑定者
     */
    void  bind(T target,FlowableProcessor<Event> mFlowableProcessor);
}
