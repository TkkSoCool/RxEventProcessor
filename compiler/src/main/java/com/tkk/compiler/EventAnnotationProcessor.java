package com.tkk.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.tkk.api.annotation.Recipient;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Created  on 2018/1/24
 *
 * @author 唐开阔
 * @describe 注解处理器
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class EventAnnotationProcessor extends AbstractProcessor {
    private Types mTypeUtils;
    /**
     * 元素工具类
     * 包：PackageElement
     * 类：TypeElement
     * 参数：VariableElement
     * 方法：ExecuteableElement
     */
    private Elements mElementUtils;
    private Filer mFiler;
    private Messager mMessager;
    //一个有对应注解的类对应一个 生成文件
    Map<TypeElement, SendEnevtClassInfo> targetClassMap = new LinkedHashMap<>();
    /**
     * 初始化一些工具类
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //初始化我们需要的基础工具
        mTypeUtils = processingEnv.getTypeUtils();
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }

    /**
     * 处理注解信息
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(Recipient.class)) {
            parseRecipient(annotatedElement);
        }
        //遍历所有的类信息
        for (SendEnevtClassInfo classInfo:targetClassMap.values()){
            try {
                classInfo.generateFinder().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void parseRecipient(Element element) {
        /**
         * 判断合法性，不合法直接返回
         */
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
            return;
        }
        //包含一个生成类中所有信息
        SendEnevtClassInfo classInfo =getSendEnevtClassInfo(enclosingElement);
        RecipientInfo recipientInfo = new RecipientInfo(element);
        classInfo.addRecipientInfoMethod(recipientInfo);
    }

    /**
     * 获取类文件
     * @param element
     * @return
     */
    private SendEnevtClassInfo getSendEnevtClassInfo(TypeElement element){
        SendEnevtClassInfo classInfo = targetClassMap.get(element);
        if (classInfo == null) {
            classInfo = new SendEnevtClassInfo(element,mElementUtils);
            targetClassMap.put(element, classInfo);
        }
        return classInfo;
    }



    /**
         * 指定该注解处理器需要处理的注解类型
         * @return 需要处理的注解类型名的集合Set<String>
         */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(Recipient.class.getCanonicalName());
        return types;
    }

}
