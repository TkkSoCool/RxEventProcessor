package com.tkk.api;

import com.tkk.api.entity.Event;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created  on 2018/1/24
 * @author 唐开阔
 * @describe 事件处理器
 *
 */

public class RxEventProcessor {
    static volatile RxEventProcessor sInstance;
    private FlowableProcessor<Event> mProcessor;
    private Map<Integer, Unbinder> subscriberUnbinds = new HashMap<>();

    private RxEventProcessor() {
        mProcessor = PublishProcessor.create();
        mProcessor = mProcessor.toSerialized();
    }

    public synchronized static RxEventProcessor get() {
        if (null == sInstance) {
            sInstance = new RxEventProcessor();
        }
        return sInstance;
    }

    public void post(String tag, Object... objects) {
        Event event = new Event(tag, objects);
        mProcessor.onNext(event);
    }

    public void bind(Object target) {
        int subscriberId = target.hashCode();
        //判断是否已经注册
        if (!subscriberUnbinds.containsKey(subscriberId)) {
            String className = target.getClass().getName();
            try {
                Class<?> finderClass = Class.forName(className + "$$SEND_ENEVT");
                Object targetEventProcessor = finderClass.newInstance();
                Binder binder = (Binder) targetEventProcessor;
                Unbinder unbinder = (Unbinder) targetEventProcessor;
                binder.bind(target, mProcessor);
                subscriberUnbinds.put(subscriberId, unbinder);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void unBind(Object target) {
        int subscriberId = target.hashCode();
        if (subscriberUnbinds.containsKey(subscriberId)){
            subscriberUnbinds.get(subscriberId).unBind();
        }
    }
}
