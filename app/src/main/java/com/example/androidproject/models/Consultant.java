package com.example.androidproject.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Consultant implements Parcelable {
    private String name,username,password,consultantId;
    private long rating;

    public Consultant() {
    }

    protected Consultant(Parcel in) {
        name = in.readString();
        username = in.readString();
        password = in.readString();
        consultantId = in.readString();
        rating = in.readLong();
    }

    public static final Creator<Consultant> CREATOR = new Creator<Consultant>() {
        @Override
        public Consultant createFromParcel(Parcel in) {
            return new Consultant(in);
        }

        @Override
        public Consultant[] newArray(int size) {
            return new Consultant[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(String consultantId) {
        this.consultantId = consultantId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(consultantId);
        dest.writeLong(rating);
    }
}
