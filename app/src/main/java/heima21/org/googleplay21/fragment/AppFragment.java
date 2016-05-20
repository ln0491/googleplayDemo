package heima21.org.googleplay21.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.base.ListItemClickAdapter;
import heima21.org.googleplay21.base.LoadDataFragment;
import heima21.org.googleplay21.bean.HomeBean;
import heima21.org.googleplay21.holder.HomeHolder;
import heima21.org.googleplay21.protocol.AppProtocol;
import heima21.org.googleplay21.util.UiUtil;
import heima21.org.googleplay21.view.LoadDataUi;

/**
 * 应用页面
 */
public class AppFragment extends LoadDataFragment {

//    private List<String> mDatas;
    private List<HomeBean.ApkItem> mDatas;
    private AppProtocol mProtocol;
    private ListView mListView;

    private class AppAdapter extends ListItemClickAdapter {

        public AppAdapter(List<HomeBean.ApkItem> mDatas) {
            super(mDatas, mListView);
        }

        @Override
        protected List<HomeBean.ApkItem> getLoadMoreData() throws Exception {
            Map<String,String> params = new HashMap<>();
            params.put("index", "" + mDatas.size());
            mProtocol.setParams(params);

            return  mProtocol.loadData();
        }
    }
    
    @Override
    protected View onInitSuccessView() {
//        TextView tv = new TextView(UiUtil.getContext());
//        tv.setGravity(Gravity.CENTER);
//        tv.setText("我是来自服务器的App页面");
//        tv.setTextColor(Color.BLACK);
//        tv.setTextSize(25);
//        return tv;
        mListView = new ListView(UiUtil.getContext());
        mListView.setAdapter(new AppAdapter(mDatas));
        mListView.setBackgroundColor(Color.parseColor("#cccccc"));
        return mListView;
    }

    @Override
    protected LoadDataUi.Result doInBackground() {
        //3.使用协议
        mProtocol = new AppProtocol();
        //需要设置一个map参数
        Map<String,String> params = new HashMap<>();
        params.put("index", "0");
        mProtocol.setParams(params);
        try {
            mDatas =  mProtocol.loadData();
            if(mDatas ==null ||mDatas.size()==0){
                return LoadDataUi.Result.EMPTY;
            }
            return LoadDataUi.Result.SUCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return LoadDataUi.Result.FAILED;
        }

        //2.使用模拟数据
//        mDatas = new ArrayList<>();
//        for(int i=0;i<50;i++){
//            mDatas.add("我是条目：" + i);
//        }
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Random random = new Random();
//        LoadDataUi.Result[] results = new LoadDataUi.Result[]{
//                LoadDataUi.Result.EMPTY, LoadDataUi.Result.FAILED, LoadDataUi.Result.SUCESS
//        };
//        return LoadDataUi.Result.SUCESS;
    }
//    private String TAG = "AppFragment";
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        TextView textView = new TextView(UiUtil.getContext());
//        textView.setTextColor(Color.BLACK);
////            textView.setText(mTitles[position]);
//        textView.setText("App页面Fragment");
//        textView.setTextSize(25);
//        textView.setGravity(Gravity.CENTER);
//        return textView;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        //2.取服务器加载数据
//        Log.e(TAG, "取服务器加载数据");
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        //3.拿到数据后更新view
//        Log.e(TAG, "成功拿到数据，更新UI");
//    }
}
