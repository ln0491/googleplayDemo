package heima21.org.googleplay21.manager;

import android.os.Environment;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import heima21.org.googleplay21.util.CommonUtils;
import heima21.org.googleplay21.util.Constants;
import heima21.org.googleplay21.util.UiUtil;

/**
 * 多线程、断点续传管理类
 */
public class DownloadManager {
    //可以监听到4种状态
    public static final int STATE_UNDOWNLOAD = 0;  //ok
    //等待状态，有空余线程，很快拜托等待状态，无空余线程，就会阻塞住
    public static final int STATE_WAITING = 1;  //OK
    public static final int STATE_DOWNLOADING = 2;  //OK
    public static final int STATE_PAUSE= 3;  //TODO:

    public static final int STATE_FAILED = 4;  //OK
    public static final int STATE_SUCCESS = 5; //OK
    public static final int STATE_INSTALLED = 6;  //OK

    private static DownloadManager sInstance;
    private String TAG = "DownloadManager";

    public int getState(String packageName,String downloadUrl,long size) {
        //1.是否为已经安装的状态
        if(CommonUtils.isInstalled(UiUtil.getContext(),packageName)){
            return STATE_INSTALLED;
        }
        //2.去listener中去取（4种下载流程状态）
        OnDownloadListener listener = mListeners.get(packageName);
        if(listener!=null){
           return listener.getState();
        }else{
            //下载线程池中没有当前包名的状态
            //1.暂停，下载一部分内容，用户点击按钮会触发暂停（文件只下载了一部分）
            File file = getDownloadFile(downloadUrl);
            if(file.exists()){
                if(file.length() == size){
                    //下载完成
                    return STATE_SUCCESS;
                }else{
                    //下载部分
                    return STATE_PAUSE;
                }
            }else{
                //2.用户没有下载
                return STATE_UNDOWNLOAD;
            }
        }
    }

    public void open(String packageName) {
        CommonUtils.openApp(UiUtil.getContext(),packageName);
    }

    public void pause(String packageName) {
        //1.改变当前状态为暂停，使用listener
        OnDownloadListener listener = mListeners.get(packageName);
        if(listener!=null){
            listener.onStateChange(STATE_PAUSE);
        }
    }

    public interface OnDownloadListener{
        //1.在下载过程中，downloadtask第一时间拿到新的状态
        //2.task通知listener状态变了，
        //3.监听器的主人可以根据onstateChange拿到状态
        public  void onStateChange(int state);

        //获取状态的方法
        public int getState();

        //进度改变
        public void onProgressChange(long progress);

        public void setTask(DownloadTask task);
    }

    private Map<String,OnDownloadListener> mListeners = new HashMap<>();
    public void removeOnDownloadListener(String packname, OnDownloadListener listener){
        if(mListeners.containsKey(packname)){
            //已经有这个监听器
            mListeners.remove(listener);
        }
    }
    public void addOnDownloadListener(String packname, OnDownloadListener listener){
        if(mListeners.containsKey(packname)){
            //已经有这个监听器
            return;
        }else{
            mListeners.put(packname, listener);
        }
    }

    private DownloadManager() {
    }

    public static DownloadManager getInstance() {
        if (sInstance == null) {
            //因为是多线程下载，所以要上线程锁
            synchronized (DownloadManager.class) {
                if (sInstance == null) {
                    sInstance = new DownloadManager();
                }
            }
        }
        return sInstance;
    }

    public void install(String packageName,String downloadUrl) {
        if(CommonUtils.isInstalled(UiUtil.getContext(),packageName)){
            return;
        }
        File apkFile = getDownloadFile(downloadUrl);
        if(apkFile.exists()) {
            CommonUtils.installApp(UiUtil.getContext(), apkFile);
        }
    }


    //下载文件的方法，需要提供uri，
    public void download(String uri, String packname) {
        //2.使用线程池
        //###等待中的状态，如果是第一次，时间非常短，---》STATE_WAITING
        notifyState(packname, STATE_WAITING);
        DownloadTask task = new DownloadTask(uri, packname);
        //绑定方法，一个listener对应一个task
        OnDownloadListener listener = mListeners.get(packname);
        if(listener!=null){
            listener.setTask(task);
        }
        ThreadManager.getDownloadPool().execute(task);
    }

