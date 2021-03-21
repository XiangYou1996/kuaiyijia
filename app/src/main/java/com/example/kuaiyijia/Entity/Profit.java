package com.example.kuaiyijia.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/*
Author by: xy
Coding On 2021/3/12;
*/
public class Profit implements Parcelable {
    private String ID;
    private String OrderID;
    private String PersonID;
    private String PersonName;
    private String Station;
    private String Money;

    public Profit(String ID, String orderID, String personID, String personName, String station, String money) {
        this.ID = ID;
        OrderID = orderID;
        PersonID = personID;
        PersonName = personName;
        Station = station;
        Money = money;
    }

    protected Profit(Parcel in) {
        ID = in.readString();
        OrderID = in.readString();
        PersonID = in.readString();
        PersonName = in.readString();
        Station = in.readString();
        Money = in.readString();
    }

    public static final Creator<Profit> CREATOR = new Creator<Profit>() {
        @Override
        public Profit createFromParcel(Parcel in) {
            return new Profit(in);
        }

        @Override
        public Profit[] newArray(int size) {
            return new Profit[size];
        }
    };

    public String getPersonName() {
        return PersonName;
    }

    public void setPersonName(String personName) {
        PersonName = personName;
    }

    public String getStation() {
        return Station;
    }

    public void setStation(String station) {
        Station = station;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getPersonID() {
        return PersonID;
    }

    public void setPersonID(String personID) {
        PersonID = personID;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(OrderID);
        dest.writeString(PersonID);
        dest.writeString(PersonName);
        dest.writeString(Station);
        dest.writeString(Money);
    }
}
