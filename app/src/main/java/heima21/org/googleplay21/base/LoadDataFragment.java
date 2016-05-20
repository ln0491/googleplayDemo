package heima21.org.googleplay21.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import heima21.org.googleplay21.util.UiUtil;
import heima21.org.googleplay21.view.LoadDataUi;

/**
 * 负责访问网络数据的Fragment基类
 */
public abstract class LoadDataFragment extends BaseFragment{

    private String TAG = "LoadDataFragment";
    private LoadDataUi mUi;

    /**
     * 1.初始化不同页面相同的UI部分，loading的时候,网络错误的时候（server crash，网络线路问题）、
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("MainActivity", "onCreateView");
//        TextView textView = new TextView(UiUtil.getContext());
//        textView.setTextColor(Color.BLACK);
////            textView.setText(mTitles[position]);
//        textView.setText("首页Fragment");
//        textView.setTextSize(25);
//        textView.setGravity(Gravity.CENTER);

        //1.重复价值mUi就会不同对象，改为使用同一个对象
        if(mUi == null) {
            mUi = new LoadDataUi(UiUtil.getContext()) {
                @Override
                protected View getSuccessView() {
                    //实例了抽象类对象，必须复写方法,当前类也是基类，因此继续交给子类
                    return onInitSuccessView();
                }

                @Override
                protected Result onLoadData() {
                    //实例了抽象类对象，必须复写方法,当前类也是基类，因此继续交给子类
                    Log.e("LoadDataFragment", "耗时操作");
                    return doInBackground();
                }
            };
        }
        return mUi;
    }

    protected abstract View onInitSuccessView();

    /**
     * 访问服务器，子类复写此方法
     * @return
     */
    protected abstract LoadDataUi.Result doInBackground();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("MainActivity", "onActivityCreated");
        //2.去服务器加载数据


//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    public void loadData(){
//        Log.e(TAG, "去服务器加载数据");
//        int state = doInBackground();
//        mCurrentState =state;
        if(mUi !=null){
            mUi.loadData();
        }
    }
}
