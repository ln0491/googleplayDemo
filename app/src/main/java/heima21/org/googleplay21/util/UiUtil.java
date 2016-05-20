package heima21.org.googleplay21.util;

import android.content.Context;
import android.os.Handler;
import android.util.TypedValue;
import android.widget.TextView;

import heima21.org.googleplay21.base.GooglePlayApplication;

/**
 * Ui操作相关的工具类，hanler更新Ui,获取资源的方法getResource
 */
public class UiUtil {

    //全局的context对象
    private static Context mContext;
    private static Handler mHandler;


    public static void init(Context googlePlayApplication) {
        mContext = googlePlayApplication;
        mHandler = new Handler();
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 提交任务，主线程更新UI
     * @param task
     */
    public static void post(Runnable task){
        mHandler.post(task);
    }

    /**
     * 延时任务
     * @param task
     * @param delay
     */
    public static void post(Runnable task, long delay){
        mHandler.postDelayed(task, delay);
    }

    public static void remove(Runnable task){
        mHandler.removeCallbacks(task);
    }

    /**
     * 获取String Array
     * @param resId
     * @return
     */
    public static String[] getStringArray(int resId) {
        return mContext.getResources().getStringArray(resId);
    }

    public static int dp2Px(int value) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,value,UiUtil.getContext().getResources().getDisplayMetrics());
        return (int) (px + 0.5f);
    }

    public static String getString(int resId) {
        return mContext.getString(resId);
    }

    //文字占位符
    public static String getString(int resId,Object... args) {
        return mContext.getString(resId, args);
    }
}
