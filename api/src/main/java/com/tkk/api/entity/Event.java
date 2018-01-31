package com.tkk.api.entity;

public class Event {
    private final String tag;
    private final Object[] data;

    public String getTag() {
        return tag;
    }

    public Object[] getData() {
        return data;
    }

    public Event(String tag, Object[] data) {

        this.tag = tag;
        this.data = data;
    }
}
