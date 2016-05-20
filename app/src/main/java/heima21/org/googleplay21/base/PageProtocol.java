package heima21.org.googleplay21.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 支持分页加载的协议
 */
public abstract class PageProtocol<T> extends BaseProtocol<T> {


    //分页加载的方法
    public T loadPage(String index) throws Exception{
        Map<String,String> params = new HashMap<String,String>();
        params.put("index", index);
        setParams(params);
        return loadData();
    }
}
