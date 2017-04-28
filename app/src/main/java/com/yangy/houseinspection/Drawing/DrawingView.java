package com.yangy.houseinspection.Drawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.yangy.houseinspection.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangy on 2017/04/27
 */
public class DrawingView extends View {

    private static final float MAX_SCALE = 2.0f;//最大缩放倍数
    private static final float MIN_SCALE = 1.0f;//最小缩放倍数
    public static final int DRAG = 1;//偏移
    public static final int ZOOM = 2;//缩放
    public static final int NONE = 0;
    private static final int AREA_SIZE = 10;//小圆点半径
    private float INIT_SCALE = 1.0f;//记录初始缩放倍数
    public int mode = 0;
    private List<RoomBean> roomList = new ArrayList<>();//存放处理后房间名称的坐标
    private List<TaggingBean> taggingBeanList = new ArrayList<>();//存放处理后任务信息
    public List<RoomListBean> roomListBeenEx = new ArrayList<>();//存放处理后的房间信息
    public String type;
    public String X;
    public String Y;
    public String state;
    /**
     * 绘制普通文字的画笔
     **/
    private Paint mTextPaint;
    /**
     * 绘制房间范围的画笔
     **/
    private Paint mPaint;
    /**
     * 文字的大小
     **/
    private float mTextSizeN;

    private int rectValue = 12;//任务圆点半径

    private Canvas mCanvas = null;
    private Bitmap mBitmap = null;//yuantu
    private Bitmap pointBitMap;//用于显示添加的任务圆点(待整改)
    private Bitmap pointBitMapTwo;//用于显示添加的任务圆点(待销项)
    private Bitmap pointBitMapThere;//用于显示添加的任务圆点(已销项)
    private Bitmap mBottomBitmap = null;// ditu

    private Paint mBitmapPaint = null;//zuotu

    private Matrix matrix = new Matrix();
    private Matrix startMatrix = new Matrix();//记录原图矩阵
    private Matrix saveMatrix = new Matrix();//记录缩放之后的矩阵
    private float[] values = new float[9];
    private float[] startValues = new float[9];

    private int viewWidth = 480;//控件的宽度
    private int viewHeight = 800;

    private float imgWidth = 480;//图片的宽度
    private float imgHeight = 800;

    private float x_down = 0;//记录触摸时的坐标
    private float y_down = 0;//记录触摸时的坐标
    private float initDis = 1f;//两点按下时的距离
    private long mLastTime = 0L;//记录触摸时间
    private IDrawingView presenter;

    public DrawingView(Context context) {
        super(context);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    /**
     * 初始化
     */
    private void init() {
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor("#01B81B"));
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#F04D1C"));
        pointBitMap = BitmapFactory.decodeResource(getResources(), R.mipmap.one_circular);//添加大头针
        pointBitMapTwo = BitmapFactory.decodeResource(getResources(), R.mipmap.two_circular);//添加大头针
        pointBitMapThere = BitmapFactory.decodeResource(getResources(), R.mipmap.there_circular);//添加大头针
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        matrix = new Matrix();
    }

    /**
     * 设置接口回调
     *
     * @param iDrawingView 接口对象
     */
    public void setInterfaceCallback(IDrawingView iDrawingView) {
        presenter = iDrawingView;
    }

    /**
     * 刷新视图   （演示用）
     */
    public void setRefresh(String X, String Y, String state, List<TaggingBean> taggingBeanList) {
        this.X = X;
        this.Y = Y;
        this.state = state;
        this.taggingBeanList = taggingBeanList;
        postInvalidate();
    }

