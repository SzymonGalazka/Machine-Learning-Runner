package com.pl.runner.box2d;

import com.pl.runner.enums.UserDataType;

public abstract class UserData {
    protected UserDataType userDataType;

    public UserData(){

    }

    public UserDataType getUserDataType(){
        return userDataType;
    }


}
