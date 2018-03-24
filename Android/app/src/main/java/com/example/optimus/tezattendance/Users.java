package com.example.optimus.tezattendance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by optimus on 24/3/18.
 */

public class Users implements Parcelable {

    public String type, idd, password, className, fireUID;



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(type);
        parcel.writeString(idd);
        parcel.writeString(password);
        parcel.writeString(className);
        parcel.writeString(fireUID);


    }


    public static final Parcelable.Creator<Users> CREATOR
            = new Parcelable.Creator<Users>() {
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };



    public Users(Parcel in) {

        type = in.readString();
        idd = in.readString();
        password = in.readString();
        className= in.readString();
        fireUID= in.readString();
    }





    public Users(){

    }

    public Users(String type, String idd, String password, String className, String fireUID) {
        this.type = type;
        this.idd = idd;
        this.password = password;
        this.className = className;
        this.fireUID = fireUID;
    }
}
