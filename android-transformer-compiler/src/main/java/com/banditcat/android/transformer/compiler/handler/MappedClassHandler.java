package com.banditcat.android.transformer.compiler.handler;

import com.banditcat.android.transformer.annotation.MappedClass;
import com.banditcat.android.transformer.compiler.holder.GeneratedClassHolder;
import com.banditcat.android.transformer.compiler.holder.MappedClassHolder;
import com.banditcat.android.transformer.compiler.model.AnnotationElements;
import com.banditcat.android.transformer.compiler.process.ProcessHolder;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JFieldRef;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;


/**
 * 项目名称：Study
 * 类描述：
 * 创建作者：banditcat
 * 创建时间：15/11/11 下午1:22
 * 修改作者：banditcat
 * 修改时间：15/11/11 下午1:22
 * 修改备注：
 */
public class MappedClassHandler extends BaseGeneratingAnnotationHandler<MappedClassHolder> {


    private Messager mMessager;

    public MappedClassHandler(ProcessingEnvironment processingEnvironment, Messager messager) {
        super(MappedClass.class, processingEnvironment);

        mMessager = messager;
    }

    @Override
    public boolean validate(Element element, AnnotationElements validatedElements) {
        return true;
    }

    @Override
    public void process(Element element, MappedClassHolder holder) throws Exception {

    }


    @Override
    public MappedClassHolder createGeneratedClassHolder(ProcessHolder processHolder
            , TypeElement annotatedElement) throws Exception {






        return new MappedClassHolder(processHolder, annotatedElement);
    }
}
