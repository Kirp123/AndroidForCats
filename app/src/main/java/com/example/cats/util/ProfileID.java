package com.example.cats.util;

public class ProfileID {

    String profileId;
    public <T extends ProfileID> T withId(String s){

        this.profileId = s;
        return (T) this;
    }
}
