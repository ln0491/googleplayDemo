package heima21.org.googleplay21.holder;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heima21.org.googleplay21.R;
import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.bean.DetailBean;
import heima21.org.googleplay21.manager.DownloadManager;
import heima21.org.googleplay21.manager.ThreadManager;
import heima21.org.googleplay21.util.UiUtil;
import heima21.org.googleplay21.view.ProgressButton;

/**
 * Created by Administrator on 2016/5/5.
 */
public class DetailDownloadHolder extends BaseHolder<DetailBean> implements DownloadManager.OnDownloadListener{
    @Bind(value = R.id.app_detail_download_btn_download)
    protected ProgressButton mDownloadBtn; //下载按钮需要知道当前下载状态，第一种是直接主动获取，第二种是监听获取
    private DetailBean mData;
    private String TAG = "DetailDownloadHolder";
    private int mCurrentState;
    private long mProgress;
    private DownloadManager.DownloadTask mTask;

    @Override
    public View inflateAndFindView() {
        View downloadView = View.inflate(UiUtil.getContext(), R.layout.item_app_detail_bottom,null);
        ButterKnife.bind(this, downloadView);
        return downloadView;
    }

    @Override
    public void setData(DetailBean data) {
        this.mData = data;
//        //主动获取一次状态，初始化
//        mCurrentState = DownloadManager.getInstance().getState(mData.packageName,mData.downloadUrl,mData.size);
//        refreshUi();
        checkState();
    }

    @OnClick(value = R.id.app_detail_download_btn_download)
    public void clickDownloadBtn(View view){
//        //1.开启下载
//        download();
        //点击行为指向下一步的任务
        switch (mCurrentState){
            case DownloadManager.STATE_UNDOWNLOAD:
                download();
                break;
            case DownloadManager.STATE_WAITING:
                cancel();//线程池满，取消任务
                break;
            case DownloadManager.STATE_DOWNLOADING:
                pause();
                break;
            case DownloadManager.STATE_FAILED:
                download();//重新下载
                break;
            case DownloadManager.STATE_PAUSE:
                download();//继续下载（从哪里开始下载range）
                break;
            case DownloadManager.STATE_SUCCESS:
                install();
                break;
            case DownloadManager.STATE_INSTALLED:
                open();
                break;
        }
    }

    private void open() {
        DownloadManager.getInstance().open(mData.packageName);
    }

    private void install() {
        DownloadManager.getInstance().install(mData.packageName, mData.downloadUrl);
    }

    private void pause() {
        //暂停需要打断下载动作，设置一个开关（flag)，终止task中的文件流写，与网络流读
        DownloadManager.getInstance().pause(mData.packageName);
    }

    private void cancel() {
        //1.调用线程池去remove Task
        ThreadManager.getDownloadPool().remove(mTask);
        //2.切换状态到不下载，更新ui
        onStateChange(DownloadManager.STATE_UNDOWNLOAD);
        refreshUi();
    }

    private void download() {
        DownloadManager.getInstance().download(mData.downloadUrl, mData.packageName);
    }

    @Override
    public void onStateChange(int state) {
        Log.e(TAG,state + "..............");
        mCurrentState = state;
        refreshUi();
    }

    @Override
    public int getState() {
        return mCurrentState;

    }

    @Override
    public void onProgressChange(long progress) {
        this.mProgress = progress;
        //不停的刷新UI
        refreshUi();
    }

    @Override
    public void setTask(DownloadManager.DownloadTask task) {
        this.mTask = task;
    }

    private void refreshUi() {
        //子线程通知我们更新
        UiUtil.post(new Runnable() {
            @Override
            public void run() {
                switch (mCurrentState) {
                    case DownloadManager.STATE_UNDOWNLOAD:
                        //Ui显示提示下一步操作
                        mDownloadBtn.setText("下载");
                        break;
                    case DownloadManager.STATE_WAITING:
                        mDownloadBtn.setText("等待中");
                        break;
                    case DownloadManager.STATE_DOWNLOADING:
                        int percent = (int) (mProgress * 100f / mData.size);
                        //不断的更新矩形，进度条的进度
                        mDownloadBtn.setProgress(percent);
                        mDownloadBtn.setProgressDrawable(UiUtil.getContext().getResources().getDrawable(R.drawable.sp_progress));
                        mDownloadBtn.setText(percent + "%");
                        break;
                    case DownloadManager.STATE_FAILED:
                        mDownloadBtn.setText("重试");
                        break;
                    case DownloadManager.STATE_PAUSE:
                        mDownloadBtn.setText("继续下载");
                        break;
                    case DownloadManager.STATE_SUCCESS:
                        mDownloadBtn.setText("安装");
                        break;
                    case DownloadManager.STATE_INSTALLED:
                        mDownloadBtn.setText("打开");
                        break;
                }
            }
        });

    }

    public void checkState() {
        //主动获取一次状态，初始化
        mCurrentState = DownloadManager.getInstance().getState(mData.packageName,mData.downloadUrl,mData.size);
        refreshUi();
    }
}
