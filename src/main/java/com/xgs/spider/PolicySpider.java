package com.xgs.spider;

import com.arronlong.httpclientutil.common.HttpConfig;
import com.xgs.pojo.Article;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;

public class PolicySpider {

  static Map<String, String> map;

  static {
    map = new HashMap<>();
    map.put("rd", "http://www.farmer.com.cn/xbpd/xw/rdjj_2458/");
    map.put("zc", "http://www.farmer.com.cn/xbpd/zc/");
    map.put("jd", "http://www.farmer.com.cn/xbpd/zc/zcyjd_2461/");
    map.put("al", "http://www.farmer.com.cn/xbpd/al/");
    map.put("rw", "http://www.farmer.com.cn/xbpd/al/xwrw_2468/");
    map.put("zf", "http://www.farmer.com.cn/xbpd/kj/zf/");
    map.put("wh", "http://www.farmer.com.cn/xbpd/wl/wh/");
    map.put("sj", "http://www.farmer.com.cn/xbpd/sj/");
    map.put("fx", "http://www.farmer.com.cn/xbpd/sj/fx/");
  }

  //提供各个种类信息的爬取操作
  HttpConfig config;
  CookieStore cookieStore = new BasicCookieStore();
  HttpClientContext context = new HttpClientContext();

  List<Article> getArticles() {

    return null ;
  }


}
