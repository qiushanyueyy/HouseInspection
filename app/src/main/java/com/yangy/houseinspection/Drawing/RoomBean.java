package com.yangy.houseinspection.Drawing;

/**
 * 存放通过标准比例处理后房间名称的坐标
 * Created by yangy on 2017/04/27
 */
public class RoomBean {
    private String text;
    private float posX;
    private float posY;

    public RoomBean(String text, float posX, float posY) {
        this.text = text;
        this.posX = posX;
        this.posY = posY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }
}
