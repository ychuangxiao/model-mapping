
package com.banditcat.android.transformer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 文件名称：{@link Parse}
 * <br/>
 * 功能描述：Use this annotation to configure the data mapping between two class.
 * <br/>
 * 创建作者：banditcat
 * <br/>
 * 创建时间：16/8/25 13:41
 * <br/>
 * 修改作者：banditcat
 * <br/>
 * 修改时间：16/8/25 13:41
 * <br/>
 * 修改备注：
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface FiledByClass {
    /**
     * Use this property to establish a specific field name, for example, if the two fields does not have the same name.
     * @return
     */
    String toField() default "";
}
