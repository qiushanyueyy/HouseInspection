package com.yangy.houseinspection.Drawing;

import com.yangy.houseinspection.ProblemCoordinate;

import java.io.Serializable;
import java.util.List;

/**
 * 存放问题信息的实体类
 * Created by yangy on 2017/04/27
 */

public class ProblemListBean implements Serializable {
    private String problemId;//问题ID
    private String problemState;//问题状态
    private ProblemCoordinate problemCoordinate;//问题坐标

    public ProblemListBean(String problemId, String problemState, ProblemCoordinate problemCoordinate) {
        this.problemId = problemId;
        this.problemState = problemState;
        this.problemCoordinate = problemCoordinate;
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

    public ProblemCoordinate getProblemCoordinate() {
        return problemCoordinate;
    }

    public void setProblemCoordinate(ProblemCoordinate problemCoordinate) {
        this.problemCoordinate = problemCoordinate;
    }
}
