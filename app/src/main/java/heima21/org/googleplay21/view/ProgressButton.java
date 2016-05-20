package heima21.org.googleplay21.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

/*
 *  @项目名：  ProgressDemo 
 *  @包名：    org.itheima15.progressdemo
 *  @文件名:   ProgressButton
 *  @创建者:   Administrator
 *  @创建时间:  2015/11/27 16:56
 *  @描述：    带进度条的button
 */
public class ProgressButton
        extends Button
{
    private Paint mPaint = new Paint();
    private int      mProgress;
    private int      mMax;
    private Drawable mProgressDrawable;//进度的背景

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgress(int progress) {
        this.mProgress = progress;

        invalidate();
    }

    public void setProgressDrawable(Drawable drawable) {
        this.mProgressDrawable = drawable;
    }

    public void setMax(int max) {
        this.mMax = max;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画进度
        mPaint.setColor(Color.BLUE);

        float percent = 0;
        if (mMax == 0) {
            percent = mProgress / 100f;
        } else {
            percent = mProgress * 1f / mMax;
        }

        int left   = 0;
        int top    = 0;
        int right  = (int) (getMeasuredWidth() * percent + 0.5f);
        int bottom = getMeasuredHeight();
        //画矩形
        if (mProgressDrawable == null) {
            canvas.drawRect(left, top, right, bottom, mPaint);
        } else {
            //设置drawable 边界
            mProgressDrawable.setBounds(left, top, right, bottom);

            //在canvas上画drawable
            mProgressDrawable.draw(canvas);
        }

        //画文字
        super.onDraw(canvas);

    }
}
