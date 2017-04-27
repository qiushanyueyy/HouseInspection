package com.yangy.houseinspection.Drawing;

/**
 * Created by yangy on 2017/04/27
 */

import com.yangy.houseinspection.ProblemListBean;

/**
 * 用于自定义View绘画操作之后通知Activity的接口
 * 如果需要 可以通过接口传递一些参数
 */
public interface IDrawingView {

    void input(float x_pos, float y_pos);//问题录入
    void modify(float x_pos, float y_pos);//修改问题坐标
    void problem(ProblemListBean problemListBean);//选中问题

}
