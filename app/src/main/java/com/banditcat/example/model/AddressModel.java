package com.banditcat.example.model;


import com.banditcat.android.transformer.annotation.Filed;
import com.banditcat.android.transformer.annotation.MappedClass;
import com.banditcat.example.entity.AddressEntity;

@MappedClass(with = AddressEntity.class)
public class AddressModel {

    @Filed
    String familyAddress;

    @Filed
    String companyAddress;

    public String getFamilyAddress() {
        return familyAddress;
    }

    public void setFamilyAddress(String familyAddress) {
        this.familyAddress = familyAddress;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

}
