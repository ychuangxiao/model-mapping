
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
package com.banditcat.android.transformer.compiler.model;


/**
 * 项目名称：Study
 * 类描述：
 * 创建作者：banditcat
 * 创建时间：15/11/10 下午5:06
 * 修改作者：banditcat
 * 修改时间：15/11/10 下午5:06
 * 修改备注：
 */
public class AnnotationClassHolder implements AnnotationClass {


    /**
     * 映射类
     */
    private ClassInfo mMappedClassInfo;
    /**
     * 来源类
     */
    private ClassInfo mLinkClassInfo;

    /**
     * 集合类
     */
    private ClassInfo mCollectionClassInfo;




    @Override
    public ClassInfo getMappedClassInfo() {
        return mMappedClassInfo;
    }

    @Override
    public ClassInfo getLinkClassInfo() {
        return mLinkClassInfo;
    }

    @Override
    public ClassInfo getCollectionClassInfo() {
        return mCollectionClassInfo;
    }

    public void setMappedClassInfo(ClassInfo mappedClassInfo) {
        mMappedClassInfo = mappedClassInfo;
    }

    public void setLinkClassInfo(ClassInfo linkClassInfo) {
        mLinkClassInfo = linkClassInfo;
    }

    public void setCollectionClassInfo(ClassInfo collectionClassInfo) {
        mCollectionClassInfo = collectionClassInfo;
    }
}
