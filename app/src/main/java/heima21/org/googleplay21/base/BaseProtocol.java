package heima21.org.googleplay21.base;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import heima21.org.googleplay21.bean.HomeBean;
import heima21.org.googleplay21.util.Constants;
import heima21.org.googleplay21.util.MD5Utils;
import heima21.org.googleplay21.util.UiUtil;

/**
 * 支持多个页面的协议
 */
public abstract class BaseProtocol<T> {

    //支持多个页面的参数
    protected Map<String, String> mParams;
    private String TAG = "BaseProtocol";

    //子类url需要传参，必须调用此方法
    public void setParams(Map<String, String> mParams) {
        this.mParams = mParams;
    }

    /**
     * 加载数据,缓存写在这里（读缓存、保存缓存）
     */
    public T loadData() throws Exception{
        //是否有缓存
        T cache = getDataFromLocol();
        if(cache!=null){
            Log.e(TAG,"使用缓存");
            return cache;
        }
        Log.e(TAG,"网络访问");
        return getDataFromServer();
    }

    /**
     * 从本地取缓存
     * @return
     */
    private T getDataFromLocol() {
        //1.找到之前保存的缓存文件
        File cacheFile = getCacheFile();
        //2.文件流读取json
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(cacheFile));
            String time = reader.readLine();
            //时间计算,判断缓存是否过期
            if(Long.parseLong(time) + Constants.CACHE_OUT_TIME < System.currentTimeMillis()){
                //过期了
                return null;
            }
            String json = reader.readLine();
            Log.e(TAG,json);
            //3.转化成bean
            return parserJson(json);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private File getCacheFile() {
        //dir,--->fileName
        File dir = null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //has sdcard
            dir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + UiUtil.getContext().getPackageName() + "/json");
        }else{
            //rom
            dir = new File(UiUtil.getContext().getCacheDir(), "json");
        }
        if(!dir.exists()){
            //多级目录
            dir.mkdirs();
        }
        //一般以接口作为文件名，加密，一方面可以解决斜杠等特殊字符，接口需要保密
        String fileName = MD5Utils.encode(getUrl());
        Log.e(TAG, "fileName:" + fileName);
        File file = new File(dir, fileName);
        return file;
    }

    private T getDataFromServer() throws IOException {
        //1.new client
        OkHttpClient client = new OkHttpClient();
        //2.生成url
//        String url = Constants.BASE_SEVER + "/home?index=" + index;
        String url = getUrl();
        Request request = new Request.Builder().get().url(url).build();
//        try {
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            String json = response.body().string();
            T bean = parserJson(json);
            //save cache,要保存机器使用的一行格式
            saveCache(new Gson().toJson(bean));
            return bean;
//            Gson gson = new Gson();
//            T homeBean = gson.fromJson(json, T.class);
//            return homeBean;
        }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        return null;
    }

    private void saveCache(String json) {
        Log.e(TAG, "写入缓存");
        //fileWrite
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(getCacheFile()));
            //写入缓存时间
            writer.write(System.currentTimeMillis() + "");
            writer.write("\r\n");
            writer.write(json);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(writer!=null){
                try {
                    writer.close();
                    writer = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    protected abstract T parserJson(String json);

    //生成url，字符串拼接
    private String getUrl() {
        //1.base url
        String url = Constants.BASE_SEVER;
        //2.接口的path,不同页面是不一样
        url += getInterfacePath();
        //3.参数
        if(mParams != null){
            url+= "?";
            for(Map.Entry<String,String> entry : mParams.entrySet()){
                String key = entry.getKey();
                url += key;
                url+= "=";
                String value = entry.getValue();
                url += value;
                url+= "&";
            }
            url = url.substring(0, url.length() -1);
        }
        Log.e("BaseProtocol",url);
        return url;
    }

    //接口的字符串
    protected abstract String getInterfacePath();


}
