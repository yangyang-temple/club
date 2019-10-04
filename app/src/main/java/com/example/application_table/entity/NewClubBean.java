package com.example.application_table.entity;

public class NewClubBean {
    private int imageId;//社团图片
    private String name;//社团名称
    private String info;//社团简介
    private String type;//社团所属类型

    public NewClubBean() {
    }

    public NewClubBean(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public NewClubBean(String name, String info, String type) {
        this.name = name;
        this.info = info;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
