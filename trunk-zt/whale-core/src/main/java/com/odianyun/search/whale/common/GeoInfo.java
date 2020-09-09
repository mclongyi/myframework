package com.odianyun.search.whale.common;

import java.util.List;

/**
 * Created by fishcus on 16/12/28.
 */
public class GeoInfo {
    //结构化地址信息
    private String formatted_address;
    //地址所在的省份名
    private String province;
    //地址所在的城市名
    private String city;
    //城市编码
    private String citycode;
    //地址所在的区
    private String districts;
    //地址所在的乡镇
    private List<String> township;
    //街道
    private List<String> street;
    //门牌
    private List<String> number;
    //区域编码
    private String adcode;
    //坐标点
    private String location;
    //匹配级别
    private String level;

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getDistricts() {
        return districts;
    }

    public void setDistricts(String districts) {
        this.districts = districts;
    }

    public List<String> getTownship() {
        return township;
    }

    public void setTownship(List<String> township) {
        this.township = township;
    }

    public List<String> getStreet() {
        return street;
    }

    public void setStreet(List<String> street) {
        this.street = street;
    }

    public List<String> getNumber() {
        return number;
    }

    public void setNumber(List<String> number) {
        this.number = number;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "GeoInfo{" +
                "formatted_address='" + formatted_address + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", citycode='" + citycode + '\'' +
                ", district='" + districts + '\'' +
                ", township=" + township +
                ", street=" + street +
                ", number=" + number +
                ", adcode='" + adcode + '\'' +
                ", location='" + location + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
