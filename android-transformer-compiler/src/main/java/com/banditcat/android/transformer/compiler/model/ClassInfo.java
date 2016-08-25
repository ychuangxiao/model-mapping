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
