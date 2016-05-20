package heima21.org.googleplay21.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import heima21.org.googleplay21.base.BaseProtocol;
import heima21.org.googleplay21.bean.DetailBean;

/**
 * Created by Administrator on 2016/5/5.
 */
public class DetailProtocol extends BaseProtocol<DetailBean> {
    @Override
    protected DetailBean parserJson(String json) {
        return new Gson().fromJson(json, new TypeToken<DetailBean>(){}.getType());
    }

    @Override
    protected String getInterfacePath() {
        return "/detail";
    }
}
