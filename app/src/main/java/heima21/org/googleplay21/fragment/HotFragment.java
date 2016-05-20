package heima21.org.googleplay21.fragment;

import android.graphics.Color;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import heima21.org.flowlayoutlibrary.FlowLayout;
import heima21.org.googleplay21.R;
import heima21.org.googleplay21.base.LoadDataFragment;
import heima21.org.googleplay21.protocol.HotProtocol;
import heima21.org.googleplay21.util.UiUtil;
import heima21.org.googleplay21.view.LoadDataUi;

/**
 * Created by Administrator on 2016/5/3.
 */
public class HotFragment extends LoadDataFragment {
    private List<String> mDatas;

    @Override
    protected View onInitSuccessView() {
        ScrollView scrollView = new ScrollView(UiUtil.getContext());
        scrollView.setFillViewport(true); //后续添加ziview,高度可以自动刷新
        FlowLayout flowLayout = new FlowLayout(UiUtil.getContext());
        flowLayout.setPadding(10,10,10,10);

        Random random = new Random();
        //add textview
        for(int i=0;i<mDatas.size();i++){
            TextView tv = new TextView(UiUtil.getContext());
//            tv.setBackgroundResource(R.drawable.recommend_item_bg);
            tv.setText(mDatas.get(i));
            tv.setPadding(5, 5, 5, 5);
            tv.setGravity(Gravity.CENTER);
            int a  = 230;
            int r = 30 + random.nextInt(200);
            int g = 30 + random.nextInt(200);
            int b = 30 + random.nextInt(200);

            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            gradientDrawable.setCornerRadius(UiUtil.dp2Px(5));
            gradientDrawable.setColor(Color.argb(a, r, g, b));
//            tv.setBackgroundDrawable(gradientDrawable);
            tv.setTextColor(Color.WHITE);

            GradientDrawable gradientDrawable2 = new GradientDrawable();
            gradientDrawable2.setShape(GradientDrawable.RECTANGLE);
            gradientDrawable2.setCornerRadius(UiUtil.dp2Px(5));
            gradientDrawable2.setColor(Color.GRAY);
            tv.setTextColor(Color.WHITE);

            //2.代码写一个selector,2张图片支持不同状态
            StateListDrawable selector = new StateListDrawable();
            selector.addState(new int[]{android.R.attr.state_pressed}, gradientDrawable2);
            selector.addState(new int[]{}, gradientDrawable);

            tv.setBackgroundDrawable(selector);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            flowLayout.addView(tv);
        }

        scrollView.addView(flowLayout);
        return scrollView;
    }

    @Override
    protected LoadDataUi.Result doInBackground() {
        HotProtocol mProtocol = new HotProtocol();
        Map<String,String> params = new HashMap<>();
        params.put("index", "0");
        mProtocol.setParams(params);
        try {
            mDatas = mProtocol.loadData();
            if(mDatas == null){
                return LoadDataUi.Result.EMPTY;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return LoadDataUi.Result.FAILED;
        }
        return LoadDataUi.Result.SUCESS;
    }
}
