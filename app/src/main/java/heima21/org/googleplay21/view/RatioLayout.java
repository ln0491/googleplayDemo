package heima21.org.googleplay21.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import heima21.org.googleplay21.R;

/**
 * 通用的具有自定义宽高比的viewgroup,放置imageview/imagebutton，复合view
 */
public class RatioLayout extends FrameLayout {
    //默认宽高比
    private float mRatio = 2.5f;
    public static final int RELATIVE_WIDTH = 0;
    public static final int RELATIVE_HEIGHT = 1;
    //相对的走向
    private int mRelative = RELATIVE_HEIGHT;

    public RatioLayout(Context context) {
//        super(context);
        this(context,null);
    }

    public void setRelative(int mRelative) {
        this.mRelative = mRelative;
    }

    public void setRatio(float mRatio) {
        this.mRatio = mRatio;
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //resId:建立一个自定属性的xml, atrrs:布局xml中的属性
       TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);

        mRatio = typedArray.getFloat(R.styleable.RatioLayout_mRatio, 2.5f);
        mRelative = typedArray.getInt(R.styleable.RatioLayout_mRetative, RELATIVE_WIDTH);

        typedArray.recycle();
    }

    //只需要复写onMeasure，因为onlayout默认的，只有一个孩子，mode
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //1.取出mode与size
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int heigtMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && mRatio != 0 && mRelative == RELATIVE_WIDTH) {
            //2.计算高度
            float totalHeight = mWidth / mRatio;

            //3.测量孩子
//            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            //4.支持padding
            int childWidth = mWidth - getPaddingLeft() - getPaddingRight();
            int childHeight = (int) (totalHeight - getPaddingTop() - getPaddingBottom() + 0.5f);
            int childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthSpec, childHeightSpec);

            //4.setMeasureDimision(确认自己的大小）
            setMeasuredDimension(mWidth, (int) (totalHeight + 0.5f));
        } else if (heigtMode == MeasureSpec.EXACTLY && mRatio != 0 && mRelative == RELATIVE_HEIGHT) {
            //固定高度，计算宽度
            float totalWidth = mHeight * mRatio;

            int childHeight  = mHeight - getPaddingTop() - getPaddingBottom();
            int childHeightSpec  = MeasureSpec.makeMeasureSpec(childHeight,MeasureSpec.EXACTLY);
            int childWidth = (int) (totalWidth - getPaddingLeft() - getPaddingRight() + 0.5f);
            int childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            measureChildren(childWidthSpec, childHeightSpec);

            setMeasuredDimension((int) (totalWidth +0.5f),mHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
