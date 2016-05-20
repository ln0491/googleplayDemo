package heima21.org.googleplay21.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import heima21.org.googleplay21.base.BaseProtocol;
import heima21.org.googleplay21.bean.HomeBean;

/**
 * Created by Administrator on 2016/4/29.
 */
public class AppProtocol extends BaseProtocol<List<HomeBean.ApkItem>> {
    @Override
    protected List<HomeBean.ApkItem> parserJson(String json) {
        Gson gson = new Gson();
        //泛型解析，type是class实现的接口，使用typetoken的匿名类，再getType即可
        return gson.fromJson(json, new TypeToken<List<HomeBean.ApkItem>>(){}.getType());
    }

    @Override
    protected String getInterfacePath() {
        return "/app";
    }
}
