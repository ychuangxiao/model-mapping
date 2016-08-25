package com.banditcat.example.entity;

import java.util.Collection;

 
 /**
  * 文件名称：{@link EmployeeEntity}
  * <br/>
  * 功能描述： 
  * <br/>
  * 创建作者：banditcat
  * <br/>
  * 创建时间：16/8/25 14:12
  * <br/>
  * 修改作者：banditcat
  * <br/>
  * 修改时间：16/8/25 14:12
  * <br/>
  * 修改备注：
  */
public class EmployeeEntity extends UserEntity {

    String address;

    Collection<FamilyEntity> familyEntities;

    AddressEntity addresso;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Collection<FamilyEntity> getFamilyEntities() {
        return familyEntities;
    }

    public void setFamilyEntities(Collection<FamilyEntity> familyEntities) {
        this.familyEntities = familyEntities;
    }

    public AddressEntity getAddresso() {
        return addresso;
    }

    public void setAddresso(AddressEntity addresso) {
        this.addresso = addresso;
    }
}
