package heima21.org.googleplay21.protocol;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import heima21.org.googleplay21.base.BaseProtocol;
import heima21.org.googleplay21.bean.HomeBean;
import heima21.org.googleplay21.util.Constants;

/**
 * 首页fragment的应用层协议
 */
public class HomeProtocol {

//    @Override
//    protected HomeBean parserJson(String json) {
//        return null;
//    }
//
//    @Override
//    protected String getInterfacePath() {
//        return null;
//    }

    /**
     * 加载数据
     */
    public HomeBean loadData(String index) throws Exception{
        OkHttpClient client = new OkHttpClient();
        String url = Constants.BASE_SEVER + "/home?index=" + index;
        Request request = new Request.Builder().get().url(url).build();
//        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                String json = response.body().string();
                Gson gson = new Gson();
                HomeBean homeBean = gson.fromJson(json, HomeBean.class);
                return homeBean;
            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        return null;
    }
}
