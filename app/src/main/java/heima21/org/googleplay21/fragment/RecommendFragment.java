package heima21.org.googleplay21.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import heima21.org.googleplay21.base.LoadDataFragment;
import heima21.org.googleplay21.protocol.RecommendProtocol;
import heima21.org.googleplay21.util.UiUtil;
import heima21.org.googleplay21.view.LoadDataUi;
import heima21.org.googleplay21.view.RandomLayout;
import heima21.org.googleplay21.view.StellarMap;

/**
 * Created by Administrator on 2016/5/3.
 */
public class RecommendFragment extends LoadDataFragment {
    private List<String> mDatas;

    @Override
    protected View onInitSuccessView() {
        //使用stellarMap
        StellarMap layout = new StellarMap(UiUtil.getContext());
//        layout.setPadding(18,18,18,18);
        //放在mloadUi,-->successView
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 8;
        params.topMargin = 8;
        params.rightMargin = 8;
        layout.setLayoutParams(params);
//        RandomLayout randomLayout = new RandomLayout()
        layout.setRegularity(10,10);
        layout.setAdapter(new RecommendAdapter());
        layout.setGroup(0,true);
        return layout;
    }

    private class RecommendAdapter implements StellarMap.Adapter {
        private int PAGE_SIZE = 15;
        Random random = new Random();

        /**
         * 组的数量，一个页面就是一个组
         *
         * @return
         */
        @Override
        public int getGroupCount() {
            if (mDatas.size() % PAGE_SIZE == 0) {
                return mDatas.size() / PAGE_SIZE;
            }
            return mDatas.size() / PAGE_SIZE + 1;
        }

        @Override
        public int getCount(int group) {
            if(group == getGroupCount() -1){
                return mDatas.size() % PAGE_SIZE;
            }
//            if (mDatas.size() % PAGE_SIZE != 0) {
//                return mDatas.size() % PAGE_SIZE;
//            }
            return PAGE_SIZE;
        }

        @Override
        public View getView(int group, int position, View convertView) {
            //取出当前postion的data
            int location = group * PAGE_SIZE + position;
            String data = mDatas.get(location);
            TextView tv = new TextView(UiUtil.getContext());
            tv.setText(data);
            int a = 230;
            int r = random.nextInt(200) + 30;
            int g = random.nextInt(200) + 30;
            int b = random.nextInt(200) + 30;
            tv.setTextColor(Color.argb(a, r, g, b));

            tv.setTextSize(random.nextInt(10) + 14);
            return tv;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            return 0;
        }
    }

    @Override
    protected LoadDataUi.Result doInBackground() {
        RecommendProtocol mProtocol = new RecommendProtocol();
        Map<String, String> params = new HashMap<>();
        params.put("index", "0");
        mProtocol.setParams(params);
        try {
            mDatas = mProtocol.loadData();
            if (mDatas == null) {
                return LoadDataUi.Result.EMPTY;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return LoadDataUi.Result.FAILED;
        }
        return LoadDataUi.Result.SUCESS;
    }
}
