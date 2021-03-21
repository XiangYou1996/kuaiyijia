package com.example.kuaiyijia.Entity;

/*
Author by: xy
Coding On 2021/3/14;
*/
public class ProfitDetailItem {
    private String OrderID ;
    private String SendAddress;
    private String ReceiveAddress;
    private String profit;

    public ProfitDetailItem(String orderID, String sendAddress, String receiveAddress, String profit) {
        OrderID = orderID;
        SendAddress = sendAddress;
        ReceiveAddress = receiveAddress;
        this.profit = profit;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getSendAddress() {
        return SendAddress;
    }

    public void setSendAddress(String sendAddress) {
        SendAddress = sendAddress;
    }

    public String getReceiveAddress() {
        return ReceiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        ReceiveAddress = receiveAddress;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }
}
