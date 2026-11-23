package com.findabin.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Rating {
    @DocumentId
    private String id;

    private String userId;
    private double stars;
    private String comment;
    private String binName;
    @ServerTimestamp
    private Timestamp createdAt;

    public Rating(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
