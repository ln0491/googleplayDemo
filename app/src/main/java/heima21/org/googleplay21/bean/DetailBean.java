package heima21.org.googleplay21.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/5.
 */
public class DetailBean {


    /**
     * author : 有缘网
     * date : 2014-04-24
     * des : 产品介绍
     * downloadNum : 200万+
     * downloadUrl : app/com.youyuan.yyhl/com.youyuan.yyhl.apk
     * iconUrl : app/com.youyuan.yyhl/icon.jpg
     * id : 1525490
     * name : 有缘网
     * packageName : com.youyuan.yyhl
     * safe : [{"safeDes":"已通过安智市场官方认证，是正版软件","safeDesColor":0,"safeDesUrl":"app/com.youyuan.yyhl/safeDesUrl0.jpg","safeUrl":"app/com.youyuan.yyhl/safeIcon0.jpg"},{"safeDes":"已通过安智市场安全检测，请放心使用","safeDesColor":0,"safeDesUrl":"app/com.youyuan.yyhl/safeDesUrl1.jpg","safeUrl":"app/com.youyuan.yyhl/safeIcon1.jpg"},{"safeDes":"无任何形式的广告","safeDesColor":0,"safeDesUrl":"app/com.youyuan.yyhl/safeDesUrl2.jpg","safeUrl":"app/com.youyuan.yyhl/safeIcon2.jpg"}]
     * screen : ["app/com.youyuan.yyhl/screen0.jpg","app/com.youyuan.yyhl/screen1.jpg","app/com.youyuan.yyhl/screen2.jpg","app/com.youyuan.yyhl/screen3.jpg","app/com.youyuan.yyhl/screen4.jpg"]
     * size : 3876203
     * stars : 4.0
     * version : 4.1.9
     */

    public String author;
    public String date;
    public String des;
    public String downloadNum;
    public String downloadUrl;
    public String iconUrl;
    public int id;
    public String name;
    public String packageName;
    public int size;
    public float stars;
    public String version;
    /**
     * safeDes : 已通过安智市场官方认证，是正版软件
     * safeDesColor : 0
     * safeDesUrl : app/com.youyuan.yyhl/safeDesUrl0.jpg
     * safeUrl : app/com.youyuan.yyhl/safeIcon0.jpg
     */

    public List<SafeEntity> safe;
    public List<String> screen;

    public static class SafeEntity {
        public String safeDes;
        public int safeDesColor;
        public String safeDesUrl;
        public String safeUrl;
    }
}
