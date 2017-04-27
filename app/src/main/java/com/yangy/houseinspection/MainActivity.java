package com.yangy.houseinspection;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yangy.houseinspection.Drawing.CoordinateBean;
import com.yangy.houseinspection.Drawing.DrawingView;
import com.yangy.houseinspection.Drawing.IDrawingView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PermissionListener ,IDrawingView {
    private static Toast mToast;
    private float proportion;//跟后台约定的标准比例
    private String state;//问题状态
    private String X;//查看时传入的X轴坐标
    private String Y;//查看时传入的Y轴坐标
    private List<RoomListBean> areaCoordinate = new ArrayList<>();//房间坐标系
    private List<ProblemListBean> problemList = new ArrayList<>();//任务坐标系

    private DrawingView myView;
    private TextView tv_xian;
    private LinearLayout ll_bottom;

    private String type = "";//see代表查看 modify代表修改  空代表问题录入
    private String graphPath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493113149437&di=a83acefadf4ccdf5146c0a68dfd2ed35&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F17%2F71%2F69%2F557d700b125ed_1024.jpg";


    private Bitmap bitmap = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (bitmap != null) {
                    if (!myView.setBitmapCoordinate(bitmap,MainActivity.this, areaCoordinate, problemList,proportion, type, X, Y, state)) {
                        showShort("显示图纸失败");
                    }
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        andPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);//适配6.0权限
        myView = (DrawingView)findViewById(R.id.myView);
        myView.setInterfaceCallback(this);
        tv_xian = (TextView) findViewById(R.id.tv_xian);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        /**因为手机的坐标是从左上角开始计算，如果后台没有进行处理的话  可能返回的Y轴与要显示的效果正好相反  需要用图片高度减去Y轴得到正确的显示数据**/
        List<CoordinateBean> coordinate = new ArrayList<>();
        coordinate.add(new CoordinateBean(200,100));
        coordinate.add(new CoordinateBean(400,100));
        coordinate.add(new CoordinateBean(400,300));
        coordinate.add(new CoordinateBean(200,300));
        areaCoordinate.add(new RoomListBean("1","厨房",coordinate,new CoordinateBean(300,200)));
        List<CoordinateBean> coordinates = new ArrayList<>();
        coordinates.add(new CoordinateBean(600,50));
        coordinates.add(new CoordinateBean(900,50));
        coordinates.add(new CoordinateBean(900,300));
        coordinates.add(new CoordinateBean(600,300));
        areaCoordinate.add(new RoomListBean("2","次卧",coordinates,new CoordinateBean(750,180)));
//        /**模拟修改**/
//        type = "modify";
//        X = "200";
//        Y = "134";
//        state = "1";
//        /**模拟查看**/
//        type = "see";
//        X = "200";
//        Y = "300";
//        state = "2";
        if (!TextUtils.isEmpty(type)){
            tv_xian.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.GONE);
        }
        loadImageNet();
    }
    /**
     * 获取图片bitmap对象
     */
    private void loadImageNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = BitmapUtils.getBitmap(graphPath);
                //跟后台约定宽度固定为1080进行不同尺寸屏幕的适配
                float standard = (float) 1080.0;
                //跟后台约定的标准比例
                proportion = bitmap.getWidth()/standard;
//                //根据标准比例换算过后的图片高度
//                float imgHeight =bitmap.getHeight()/proportion;
                handler.sendEmptyMessage(0);
            }
        }).start();
    }
    /**
     * 短时间显示Toast
     */
    public void showShort(CharSequence message) {
        if(mToast == null) {
            mToast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        }else{
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 适配6.0权限
     * 复制下面所有内容 build里配置远程库compile 'com.yanzhenjie:permission:1.0.5'
     * implements PermissionListener  实现接口
     **/
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 100;
    private static final int REQUEST_CODE_SETTING = 300;
    /**
     * 申请权限
     */
    public static void andPermissions(Activity activity, @NonNull String... permissions){
        if (Build.VERSION.SDK_INT >= 23) {
            // 先判断是否有权限。
            if(AndPermission.hasPermission(activity, permissions)) {
                // 有权限，直接do anything.
                return;
            } else {
                // 申请权限。
                AndPermission.with(activity)
                        .requestCode(REQUEST_CODE_PERMISSION_LOCATION)
                        .permission(permissions)
                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                        .rationale(rationaleListener)
                        .send();
            }
        }
    }
    private static RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode,Rationale rationale) {
            rationale.resume();
        }
    };
    @Override
    public void onSucceed(int requestCode, List<String> grantPermissions) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION: {
                showShort("获取权限成功");
                break;
            }
        }
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION: {
                showShort("获取权限失败");
                break;
            }
        }

        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();

            // 第二种：用自定义的提示语。
//             AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("权限申请失败")
//                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
//                     .setPositiveButton("好，去设置")
//                     .show();

//            第三种：自定义dialog样式。
//            SettingService settingHandle = AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            你的dialog点击了确定调用：
//            settingHandle.execute();
//            你的dialog点击了取消调用：
//            settingHandle.cancel();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // listener方式，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void input(float x_pos, float y_pos) {//录入问题的回调
        showShort("录入回调");
        problemList.add(new ProblemListBean("任务"+problemList.size(),"0",new ProblemCoordinate(""+x_pos/proportion,""+y_pos/proportion)));
        myView.setRefresh(X,Y,state,problemList);
//        Intent intent = new Intent(this, ProblemEntryActivity.class);
//        intent.putExtra("roomId", key);
//        intent.putExtra("roomName", roomName);
//        intent.putExtra("houseNumberName", houseNumberName);
//        intent.putExtra("buildingName",buildingName);
//        intent.putExtra("houseNumberId", houseNumberId);
//        intent.putExtra("taskSrc", taskSrc);
//        intent.putExtra("taskId", taskId);
//        intent.putExtra("X",""+x_pos);
//        intent.putExtra("Y",""+y_pos);
//        intent.putExtra("proportion", ""+proportion);
//        startActivity(intent);
    }

    @Override
    public void modify(float x_pos, float y_pos) {//修改的回调
        showShort("修改回调");
        X = ""+x_pos;
        Y = ""+y_pos;
        myView.setRefresh(X,Y,state,problemList);
//        Intent intent = new Intent();
//        intent.putExtra("roomId", key);
//        intent.putExtra("roomName", roomName);
//        intent.putExtra("houseNumberName", houseNumberName);
//        intent.putExtra("buildingName",buildingName);
//        intent.putExtra("houseNumberId", houseNumberId);
//        intent.putExtra("taskSrc", taskSrc);
//        intent.putExtra("taskId", taskId);
//        intent.putExtra("X",""+x_pos);
//        intent.putExtra("Y",""+y_pos);
//        intent.putExtra("proportion", ""+proportion);
//        setResult(ProblemEntryActivity.GET_DRAWING_VIEW, intent);
//        finish();
    }

    @Override
    public void problem(ProblemListBean problemListBean) {//选中问题的回调
        showShort("问题回调"+problemListBean.getProblemId());
//        Intent intent = new Intent(this, DetailsOfTheProblemActivity.class);
//        intent.putExtra("problemList", problemList.get(i));
//        startActivity(intent);
//        return;
    }
}