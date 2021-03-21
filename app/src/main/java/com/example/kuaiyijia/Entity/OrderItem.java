package com.example.kuaiyijia.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/*
Author by: xy
Coding On 2021/3/16;
*/
public class OrderItem implements Parcelable {
    private String ID;
    private String order_number;
    private String huowu_status;
    private String send_address;
    private String receive_address;
    private String order_status;
    private String daishou_money;
    private String dianfu_money;
    private String receive_station;
    private String receive_person;
    private String receive_tel;
    private String send_person;
    private String send_tel;
    private String service_way;
    private String addition;
    private String item_nums;
    private String mail_fess;

    public String getMail_fess() {
        return mail_fess;
    }

    public void setMail_fess(String mail_fess) {
        this.mail_fess = mail_fess;
    }

    public OrderItem(String ID, String order_number, String huowu_status, String send_address, String receive_address, String order_status, String daishou_money, String dianfu_money, String receive_station, String receive_person, String receive_tel, String send_person,
                     String send_tel, String service_way, String addition, String item_nums, String mail_fess) {
        this.ID = ID;
        this.order_number = order_number;
        this.huowu_status = huowu_status;
        this.send_address = send_address;
        this.receive_address = receive_address;
        this.order_status = order_status;
        this.daishou_money = daishou_money;
        this.dianfu_money = dianfu_money;
        this.receive_station = receive_station;
        this.receive_person = receive_person;
        this.receive_tel = receive_tel;
        this.send_person = send_person;
        this.send_tel = send_tel;
        this.service_way = service_way;
        this.addition = addition;
        this.item_nums = item_nums;
        this.mail_fess = mail_fess;
    }

    protected OrderItem(Parcel in) {
        ID = in.readString();
        order_number = in.readString();
        huowu_status = in.readString();
        send_address = in.readString();
        receive_address = in.readString();
        order_status = in.readString();
        daishou_money = in.readString();
        dianfu_money = in.readString();
        receive_station = in.readString();
        receive_person = in.readString();
        receive_tel = in.readString();
        send_person = in.readString();
        send_tel = in.readString();
        service_way = in.readString();
        addition = in.readString();
        item_nums = in.readString();
        mail_fess = in.readString();
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getHuowu_status() {
        return huowu_status;
    }

    public void setHuowu_status(String huowu_status) {
        this.huowu_status = huowu_status;
    }

    public String getSend_address() {
        return send_address;
    }

    public void setSend_address(String send_address) {
        this.send_address = send_address;
    }

    public String getReceive_address() {
        return receive_address;
    }

    public void setReceive_address(String receive_address) {
        this.receive_address = receive_address;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getDaishou_money() {
        return daishou_money;
    }

    public void setDaishou_money(String daishou_money) {
        this.daishou_money = daishou_money;
    }

    public String getDianfu_money() {
        return dianfu_money;
    }

    public void setDianfu_money(String dianfu_money) {
        this.dianfu_money = dianfu_money;
    }

    public String getReceive_station() {
        return receive_station;
    }

    public void setReceive_station(String receive_station) {
        this.receive_station = receive_station;
    }

    public String getReceive_person() {
        return receive_person;
    }

    public void setReceive_person(String receive_person) {
        this.receive_person = receive_person;
    }

    public String getReceive_tel() {
        return receive_tel;
    }

    public void setReceive_tel(String receive_tel) {
        this.receive_tel = receive_tel;
    }

    public String getSend_person() {
        return send_person;
    }

    public void setSend_person(String send_person) {
        this.send_person = send_person;
    }

    public String getSend_tel() {
        return send_tel;
    }

    public void setSend_tel(String send_tel) {
        this.send_tel = send_tel;
    }

    public String getService_way() {
        return service_way;
    }

    public void setService_way(String service_way) {
        this.service_way = service_way;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getItem_nums() {
        return item_nums;
    }

    public void setItem_nums(String item_nums) {
        this.item_nums = item_nums;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(order_number);
        dest.writeString(huowu_status);
        dest.writeString(send_address);
        dest.writeString(receive_address);
        dest.writeString(order_status);
        dest.writeString(daishou_money);
        dest.writeString(dianfu_money);
        dest.writeString(receive_station);
        dest.writeString(receive_person);
        dest.writeString(receive_tel);
        dest.writeString(send_person);
        dest.writeString(send_tel);
        dest.writeString(service_way);
        dest.writeString(addition);
        dest.writeString(item_nums);
        dest.writeString(mail_fess);
    }
}
