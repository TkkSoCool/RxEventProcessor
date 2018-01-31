package com.tkk.compiler;

import com.tkk.api.ThreadType;
import com.tkk.api.annotation.Recipient;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;

/**
 * Created  on 2018/1/25
 *
 * @author 唐开阔
 * @describe  注解信息{@link com.tkk.api.annotation.Recipient}
 */

public class RecipientInfo {
    private Name mMethodName;
    private String tag;
    private ThreadType subscribeType;
    private ThreadType observeType;

    public String getTag() {
        return tag;
    }

    public Name getmMethodName() {
        return mMethodName;
    }

    List<? extends VariableElement> parameters;//方法的参数



    public RecipientInfo(Element element) {
        ExecutableElement methodElement = (ExecutableElement) element;
        this.mMethodName = methodElement.getSimpleName();
        Recipient recipient = methodElement.getAnnotation(Recipient.class);
        this.tag = recipient.tag();
        this.subscribeType = recipient.subscribeOn();
        this.observeType = recipient.observeOn();
        this.parameters = methodElement.getParameters();
    }

    public String getParametersStr(){
        if (parameters == null || parameters.size() == 0){
            return "";
        }
        int size = parameters.size();
        if (size == 1){
            String type =  "("+parseBaseType(parameters.get(0))+")";
            return type+"objects[0]";
        }
        String end = "";
        for (int i = 0; i < size; i++) {
            String type =  "("+parseBaseType(parameters.get(i))+")";
            if (i == 0){
                end = end + type+"objects["+i+"]";
            }else {
                end = end + ","+type+"objects["+i+"]";

            }
        }
        return end;
    }

    private String parseBaseType(VariableElement variableElement) {
        TypeKind typeKind = variableElement.asType().getKind();
        switch (typeKind) {
            case BOOLEAN: {
                return Boolean.class.getSimpleName();
            }
            case BYTE: {
                return Byte.class.getSimpleName();
            }
            case SHORT: {
                return Short.class.getSimpleName();
            }
            case INT: {
                return Integer.class.getSimpleName();
            }
            case LONG: {
                return Long.class.getSimpleName();
            }
            case CHAR: {
                return Character.class.getSimpleName();
            }
            case FLOAT: {
                return Float.class.getSimpleName();
            }
            case DOUBLE: {
                return Double.class.getSimpleName();
            }
            default: {
                if (variableElement.asType() instanceof DeclaredType) {
                    return handleGenericTypeVariable(variableElement);
                }
                return variableElement.asType().toString();
            }
        }
    }
    /**
     * 处理泛型,返回不带泛型的类类型,List<User>,直接返回java.util.List.class而不是java.util.List<User>.class
     *
     * @return String
     */
    private String handleGenericTypeVariable(VariableElement variableElement) {
        DeclaredType declaredType = (DeclaredType) variableElement.asType();
        return ((TypeElement) declaredType.asElement()).getQualifiedName().toString();
    }

    public String getSubscribeThreadType(){
        return   getThreadStr(subscribeType);

    }
    public String getObserveThreadType(){
      return   getThreadStr(observeType);
    }
    public String getThreadStr(ThreadType type){
        if (type.equals(ThreadType.MAIN)){
            return "io.reactivex.android.schedulers.AndroidSchedulers.mainThread()";
        }else if (type.equals(ThreadType.IO)){
            return "io.reactivex.schedulers.Schedulers.io()";

        }
        else if (type.equals(ThreadType.NEW)){
            return "io.reactivex.schedulers.Schedulers.newThread()";

        }
        else if (type.equals(ThreadType.COMPUTATION)){
            return "io.reactivex.schedulers.Schedulers.computation()";

        }
        else if (type.equals(ThreadType.TRAMPOLINE)){
            return "io.reactivex.schedulers.Schedulers.trampoline()";

        }
        else if (type.equals(ThreadType.SINGLE)){
            return "io.reactivex.schedulers.Schedulers.single()";

        }
        else {
            return "io.reactivex.android.schedulers.AndroidSchedulers.mainThread()";
        }
    }
}
