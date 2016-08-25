package com.banditcat.example.entity;

 
 /**
  * 文件名称：{@link FamilyEntity}
  * <br/>
  * 功能描述： 
  * <br/>
  * 创建作者：banditcat
  * <br/>
  * 创建时间：16/8/25 14:19
  * <br/>
  * 修改作者：banditcat
  * <br/>
  * 修改时间：16/8/25 14:19
  * <br/>
  * 修改备注：
  */
public class FamilyEntity {

    private String address;
    private String telPhone;
    private String cityName;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
