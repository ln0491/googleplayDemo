package heima21.org.googleplay21.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import heima21.org.googleplay21.base.BaseProtocol;

/**
 * Created by Administrator on 2016/5/3.
 */
public class HotProtocol extends BaseProtocol<List<String>> {
    @Override
    protected List<String> parserJson(String json) {
        return new Gson().fromJson(json,new TypeToken<List<String>>(){}.getType());
    }

    @Override
    protected String getInterfacePath() {
        return "/hot";
    }
}
