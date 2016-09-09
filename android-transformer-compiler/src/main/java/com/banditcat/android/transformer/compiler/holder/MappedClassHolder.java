
/**
 * Copyright (C) 2013-2016 banditcat
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.banditcat.android.transformer.compiler.holder;

import com.banditcat.android.transformer.annotation.MappedClass;
import com.banditcat.android.transformer.compiler.helper.ModelConstants;
import com.banditcat.android.transformer.compiler.process.ProcessHolder;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JForEach;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.TypeElement;

import static com.banditcat.android.transformer.compiler.helper.ModelConstants.GENERATION_CLASS_NAME_SUFFIX;
import static com.banditcat.android.transformer.compiler.helper.ModelConstants.GENERATION_METHOD_NAME_TRANSFORMER;
/**
 * 项目名称：Study
 * 类描述：  映射类拥有者,一般只有类才创建Holder
 * 创建作者：banditcat
 * 创建时间：15/11/11 下午3:14
 * 修改作者：banditcat
 * 修改时间：15/11/11 下午3:14
 * 修改备注：
 */
public class MappedClassHolder extends  EComponentWithViewSupportHolder {


    final String dataFiledName="data";
    final String resultFiledName="result";


    public MappedClassHolder(ProcessHolder processHolder, TypeElement annotatedElement) throws Exception {
        super(processHolder, annotatedElement);

        createClass();

        createMethod();


    }




    /**
     * 创建类基本信息,单例结构
     *
     * @throws ClassNotFoundException
     * @throws JClassAlreadyExistsException
     */
    void createClass() throws ClassNotFoundException, JClassAlreadyExistsException {



        JType type = super.codeModel().parseType(annotatedElement.getSimpleName().toString() + GENERATION_CLASS_NAME_SUFFIX);


        // 定义静态成员变量
        getGeneratedClass().field(JMod.PRIVATE + JMod.STATIC, type, ModelConstants.GENERATION_INSTANCE);

        // 定义单例类 Singleton 的构造函数
        getGeneratedClass().constructor(JMod.PRIVATE);


        // 生成 Singleton 类的成员方法 getInstanceMethod
        JMethod getInstanceMethod = getGeneratedClass().method(JMod.PUBLIC + JMod.STATIC, type,
                ModelConstants.GENERATION_GET_INSTANCE);
        JBlock getInstanceBody = getInstanceMethod.body();
        JFieldRef fieldRef = JExpr.ref(ModelConstants.GENERATION_INSTANCE);
        JConditional conditionIf = getInstanceBody._if(fieldRef.eq(JExpr
                ._null()));
        JBlock thenPart = conditionIf._then();
        thenPart.assign(fieldRef, JExpr._new(type));
        getInstanceBody._return(fieldRef);
    }


    void createMethod() throws ClassNotFoundException {
        createMappedTransformerMethod();
        createLinkTransformerMethod();
        createLinkTransformerRealmMethod();
        createMappedCollectionMethod();
        createLinkCollectionMethod();
    }


    /**
     * 创建映射类(ViewModel)到被映射类(Entity)之间的转换方法
     * @throws ClassNotFoundException
     */
    private void createMappedTransformerMethod() throws ClassNotFoundException {
        AnnotationMirror annotationMirror = codeModelHelper.getAnnotationMirror(annotatedElement, MappedClass.class);

        AnnotationValue annotationValue = codeModelHelper.getAnnotationValue(annotationMirror, ModelConstants.GENERATION_NAME_WITH);

        JType paramJType = codeModel().parseType(annotatedElement.getQualifiedName().toString());
        JType resultJType = codeModel().parseType(annotationValue.getValue().toString());

        JMethod transformerMethod = generatedClass.method(JMod.PUBLIC, resultJType, GENERATION_METHOD_NAME_TRANSFORMER); // 创建公共方法 newMyDate

        JFieldRef dataFieldRef = JExpr.ref(dataFiledName);
        JFieldRef resultFieldRef = JExpr.ref(resultFiledName);


        //设置参数
        transformerMethod.param(paramJType, dataFiledName);


        JBlock jBlock = transformerMethod.body();

        JVar result = jBlock.decl(resultJType, resultFiledName, JExpr._null()); // 定义变量


        //判断输入参数是否为空
        JConditional outerIf = jBlock._if(JExpr._null().ne(dataFieldRef));

        JBlock thenPart = outerIf._then();
        thenPart.assign(resultFieldRef, JExpr._new(resultJType));





        //具体的复制

        jBlock._return(result);

        setMappedJBlock(thenPart);//设置判读对象
        setDataJFieldRef(dataFieldRef);
        setResultJFieldRef(resultFieldRef);
        setMappedTransformer(transformerMethod);
    }


