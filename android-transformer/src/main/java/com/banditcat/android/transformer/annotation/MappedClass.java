

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
package com.banditcat.android.transformer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 文件名称：{@link Parse}
 * <br/>
 * 功能描述：Use this annotation to configure the data mapping between two classß.
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
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface MappedClass {

    /**
     * Use this property to establish the linked object type.
     */
    Class<?> with();
}
