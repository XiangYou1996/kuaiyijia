package com.example.kuaiyijia.Entity;

/*
Author by: xy
Coding On 2021/3/11;
*/
public class Location {
    private double Latitude;
    private double Longitude;
    private String Address;
    private String Country;
    private String Province;
    private String City;
    private String Street;
    private String StreetNum;
    private String CityCode;
    private String AreaCode;

    public Location(double latitude, double longitude, String address, String country, String province,
                    String city, String street, String streetNum, String cityCode, String areaCode) {
        Latitude = latitude;
        Longitude = longitude;
        Address = address;
        Country = country;
        Province = province;
        City = city;
        Street = street;
        StreetNum = streetNum;
        CityCode = cityCode;
        AreaCode = areaCode;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getStreetNum() {
        return StreetNum;
    }

    public void setStreetNum(String streetNum) {
        StreetNum = streetNum;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String cityCode) {
        CityCode = cityCode;
    }

    public String getAreaCode() {
        return AreaCode;
    }

    public void setAreaCode(String areaCode) {
        AreaCode = areaCode;
    }
}