    /**
     * 创建被映射类(Entity)到映射类(ViewModel)之间的转换方法
     * @throws ClassNotFoundException
     */
    private void createLinkTransformerMethod() throws ClassNotFoundException {
        AnnotationMirror annotationMirror = codeModelHelper.getAnnotationMirror(annotatedElement, MappedClass.class);

        AnnotationValue annotationValue = codeModelHelper.getAnnotationValue(annotationMirror, ModelConstants.GENERATION_NAME_WITH);

        JType paramJType = codeModel().parseType(annotationValue.getValue().toString());
        JType resultJType = codeModel().parseType(annotatedElement.getQualifiedName().toString());

        JMethod transformerMethod = generatedClass.method(JMod.PUBLIC, resultJType, GENERATION_METHOD_NAME_TRANSFORMER); // 创建公共方法 newMyDate
        JFieldRef dataFieldRef = JExpr.ref(dataFiledName);
        JFieldRef resultFieldRef = JExpr.ref(resultFiledName);


        //设置参数
        transformerMethod.param(paramJType, dataFiledName);


        JBlock jBlock = transformerMethod.body();

        JVar result = jBlock.decl(resultJType, resultFiledName, JExpr._null()); // 定义变量


        //判断输入参数是否为空
        JConditional outerIf = jBlock._if(JExpr._null().ne(dataFieldRef));

        JBlock thenPart = outerIf._then();
        thenPart.assign(resultFieldRef, JExpr._new(resultJType));





        //具体的复制

        jBlock._return(result);

        setLinkJBlock(thenPart);//设置判读对象
        setDataJFieldRef(dataFieldRef);
        setResultJFieldRef(resultFieldRef);
        setLinkTransformer(transformerMethod);
    }


    /**
     * 创建被映射类(Entity)到映射类(Realms数据库)之间的转换方法
     * @throws ClassNotFoundException
     */
    private void createLinkTransformerRealmMethod() throws ClassNotFoundException {
        AnnotationMirror annotationMirror = codeModelHelper.getAnnotationMirror(annotatedElement, MappedClass.class);

        AnnotationValue annotationValue = codeModelHelper.getAnnotationValue(annotationMirror, ModelConstants.GENERATION_NAME_WITH);

        JType paramJType = codeModel().parseType(annotationValue.getValue().toString());
        JType resultJType = codeModel().parseType(annotatedElement.getQualifiedName().toString());

        JMethod transformerMethod = generatedClass.method(JMod.PUBLIC, resultJType, GENERATION_METHOD_NAME_TRANSFORMER); // 创建公共方法 newMyDate


        JFieldRef dataFieldRef = JExpr.ref(dataFiledName);
        JFieldRef resultFieldRef = JExpr.ref(resultFiledName);


        //设置参数
        transformerMethod.param(paramJType, dataFiledName);
        transformerMethod.param(resultJType, resultFiledName);

        JBlock jBlock = transformerMethod.body();

        //JVar result = jBlock.decl(resultJType, resultFiledName, JExpr._null()); // 定义变量


        //判断输入参数是否为空
        JConditional outerIf = jBlock._if(JExpr._null().ne(dataFieldRef));

        JBlock thenPart = outerIf._then();
       // thenPart.assign(resultFieldRef, JExpr._new(resultJType));





        //具体的复制

        jBlock._return(resultFieldRef);

        setLinkRealmJBlock(thenPart);//设置判读对象
        setDataJFieldRef(dataFieldRef);
        setResultJFieldRef(resultFieldRef);
        setLinkRealmTransformer(transformerMethod);
    }


