package com.example.kuaiyijia.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class CarListItem implements Serializable, Parcelable {
    // 车在数据库中的ID
    private String ID;
    // 车牌号
    private String carPlateNum;
    private String CarType;
    private String Load;
    private String lenth;
    private String width;
    private String height;

    // 车辆识别号
    private String carIdentityId;
    // 行驶证号
    private String carLicenseId;

    public CarListItem(String ID, String carPlateNum, String carType, String load, String lenth,
                       String width, String height, String carIdentityId, String carLicenseId) {
        this.ID = ID;
        this.carPlateNum = carPlateNum;
        this.CarType = carType;
        this.Load = load;
        this.lenth = lenth;
        this.width = width;
        this.height = height;
        this.carIdentityId = carIdentityId;
        this.carLicenseId = carLicenseId;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    protected CarListItem(Parcel in) {
        carPlateNum = in.readString();
        CarType = in.readString();
        Load = in.readString();
        lenth = in.readString();
        width = in.readString();
        height = in.readString();
        carIdentityId = in.readString();
        carLicenseId = in.readString();
        ID =in.readString();
    }

    public static final Creator<CarListItem> CREATOR = new Creator<CarListItem>() {
        @Override
        public CarListItem createFromParcel(Parcel in) {
            return new CarListItem(in);
        }

        @Override
        public CarListItem[] newArray(int size) {
            return new CarListItem[size];
        }
    };

    public String getCarIdentityId() {
        return carIdentityId;
    }

    public void setCarIdentityId(String carIdentityId) {
        this.carIdentityId = carIdentityId;
    }

    public String getCarLicenseId() {
        return carLicenseId;
    }

    public void setCarLicenseId(String carLicenseId) {
        this.carLicenseId = carLicenseId;
    }


    public String getLenth() {
        return lenth;
    }

    public void setLenth(String lenth) {
        this.lenth = lenth;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getCarPlateNum() {
        return carPlateNum;
    }

    public String getCarType() {
        return CarType;
    }


    public String getLoad() {
        return Load;
    }

    public void setCarPlateNum(String carPlateNum) {
        this.carPlateNum = carPlateNum;
    }

    public void setCarType(String carType) {
        CarType = carType;
    }


    public void setLoad(String load) {
        Load = load;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(carPlateNum);
        dest.writeString(CarType);
        dest.writeString(Load);
        dest.writeString(lenth);
        dest.writeString(width);
        dest.writeString(height);
        dest.writeString(carIdentityId);
        dest.writeString(carLicenseId);
        dest.writeString(ID);
    }
}
