package heima21.org.googleplay21.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import heima21.org.googleplay21.base.PageProtocol;
import heima21.org.googleplay21.bean.HomeBean;

/**
 * Created by Administrator on 2016/4/29.
 */
public class GameProtocol extends PageProtocol<List<HomeBean.ApkItem>> {
    @Override
    protected List<HomeBean.ApkItem> parserJson(String json) {
        return new Gson().fromJson(json, new TypeToken<List<HomeBean.ApkItem>>(){}.getType());
    }

    @Override
    protected String getInterfacePath() {
        return "/game";
    }
}
