package com.wqz.estate.keeper.bean;

import com.wqz.estate.keeper.pojo.AnjukeAddressInfo;
import com.wqz.estate.keeper.pojo.LianjiaAddressInfo;

public class AddressBean
{
	private Integer id;

    private String name;

    private String address;

    private Double lon;

    private Double lat;

    private String src;
    
    public AddressBean(AnjukeAddressInfo anjukeAddressInfo)
    {
    	this.id = anjukeAddressInfo.getId();
    	this.name = anjukeAddressInfo.getName();
    	this.address = anjukeAddressInfo.getAddress();
    	this.lon = anjukeAddressInfo.getLon();
    	this.lat = anjukeAddressInfo.getLat();
    	this.src = "AnJuKe";
    }
    
    public AddressBean(LianjiaAddressInfo lianjiaAddressInfo)
    {
    	this.id = lianjiaAddressInfo.getId();
    	this.name = lianjiaAddressInfo.getName();
    	this.address = lianjiaAddressInfo.getAddress();
    	this.lon = lianjiaAddressInfo.getLon();
    	this.lat = lianjiaAddressInfo.getLat();
    	this.src = "LianJia";
    }
    
    public AddressBean()
    {
    	
    }
    
    public String getSrc()
    {
    	return src;
    }
    
    public void setSrc(String src)
    {
    	this.src = src;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
