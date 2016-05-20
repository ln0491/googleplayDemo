package heima21.org.googleplay21.holder;

import android.media.Rating;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heima21.org.googleplay21.R;
import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.bean.HomeBean;
import heima21.org.googleplay21.manager.DownloadManager;
import heima21.org.googleplay21.manager.ThreadManager;
import heima21.org.googleplay21.util.Constants;
import heima21.org.googleplay21.util.UiUtil;
import heima21.org.googleplay21.view.CircleProgressView;

/**
 * 主页的holder
 */
public class HomeHolder extends BaseHolder<HomeBean.ApkItem> implements DownloadManager.OnDownloadListener {

    @Bind(value = R.id.item_appinfo_ll_download)
    protected LinearLayout mLLDownload;
    @Bind(value = R.id.item_appinfo_tv_downloadstate)
    protected TextView mTvDownloadState;

    @Bind(value = R.id.item_appinfo_progress)
    protected CircleProgressView mProgressView;

    @Bind(value = R.id.item_appinfo_iv_icon)
    protected ImageView mIvIcon;

    @Bind(value = R.id.item_appinfo_tv_title)
    protected TextView mTvTitle;
    @Bind(value = R.id.item_appinfo_tv_size)
    protected TextView mTvSize;
    @Bind(value = R.id.item_appinfo_tv_des)
    protected TextView mTvDes;

    @Bind(value = R.id.item_appinfo_rb_stars)
    protected RatingBar mRbStars;
    private String TAG = getClass().getSimpleName();
    private int mCurrentState;
    private long mProgress;
    private DownloadManager.DownloadTask mTask;
    private HomeBean.ApkItem mData;

    @Override
    public View inflateAndFindView() {
        View convertView = View.inflate(UiUtil.getContext(), R.layout.item_app_info,null);
        ButterKnife.bind(this, convertView);
//        View convertView = View.inflate(UiUtil.getContext(), R.layout.home_temp_item,null);
//        textView = (TextView) convertView.findViewById(R.id.home_list_item_title);
        return convertView;
    }

    @Override
    public void setData(HomeBean.ApkItem data) {
        this.mData = data;
//        http://localhost:8080/GooglePlayServer/image?name=app/com.youyuan.yyhl/icon.jpg
        String iconUrl = Constants.IMG_URL + data.iconUrl;
        //with(context)-->load(url)-->into(view) 替代图 /错误图
        Picasso.with(UiUtil.getContext()).load(iconUrl).
                placeholder(R.drawable.ic_default).error(R.drawable.ic_default).into(mIvIcon);
        mTvTitle.setText(data.name);
        mTvSize.setText(Formatter.formatFileSize(UiUtil.getContext() ,data.size) );
        mTvDes.setText(data.des);
//        textView.setText(data +"来自于抽取后的Adapter");
        checkState();
    }

    public void checkState() {
        //主动获取一次状态，初始化
        mCurrentState = DownloadManager.getInstance().getState(mData.packageName,mData.downloadUrl,mData.size);
        refreshUi();
    }

    @OnClick(value = R.id.item_appinfo_ll_download)
    public void onClickLinearLayout(View view){
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
        Log.e(TAG, state + "..............");
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
                        mTvDownloadState.setText("下载");
                        mProgressView.setState(CircleProgressView.State.DOWNLOAD);
                        break;
                    case DownloadManager.STATE_WAITING:
                        mTvDownloadState.setText("等待中");
                        mProgressView.setState(CircleProgressView.State.WAITING);
                        break;
                    case DownloadManager.STATE_DOWNLOADING:
                        int percent = (int) (mProgress * 100f / mData.size);
                        //不断的更新矩形，进度条的进度
                        mProgressView.setProgress(percent);
                        mProgressView.setState(CircleProgressView.State.LOADING);
//                        mProgressView.setProgressDrawable(UiUtil.getContext().getResources().getDrawable(R.drawable.sp_progress));
                        mTvDownloadState.setText(percent + "%");
                        break;
                    case DownloadManager.STATE_FAILED:
                        mTvDownloadState.setText("重试");
                        mProgressView.setState(CircleProgressView.State.RETRY);
                        break;
                    case DownloadManager.STATE_PAUSE:
                        mTvDownloadState.setText("继续下载");
                        mProgressView.setState(CircleProgressView.State.PAUSE);
                        break;
                    case DownloadManager.STATE_SUCCESS:
                        mTvDownloadState.setText("安装");
                        mProgressView.setState(CircleProgressView.State.OPEN);
                        break;
                    case DownloadManager.STATE_INSTALLED:
                        mTvDownloadState.setText("打开");
                        mProgressView.setState(CircleProgressView.State.OPEN);
                        break;
                }
            }
        });

    }
}
