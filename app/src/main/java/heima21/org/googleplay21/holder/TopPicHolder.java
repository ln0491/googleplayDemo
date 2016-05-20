package heima21.org.googleplay21.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import heima21.org.googleplay21.R;
import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.util.Constants;
import heima21.org.googleplay21.util.UiUtil;

/**
 * Created by Administrator on 2016/4/29.
 */
public class TopPicHolder extends BaseHolder<List<String>> implements ViewPager.OnPageChangeListener, View.OnTouchListener {

    @Bind(value = R.id.item_home_picture_pager)
    protected ViewPager mPager;

    @Bind(value = R.id.item_home_picture_container_indicator)
    protected LinearLayout mIndicator;
    private AutoScrollTask mAutoScrollTask;

    @Override
    public View inflateAndFindView() {
        View picView = View.inflate(UiUtil.getContext(), R.layout.item_home_picture,null);
        ButterKnife.bind(this, picView);
        return picView;
    }

    @Override
    public void setData(List<String> data) {
        mPager.setAdapter(new HomePicAdapter(data));

        //此处添加指示器
        for(int i=0;i< data.size();i++){
            View point = new View(UiUtil.getContext());
            if(i ==0){
                point.setBackgroundResource(R.drawable.indicator_selected);
            }else {
                point.setBackgroundResource(R.drawable.indicator_normal);
            }
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(UiUtil.dp2Px(6),UiUtil.dp2Px(6));
            param.bottomMargin = UiUtil.dp2Px(6);
            param.leftMargin = UiUtil.dp2Px(6);
            mIndicator.addView(point, param);
        }

        mPager.setOnPageChangeListener(this);
        int index = Integer.MAX_VALUE/2;
        index -= index % data.size();
        mPager.setCurrentItem(index);

        mPager.setOnTouchListener(this);

        mPager.setPageTransformer(true, new DepthPageTransformer());

        if(mAutoScrollTask == null){
            mAutoScrollTask = new AutoScrollTask();
        }
        mAutoScrollTask.start();
    }

    public class DepthPageTransformer
            implements ViewPager.PageTransformer
    {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mAutoScrollTask.stop();
                break;
            case MotionEvent.ACTION_UP:
                mAutoScrollTask.start();
                break;
        }
        return false;
    }


    private class AutoScrollTask implements Runnable {
        @Override
        public void run() {
              //选择下一个
            int current = mPager.getCurrentItem();
            current += 1;
            mPager.setCurrentItem(current);

            UiUtil.post(this, 2000);
        }

        public void start(){
            stop();
            UiUtil.post(this, 2000);
        }

        public void stop(){
            UiUtil.remove(this);
        }
    }

    private List<String> mDatas;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int count = mIndicator.getChildCount();
        position = position % count;
        //1.获取总共有几页
        for(int i =0;i<count;i++){
            View point = mIndicator.getChildAt(i);
            if(i == position){
                point.setBackgroundResource(R.drawable.indicator_selected);
            }else {
                point.setBackgroundResource(R.drawable.indicator_normal);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class HomePicAdapter extends PagerAdapter {

        public HomePicAdapter(List<String> data) {
            mDatas = data;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //postion变成余数
            int count = mIndicator.getChildCount();
            position = position % count;

            ImageView iv = new ImageView(UiUtil.getContext());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            String url = Constants.IMG_URL + mDatas.get(position);
            Picasso.with(UiUtil.getContext()).load(url).error(R.drawable.ic_default)
                .placeholder(R.drawable.ic_default).into(iv);
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            if(mDatas!=null){
//                return mDatas.size();
                return Integer.MAX_VALUE;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
