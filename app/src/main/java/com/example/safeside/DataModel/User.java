package com.example.safeside.DataModel;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("userName")
    private String userName;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;
}
