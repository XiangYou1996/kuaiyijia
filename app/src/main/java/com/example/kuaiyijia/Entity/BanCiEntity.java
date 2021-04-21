package com.example.kuaiyijia.Entity;

/*
Author by: xy
Coding On 2021/4/16;
*/
public class BanCiEntity {
    private String BID;
    private String LID;
    private String CID;
    private String HYBID;

    public BanCiEntity(String BID, String LID, String CID, String HYBID, String start_Time) {
        this.BID = BID;
        this.LID = LID;
        this.CID = CID;
        this.HYBID = HYBID;
        Start_Time = start_Time;
    }

    private String Start_Time;

    public String getBID() {
        return BID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }

    public String getLID() {
        return LID;
    }

    public void setLID(String LID) {
        this.LID = LID;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getHYBID() {
        return HYBID;
    }

    public void setHYBID(String HYBID) {
        this.HYBID = HYBID;
    }

    public String getStart_Time() {
        return Start_Time;
    }

    public void setStart_Time(String start_Time) {
        Start_Time = start_Time;
    }
}
