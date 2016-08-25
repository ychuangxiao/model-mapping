package com.banditcat.example.model;


import com.banditcat.android.transformer.annotation.Filed;
import com.banditcat.android.transformer.annotation.FiledByClass;
import com.banditcat.android.transformer.annotation.FiledByCollection;
import com.banditcat.android.transformer.annotation.MappedClass;
import com.banditcat.example.entity.EmployeeEntity;

import java.util.Collection;

@MappedClass(with = EmployeeEntity.class)
public class EmployeeModel extends UserModel {

    @Filed
    String address;

    @FiledByCollection(with = FamilyModel.class,toField = "familyEntities")
    Collection<FamilyModel> familyModels;

    @FiledByClass(toField = "addressEntity")
    AddressModel addressModel;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Collection<FamilyModel> getFamilyModels() {
        return familyModels;
    }

    public void setFamilyModels(Collection<FamilyModel> familyModels) {
        this.familyModels = familyModels;
    }

    public AddressModel getAddressModel() {
        return addressModel;
    }

    public void setAddressModel(AddressModel addressModel) {
        this.addressModel = addressModel;
    }
}
