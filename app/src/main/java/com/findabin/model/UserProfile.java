package com.findabin.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

public class UserProfile {

    @DocumentId
    private String id;
    private String firstName;
    private String lastName;
    private String email;

    @ServerTimestamp
    private Timestamp createdAt;

    public UserProfile(){

    }

    public String getFirstName(){
        return(firstName);
    }

    public String getLastName(){
        return(lastName);
    }

    public String getEmail(){
        return(email);
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
