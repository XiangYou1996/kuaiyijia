package com.example.kuaiyijia.Entity;

/*
Author by: xy
Coding On 2021/3/15;
*/
public class ProfitDetailMoreItem {
    private String OrderID;
    private String station;
    private String profit;

    public ProfitDetailMoreItem(String orderID, String station, String profit) {
        OrderID = orderID;
        this.station = station;
        this.profit = profit;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }
}