    /**
     * 创建映射类(ViewModel list)到被映射类(Entity list)之间的转换方法
     * @throws ClassNotFoundException
     */
    private void createMappedCollectionMethod() throws ClassNotFoundException {
        AnnotationMirror annotationMirror = codeModelHelper.getAnnotationMirror(annotatedElement, MappedClass.class);

        AnnotationValue annotationValue = codeModelHelper.getAnnotationValue(annotationMirror, ModelConstants.GENERATION_NAME_WITH);



        TypeElement forClassTypeElement = getTypeElement(annotationValue);

        JType paramJType = codeModel().parseType(String.format("java.util.Collection<%s>"
                , annotatedElement.getQualifiedName().toString()));

        JType resultJType =codeModel().parseType(String.format("java.util.Collection<%s>"
                , annotationValue.getValue().toString()));

        JType resultNewJType =codeModel().parseType("java.util.ArrayList");

        //创建 v to e transformer
        JMethod transformerMethod = generatedClass.method(JMod.PUBLIC, resultJType
                , String.format("%s%s",GENERATION_METHOD_NAME_TRANSFORMER
                ,annotatedElement.getSimpleName().toString()));



        JFieldRef dataFieldRef = JExpr.ref(dataFiledName);
        JFieldRef resultFieldRef = JExpr.ref(resultFiledName);


        //设置参数
        transformerMethod.param(paramJType, dataFiledName);


        JBlock jBlock = transformerMethod.body();

        JVar result = jBlock.decl(resultJType, resultFiledName, JExpr._null()); // 定义变量


        //判断输入参数是否为空
        JConditional outerIf = jBlock._if(JExpr._null().ne(dataFieldRef));

        JBlock thenPart = outerIf._then();
        thenPart.assign(resultFieldRef, JExpr._new(resultNewJType));




        String forFiled = codeModelHelper.toLowerCamelCase(annotatedElement.getSimpleName().toString());


        JForEach foreach = thenPart.forEach(codeModel().parseType(annotatedElement.getQualifiedName().toString())
                , forFiled, JExpr.ref("data"));

        JBlock whileBody = foreach.body();

        JExpression sentance1 = JExpr.ref("this").invoke("transformer").arg(JExpr.ref(forFiled));

        whileBody.invoke(JExpr.ref("result"), "add").arg(sentance1);

        //具体的复制

        jBlock._return(result);


    }


    /**
     * 创建映射类(Entity list)到被映射类(ViewModel list)之间的转换方法
     * @throws ClassNotFoundException
     */
    private void createLinkCollectionMethod() throws ClassNotFoundException {
        AnnotationMirror annotationMirror = codeModelHelper.getAnnotationMirror(annotatedElement, MappedClass.class);

        AnnotationValue annotationValue = codeModelHelper.getAnnotationValue(annotationMirror, ModelConstants.GENERATION_NAME_WITH);



        TypeElement forClassTypeElement = getTypeElement(annotationValue);

        JType paramJType = codeModel().parseType(String.format("java.util.Collection<%s>"
                , annotationValue.getValue().toString()));

        JType resultJType = codeModel().parseType(String.format("java.util.Collection<%s>"
                , annotatedElement.getQualifiedName().toString()));

        JType resultNewJType =codeModel().parseType("java.util.ArrayList");

        //创建 v to e transformer
        JMethod transformerMethod = generatedClass.method(JMod.PUBLIC, resultJType
                , String.format("%s%s", GENERATION_METHOD_NAME_TRANSFORMER
                , forClassTypeElement.getSimpleName().toString()));



        JFieldRef dataFieldRef = JExpr.ref(dataFiledName);
        JFieldRef resultFieldRef = JExpr.ref(resultFiledName);


        //设置参数
        transformerMethod.param(paramJType, dataFiledName);


        JBlock jBlock = transformerMethod.body();

        JVar result = jBlock.decl(resultJType, resultFiledName, JExpr._null()); // 定义变量


        //判断输入参数是否为空
        JConditional outerIf = jBlock._if(JExpr._null().ne(dataFieldRef));

        JBlock thenPart = outerIf._then();
        thenPart.assign(resultFieldRef, JExpr._new(resultNewJType));




        String forFiled = codeModelHelper.toLowerCamelCase(forClassTypeElement.getSimpleName().toString());


        JForEach foreach = thenPart.forEach(codeModel().parseType(forClassTypeElement.getQualifiedName().toString())
                , forFiled, JExpr.ref("data"));

        JBlock whileBody = foreach.body();

        JExpression sentance1 = JExpr.ref("this").invoke("transformer").arg(JExpr.ref(forFiled));

        whileBody.invoke(JExpr.ref("result"), "add").arg(sentance1);

        //具体的复制

        jBlock._return(result);


    }



    /*private JMethod mappedTransformer;//映射方法 mapped to link
    private JMethod linkTransformer;//映射方法 link to mapped

    private JMethod mappedCollectionTransformer;//映射方法 mapped's collection to link's collection
    private JMethod linkCollectionTransformer;//映射方法 link's collection to mapped's collection
*/
}
