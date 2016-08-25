package com.banditcat.android.transformer.compiler.handler;

import com.banditcat.android.transformer.compiler.holder.GeneratedClassHolder;
import com.banditcat.android.transformer.compiler.process.ProcessHolder;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;

/**
 * 项目名称：Study
 * 类描述：   设置注解类信息
 * 创建作者：banditcat
 * 创建时间：15/11/10 下午1:19
 * 修改作者：banditcat
 * 修改时间：15/11/10 下午1:19
 * 修改备注：
 */
public class AnnotationHandlers {
    private List<AnnotationHandler<? extends GeneratedClassHolder>> annotationHandlers = new ArrayList<AnnotationHandler<? extends GeneratedClassHolder>>();
    private List<GeneratingAnnotationHandler<? extends GeneratedClassHolder>> generatingAnnotationHandlers = new ArrayList<GeneratingAnnotationHandler<? extends GeneratedClassHolder>>();
    private List<AnnotationHandler<? extends GeneratedClassHolder>> decoratingAnnotationHandlers = new ArrayList<AnnotationHandler<? extends GeneratedClassHolder>>();

    private Messager mMessager;
    /**
     * 注解名字集合
     */
    private Set<String> supportedAnnotationNames;

    public AnnotationHandlers(ProcessingEnvironment processingEnvironment,Messager messager) {

        mMessager = messager;


        add(new MappedClassHandler(processingEnvironment,mMessager));
        add(new FiledHandler(processingEnvironment,mMessager));
        add(new FiledByCollectionHandler(processingEnvironment,mMessager));
        add(new ParseHandler(processingEnvironment,mMessager));
        add(new FiledByClassHandler(processingEnvironment,mMessager));
    }

    public void add(AnnotationHandler<? extends GeneratedClassHolder> annotationHandler) {
        annotationHandlers.add(annotationHandler);
        decoratingAnnotationHandlers.add(annotationHandler);
    }

    public void add(GeneratingAnnotationHandler<? extends GeneratedClassHolder> annotationHandler) {
        annotationHandlers.add(annotationHandler);
        generatingAnnotationHandlers.add(annotationHandler);
    }

    public List<AnnotationHandler<? extends GeneratedClassHolder>> getDecorating() {
        return decoratingAnnotationHandlers;
    }

    public List<GeneratingAnnotationHandler<? extends GeneratedClassHolder>> getGenerating() {
        return generatingAnnotationHandlers;
    }


    public void setProcessHolder(ProcessHolder processHolder) {
        for (AnnotationHandler<?> annotationHandler : annotationHandlers) {
            annotationHandler.setProcessHolder(processHolder);
        }
    }

    public Set<String> getSupportedAnnotationTypes() {

        if (supportedAnnotationNames == null) {
            Set<String> annotationNames = new HashSet<String>();
            for (AnnotationHandler<?> annotationHandler : annotationHandlers) {
                annotationNames.add(annotationHandler.getTarget());
            }
            supportedAnnotationNames = Collections.unmodifiableSet(annotationNames);
        }
        return supportedAnnotationNames;
    }

    public List<AnnotationHandler<? extends GeneratedClassHolder>> getAnnotationHandlers() {
        return annotationHandlers;
    }
}
