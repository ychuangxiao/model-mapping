package com.banditcat.example.model;


import com.banditcat.android.transformer.annotation.Filed;
import com.banditcat.android.transformer.annotation.MappedClass;
import com.banditcat.android.transformer.annotation.Parse;
import com.banditcat.example.convert.LongToStringParser;
import com.banditcat.example.convert.StringToLongParser;
import com.banditcat.example.entity.UserEntity;


@MappedClass(with = UserEntity.class)
public class UserMode {


    @Filed(toField = "userId")
    @Parse(originToDestinationWith = StringToLongParser.class, destinationToOriginWith = LongToStringParser.class)
    private String stringUserId;


    @Filed
    private String userName;
    @Filed(toField = "fullName")
    private String fullName2;


    public String getStringUserId() {
        return stringUserId;
    }

    public void setStringUserId(String stringUserId) {
        this.stringUserId = stringUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName2() {
        return fullName2;
    }

    public void setFullName2(String fullName2) {
        this.fullName2 = fullName2;
    }
}
