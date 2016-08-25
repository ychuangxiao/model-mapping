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
public interface AnnotationClass {

    /**
     * 映射类
     */
    ClassInfo getMappedClassInfo();

    /**
     * 来源类
     */
    ClassInfo getLinkClassInfo();

    /**
     * 集合类
     */
    ClassInfo getCollectionClassInfo();


}
