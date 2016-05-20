package heima21.org.googleplay21.holder;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
public class DetailSafeHolder extends BaseHolder<DetailBean> {
    @Bind(value = R.id.app_detail_safe_iv_arrow)
    protected ImageView mIvArrow;

    @Bind(value = R.id.app_detail_safe_pic_container)
    protected LinearLayout mPicContainer;
    @Bind(value = R.id.app_detail_safe_des_container)
    protected LinearLayout mDesContainer;

    @Override
    public View inflateAndFindView() {
        View infoView = View.inflate(UiUtil.getContext(), R.layout.item_app_detail_safe, null);
        ButterKnife.bind(this, infoView);
        mIvArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return infoView;
    }

    private boolean isOpened = true;
    //展开与收缩
    private void toggle() {
        //1.收缩：改变desCotainer的高度为0
        //2.需要使用到动画过程中的height数值 ValueAnimator
        mDesContainer.measure(0,0);
        int height = mDesContainer.getMeasuredHeight();
        if(isOpened){
            doAnimation(height , 0);
            ObjectAnimator animator = ObjectAnimator.ofFloat(mIvArrow,"rotation", 0,180).setDuration(2000);
            animator.start();
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDesContainer.getLayoutParams();
//            params.height = 0;
//            mDesContainer.setLayoutParams(params);
        }else{
            doAnimation(0,height);
            ObjectAnimator animator = ObjectAnimator.ofFloat(mIvArrow,"rotation", -180,0).setDuration(2000);
            animator.start();
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDesContainer.getLayoutParams();
//            params.height = height;
//            mDesContainer.setLayoutParams(params);
        }
        isOpened = !isOpened;
    }

    private void doAnimation(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start,end);
        //动态刷新layoutparmas
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDesContainer.getLayoutParams();
                params.height = value;
                mDesContainer.setLayoutParams(params);
            }
        });
        animator.setDuration(2000);
        animator.start();
    }

    @Override
    public void setData(DetailBean data) {
        addPic(data.safe);
        addDes(data.safe);
    }

    //添加描述
    private void addDes(List<DetailBean.SafeEntity> safe) {
        for(int i=0;i<safe.size();i++){
            DetailBean.SafeEntity entity = safe.get(i);
            LinearLayout layout = new LinearLayout(UiUtil.getContext());
            layout.setPadding(6,6,6,6);
            ImageView iv = new ImageView(UiUtil.getContext());
            String url = Constants.IMG_URL + entity.safeDesUrl;
            Picasso.with(UiUtil.getContext()).load(url).into(iv);
            layout.addView(iv);

            TextView tv = new TextView(UiUtil.getContext());
            tv.setText(entity.safeDes);
            layout.addView(tv);

            mDesContainer.addView(layout);
        }
    }

    //添加绿色图片
    private void addPic(List<DetailBean.SafeEntity> safe) {
        for(int i=0;i<safe.size();i++){
            ImageView iv = new ImageView(UiUtil.getContext());
            DetailBean.SafeEntity entity = safe.get(i);
            String url = Constants.IMG_URL + entity.safeUrl;
            Picasso.with(UiUtil.getContext()).load(url).into(iv);
            mPicContainer.addView(iv);
        }


    }
}
