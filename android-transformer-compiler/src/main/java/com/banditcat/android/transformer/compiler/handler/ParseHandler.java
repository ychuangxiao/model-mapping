package com.banditcat.android.transformer.compiler.handler;

import com.banditcat.android.transformer.annotation.Filed;
import com.banditcat.android.transformer.annotation.Parse;
import com.banditcat.android.transformer.compiler.helper.ModelConstants;
import com.banditcat.android.transformer.compiler.holder.EComponentWithViewSupportHolder;
import com.banditcat.android.transformer.compiler.model.AnnotationElements;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;


/**
 * 项目名称：Study
 * 类描述：
 * 创建作者：banditcat
 * 创建时间：15/11/11 下午1:22
 * 修改作者：banditcat
 * 修改时间：15/11/11 下午1:22
 * 修改备注：
 */
public class ParseHandler extends BaseAnnotationHandler<EComponentWithViewSupportHolder> {


    private Messager mMessager;

    public ParseHandler(ProcessingEnvironment processingEnvironment, Messager messager) {
        super(Parse.class, processingEnvironment);

        this.mMessager = messager;
    }

    @Override
    public boolean validate(Element element, AnnotationElements validatedElements) {
        return false;
    }

    @Override
    public void process(Element element, EComponentWithViewSupportHolder holder) throws Exception {

        AnnotationMirror parseAnnotationMirror = codeModelHelper.getAnnotationMirror(element, Parse.class);

        AnnotationValue originToDestinationWithValue
                = codeModelHelper.getAnnotationValue(parseAnnotationMirror, ModelConstants.GENERATION_ORIGIN_TO_DESTINATION_WITH);
        AnnotationValue destinationToOriginWithValue
                = codeModelHelper.getAnnotationValue(parseAnnotationMirror, ModelConstants.GENERATION_DESTINATION_TO_ORIGIN_WITH);


        AnnotationMirror annotationMirror = codeModelHelper.getAnnotationMirror(element, Filed.class);


        AnnotationValue annotationValue = codeModelHelper.getAnnotationValue(annotationMirror, ModelConstants.GENERATION_TOFIELD);


        String fieldName = codeModelHelper.toUpperCamelCase(element.getSimpleName().toString());

        String linkFieldName = (null != annotationValue)
                ?  codeModelHelper.toUpperCamelCase(annotationValue.getValue().toString())
                :fieldName;



        //映射类M到被映射类E

        JExpression jExpression = holder.getDataJFieldRef().invoke(String.format("%s%s",ModelConstants.GENERATION_GET_PREFIX, fieldName));


        JClass singleton = holder.codeModel().ref(originToDestinationWithValue.getValue().toString());


        JInvocation getIns = singleton.staticInvoke(ModelConstants.GENERATION_GET_INSTANCE);


        holder.getMappedJBlock()
                .invoke(holder.getResultJFieldRef(), String.format("%s%s",ModelConstants.GENERATION_SET_PREFIX, linkFieldName))
                .arg(getIns.invoke(ModelConstants.GENERATION_PARSE).arg(jExpression));


        // E到M
        jExpression = holder.getDataJFieldRef().invoke(String.format("%s%s", ModelConstants.GENERATION_GET_PREFIX,linkFieldName));


        singleton = holder.codeModel().ref(destinationToOriginWithValue.getValue().toString());

        getIns = singleton.staticInvoke(ModelConstants.GENERATION_GET_INSTANCE);
        holder.getLinkJBlock()
                .invoke(holder.getResultJFieldRef(), String.format("%s%s",ModelConstants.GENERATION_SET_PREFIX, fieldName))
                .arg(getIns.invoke(ModelConstants.GENERATION_PARSE).arg(jExpression));



        // E到M(databse)
        jExpression = holder.getDataJFieldRef().invoke(String.format("%s%s", ModelConstants.GENERATION_GET_PREFIX,linkFieldName));


        singleton = holder.codeModel().ref(destinationToOriginWithValue.getValue().toString());

        getIns = singleton.staticInvoke(ModelConstants.GENERATION_GET_INSTANCE);
        holder.getLinkRealmJBlock()
                .invoke(holder.getResultJFieldRef(), String.format("%s%s",ModelConstants.GENERATION_SET_PREFIX, fieldName))
                .arg(getIns.invoke(ModelConstants.GENERATION_PARSE).arg(jExpression));
    }


}
