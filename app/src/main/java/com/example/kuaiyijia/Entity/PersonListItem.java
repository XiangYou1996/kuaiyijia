package com.example.kuaiyijia.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PersonListItem implements Serializable, Parcelable {

    // 人员的ID
    private String person_ID;
    // name , telephone , webBranch belonged , station
    private String person_name;
    private String person_tel;
    private String person_webBranch;
    private String person_sation_ID;
    private String person_sation;

    public String getPerson_sation_ID() {
        return person_sation_ID;
    }

    public void setPerson_sation_ID(String person_sation_ID) {
        this.person_sation_ID = person_sation_ID;
    }

    public PersonListItem(String person_ID, String person_name, String person_tel, String person_webBranch, String person_sation_ID, String person_sation) {
        this.person_ID = person_ID;
        this.person_name = person_name;
        this.person_tel = person_tel;
        this.person_webBranch = person_webBranch;
        this.person_sation_ID = person_sation_ID;
        this.person_sation = person_sation;
    }

    protected PersonListItem(Parcel in) {
        person_ID = in.readString();
        person_name = in.readString();
        person_tel = in.readString();
        person_webBranch = in.readString();
        person_sation_ID = in.readString();
        person_sation = in.readString();
    }

    public static final Creator<PersonListItem> CREATOR = new Creator<PersonListItem>() {
        @Override
        public PersonListItem createFromParcel(Parcel in) {
            return new PersonListItem(in);
        }

        @Override
        public PersonListItem[] newArray(int size) {
            return new PersonListItem[size];
        }
    };

    public String getPerson_ID() {
        return person_ID;
    }

    public void setPerson_ID(String person_ID) {
        this.person_ID = person_ID;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPerson_tel() {
        return person_tel;
    }

    public void setPerson_tel(String person_tel) {
        this.person_tel = person_tel;
    }

    public String getPerson_webBranch() {
        return person_webBranch;
    }

    public void setPerson_webBranch(String person_webBranch) {
        this.person_webBranch = person_webBranch;
    }

    public String getPerson_sation() {
        return person_sation;
    }

    public void setPerson_sation(String person_sation) {
        this.person_sation = person_sation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(person_ID);
        dest.writeString(person_name);
        dest.writeString(person_tel);
        dest.writeString(person_webBranch);
        dest.writeString(person_sation_ID);
        dest.writeString(person_sation);
    }
}
