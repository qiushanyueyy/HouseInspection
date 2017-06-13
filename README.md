# HouseInspection
Android custom drawing label controls, can be directly marked on the drawings

![image](https://github.com/qiushanyueyy/HouseInspection/blob/master/app/src/image/1.png)
![image](https://github.com/qiushanyueyy/HouseInspection/blob/master/app/src/image/2.png)

# Dependencies
* Gradle：
```groovy
compile 'com.yanzhenjie:permission:1.0.5'
```

# 手势实现：重写onTouchEvent()方法监听触摸事件：   
```java
当只有一个手指触摸时设置mode状态为DRAG，代表偏移操作。
MotionEvent.ACTION_DOWN:获得触摸点坐标和触摸时间，
MotionEvent.ACTION_MOVE:根据触摸点坐标和当前坐标计算偏移量设置偏移。 
MotionEvent.ACTION_UP:如果触摸时间和抬起时间间隔小于200毫秒，并且触摸点在图片上，则算作点击事件。
当两只手指触摸时设置mode状态为ZOOM，代表缩放操作。
MotionEvent.ACTION_POINTER_DOWN:记录按下时两点的距离（通过spacing()方法获得按下时两点的距离），
MotionEvent.ACTION_MOVE:根据按下时记录的XY坐标和当前XY坐标计算偏移量，
MotionEvent.ACTION_UP:记录缩放偏移之后的Matri信息，通过setEdge()方法判断缩放比例有么有超过最大值或最小值。
点击事件onDrawClick（x,y）传入点击的坐标，如果点击在任务圆点范围内，则调用回调接口的problem（）将信息传递给Activity。
如果不在任务圆点范围内，则调用isPolygonContainsPoint（）方法判断点击是否在房间范围内，根据type判断状态，
如果是修改问题坐标则调用接口回调的modify（）方法将信息传递给Activity，如果是录入问题则调用接口回调的input（）方法将信息传递给Activity。
```

# 适配Android6.0权限
[AndPermission](https://github.com/yanzhenjie/AndPermission)
```java
MainActivity中实现PermissionListener接口 
调用andPermissions（）方法添加6.0权限
```
# Permission
```xml
<uses-permission android:name="android.permission.WRITE_SETTINGS" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
<uses-permission android:name="android.permission.INTERNET"/>
```

# 工具类和实体类
```java
CoordinateBean储存房间坐标的实体类
TaggingBean储存通过标准比例处理后任务圆点的坐标的实体类
RoomBean储存通过标准比例处理后房间名称的坐标的实体类
ProblemListBean储存问题信息的实体类
ProblemCoordinate储存问题坐标的实体类
RoomListBean储存房间信息的实体类
IDrawingView回调接口
BitmapUtils 通过图片路径获取图片的bitmap对象（支持本地路径和网络路径）
CoordinateUtils 坐标适配处理的帮助类
```
