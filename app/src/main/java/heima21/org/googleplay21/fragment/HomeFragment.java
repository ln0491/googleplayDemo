package heima21.org.googleplay21.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import heima21.org.googleplay21.R;
import heima21.org.googleplay21.base.BaseFragment;
import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.base.ListItemClickAdapter;
import heima21.org.googleplay21.base.LoadDataFragment;
import heima21.org.googleplay21.base.SuperBaseAdapter;
import heima21.org.googleplay21.bean.HomeBean;
import heima21.org.googleplay21.holder.HomeHolder;
import heima21.org.googleplay21.holder.TopPicHolder;
import heima21.org.googleplay21.protocol.HomeProtocol;
import heima21.org.googleplay21.util.UiUtil;
import heima21.org.googleplay21.view.LoadDataUi;

/**
 * 首页Fragment
 */
public class HomeFragment extends LoadDataFragment {

    //    private List<String> mDatas;
    private List<HomeBean.ApkItem> mDatas;
    private String TAG = "HomeFragment";
    private HomeProtocol mProtocol;
    private List<String> mPics;
    private ListView mListView;

    private class HomeAdapter extends ListItemClickAdapter {

        public HomeAdapter(List<HomeBean.ApkItem> mDatas) {
            super(mDatas,mListView);
        }

//        @Override
//        protected View inflateConvertView() {
//            return null;
//        }


        @Override
        protected List<HomeBean.ApkItem> getLoadMoreData() throws Exception {
            //2.使用协议
            HomeBean homeBean = mProtocol.loadData(mDatas.size() + "");
            return homeBean.list;
            //使用okhttpclient去取数据了
//            OkHttpClient client = new OkHttpClient();
//            String url = "http://192.168.56.1:8080/GooglePlayServer/home?index=" + mDatas.size();
//            Log.e(TAG, url);
//            Request request = new Request.Builder().get().url(url).build();
//            Response response = client.newCall(request).execute();
//            if (response.isSuccessful()) {
//                final String json = response.body().string();
//                Gson gson = new Gson();
//                HomeBean homeBean = gson.fromJson(json, HomeBean.class);
//                if (homeBean != null) {
//                    return homeBean.list;
//                }
//            }
//            return null;
        }

        //        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder=null;
//            if(convertView == null){
//                holder = new ViewHolder();
//
//                convertView = View.inflate(UiUtil.getContext(), R.layout.home_temp_item,null);
//
//                holder.textView = (TextView) convertView.findViewById(R.id.home_list_item_title);
//
//                convertView.setTag(holder);
//            }else{
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            //set Data
//            String data = mDatas.get(position);
//            holder.textView.setText(data + "");
//            return convertView;
//        }
    }

//    class ViewHolder{
//        TextView textView;
//    }

    @Override
    protected View onInitSuccessView() {
        //2.使用listview
        mListView = new ListView(UiUtil.getContext());
        mListView.setBackgroundColor(Color.parseColor("#cccccc"));

        //添加轮播图，先创建一个holder
        TopPicHolder picHolder = new TopPicHolder();
        View picView = picHolder.inflateAndFindView();
        mListView.addHeaderView(picView);
        picHolder.setData(mPics);

        mListView.setAdapter(new HomeAdapter(mDatas));
        //1.使用模拟Textview
//        TextView tv = new TextView(UiUtil.getContext());
//        tv.setGravity(Gravity.CENTER);
//        tv.setText("我是来自服务器的主页");
//        tv.setTextColor(Color.BLACK);
//        tv.setTextSize(25);
        return mListView;
    }

    @Override
    protected LoadDataUi.Result doInBackground() {
        //4.使用协议
        mProtocol = new HomeProtocol();
        HomeBean homeBean = null;
        try {
            homeBean = mProtocol.loadData("0");
            if(homeBean!=null){
                mDatas = homeBean.list;
                mPics = homeBean.picture;
                if(mDatas == null || mDatas.size() ==0){
                    return LoadDataUi.Result.EMPTY;
                }
                return LoadDataUi.Result.SUCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return LoadDataUi.Result.FAILED;
        }
        return LoadDataUi.Result.EMPTY;


        //3.进行真实网络接口访问,有些组件只有网络访问，线程自己处理，还有一些组件内部维护线程池
        //Okhttpclient 官方的访问组件、设计好、速度快、跟上android脚步
//        OkHttpClient client = new OkHttpClient();
//        String url = "http://192.168.56.1:8080/GooglePlayServer/home?index=0";
//        Request request = new Request.Builder().get().url(url).build();
//        try {
//            Response response = client.newCall(request).execute();
//            if (response.isSuccessful()) {
//                final String json = response.body().string();
//                Gson gson = new Gson();
//                HomeBean homeBean = gson.fromJson(json, HomeBean.class);
//                if (homeBean != null) {
//                    mDatas = homeBean.list;
//                    if (mDatas == null || mDatas.size() == 0) {
//                        Log.e(TAG, "22222222222222222");
//                        return LoadDataUi.Result.EMPTY;
//                    } else {
//                        Log.e(TAG, "1111111111111");
//                        return LoadDataUi.Result.SUCESS;
//                    }
//                } else {
//                    Log.e(TAG, "22222222222222222");
//                    return LoadDataUi.Result.EMPTY;
//                }

//                UiUtil.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(UiUtil.getContext(), json, Toast.LENGTH_LONG).show();
//                    }
//                });

//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "33333333333");
//            return LoadDataUi.Result.FAILED;
//        }
        //2.使用模拟数据
//        mDatas = new ArrayList<>();
//        for(int i=0;i<50;i++){
//            mDatas.add("我是条目：" + i);
//        }
//
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

//    private String TAG = "HomeFragment";
//
//    /**
//     * 1.网络加载前，初始化view
//     *
//     * @param inflater
//     * @param container
//     * @param savedInstanceState
//     * @return
//     */
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        TextView textView = new TextView(UiUtil.getContext());
//        textView.setTextColor(Color.BLACK);
////            textView.setText(mTitles[position]);
//        textView.setText("首页Fragment");
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
