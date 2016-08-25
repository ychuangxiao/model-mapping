package com.banditcat.example.entity;


/**
 * 文件名称：{@link UserEntity}
 * <br/>
 * 功能描述：用户实体
 * <br/>
 * 创建作者：banditcat
 * <br/>
 * 创建时间：16/8/25 14:11
 * <br/>
 * 修改作者：banditcat
 * <br/>
 * 修改时间：16/8/25 14:11
 * <br/>
 * 修改备注：
 */
public class UserEntity {


    private Long userId;//用户id
    private String userName;//姓名
    private String fullName;//全名

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
