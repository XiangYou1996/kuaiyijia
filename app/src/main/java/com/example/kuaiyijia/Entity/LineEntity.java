package com.example.kuaiyijia.Entity;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/*
Author by: xy
Coding On 2021/4/14;
*/
public class LineEntity implements Serializable {

    private String id;
    private String start_station;
    private String end_station;
    private String CID;
    private String HYBID;

    public LineEntity(String id, String start_station, String end_station, String CID, String HYBID) {
        this.id = id;
        this.start_station = start_station;
        this.end_station = end_station;
        this.CID = CID;
        this.HYBID = HYBID;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnd_station() {
        return end_station;
    }

    public void setEnd_station(String end_station) {
        this.end_station = end_station;
    }

    public String getStart_station() {
        return start_station;
    }

    public void setStart_station(String start_station) {
        this.start_station = start_station;
    }
}
