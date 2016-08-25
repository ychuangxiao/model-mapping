package com.banditcat.android.transformer.compiler.handler;

import com.banditcat.android.transformer.annotation.Filed;
import com.banditcat.android.transformer.annotation.Parse;
import com.banditcat.android.transformer.compiler.helper.ModelConstants;
import com.banditcat.android.transformer.compiler.holder.EComponentWithViewSupportHolder;
import com.banditcat.android.transformer.compiler.model.AnnotationElements;
import com.sun.codemodel.JExpression;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

import static com.sun.codemodel.JExpr.ref;


/**
 * 项目名称：Study
 * 类描述：
 * 创建作者：banditcat
 * 创建时间：15/11/11 下午1:22
 * 修改作者：banditcat
 * 修改时间：15/11/11 下午1:22
 * 修改备注：
 */
public class FiledHandler extends BaseAnnotationHandler<EComponentWithViewSupportHolder> {

    private Messager mMessager;

    public FiledHandler(ProcessingEnvironment processingEnvironment, Messager messager) {
        super(Filed.class, processingEnvironment);

        mMessager = messager;
    }

    @Override
    public boolean validate(Element element, AnnotationElements validatedElements) {
        return false;
    }

    @Override
    public void process(Element element, EComponentWithViewSupportHolder holder) throws Exception {
        mElement = element;
        mGeneratedClassHolder = holder;








        createMethodEx();
    }


    EComponentWithViewSupportHolder mGeneratedClassHolder;
    Element mElement;


    void createMethodEx() throws ClassNotFoundException {



        AnnotationMirror parseAnnotationMirror = codeModelHelper.getAnnotationMirror(mElement, Parse.class);
        AnnotationValue originToDestinationWithValue = codeModelHelper.getAnnotationValue(parseAnnotationMirror, ModelConstants.GENERATION_ORIGIN_TO_DESTINATION_WITH);
        AnnotationValue destinationToOriginWithValue = codeModelHelper.getAnnotationValue(parseAnnotationMirror, ModelConstants.GENERATION_DESTINATION_TO_ORIGIN_WITH);


        //如果有字段转换类型,直接跳出去
        if(null != originToDestinationWithValue && null !=destinationToOriginWithValue)
        {
            return;
        }


        AnnotationMirror annotationMirror = codeModelHelper.getAnnotationMirror(mElement, Filed.class);


        AnnotationValue annotationValue = codeModelHelper.getAnnotationValue(annotationMirror, ModelConstants.GENERATION_TOFIELD);


        String fieldName = mElement.getSimpleName().toString();

        String linkFieldName;


        if (null != annotationValue) {

            linkFieldName = annotationValue.getValue().toString();
            mMessager.printMessage(Diagnostic.Kind.NOTE,
                    "annotationValue: " +
                            annotationValue.getValue().toString() + "   ..... ");
        } else {
            linkFieldName = fieldName;
        }


        //给方法添加字段信息


        //首字母大写
        fieldName = codeModelHelper.toUpperCamelCase(fieldName);
        linkFieldName = codeModelHelper.toUpperCamelCase(linkFieldName);

        //映射类到被映射类
        JExpression jExpression = mGeneratedClassHolder.getDataJFieldRef().invoke(String.format("%s%s", ModelConstants.GENERATION_GET_PREFIX,fieldName));

        mGeneratedClassHolder.getMappedJBlock()
                .invoke(mGeneratedClassHolder.getResultJFieldRef(), String.format("%s%s",ModelConstants.GENERATION_SET_PREFIX, linkFieldName))
                .arg(jExpression);


        //被映射类到映射类
        jExpression = mGeneratedClassHolder.getDataJFieldRef().invoke(String.format("%s%s",ModelConstants.GENERATION_GET_PREFIX, linkFieldName));


        mGeneratedClassHolder.getLinkJBlock()
                .invoke(mGeneratedClassHolder.getResultJFieldRef(), String.format("%s%s", ModelConstants.GENERATION_SET_PREFIX, fieldName))
                .arg(jExpression);


        //被映射类到映射类
        jExpression = mGeneratedClassHolder.getDataJFieldRef().invoke(String.format("%s%s",ModelConstants.GENERATION_GET_PREFIX, linkFieldName));


        mGeneratedClassHolder.getLinkRealmJBlock()
                .invoke(mGeneratedClassHolder.getResultJFieldRef(), String.format("%s%s", ModelConstants.GENERATION_SET_PREFIX, fieldName))
                .arg(jExpression);

    }


}
