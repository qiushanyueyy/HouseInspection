package com.yangy.houseinspection.Drawing;

import java.io.Serializable;
import java.util.List;

/**
 * 存放房间信息的实体类
 * Created by yangy on 2017/04/27
 */

public class RoomListBean implements Serializable {
    private String roomId;//房间ID
    private String roomName;//房间名称
    private List<CoordinateBean> coordinate;//房间区域坐标系
    private CoordinateBean centerCoordinate;//房间名字坐标

    public RoomListBean(String roomId,String roomName,List<CoordinateBean> coordinate,CoordinateBean centerCoordinate){
        this.roomId = roomId;
        this.roomName = roomName;
        this.coordinate = coordinate;
        this.centerCoordinate = centerCoordinate;
    }
    public CoordinateBean getCenterCoordinate() {
        return centerCoordinate;
    }

    public void setCenterCoordinate(CoordinateBean centerCoordinate) {
        this.centerCoordinate = centerCoordinate;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<CoordinateBean> getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(List<CoordinateBean> coordinate) {
        this.coordinate = coordinate;
    }
}
