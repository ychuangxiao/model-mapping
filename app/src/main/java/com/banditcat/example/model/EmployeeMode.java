package com.banditcat.example.model;


import com.banditcat.android.transformer.annotation.Filed;
import com.banditcat.android.transformer.annotation.FiledByClass;
import com.banditcat.android.transformer.annotation.FiledByCollection;
import com.banditcat.android.transformer.annotation.MappedClass;
import com.banditcat.example.entity.EmployeeEntity;

import java.util.Collection;

@MappedClass(with = EmployeeEntity.class)
public class EmployeeMode extends UserMode {

    @Filed
    String address;

    @FiledByCollection(with = FamilyMode.class,toField = "familyEntities")
    Collection<FamilyMode> familyModes;

    @FiledByClass(toField = "addresso")
    AddressMode addressMode;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Collection<FamilyMode> getFamilyModes() {
        return familyModes;
    }

    public void setFamilyModes(Collection<FamilyMode> familyModes) {
        this.familyModes = familyModes;
    }

    public AddressMode getAddressMode() {
        return addressMode;
    }

    public void setAddressMode(AddressMode addressMode) {
        this.addressMode = addressMode;
    }
}
