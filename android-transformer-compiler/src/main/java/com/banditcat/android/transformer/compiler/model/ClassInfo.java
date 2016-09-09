
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
 * 类描述： 类信息,主要是存储模型类信息/字段信息/字段类型
 * 创建作者：banditcat
 * 创建时间：15/11/10 下午5:21
 * 修改作者：banditcat
 * 修改时间：15/11/10 下午5:21
 * 修改备注：
 */
public class ClassInfo {

    public final String className;
    public final String packageName;


    MapperFieldInfo mMapperFieldInfo;


    public ClassInfo(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    public String getFullName() {
        return String.format("%s.%s", packageName, className);
    }

    @Override
    public String toString() {
        return String.format("%s.%s", packageName, className);
    }


    public MapperFieldInfo getMapperFieldInfo() {
        return mMapperFieldInfo;
    }

    public void setMapperFieldInfo(MapperFieldInfo mapperFieldInfo) {
        mMapperFieldInfo = mapperFieldInfo;
    }
}
