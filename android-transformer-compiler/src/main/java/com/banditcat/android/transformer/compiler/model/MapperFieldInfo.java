
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
 * 创建时间：15/11/10 下午5:26
 * 修改作者：banditcat
 * 修改时间：15/11/10 下午5:26
 * 修改备注：
 */
public class MapperFieldInfo {

    public final String fieldName;
    public final String fieldType;
    public final String withFieldName;
    public final boolean isPublicField;
    public String originToDestinationParserPackageName;
    public String originToDestinationParserClassName;
    public String destinationToOriginParserPackageName;
    public String destinationToOriginParserClassName;

    public MapperFieldInfo(String fieldName, String fieldType, String withFieldName, boolean isPublicField) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.withFieldName = withFieldName;
        this.isPublicField = isPublicField;
    }
}
