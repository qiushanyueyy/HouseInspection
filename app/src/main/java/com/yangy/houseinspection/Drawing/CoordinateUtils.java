package com.yangy.houseinspection.Drawing;

import java.util.ArrayList;
import java.util.List;

/**
 * 坐标适配处理的帮助类
 * Created by yangy on 2017/04/27
 */

public class CoordinateUtils {
    /**
     * 计算适配后房间名称的坐标
     *
     * @param area_coordinate 后台返回的房间信息的原数据
     * @param proportion      标准比例
     * @return 适配后的房间名称坐标
     */
    public static List<WzBean> getWzBean(List<RoomListBean> area_coordinate, float proportion) {
        List<WzBean> wzList = new ArrayList<>();
        //房间名称
        for (int i = 0; i < area_coordinate.size(); i++) {
            if (area_coordinate.get(i).getCenterCoordinate() != null) {
                wzList.add(new WzBean(area_coordinate.get(i).getRoomName(), area_coordinate.get(i).getCenterCoordinate().getX() * proportion, area_coordinate.get(i).getCenterCoordinate().getY() * proportion));
            }
        }
        return wzList;
    }

    /**
     * 计算适配后的房间坐标系
     *
     * @param area_coordinate 后台返回的房间信息的原数据
     * @param proportion      标准比例
     * @return 适配后的房间坐标系
     */
    public static List<RoomListBean> getRoomListBeanList(List<RoomListBean> area_coordinate, float proportion) {
        List<RoomListBean> roomListBeenEx = new ArrayList<>();
        for (RoomListBean roomListBean : area_coordinate) {
            for (int i = 0; i < roomListBean.getCoordinate().size(); i++) {
                roomListBean.getCoordinate().set(i, new CoordinateBean(roomListBean.getCoordinate().get(i).getX() * proportion, roomListBean.getCoordinate().get(i).getY() * proportion));
            }
            roomListBeenEx.add(roomListBean);
        }
        return roomListBeenEx;
    }

    /**
     * 计算适配后任务圆点的坐标
     *
     * @param problemList 后台返回的任务信息的原数据
     * @param proportion  标准比例
     * @return 适配后的任务圆点坐标
     */
    public static List<TaggingBean> getTaggingBean(List<ProblemListBean> problemList, float proportion) {
        List<TaggingBean> taggingBeanList = new ArrayList<>();
        for (int i = 0; i < problemList.size(); i++) {
            if (problemList.get(i).getProblemCoordinate() != null) {
                taggingBeanList.add(new TaggingBean(problemList.get(i).getProblemId(), problemList.get(i).getProblemState(), (int) (Float.parseFloat(problemList.get(i).getProblemCoordinate().getX()) * proportion), (int) (Float.parseFloat(problemList.get(i).getProblemCoordinate().getY()) * proportion)));
            }
        }
        return taggingBeanList;
    }

    /**
     * 通过线性规则计算点击的坐标点是否在房间区域范围内
     *
     * @param mPoints 各房间的坐标集合
     * @param x_pos   点击的X轴坐标
     * @param y_pos   点击的Y轴坐标
     * @return 点击的位置所属房间的ID
     */
    public static RoomListBean isPolygonContainsPoint(List<RoomListBean> mPoints, float x_pos, float y_pos) {
        for (int i = 0; i < mPoints.size(); i++) {
            int nCross = 0;
            for (int j = 0; j < mPoints.get(i).getCoordinate().size(); j++) {
                CoordinateBean p1 = mPoints.get(i).getCoordinate().get(j);
                CoordinateBean p2 = mPoints.get(i).getCoordinate().get((j + 1) % mPoints.get(i).getCoordinate().size());
                // 求解 y=p.y 与 p1p2 的交点
                if (p1.getY() == p2.getY()) // p1p2 与 y=p0.y平行
                    continue;
                // 交点在p1p2延长线上
                if (y_pos < Math.min(p1.getY(), p2.getY()))
                    continue;
                // 交点在p1p2延长线上
                if (y_pos >= Math.max(p1.getY(), p2.getY()))
                    continue;
                // 求交点的 X 坐标
                double x = (double) (y_pos - (p1.getY())) * (double) (p2.getX() - p1.getX())
                        / (double) ((p2.getY()) - (p1.getY())) + p1.getX();
                if (x > x_pos)
                    nCross++; // 只统计单边交点
            }
            if ((nCross % 2 == 1)) {// 单边交点为奇数，点在多边形内
                return mPoints.get(i);
            }
        }
        return null;
    }
}
