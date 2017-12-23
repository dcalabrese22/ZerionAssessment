package com.dcalabrese22.dan.zerionassessment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dan on 12/23/17.
 */


/**
 * Custom object that represents an object retrieved from the iformbuilder database. Implements
 * parcelable to pass details to RecordDetailActivity and for saving state
 */
public class ZerionRecord implements Parcelable{

    private int id;
    private String name;
    private int age;
    private String phone;
    private String date;
    private String pictureUrl;

    public ZerionRecord(int id, String name, int age, String phone, String date, String pictureUrl) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.date = date;
        this.pictureUrl = pictureUrl;
    }

    public ZerionRecord(String name, int age, String phone, String date, String pictureUrl) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.date = date;
        this.pictureUrl = pictureUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ZerionRecord createFromParcel(Parcel in) {
            return  new ZerionRecord(in);
        }

        @Override
        public ZerionRecord[] newArray(int i) {
            return new ZerionRecord[i];
        }
    };

    public ZerionRecord(Parcel in) {
        id = in.readInt();
        name = in.readString();
        age = in.readInt();
        phone = in.readString();
        date = in.readString();
        pictureUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(age);
        parcel.writeString(phone);
        parcel.writeString(date);
        parcel.writeString(pictureUrl);
    }
}
