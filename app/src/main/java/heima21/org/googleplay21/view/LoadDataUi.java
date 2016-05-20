package heima21.org.googleplay21.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import heima21.org.googleplay21.R;
import heima21.org.googleplay21.manager.ThreadManager;
import heima21.org.googleplay21.util.UiUtil;

/**
 * 不同页面相同的部分，loading/empty/error 3种view
 */
public abstract class LoadDataUi extends FrameLayout{
    private int  mCurrentState = STATE_NONE;
    public static final int STATE_NONE = 0; //请求前
    public static final int STATE_LOADING= 1;//发起请求
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_SUCCESS = 4;

    private String TAG = "LoadDataUi";
    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;
    private View mSuccessView;

    public LoadDataUi(Context context) {
        super(context);

        initView(context);
    }

    /**
     * 添加共同部分，3种view
     * @param context
     */
    private void initView(Context context) {
        Log.e(TAG,"add LoadingView");
        mLoadingView = View.inflate(context, R.layout.pager_loading, null);
        addView(mLoadingView);

        //TODO:添加其他两个
        mEmptyView = View.inflate(context, R.layout.pager_empty, null);
        addView(mEmptyView);

        mErrorView = View.inflate(context, R.layout.pager_error, null);
        mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //loaddata重新加载
                loadData();
            }
        });
        addView(mErrorView);

        updateUI();
    }

    private void updateUI() {
        //第一次初始化都不显示
        mLoadingView.setVisibility(mCurrentState == STATE_LOADING ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(mCurrentState == STATE_EMPTY ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(mCurrentState == STATE_ERROR ? View.VISIBLE : View.GONE);

        //TODO:把成功view添加上去
        if(mCurrentState == STATE_SUCCESS && mSuccessView == null){
            //不同页面的成功Ui不同的，必须子类复写
            mSuccessView = getSuccessView();
            addView(mSuccessView);
        }

        if(mSuccessView!=null){
            mSuccessView.setVisibility(mCurrentState == STATE_SUCCESS ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 访问成功后的View
     * @return
     */
    protected abstract View getSuccessView();

    /**
     * 第二阶段，开启子线程访问服务器
     */
    public void loadData() {
        //是否重新加载，取决于上一次返回状态，如果前面加载成功，拒绝掉这次加载请求
        if(mCurrentState == STATE_SUCCESS || mCurrentState == STATE_LOADING){
            return;
        }

        //此时要先更新成loading状态
        mCurrentState = STATE_LOADING;
        updateUI();
        //1.提高性能，支持多核心cpu
        //2.线程比较耗性能，占用栈内存寸，runnable耗费堆内存资源，OOM，ANR
        //3.控制好线程创建、回收时机，线程池调度器一定程度提高性能
        //4.代码质量（排错的时候）
//        new Thread(new LoadDataTask()){}.start();

        ThreadManager.getNormalPool().execute(new LoadDataTask());
    }

    private class LoadDataTask implements Runnable{
        @Override
        public void run() {
            //urlconnection、httpclient、第三方包，不同页面接口不一样，
            // db获取数据，要把任务交给子类实现，强制子类复写这个行为

            Result result = onLoadData();
            mCurrentState = result.getState();

            //3.拿到数据后更新view
//        Log.e(TAG, "成功拿到数据，更新UI");
            UiUtil.post(new Runnable() {
                @Override
                public void run() {
                    updateUI();
                }
            });


//            Log.e(TAG,"请求服务器");
//            UiUtil.post(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(UiUtil.getContext(),"请求服务器",Toast.LENGTH_LONG).show();
//                }
//            });

//            Log.e(TAG, "成功拿到数据，更新UI");
        }
    }

    public enum Result{
        SUCESS(STATE_SUCCESS),FAILED(STATE_ERROR),EMPTY(STATE_EMPTY);

        private int state;

        Result(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }

    /**
     * 子类复写此方法，访问网络
     */
    protected abstract Result onLoadData();
}
