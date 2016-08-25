/**
 * Copyright (C) 2010-2014 eBusiness Information, Excilys Group
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

import com.banditcat.android.transformer.compiler.process.ProcessHolder;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JVar;


import javax.lang.model.element.TypeElement;

import static com.sun.codemodel.JExpr._new;
import static com.sun.codemodel.JExpr._null;
import static com.sun.codemodel.JExpr._this;
import static com.sun.codemodel.JExpr.cast;
import static com.sun.codemodel.JExpr.invoke;
import static com.sun.codemodel.JMod.FINAL;
import static com.sun.codemodel.JMod.PRIVATE;
import static com.sun.codemodel.JMod.PUBLIC;

/**
 * 文件名称：EComponentWithViewSupportHolder
 * <br/>
 * 功能描述：
 * <br/>
 * 创建作者：banditcat
 * <br/>
 * 创建时间：15/12/1 14:47
 * <br/>
 * 修改作者：banditcat
 * <br/>
 * 修改时间：15/12/1 14:47
 * <br/>
 * 修改备注：支持数据库对象
 *
 *
 */
public abstract class EComponentWithViewSupportHolder extends EComponentHolder {


    private JFieldRef dataJFieldRef;//数据字段
    private JFieldRef resultJFieldRef;//结果字段

    private JMethod mappedTransformer;//映射方法 mapped to link

    private JBlock mappedJBlock;
    private JMethod linkTransformer;//映射方法 link to mapped
    private JBlock linkJBlock;
    private JMethod mappedCollectionTransformer;//映射方法 mapped's collection to link's collection


    private JBlock linkRealmJBlock;//映射方法 link to mapped database
    private JMethod linkRealmTransformer;//映射方法 link to mapped database

    private JBlock mappedCollectionJBlock;
    private JMethod linkCollectionTransformer;//映射方法 link's collection to mapped's collection
    private JBlock linkCollectionJBlock;

    public EComponentWithViewSupportHolder(ProcessHolder processHolder, TypeElement annotatedElement) throws Exception {
        super(processHolder, annotatedElement);


    }


    public JMethod getMappedTransformer() {
        return mappedTransformer;
    }

    public void setMappedTransformer(JMethod mappedTransformer) {
        this.mappedTransformer = mappedTransformer;
    }

    public JMethod getLinkTransformer() {
        return linkTransformer;
    }

    public void setLinkTransformer(JMethod linkTransformer) {
        this.linkTransformer = linkTransformer;
    }

    public JMethod getMappedCollectionTransformer() {
        return mappedCollectionTransformer;
    }

    public void setMappedCollectionTransformer(JMethod mappedCollectionTransformer) {
        this.mappedCollectionTransformer = mappedCollectionTransformer;
    }

    public JMethod getLinkCollectionTransformer() {
        return linkCollectionTransformer;
    }

    public void setLinkCollectionTransformer(JMethod linkCollectionTransformer) {
        this.linkCollectionTransformer = linkCollectionTransformer;
    }

    public JBlock getMappedJBlock() {
        return mappedJBlock;
    }

    public void setMappedJBlock(JBlock mappedJBlock) {
        this.mappedJBlock = mappedJBlock;
    }

    public JBlock getLinkJBlock() {
        return linkJBlock;
    }

    public void setLinkJBlock(JBlock linkJBlock) {
        this.linkJBlock = linkJBlock;
    }

    public JBlock getMappedCollectionJBlock() {
        return mappedCollectionJBlock;
    }

    public void setMappedCollectionJBlock(JBlock mappedCollectionJBlock) {
        this.mappedCollectionJBlock = mappedCollectionJBlock;
    }

    public JBlock getLinkCollectionJBlock() {
        return linkCollectionJBlock;
    }

    public void setLinkCollectionJBlock(JBlock linkCollectionJBlock) {
        this.linkCollectionJBlock = linkCollectionJBlock;
    }

    public JFieldRef getDataJFieldRef() {
        return dataJFieldRef;
    }

    public void setDataJFieldRef(JFieldRef dataJFieldRef) {
        this.dataJFieldRef = dataJFieldRef;
    }

    public JFieldRef getResultJFieldRef() {
        return resultJFieldRef;
    }

    public void setResultJFieldRef(JFieldRef resultJFieldRef) {
        this.resultJFieldRef = resultJFieldRef;
    }


    public JBlock getLinkRealmJBlock() {
        return linkRealmJBlock;
    }

    public void setLinkRealmJBlock(JBlock linkRealmJBlock) {
        this.linkRealmJBlock = linkRealmJBlock;
    }

    public JMethod getLinkRealmTransformer() {
        return linkRealmTransformer;
    }

    public void setLinkRealmTransformer(JMethod linkRealmTransformer) {
        this.linkRealmTransformer = linkRealmTransformer;
    }
}
