package com.mustafaelhadad.testprojectqr.Model;

import com.google.firebase.firestore.DocumentId;

public class UserModel {

    @DocumentId
    private String uaerID;
    private String userName;
    private  String admin ;

    public UserModel() {
    }

    public String isAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }


    public String getId() {
        return uaerID;
    }

    public void setId(String uaerID) {
        this.uaerID = uaerID;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
