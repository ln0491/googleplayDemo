package heima21.org.googleplay21.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class HomeBean {


    public List<String> picture;
    /**
     * id : 1525490
     * name : 有缘网
     * packageName : com.youyuan.yyhl
     * iconUrl : app/com.youyuan.yyhl/icon.jpg
     * stars : 4
     * size : 3876203
     * downloadUrl : app/com.youyuan.yyhl/com.youyuan.yyhl.apk
     * des : 产品介绍：有缘是时下最受大众单身男女亲睐的婚恋交友软件。有缘网专注于通过轻松、
     */

    public List<ApkItem> list;

    public static class ApkItem {
        public int id;
        public String name;
        public String packageName;
        public String iconUrl;
        public float stars;
        public int size;
        public String downloadUrl;
        public String des;
    }
}
