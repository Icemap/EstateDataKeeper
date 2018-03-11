package com.wqz.estate.keeper.pojo;

public class LianjiaHouseData {
    private Integer id;

    private String contentUrl;

    private String imgUrl;

    private String title;

    private String address;

    private String roomNum;

    private Double area;

    private String faceto;

    private String decoration;

    private Byte haslift;

    private String flood;

    private Integer buildtime;

    private String structure;

    private String district;

    private String taxfree;

    private Double totalprice;

    private Double unitprice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl == null ? null : contentUrl.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum == null ? null : roomNum.trim();
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getFaceto() {
        return faceto;
    }

    public void setFaceto(String faceto) {
        this.faceto = faceto == null ? null : faceto.trim();
    }

    public String getDecoration() {
        return decoration;
    }

    public void setDecoration(String decoration) {
        this.decoration = decoration == null ? null : decoration.trim();
    }

    public Byte getHaslift() {
        return haslift;
    }

    public void setHaslift(Byte haslift) {
        this.haslift = haslift;
    }

    public String getFlood() {
        return flood;
    }

    public void setFlood(String flood) {
        this.flood = flood == null ? null : flood.trim();
    }

    public Integer getBuildtime() {
        return buildtime;
    }

    public void setBuildtime(Integer buildtime) {
        this.buildtime = buildtime;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure == null ? null : structure.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public String getTaxfree() {
        return taxfree;
    }

    public void setTaxfree(String taxfree) {
        this.taxfree = taxfree == null ? null : taxfree.trim();
    }

    public Double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Double totalprice) {
        this.totalprice = totalprice;
    }

    public Double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(Double unitprice) {
        this.unitprice = unitprice;
    }
}