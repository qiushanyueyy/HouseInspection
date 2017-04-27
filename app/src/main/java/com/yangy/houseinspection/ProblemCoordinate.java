package com.yangy.houseinspection;

import java.io.Serializable;

/**
 * 存放问题坐标的实体类
 * Created by yangy on 2017/04/27
 */

public class ProblemCoordinate implements Serializable {
    private String X;
    private String Y;
    public ProblemCoordinate(String X, String Y) {
        this.X = X;
        this.Y = Y;
    }
    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    public String getY() {
        return Y;
    }

    public void setY(String y) {
        Y = y;
    }
}
