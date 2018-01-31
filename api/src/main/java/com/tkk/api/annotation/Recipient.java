package com.tkk.api.annotation;

import com.tkk.api.ThreadType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created  on 2018/1/25
 *
 * @author 唐开阔
 * @describe
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Recipient {
    String tag() ;


    ThreadType subscribeOn() default ThreadType.MAIN;

    ThreadType observeOn() default ThreadType.MAIN;



}
