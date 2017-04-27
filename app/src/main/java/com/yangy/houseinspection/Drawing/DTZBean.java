package com.yangy.houseinspection.Drawing;

/**
 * 存放通过标准比例处理后任务圆点的坐标
 * Created by yangy on 2017/04/27.
 */
public class DTZBean {
    private String problemId;
    private int posX;
    private int poxY;

    public DTZBean(String problemId, int posX, int poxY) {

        this.problemId = problemId;
        this.posX = posX;
        this.poxY = poxY;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPoxY() {
        return poxY;
    }

    public void setPoxY(int poxY) {
        this.poxY = poxY;
    }

    public DTZBean(int posX, int poxY) {

        this.posX = posX;
        this.poxY = poxY;
    }

    public DTZBean() {

    }
}
