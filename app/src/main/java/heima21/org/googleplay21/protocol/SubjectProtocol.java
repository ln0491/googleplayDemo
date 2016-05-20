package heima21.org.googleplay21.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import heima21.org.googleplay21.base.BaseProtocol;
import heima21.org.googleplay21.base.PageProtocol;
import heima21.org.googleplay21.bean.SubjectBean;

/**
 * Created by Administrator on 2016/4/29.
 */
public class SubjectProtocol extends PageProtocol<List<SubjectBean>> {
    @Override
    protected List<SubjectBean> parserJson(String json) {
        return new Gson().fromJson(json, new TypeToken<List<SubjectBean>>(){}.getType());
    }

    @Override
    protected String getInterfacePath() {
        return "/subject";
    }
}
