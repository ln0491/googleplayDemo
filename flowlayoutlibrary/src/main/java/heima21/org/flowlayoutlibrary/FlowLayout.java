package heima21.org.flowlayoutlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持平铺textview组件，孩子是随机颜色，圆角边框
 */
public class FlowLayout extends ViewGroup{
    //3.创建孩子集合
    private List<Line> mLines = new ArrayList<>();
    //9.定义line指针
    private Line mCurrentLine;
    private int mVerticalSpace = 15;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //onmeasure会重复调用，因此需要回收line
        mLines.clear();
        mCurrentLine = null;

        Log.e("FlowLayout", "onMeasure");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int maxWidth = width - getPaddingRight() - getPaddingLeft();
        //7.测量孩子
        int count = getChildCount();
        for(int i=0;i<count;i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            //8.添加到line
            if(mCurrentLine == null){
                //新建行
                mCurrentLine = new Line(maxWidth,15); //TODO:有参数还没赋值
                mLines.add(mCurrentLine);
                mCurrentLine.addChlid(child);
            }else{
                //当前已经有孩子
                if(mCurrentLine.canAddChild(child)){
                    mCurrentLine.addChlid(child);
                }else{
                    //当前行已满，去下一行
                    mCurrentLine = new Line(maxWidth,15); //TODO:有参数还没赋值
                    mLines.add(mCurrentLine);
                    mCurrentLine.addChlid(child);
                }
            }
        }

        //9.确定自己的宽高
        int height = getPaddingTop() + getPaddingBottom();
        //10.依次增加line的高度
        for(int i=0;i<mLines.size();i++){
            Line line = mLines.get(i);
            height += line.mHeight;
            if(i!=0) {
                height += mVerticalSpace;
            }
        }
        setMeasuredDimension(width, height);

//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("FlowLayout", "onLayout");
        //13.开始测量
        l = getPaddingLeft();
        t = getPaddingTop();
        for(int i=0;i<mLines.size();i++){
            Line line = mLines.get(i);
            //14.让line去布局
            line.layout(l,t);

            //17.更新top值
            t += line.mHeight + mVerticalSpace;
        }
    }

    //1.采取封装line,间接添加
    private class Line{
        //3.创建孩子集合
        private List<View> mViews = new ArrayList<>();
        //5.需要定义已经用了多少，间距，最大宽度
        private int mUsedWidth;
        private int mSpace;
        private  int mMaxWidth;
        private String TAG = "Line";
        //11.定义单行的高度
        public int mHeight;

        public Line(int maxWidth, int space) {
            this.mMaxWidth = maxWidth;
            this.mSpace = space;
        }

        //4.考虑放不进去
        public boolean canAddChild(View child){
            if(mViews.size()==0){
                Log.e(TAG, "true");
                return true;
            }
            int childWidth = child.getMeasuredWidth();
            if(mUsedWidth + childWidth + mSpace > mMaxWidth){
                Log.e(TAG, "false");
                return false;
            }
            Log.e(TAG, "true");
            return true;
        }

        //2.必须有添加孩子的方法
        public void addChlid(View view){

            int childWidth = view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();
            //6.添加后记录已经使用的宽度
            if(mViews.size() == 0) {
                mUsedWidth += childWidth;
            }else{
                mUsedWidth += childWidth + mSpace;
            }
//           if(canAddChild(view)) {
                    mViews.add(view);
//           }
            //12.赋值line的高度
            mHeight = mHeight > childHeight? mHeight: childHeight;
        }

        public void layout(int l, int t) {
            //17.计算剩余空间
            float avg = (mMaxWidth - mUsedWidth) /mViews.size();

            //15.依次布局line的每个孩子
            for(int i=0;i<mViews.size();i++){
//                View child = getChildAt(i);
                View child = mViews.get(i);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                if(avg > 0){
                    Log.e(TAG,"avg：" + avg);
                    //重新测量一次，并且改变它的位置
                    int childWidthSpec = MeasureSpec.makeMeasureSpec((int) (childWidth + avg),MeasureSpec.EXACTLY);
                    int childHeightSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
//                    measureChild();
                    child.measure(childWidthSpec, childHeightSpec);
                    //重新赋值
                    childWidth = child.getMeasuredWidth();
                    childHeight = child.getMeasuredHeight();
                }

                int left = l;
                int top = t;
                int right = left + childWidth;
                int bottom = top + childHeight;
                child.layout(left,top,right,bottom);

                //16.刷新left
                l += childWidth + mSpace;
            }
        }
    }
}
