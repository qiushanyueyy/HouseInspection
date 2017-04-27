package com.yangy.houseinspection.Drawing;

import java.io.Serializable;

/**
 * 存放房间坐标的实体类
 * Created by yangy on 2017/04/27
 */

public class CoordinateBean implements Serializable {
    public float X;
    public float Y;

    public CoordinateBean(float X, float Y) {
        this.X = X;
        this.Y = Y;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }
}
