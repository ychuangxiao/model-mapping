package com.banditcat.android.transformer.compiler.handler;

import com.banditcat.android.transformer.annotation.FiledByCollection;
import com.banditcat.android.transformer.annotation.MappedClass;
import com.banditcat.android.transformer.compiler.helper.ModelConstants;
import com.banditcat.android.transformer.compiler.holder.EComponentWithViewSupportHolder;
import com.banditcat.android.transformer.compiler.holder.GeneratedClassHolder;
import com.banditcat.android.transformer.compiler.model.AnnotationElements;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JForEach;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import static com.banditcat.android.transformer.compiler.helper.ModelConstants.GENERATION_CLASS_NAME_SUFFIX;


/**
 * 项目名称：Study
 * 类描述：
 * 创建作者：banditcat
 * 创建时间：15/11/11 下午1:22
 * 修改作者：banditcat
 * 修改时间：15/11/11 下午1:22
 * 修改备注：
 */
public class FiledByCollectionHandler extends BaseAnnotationHandler<EComponentWithViewSupportHolder> {

    private Messager mMessager;

    public FiledByCollectionHandler(ProcessingEnvironment processingEnvironment, Messager messager) {
        super(FiledByCollection.class, processingEnvironment);

        this.mMessager = messager;
    }

    @Override
    public boolean validate(Element element, AnnotationElements validatedElements) {
        return false;
    }

    @Override
    public void process(Element element, EComponentWithViewSupportHolder holder) throws Exception {


        String fieldName = codeModelHelper.toUpperCamelCase(element.getSimpleName().toString());

        TypeMirror uiFieldTypeMirror = element.asType();
        String typeQualifiedName = uiFieldTypeMirror.toString();


        mMessager.printMessage(Diagnostic.Kind.NOTE,
                " 类型转换 字段信息 " +
                        element.getEnclosingElement().getSimpleName().toString() + " :" + fieldName + "   ..... ");

        mMessager.printMessage(Diagnostic.Kind.NOTE,
                " 类型转换 类信息 " +
                        holder.getGeneratedClass().fullName() + "   ..... ");


        //获取注解Class的信息


        AnnotationMirror collectionAnnotationMirror
                = codeModelHelper.getAnnotationMirror(element, super.getTarget());
        AnnotationValue collectionAnnotationValue = codeModelHelper.getAnnotationValue(collectionAnnotationMirror, ModelConstants.GENERATION_NAME_WITH);


        AnnotationValue toFieldAnnotationValue = codeModelHelper.getAnnotationValue(collectionAnnotationMirror
                , ModelConstants.GENERATION_TOFIELD);


        String mapperName = String.format("%s%s", collectionAnnotationValue.getValue().toString(), GENERATION_CLASS_NAME_SUFFIX);


        Element[] elements = processHolder.getOriginatingElements().getClassOriginatingElements(mapperName);


        if (null != elements && elements.length == 1) {


            //字段 toField


            AnnotationMirror annotationMirror = codeModelHelper.getAnnotationMirror(elements[0], MappedClass.class);


            AnnotationValue annotationValue = codeModelHelper.getAnnotationValue(annotationMirror, ModelConstants.GENERATION_NAME_WITH);


            String linkFieldName = (null != annotationValue)
                    ? codeModelHelper.toUpperCamelCase(toFieldAnnotationValue.getValue().toString())
                    : fieldName;





            String entityName = String.format("%s%s"
                    , ModelConstants.GENERATION_METHOD_NAME_TRANSFORMER,
                    getTypeElement(annotationValue).getSimpleName().toString()
            );

            String viewModelName = String.format("%s%s"
                    , ModelConstants.GENERATION_METHOD_NAME_TRANSFORMER,
                    elements[0].getSimpleName().toString()
            );


            //映射类M到被映射类E

            JExpression jExpression = holder.getDataJFieldRef().invoke(String.format("%s%s", ModelConstants.GENERATION_GET_PREFIX, fieldName));


            JClass singleton = holder.codeModel().ref(mapperName);


            JInvocation getIns = singleton.staticInvoke(ModelConstants.GENERATION_GET_INSTANCE);


            holder.getMappedJBlock()
                    .invoke(holder.getResultJFieldRef(), String.format("%s%s", ModelConstants.GENERATION_SET_PREFIX, linkFieldName))
                    .arg(getIns.invoke(viewModelName).arg(jExpression));


            // E到M
            jExpression = holder.getDataJFieldRef().invoke(String.format("%s%s", ModelConstants.GENERATION_GET_PREFIX, linkFieldName));


            singleton = holder.codeModel().ref(mapperName);

            getIns = singleton.staticInvoke(ModelConstants.GENERATION_GET_INSTANCE);
            holder.getLinkJBlock()
                    .invoke(holder.getResultJFieldRef(), String.format("%s%s", ModelConstants.GENERATION_SET_PREFIX, fieldName))
                    .arg(getIns.invoke(entityName).arg(jExpression));


        }

    }


}
