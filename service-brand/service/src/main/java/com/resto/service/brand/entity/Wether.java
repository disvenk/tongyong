package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Wether implements Serializable {

    private static final long serialVersionUID = 5720502907722685198L;

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
}