    /**
     * 显示图片
     *
     * @param myImg           图片bitmap对象
     * @param roomList          存放处理后房间名称信息
     * @param taggingBeanList 存放处理后任务信息
     * @param roomListBeenEx  存放处理后的房间信息
     * @param type            see代表查看 modify代表修改  空代表问题录入
     * @param X               查看时传入的X轴坐标
     * @param Y               查看时传入的Y轴坐标
     * @param state           问题状态
     * @return true代表图片显示成功，false代表图片显示失败
     */
    public boolean setBitmapCoordinate(Bitmap myImg, List<RoomBean> roomList, List<TaggingBean> taggingBeanList, List<RoomListBean> roomListBeenEx, String type, String X, String Y, String state) {
        this.roomList = roomList;
        this.taggingBeanList = taggingBeanList;
        this.roomListBeenEx = roomListBeenEx;
        this.type = type;
        this.X = X;
        this.Y = Y;
        this.state = state;

        int width = myImg.getWidth();
        int height = myImg.getHeight();
        int tempW = myImg.getWidth();
        int tempH = myImg.getHeight();
        imgWidth = tempW;
        imgHeight = tempH;

        float nproportionale;
        float nyScale;
        float tScale = 1.0f;
        if (width != 0 && height != 0) {
            nproportionale = (float) width / viewWidth;
            nyScale = (float) height / viewHeight;
            if (nproportionale >= 1 && nyScale >= 1 || nproportionale < 1 && nyScale < 1) {
                if (nproportionale > nyScale) {
                    width = (int) (width / nproportionale);
                    height = (int) (height / nproportionale);
                    tScale = nproportionale;
                } else {
                    width = (int) (width / nyScale);
                    height = (int) (height / nyScale);
                    tScale = nyScale;
                }
            }
            if (nproportionale >= 1 && nyScale < 1) {
                width = viewWidth;
                height = (int) (height / nproportionale);
                tScale = nproportionale;
            }
            if (nproportionale <= 1 && nyScale >= 1) {
                height = viewHeight;
                width = (int) (width / nyScale);
                tScale = nyScale;
            }
            matrix.postScale(1 / tScale, 1 / tScale);
            /**图片居中显示**/
            matrix.postTranslate((viewWidth - width) / 2, (viewHeight - height) / 2);

            saveMatrix.set(matrix);
            startMatrix.set(matrix);
            getImageViewIneerSize(true);
            if (myImg.getWidth() > 4000) {
                tempW = tempW / 2;
                tempH = tempH / 2;
            }
            mBitmap = Bitmap.createScaledBitmap(myImg, tempW, tempH, true);
            mBottomBitmap = Bitmap.createBitmap(tempW, tempH, Bitmap.Config.ARGB_4444);
            if (mBottomBitmap == null) {
                mBitmap.recycle();
                return false;
            }
            mCanvas.setBitmap(mBottomBitmap);
            mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            postInvalidate();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;

        mBottomBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBottomBitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(0x00FFFFFF);
        mCanvas.setBitmap(mBottomBitmap);
        if (mBitmap != null) {
            mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        }
        canvas.drawBitmap(mBottomBitmap, matrix, mBitmapPaint);

        /** 房间名称 **/
        mTextSizeN = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14 / values[0], getResources().getDisplayMetrics());//根据缩放系数计算需要绘制的房间名称的字体大小
        mTextPaint.setTextSize(mTextSizeN);
        for (RoomBean roomBean : roomList) {
            //绘制显示房间名称的底部背景
            mTextPaint.setColor(Color.parseColor("#01B81B"));
            mCanvas.drawRect(roomBean.getPosX() - (roomBean.getText().toString().length() * mTextSizeN) / 2 - 3, roomBean.getPosY() - (mTextSizeN / 2 + 3), roomBean.getPosX() + (roomBean.getText().toString().length() * mTextSizeN) / 2 + 3, roomBean.getPosY() + (mTextSizeN / 2 + 3), mTextPaint);
            //绘制房间名称
            mTextPaint.setColor(Color.parseColor("#FFFFFF"));
            mCanvas.drawText(roomBean.getText(), roomBean.getPosX() - (roomBean.getText().toString().length() * mTextSizeN) / 2, roomBean.getPosY() + (mTextSizeN / 2 - 3), mTextPaint);
        }
        /** 绘制多边形 **/
        mPaint.reset();//重置
        mPaint.setColor(Color.parseColor("#F04D1C"));
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);//设置空心
        Path path1 = new Path();
        float x = 0;//记录下起点的X轴坐标
        float y = 0;//记录下起点的Y轴坐标
        for (RoomListBean roomListBean : roomListBeenEx) {
            for (int i = 0; i < roomListBean.getCoordinate().size(); i++) {
                if (i == 0) {/**起点**/
                    path1.moveTo(roomListBean.getCoordinate().get(i).getX(), roomListBean.getCoordinate().get(i).getY());
                    x = roomListBean.getCoordinate().get(i).getX();
                    y = roomListBean.getCoordinate().get(i).getY();
                } else {
                    path1.lineTo(roomListBean.getCoordinate().get(i).getX(), roomListBean.getCoordinate().get(i).getY());
                    /**如果画完最后一个点  则终点跟起点相连封闭成矩形**/
                    if (i == roomListBean.getCoordinate().size() - 1) {
                        path1.lineTo(x, y);
                    }
                }
            }
        }
        path1.close();
        mCanvas.drawPath(path1, mPaint);
        /** 任务圆点 **/
        for (TaggingBean taggingBean : taggingBeanList) {
            Rect rect = new Rect(taggingBean.getPosX() - rectValue, taggingBean.getPoxY() - rectValue, taggingBean.getPosX() + rectValue, taggingBean.getPoxY() + rectValue);
            if (!TextUtils.isEmpty(type)) {
                //问题录入时查看的点
                switch (state) {
                    case "0"://待整改
                        mCanvas.drawBitmap(pointBitMap, null, rect, null);
                        break;
                    case "1"://待销项
                        mCanvas.drawBitmap(pointBitMapTwo, null, rect, null);
                        break;
                    case "2"://已销项
                        mCanvas.drawBitmap(pointBitMapThere, null, rect, null);
                        break;
                    default://如果不是这3种状态
                        mCanvas.drawBitmap(pointBitMap, null, rect, null);
                        break;
                }
            } else {
                for (int i = 0; i < taggingBeanList.size(); i++) {
                    switch (taggingBeanList.get(i).getProblemState()) {
                        case "0"://待整改
                            mCanvas.drawBitmap(pointBitMap, null, rect, null);
                            break;
                        case "1"://待销项
                            mCanvas.drawBitmap(pointBitMapTwo, null, rect, null);
                            break;
                        case "2"://已销项
                            mCanvas.drawBitmap(pointBitMapThere, null, rect, null);
                            break;
                        default://如果不是这3种状态
                            mCanvas.drawBitmap(pointBitMap, null, rect, null);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Map<String, Float> map1 = getImageViewIneerSize(false);
        int pointCount = event.getPointerCount();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                matrix.set(saveMatrix);
                x_down = event.getX();
                y_down = event.getY();
                mLastTime = System.currentTimeMillis();
                mode = DRAG;//偏移
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                initDis = spacing(event);//两点按下时的距离
                mode = ZOOM;//缩放
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {//偏移
                    if (values[0] > INIT_SCALE) {//如果缩放倍数是初始值时不允许偏移
                        matrix.set(saveMatrix);
                        if (Math.abs(event.getX() - x_down) > 10f || Math.abs(event.getY() - y_down) > 10) {
                            matrix.postTranslate(event.getX() - x_down, event.getY() - y_down);
                            postInvalidate();
                        }
                    }
                } else if (mode == ZOOM && pointCount >= 2) {//缩放
                    matrix.set(saveMatrix);
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        float tScale = newDist / initDis;
                        matrix.postScale(tScale, tScale, viewWidth / 2, viewHeight / 2);
                    }
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mode = NONE;
                matrix.getValues(values);
                startMatrix.getValues(startValues);
                if (mBitmap != null) {
                    setEdge();
                }
                if (System.currentTimeMillis() - mLastTime < 200) {
                    float posX = -map1.get("offsetX") * map1.get("scaleX") + x_down * map1.get("scaleX");
                    float posY = -map1.get("offsetY") * map1.get("scaleY") + y_down * map1.get("scaleY");
                    if (posX > 0 && posX < imgWidth && posY > 0 && posY < imgHeight) {
                        if (TextUtils.isEmpty(type) || !type.equals("see")) {
                            onDrawClick(posX, posY);
                        }
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 点击事件监听
     *
     * @param x_pos 点击的X轴坐标
     * @param y_pos 点击的Y轴坐标
     */
    private void onDrawClick(float x_pos, float y_pos) {
        boolean hasDtz = false;
        for (TaggingBean taggingBean : taggingBeanList) {//如果点击在任务圆点范围内
            int left_top_x = taggingBean.getPosX() - AREA_SIZE;
            int left_top_y = taggingBean.getPoxY() - AREA_SIZE;
            int right_buttom_x = taggingBean.getPosX() + AREA_SIZE;
            int right_buttom_y = taggingBean.getPoxY() + AREA_SIZE;
            if (x_pos > left_top_x && x_pos < right_buttom_x && y_pos > left_top_y && y_pos < right_buttom_y) {
                hasDtz = true;
                for (int i = 0; i < taggingBeanList.size(); i++) {
                    if (taggingBean.getProblemId().equals(taggingBeanList.get(i).getProblemId())) {
                        presenter.problem(taggingBeanList.get(i));//问题回调
                    }
                }
            }
        }
        if (!hasDtz) {
            RoomListBean roomListBean = CoordinateUtils.isPolygonContainsPoint(roomListBeenEx, x_pos, y_pos);
            if (roomListBean != null) {//如果不为空  代表点击在房间内
                if (!TextUtils.isEmpty(type)) {
                    presenter.modify(x_pos, y_pos);//修改问题坐标
                } else {
                    presenter.input(x_pos, y_pos);//录入回调
                }
            }
        }
    }

    //取两点的距离
    private float spacing(MotionEvent event) {

        try {
            float x = event.getX(1) - event.getX(0);
            float y = event.getY(1) - event.getY(0);
            float z = (float) Math.sqrt(x * x + y * y);
            if (z >= x && z >= y) {
                mode = ZOOM;
            } else {
                mode = DRAG;
            }
            return z;
        } catch (IllegalArgumentException ex) {
            return 0;
        }
    }

    /**
     * Matrix{[cosX,-sinX,translateX][sinX,cosX,translateY][0,0,scale]}
     * sinX和cosX，表示旋转角度的cos值和sin值，旋转角度是按顺时针方向计算的。
     * translateX和translateY表示x和y的平移量。scale是缩放的比例，1是不变，2是表示缩放1/2。
     *
     * @param init 是否是初次加载
     * @return 返回矩阵缩放平移系数
     */
    private Map<String, Float> getImageViewIneerSize(boolean init) {
        Map<String, Float> size = new HashMap<String, Float>();
        //获得ImageView中Image的变换矩阵
        matrix.getValues(values);
        //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
        float sx = values[0];
        float sy = values[4];
        if (init) {//记录初始缩放倍数  如果缩放倍数是初始值时不允许偏移
            INIT_SCALE = values[0];
        }
        //计算Image在屏幕上实际绘制的宽高
        size.put("scaleX", 1 / sx);
        size.put("scaleY", 1 / sy);
        size.put("offsetX", values[2]); //X轴的translate的值
        size.put("offsetY", values[5]);
        return size;
    }

    /**
     * 控制图片缩放的最大值和最小值
     */
    private void setEdge() {
        if ((values[0] / startValues[0]) > MAX_SCALE) {//如果缩放系数大于最大缩放系数   则设置为最大缩放系数
            matrix.postScale((startValues[0] * MAX_SCALE) / values[0], (startValues[0] * MAX_SCALE) / values[0], viewWidth / 2, viewHeight / 2);
        } else if (values[0] < MIN_SCALE) {//如果缩放系数小于最小缩放系数
            matrix.set(startMatrix);//显示原始矩阵
//            matrix.postScale((startValues[0] * MIN_SCALE) / values[0], (startValues[0] * MIN_SCALE) / values[0], viewWidth / 2, viewHeight / 2);//设置为最小缩放系数
        }
        saveMatrix.set(matrix);
        getImageViewIneerSize(false);
        postInvalidate();
    }


}
