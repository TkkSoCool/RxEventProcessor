package com.tkk.compiler;

import com.squareup.javapoet.ClassName;

/**
 * Created  on 2018/1/26
 *
 * @author 唐开阔
 * @describe 保存类型数据
 */

public class ClassTypeUtil {
    public static final ClassName FLOWABLE = ClassName.get("io.reactivex.processors", "FlowableProcessor");
    public static final ClassName BINDER = ClassName.get("com.tkk.api", "Binder");
    public static final ClassName UNBINDER = ClassName.get("com.tkk.api", "Unbinder");

}
