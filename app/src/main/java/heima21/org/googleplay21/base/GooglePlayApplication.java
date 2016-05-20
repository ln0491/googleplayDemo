package heima21.org.googleplay21.base;

import android.app.Application;

import heima21.org.googleplay21.util.UiUtil;

/**
 * 应用对应的类，清单文件中配置
 */
public class GooglePlayApplication extends Application {

    /**
     * 应用启动调用的第一个方法
     */
    @Override
    public void onCreate() {
        super.onCreate();
//        getApplicationContext()
        UiUtil.init(this);
    }
}
