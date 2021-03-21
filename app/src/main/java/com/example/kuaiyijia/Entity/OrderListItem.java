package com.example.kuaiyijia.Entity;

import java.io.Serializable;

public class OrderListItem implements Serializable {
    private String order_num;
    private String to_address;
    private String re_address;
    private String  num_item;
    private String   agency_money;

    public OrderListItem(String order_num, String to_address, String re_address, String  num_item, String  agency_money) {
        this.order_num = order_num;
        this.to_address = to_address;
        this.re_address = re_address;
        this.num_item = num_item;
        this.agency_money = agency_money;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getRe_address() {
        return re_address;
    }

    public void setRe_address(String re_address) {
        this.re_address = re_address;
    }

    public String  getNum_item() {
        return num_item;
    }

    public void setNum_item(String  num_item) {
        this.num_item = num_item;
    }

    public String  getAgency_money() {
        return agency_money;
    }

    public void setAgency_money(String  agency_money) {
        this.agency_money = agency_money;
    }
}

