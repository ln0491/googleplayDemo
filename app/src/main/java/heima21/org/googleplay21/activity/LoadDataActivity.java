package heima21.org.googleplay21.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import heima21.org.googleplay21.R;
import heima21.org.googleplay21.base.BaseActivity;
import heima21.org.googleplay21.util.UiUtil;
import heima21.org.googleplay21.view.LoadDataUi;

/**
 * 具有网络访问功能的公共activity
 */
public abstract class LoadDataActivity extends BaseActivity {

    private LoadDataUi mUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_load_data);

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
        setContentView(mUi);
    }

    @Override
         protected void onStart() {
        super.onStart();
        loadData();
    }

    public void loadData(){
//        Log.e(TAG, "去服务器加载数据");
//        int state = doInBackground();
//        mCurrentState =state;
        if(mUi !=null){
            mUi.loadData();
        }
    }

    protected abstract View onInitSuccessView();

    /**
     * 访问服务器，子类复写此方法
     * @return
     */
    protected abstract LoadDataUi.Result doInBackground();
}
