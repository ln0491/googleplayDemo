package heima21.org.canvasdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/5/6.
 */
public class CircleView extends View {
    private Paint mPaint = new Paint();
    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    int width;
    int height;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = width;

        width -= getPaddingLeft() - getPaddingRight();
        height -= getPaddingTop() - getPaddingBottom();

        setMeasuredDimension(width,height);
    }
//应用开发中，做一些特效：下雪，磨砂玻璃（要求看懂，会改代码）
    //特殊应用的开发：教育app，画板的功能，Canvas
    //游戏开发：2dAndroid游戏
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = width /2;
        int y = height /2;
        float radius = width/2;
        mPaint.setAntiAlias(true); //抗锯齿效果
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x,y,radius,mPaint);

        //draw rect
        float l = width/3;
        float r = width * 2/3;
        float t = height /4;
        float b = height /2;
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(l,t,r,b,mPaint);

        //arrow
        Path path = new Path();
        float x1 = width /5;
        float y1 = height/2;
        float x2 = width *4/5;
        float y2 = height/2;
        float x3 = width/2;
        float y3 = height *4/5;
        path.moveTo(x1,y1);
        path.lineTo(x2,y2);
        path.lineTo(x3,y3);
        path.lineTo(x1,y1);
        canvas.drawPath(path,mPaint);
    }
}
