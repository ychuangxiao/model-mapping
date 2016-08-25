package com.banditcat.example.entity;

 
 /**
  * 文件名称：{@link AddressEntity}
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
public class AddressEntity {

    String familyAddress;

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