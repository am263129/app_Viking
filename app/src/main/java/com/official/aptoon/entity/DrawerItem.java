package com.official.aptoon.entity;

public class DrawerItem {
    public Integer icon_id;
    public String text;

    public DrawerItem(Integer icon_id, String text){
        this.icon_id = icon_id;
        this.text = text;
    }

    public Integer getIconId() {
        return icon_id;
    }

    public String getText() {
        return text;
    }

    public void setIcon_id(Integer icon_id) {
        this.icon_id = icon_id;
    }

    public void setText(String text) {
        this.text = text;
    }
}