    //2.通知监听器状态改变
    private void notifyState(String pack, int state) {
        for(Map.Entry<String,OnDownloadListener> entry : mListeners.entrySet()){
            String packname = entry.getKey();
            OnDownloadListener listener = entry.getValue();
//            TODO:所有监听器都通知改变了，需要过滤
            if(pack.equals(packname)) {
                listener.onStateChange(state);
            }
        }
    }

    private void notifyProgress(String mPack, long progress) {
        OnDownloadListener listener = mListeners.get(mPack);
        if(listener!=null){
            //调用监听器，进度改变了
            listener.onProgressChange(progress);
        }
//        for(Map.Entry<String,OnDownloadListener> entry : mListeners.entrySet()){
//            String packname = entry.getKey();
//            OnDownloadListener listener = entry.getValue();
////            TODO:所有监听器都通知改变了，需要过滤
//            if(pack.equals(packname)) {
//                listener.onStateChange(state);
//            }
//        }
    }

    public class DownloadTask implements Runnable {
        public   String mUri;
        public  String mPack;
        private boolean isPause;

        public DownloadTask(String uri,String pack) {
            this.mPack = pack;
            this.mUri = uri;
        }

        @Override
        public void run() {
            Log.e(TAG, "我要多线程下载");
            //    http://localhost:8080/GooglePlayServer
            // /download?name=app/com.youyuan.yyhl/com.youyuan.yyhl.apk&range=0
            //3.使用Okhttpclient
            long range = 0;
            //a.暂停过的才会继续下载，才需要赋值这个range,本地文件的大小其实就是这个range
            File file = getDownloadFile(mUri);
            if(file.exists()){
                range = file.length();
            }
            OkHttpClient client = new OkHttpClient();
            String url = Constants.BASE_SEVER + "/download?name=" + mUri + "&range=" + range;
            Request request = new Request.Builder().get().url(url).build();
            InputStream ins = null;
            FileOutputStream fos = null;
            try {
                //c.从range处开始更新
                long progress = range;
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    //###开始下载--->STATE_DOWNLOADING
                    notifyState(mPack, STATE_DOWNLOADING);
                    ins = response.body().byteStream();
                    //b.追加流，append效果
                    fos = new FileOutputStream(getDownloadFile(mUri),true);
                    isPause = false;
                    int len = -1;
                    byte[] buffer = new byte[1024];
                    while ((len = ins.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();

                        //### progress增加length，刷新UI
                        progress += len;
                        //通知进度改变了
                        notifyProgress(mPack, progress);

                        //###3.在此处打断下载,需要知道实时的状态
                        OnDownloadListener listener = mListeners.get(mPack);
                        if(listener!=null){
                            int state = listener.getState();
                            if(STATE_PAUSE == state){
                                isPause = true;
                            }
                        }

                        if(isPause){
                            //4.退出循环
                            break;
                        }
                    }
                    if(isPause){
                        //通知状态暂停
                    }else {
                        Log.e(TAG, "下载成功");
                        //###开始下载--->STATE_SUCCESS
                        notifyState(mPack, STATE_SUCCESS);
                    }
                } else {
                    //TODO:
                    Log.e(TAG, "网络错误");
                    //###开始下载--->STATE_FAILED
                    notifyState(mPack,STATE_FAILED);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "下载错误");
                notifyState(mPack,STATE_FAILED);
                //###开始下载--->STATE_FAILED
            }finally {
                if(ins!=null){
                    try {
                        ins.close();
                        ins = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(fos!=null){
                    try {
                        fos.close();
                        ins = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private File getDownloadFile(String uri) {
        //1.dir
        File dir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //有sdcard
            dir = new File(Environment.getExternalStorageDirectory(),
                    "Android/data/" + UiUtil.getContext().getPackageName() + "/apk");
        } else {
            //rom
            dir = new File(UiUtil.getContext().getCacheDir(), "apk");
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        uri = uri.substring(uri.lastIndexOf("/"));
        Log.e(TAG, uri);
        File file = new File(dir, uri);
        return file;
    }
}
