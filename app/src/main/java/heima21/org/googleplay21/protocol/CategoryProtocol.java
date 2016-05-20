package heima21.org.googleplay21.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import heima21.org.googleplay21.base.BaseProtocol;
import heima21.org.googleplay21.bean.CategoryBean;
import heima21.org.googleplay21.vo.CategoryVo;

/**
 * Created by Administrator on 2016/5/3.
 */
public class CategoryProtocol extends BaseProtocol<List<CategoryBean>> {
    @Override
    protected List<CategoryBean> parserJson(String json) {
        List<CategoryVo> vos =  new Gson().fromJson(json, new TypeToken<List<CategoryVo>>(){}.getType());

        List<CategoryBean> beans = new ArrayList<>();
        //改装数据结构
        for(CategoryVo vo : vos){
            CategoryBean titleBean = new CategoryBean();
            titleBean.type = CategoryBean.TYPE_TITLE;
            titleBean.title = vo.title;
            beans.add(titleBean);

            List<CategoryVo.InfosEntity> infos = vo.infos;
            for(CategoryVo.InfosEntity info : infos){
                CategoryBean normalBean = new CategoryBean();
                normalBean.type = CategoryBean.TYPE_NORMAL;
                normalBean.name1 = info.name1;
                normalBean.name2 = info.name2;
                normalBean.name3 = info.name3;

                normalBean.url1 = info.url1;
                normalBean.url2 = info.url2;
                normalBean.url3 = info.url3;
                beans.add(normalBean);
            }
        }

        return beans;
    }

    @Override
    protected String getInterfacePath() {
        return "/category";
    }
}
