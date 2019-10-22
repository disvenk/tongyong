package com.resto.brand.web.model;

import java.io.Serializable;
import java.util.Date;

public class Wether implements Serializable {
    private Long id;

    private Long areaId;//区域id

    private String dayWeather;//白天天气

    private String nightWeather;//晚上天气

    private Date dateTime; //时间

    private String shopId; //店铺id

    private Integer weekady; //星期几

    private Integer dayTemperature;//白天温度

    private Integer nightTemperature; //晚上温度

    private String cityName; //城市名称

    private String provinceName;//省名称

    private String code;//邮编

    private String longitude;//纬度

    private String latitude;//经度

    private String dayWeatherPic; //白天天气图片

    private String nightWeatherPic;//晚上天气图片

    public String getDayWeatherPic() {
        return dayWeatherPic;
    }

    public void setDayWeatherPic(String dayWeatherPic) {
        this.dayWeatherPic = dayWeatherPic;
    }

    public String getNightWeatherPic() {
        return nightWeatherPic;
    }

    public void setNightWeatherPic(String nightWeatherPic) {
        this.nightWeatherPic = nightWeatherPic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getDayWeather() {
        return dayWeather;
    }

    public void setDayWeather(String dayWeather) {
        this.dayWeather = dayWeather == null ? null : dayWeather.trim();
    }

    public String getNightWeather() {
        return nightWeather;
    }

    public void setNightWeather(String nightWeather) {
        this.nightWeather = nightWeather == null ? null : nightWeather.trim();
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public Integer getWeekady() {
        return weekady;
    }

    public void setWeekady(Integer weekady) {
        this.weekady = weekady;
    }

    public Integer getDayTemperature() {
        return dayTemperature;
    }

    public void setDayTemperature(Integer dayTemperature) {
        this.dayTemperature = dayTemperature;
    }

    public Integer getNightTemperature() {
        return nightTemperature;
    }

    public void setNightTemperature(Integer nightTemperature) {
        this.nightTemperature = nightTemperature;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName == null ? null : provinceName.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }
}