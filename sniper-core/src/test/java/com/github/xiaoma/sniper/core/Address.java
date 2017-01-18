package com.github.xiaoma.sniper.core;

import java.io.Serializable;

/**
 * Created by machunxiao on 17/1/18.
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 4233903753048464738L;

    private long id;
    private String name;
    private int provinceId;
    private String provinceName;
    private int cityId;
    private String cityName;

    public Address() {
    }

    public Address(long id, String name, int provinceId, String provinceName, int cityId, String cityName) {
        this.id = id;
        this.name = name;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", provinceId=" + provinceId +
                ", provinceName='" + provinceName + '\'' +
                ", cityId=" + cityId +
                ", cityName=" + cityName +
                '}';
    }
}
