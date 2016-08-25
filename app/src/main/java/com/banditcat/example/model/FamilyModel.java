package com.banditcat.example.model;


import com.banditcat.android.transformer.annotation.Filed;
import com.banditcat.android.transformer.annotation.MappedClass;
import com.banditcat.example.entity.FamilyEntity;

@MappedClass(with = FamilyEntity.class)
public class FamilyModel {

    @Filed
    private String address;
    @Filed
    private String telPhone;

    @Filed
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
