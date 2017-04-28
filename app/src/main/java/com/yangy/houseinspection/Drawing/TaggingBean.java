package com.yangy.houseinspection.Drawing;

/**
 * 存放通过标准比例处理后任务圆点的坐标
 * Created by yangy on 2017/04/27.
 */
public class TaggingBean {
    private String problemId;//问题ID
    private String problemState;//问题状态
    private int posX;
    private int poxY;

    public TaggingBean(String problemId, String problemState, int posX, int poxY) {
        this.problemId = problemId;
        this.problemState = problemState;
        this.posX = posX;
        this.poxY = poxY;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getProblemState() {
        return problemState;
    }

    public void setProblemState(String problemState) {
        this.problemState = problemState;
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

    public TaggingBean(int posX, int poxY) {

        this.posX = posX;
        this.poxY = poxY;
    }

    public TaggingBean() {

    }
}
