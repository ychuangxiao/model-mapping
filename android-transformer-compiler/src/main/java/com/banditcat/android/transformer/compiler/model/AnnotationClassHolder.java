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
