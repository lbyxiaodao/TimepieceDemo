# TimepieceDemo
自定义时钟
可以用来练习自定义view
下面是主要代码
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.util.Calendar;

/**
 * Created by Li Bin Yang on 2017/12/8.
 */

public class TimepieceView extends View {
    private float mRadius; //外圆半径
    private float mPadding; //边距
    private float mViewWidth;//view的宽
    private float mTextSize; //文字大小
    private float mHourPointWidth; //时针宽度
    private float mMinutePointWidth; //分针宽度
    private float mSecondPointWidth; //秒针宽度
    private int mColorLong; //长线的颜色
    private int mColorShort; //短线的颜色
    private int mHourPointColor; //时针的颜色
    private int mMinutePointColor; //分针的颜色
    private int mSecondPointColor; //秒针的颜色
    private float mScaleWidth;//刻度线的长度
    private Paint mPaint; //画笔
    private Calendar calendar;//时间类
    public TimepieceView(Context context) {
        super(context);
    }
    public TimepieceView(Context context,AttributeSet attrs) {
        super(context,attrs);
        obtainStyle(attrs);//获取样式属性
        init();//设置画笔
    }
    private void obtainStyle(AttributeSet attrs) {
        TypedArray array = null;
        try {
            array = getContext().obtainStyledAttributes(attrs, R.styleable.WatchBoard);
            mPadding = array.getDimension(R.styleable.WatchBoard_wb_padding, DptoPx(40));
            mTextSize = array.getDimension(R.styleable.WatchBoard_wb_text_size, SptoPx(16));
            mHourPointWidth = array.getDimension(R.styleable.WatchBoard_wb_hour_pointer_width, DptoPx(5));
            mMinutePointWidth = array.getDimension(R.styleable.WatchBoard_wb_minute_pointer_width, DptoPx(3));
            mSecondPointWidth = array.getDimension(R.styleable.WatchBoard_wb_second_pointer_width, DptoPx(2));
            mColorLong = array.getColor(R.styleable.WatchBoard_wb_scale_long_color, Color.argb(225, 0, 0, 0));
            mColorShort = array.getColor(R.styleable.WatchBoard_wb_scale_short_color, Color.argb(125, 0, 0, 0));
            mMinutePointColor = array.getColor(R.styleable.WatchBoard_wb_minute_pointer_color, Color.BLACK);
            mSecondPointColor = array.getColor(R.styleable.WatchBoard_wb_second_pointer_color, Color.RED);
            mHourPointColor=array.getColor(R.styleable.WatchBoard_wb_hour_pointer_color, Color.BLACK);
        } catch (Exception e) {
            //一旦出现错误全部使用默认值
            mPadding = DptoPx(10);
            mTextSize = SptoPx(14);
            mHourPointWidth = DptoPx(5);
            mMinutePointWidth = DptoPx(3);
            mSecondPointWidth = DptoPx(2);
            mColorLong = Color.argb(225, 0, 0, 0);
            mColorShort = Color.argb(125, 0, 0, 0);
            mMinutePointColor = Color.BLACK;
            mSecondPointColor = Color.RED;
            mHourPointColor=Color.BLACK;
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }
    //Dp转px
    private float DptoPx(int value) {
        return SizeUtils.dp2px(value);
    }

    //sp转px
    private float SptoPx(int value) {
        return SizeUtils.sp2px(value);
    }
    //画笔初始化
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }
    //重新设置view大小，将view设置成一个正方形
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if(widthMode==MeasureSpec.UNSPECIFIED){
            setMeasuredDimension(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenWidth());
        }else{
            setMeasuredDimension(widthSize,widthSize);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mRadius==0){
            mRadius=w/2-mPadding;
            mViewWidth=w;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mViewWidth/2,mViewWidth/2);
        paintCircle(canvas);
        paintScale(canvas);
        paintText(canvas);
        paintIndicator(canvas);
        canvas.restore();
        //一秒钟刷新一次
        postInvalidateDelayed(1000);
    }
    //绘制外圆背景
    public void paintCircle(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, mViewWidth/2-DptoPx(10), mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, mViewWidth/2-DptoPx(30), mPaint);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        canvas.drawCircle(0, 0, mRadius, mPaint);
    }
    //绘制刻度
    private void paintScale(Canvas canvas){
        for(int i=0;i<60;i++){
            if(i%5==0){
                mScaleWidth= DptoPx(15);
                mPaint.setColor(mColorLong);
                mPaint.setStrokeWidth(SizeUtils.dp2px(2));
            }else{
                mScaleWidth= DptoPx(10);
                mPaint.setColor(mColorShort);
                mPaint.setStrokeWidth(SizeUtils.dp2px(1));
            }
            canvas.drawLine(0,-mRadius+DptoPx(5),0,-mRadius+DptoPx(5)+mScaleWidth,mPaint);
            canvas.rotate(6);
        }
    }
    //绘制文字
    private void paintText(Canvas canvas){
        mPaint.setColor(mColorLong);
        mPaint.setStrokeWidth(SizeUtils.dp2px(1));
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        float x,y;
        //根据三角函数定律，计算出绘制文字的位置

        for(int i=0;i<60;i++){
            if(i%5==0){
                x= (float) (Math.sin(Math.toRadians(i*6))*(mRadius-DptoPx(35)));
                y= -(float) (Math.cos(Math.toRadians(i*6))*(mRadius-DptoPx(35)));
                //使绘制的文字在坐标点垂直居中
                float baseline = (2*y - fontMetrics.bottom - fontMetrics.top) / 2;
                if(i==0){
                    canvas.drawText("12",x,baseline,mPaint);
                }else{
                    canvas.drawText(i/5+"",x,baseline,mPaint);
                }

            }
        }
    }
    //绘制指针
    private  void paintIndicator(Canvas canvas){
        float x,y,x1,y1;
        //获取当前时间
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); //时
        int minute = calendar.get(Calendar.MINUTE); //分
        int second = calendar.get(Calendar.SECOND); //秒
        //绘制时针
        x= (float) (Math.sin(Math.toRadians(hour*30))*(mRadius-DptoPx(55)));
        y= -(float) (Math.cos(Math.toRadians(hour*30))*(mRadius-DptoPx(55)));
        x1= (float) (Math.sin(Math.toRadians(hour*30+180))*DptoPx(20));
        y1= -(float) (Math.cos(Math.toRadians(hour*30+180))*DptoPx(20));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mHourPointColor);
        mPaint.setStrokeWidth(1);
        canvas.drawCircle(x,y,mHourPointWidth/2,mPaint);
        canvas.drawCircle(x1,y1,mHourPointWidth/2,mPaint);
        mPaint.setStrokeWidth(mHourPointWidth);
        canvas.drawLine(x,y,x1,y1,mPaint);
        //绘制分针
        x= (float) (Math.sin(Math.toRadians(minute*6))*(mRadius-DptoPx(45)));
        y= -(float) (Math.cos(Math.toRadians(minute*6))*(mRadius-DptoPx(45)));
        x1= (float) (Math.sin(Math.toRadians(minute*6+180))*DptoPx(20));
        y1= -(float) (Math.cos(Math.toRadians(minute*6+180))*DptoPx(20));
        mPaint.setColor(mMinutePointColor);
        mPaint.setStrokeWidth(1);
        canvas.drawCircle(x,y,mMinutePointWidth/2,mPaint);
        canvas.drawCircle(x1,y1,mMinutePointWidth/2,mPaint);
        mPaint.setStrokeWidth(mMinutePointWidth);
        canvas.drawLine(x,y,x1,y1,mPaint);
        //绘制秒针
        x= (float) (Math.sin(Math.toRadians(second*6))*(mRadius-DptoPx(5)));
        y= -(float) (Math.cos(Math.toRadians(second*6))*(mRadius-DptoPx(5)));
        x1= (float) (Math.sin(Math.toRadians(second*6+180))*DptoPx(20));
        y1= -(float) (Math.cos(Math.toRadians(second*6+180))*DptoPx(20));
        mPaint.setColor(mSecondPointColor);
        mPaint.setStrokeWidth(1);
        canvas.drawCircle(x,y,mSecondPointWidth/2,mPaint);
        canvas.drawCircle(x1,y1,mSecondPointWidth/2,mPaint);
        mPaint.setStrokeWidth(mSecondPointWidth);
        canvas.drawLine(x,y,x1,y1,mPaint);
        //绘制一个原点
        mPaint.setColor(mSecondPointColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, DptoPx(10), mPaint);
    }
}
