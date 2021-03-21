package com.example.kuaiyijia.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/*
Author by: xy
Coding On 2021/3/10;
*/
public class WebBranchListItem implements Parcelable {
    private String WebBranchID;
    private String WebBranchName;
    private String WebBranchJC;
    private String HuoYunTel;
    private String Cantact ; 
    private String CantactTel;
    private String DetailAddress;

    public WebBranchListItem(String webBranchID, String webBranchName, String webBranchJC, 
                             String HYTel, String cantact, String cantactTel, String detailAddress) {
        WebBranchID = webBranchID;
        WebBranchName = webBranchName;
        WebBranchJC = webBranchJC;
        HuoYunTel = HYTel;
        Cantact = cantact;
        CantactTel = cantactTel;
        DetailAddress = detailAddress;
    }

    protected WebBranchListItem(Parcel in) {
        WebBranchID = in.readString();
        WebBranchName = in.readString();
        WebBranchJC = in.readString();
        HuoYunTel = in.readString();
        Cantact = in.readString();
        CantactTel = in.readString();
        DetailAddress = in.readString();
    }

    public static final Creator<WebBranchListItem> CREATOR = new Creator<WebBranchListItem>() {
        @Override
        public WebBranchListItem createFromParcel(Parcel in) {
            return new WebBranchListItem(in);
        }

        @Override
        public WebBranchListItem[] newArray(int size) {
            return new WebBranchListItem[size];
        }
    };

    public String getWebBranchID() {
        return WebBranchID;
    }

    public void setWebBranchID(String webBranchID) {
        WebBranchID = webBranchID;
    }

    public String getWebBranchName() {
        return WebBranchName;
    }

    public void setWebBranchName(String webBranchName) {
        WebBranchName = webBranchName;
    }

    public String getWebBranchJC() {
        return WebBranchJC;
    }

    public void setWebBranchJC(String webBranchJC) {
        WebBranchJC = webBranchJC;
    }

    public String getHuoYunTel() {
        return HuoYunTel;
    }

    public void setHuoYunTel(String HYTel) {
        HuoYunTel = HuoYunTel;
    }

    public String getCantact() {
        return Cantact;
    }

    public void setCantact(String cantact) {
        Cantact = cantact;
    }

    public String getCantactTel() {
        return CantactTel;
    }

    public void setCantactTel(String cantactTel) {
        CantactTel = cantactTel;
    }

    public String getDetailAddress() {
        return DetailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        DetailAddress = detailAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(WebBranchID);
        dest.writeString(WebBranchName);
        dest.writeString(WebBranchJC);
        dest.writeString(HuoYunTel);
        dest.writeString(Cantact);
        dest.writeString(CantactTel);
        dest.writeString(DetailAddress);
    }
}
