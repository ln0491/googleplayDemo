package heima21.org.googleplay21.holder;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import heima21.org.googleplay21.R;
import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.bean.DetailBean;
import heima21.org.googleplay21.util.Constants;
import heima21.org.googleplay21.util.UiUtil;

/**
 * 详情页info部分
 */
public class DetailDesHolder extends BaseHolder<DetailBean> {
    @Bind(value = R.id.app_detail_des_iv_arrow)
    protected ImageView mIvArrow;

    @Bind(value = R.id.app_detail_des_tv_author)
    protected TextView mTvAuthor;
    @Bind(value = R.id.app_detail_des_tv_des)
    protected TextView mTvDes;
    private int mWidth;

    @Override
    public View inflateAndFindView() {
        final View desView = View.inflate(UiUtil.getContext(), R.layout.item_app_detail_des, null);
        ButterKnife.bind(this, desView);
        //view被layout也就是挂载到父view上时，
        desView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //desView以及他的子view都已经被赋值宽高
                mWidth = mTvDes.getMeasuredWidth();
            }
        });
        mIvArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(true);
            }
        });
        return desView;
    }


    @Override
    public void setData(DetailBean data) {
        mTvAuthor.setText(data.author);
        mTvDes.setText(data.des);

        toggle(false);
    }

    private boolean isOpened = true;

    //展开与收缩
    private void toggle(boolean isAnimation) {
        //1.收缩：改变desCotainer的高度为0
        //2.需要使用到动画过程中的height数值 ValueAnimator
        mTvDes.measure(View.MeasureSpec.makeMeasureSpec(mWidth, View.MeasureSpec.EXACTLY), 0);
        int height = mTvDes.getMeasuredHeight();
        if (isOpened) {
            if (isAnimation) {
                doAnimation(height, getSevenLineHeight());
                ObjectAnimator animator = ObjectAnimator.ofFloat(mIvArrow, "rotation", 0, 180).setDuration(2000);
                animator.start();
            } else {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTvDes.getLayoutParams();
                params.height = getSevenLineHeight();
                mTvDes.setLayoutParams(params);
            }
        } else {
            if (isAnimation) {
                doAnimation(getSevenLineHeight(), height);
                ObjectAnimator animator = ObjectAnimator.ofFloat(mIvArrow, "rotation", -180, 0).setDuration(2000);
                animator.start();
            } else {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTvDes.getLayoutParams();
                params.height = height;
                mTvDes.setLayoutParams(params);
            }
        }
        isOpened = !isOpened;
    }

    //获取7行文本的高度
    private int getSevenLineHeight() {
        //方法1：文字大小叠加间距大小
        //方法2：创建一个新的textview，使其文字与文字size都与原来相同，测量一下getMeasureHeight
        TextView tv = new TextView(UiUtil.getContext());
        tv.setText(mTvDes.getText());
        tv.setLines(7);
        //测量这个文本
        tv.measure(mWidth, 0);
        int height = tv.getMeasuredHeight();
        return height;
    }

    private void doAnimation(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        //动态刷新layoutparmas
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTvDes.getLayoutParams();
                params.height = value;
                mTvDes.setLayoutParams(params);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //让scrollView滚动到最后
                //1.通过构造方法，set方法传递参数
                //2.findViewbyId是父view找孩子，getparent找父view
                ViewParent parent = mTvDes.getParent();
                while (parent != null) {
                    parent = parent.getParent();
                    if (parent instanceof ScrollView) {
                        //滚动到最后
                        ScrollView scrollView = (ScrollView) parent;
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(2000);
        animator.start();
    }
}
