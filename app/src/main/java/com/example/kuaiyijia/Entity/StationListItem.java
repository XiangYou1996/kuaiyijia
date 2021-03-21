package com.example.kuaiyijia.Entity;
/*
Author by: xy
Coding On ;
*/

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class StationListItem  implements Serializable,Parcelable {
    //  岗位ID
    private String ET_ID;
    //  岗位CODE  先用这个来控制 不同的权限
    private String ET_CODE;
    private String ET_NAME;

    public StationListItem(String ET_ID, String ET_CODE, String ET_NAME) {
        this.ET_ID = ET_ID;
        this.ET_CODE = ET_CODE;
        this.ET_NAME = ET_NAME;
    }

    protected StationListItem(Parcel in) {
        ET_ID = in.readString();
        ET_CODE = in.readString();
        ET_NAME = in.readString();
    }

    public static final Creator<StationListItem> CREATOR = new Creator<StationListItem>() {
        @Override
        public StationListItem createFromParcel(Parcel in) {
            return new StationListItem(in);
        }

        @Override
        public StationListItem[] newArray(int size) {
            return new StationListItem[size];
        }
    };

    public String getET_ID() {
        return ET_ID;
    }

    public void setET_ID(String ET_ID) {
        this.ET_ID = ET_ID;
    }

    public String getET_CODE() {
        return ET_CODE;
    }

    public void setET_CODE(String ET_CODE) {
        this.ET_CODE = ET_CODE;
    }

    public String getET_NAME() {
        return ET_NAME;
    }

    public void setET_NAME(String ET_NAME) {
        this.ET_NAME = ET_NAME;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ET_ID);
        dest.writeString(ET_CODE);
        dest.writeString(ET_NAME);
    }
}
