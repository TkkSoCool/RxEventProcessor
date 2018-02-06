package com.tkk.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.tkk.api.Unbinder;
import com.tkk.api.entity.Event;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created  on 2018/1/25
 *
 * @author 唐开阔
 * @describe 生成类信息
 * javapoet 使用介绍
 * $L 代表一个常量
 * $S 代表一个字符串
 * $T 代表一个类型，并会自动导入包名
 * $N 代表一个已经定义的变量
 */

public class SendEnevtClassInfo {
    public TypeElement mClassElement; //使用注解的类元素
    private Elements mElementUtils; //元素相关的辅助类
    public List<RecipientInfo> mRecipientInfoMethods = new LinkedList<>();//方法

    public SendEnevtClassInfo(TypeElement mClassElement, Elements mElementUtils) {
        this.mClassElement = mClassElement;
        this.mElementUtils = mElementUtils;
    }

    public void addRecipientInfoMethod(RecipientInfo method) {
        mRecipientInfoMethods.add(method);
    }

    public JavaFile generateFinder() {
        String packageName = getPackageName(mClassElement);
        String className = getClassName(mClassElement, packageName);
        ClassName bindingClassName = ClassName.get(packageName, className);
        //成员变量    private CompositeDisposable mCompositeDisposable;
        FieldSpec mCompositeDisposable = FieldSpec.builder(CompositeDisposable.class, "mCompositeDisposable")
                .addModifiers(Modifier.PRIVATE)
                .initializer("new CompositeDisposable()")
                .build();


        MethodSpec.Builder bindMethodBuilder = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "target", Modifier.FINAL)
                .addParameter(ParameterizedTypeName.get(ClassTypeUtil.FLOWABLE, TypeName.get(com.tkk.api.entity.Event.class)), "flowable");
        MethodSpec.Builder unBindMethodBuilder = MethodSpec.methodBuilder("unBind")
             .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)

                .addStatement("mCompositeDisposable.clear()")
        ;

        //判断注解和发送的事件里面的tag是否一样
        for (int i = 0; i < mRecipientInfoMethods.size(); i++) {
            RecipientInfo recipientInfo = mRecipientInfoMethods.get(i);
            for (int j = 0; j < recipientInfo.parameters.size(); j++) {
            }
            bindMethodBuilder.addStatement("mCompositeDisposable.add(flowable"
                            + ".subscribeOn(" + recipientInfo.getSubscribeThreadType() + ")\n"
                            + ".observeOn(" + recipientInfo.getObserveThreadType() + ")\n"
                            + ".filter(new $T<$T>() {\n" +
                            "            @Override\n" +
                            "            public boolean test(Event event) throws $T {\n" +
                            "                return event.getTag().equals(" + "\"" + recipientInfo.getTag() + "\"" + ");\n" +
                            "            }\n" +
                            "        })\n"
                            + ".subscribeWith(new $T<Event>() {\n" +
                            "                    @Override\n" +
                            "                    public void onNext(Event event) {\n" +
                            "                   Object[] objects = event.getData();" +
                            "\n" + "target." + recipientInfo.getmMethodName() + "(" + recipientInfo.getParametersStr() + ");" +

                            "                    }\n" +
                            "\n" +
                            "                    @Override\n" +
                            "                    public void onError($T t) {\n" +
                            "\n  t.printStackTrace();" +
                            "                    }\n" +
                            "\n" +
                            "                    @Override\n" +
                            "                    public void onComplete() {\n" +
                            "\n" +
                            "                    }\n" +
                            "                })\n" +
                            ")", Predicate.class,
                    Event.class, Exception.class, ResourceSubscriber.class, Throwable.class);
        }

        TypeSpec finderClass = TypeSpec.classBuilder(bindingClassName.simpleName() + "_SUBSCRIBE_INFO")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassTypeUtil.BINDER, TypeName.get(mClassElement.asType())))
                .addField(mCompositeDisposable)
                .addSuperinterface(Unbinder.class)
                .addMethod(bindMethodBuilder.build())
                .addMethod(unBindMethodBuilder.build())
                .build();

        return JavaFile.builder(packageName, finderClass).build();
    }


    private String getPackageName(TypeElement type) {
        return mElementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }
}
