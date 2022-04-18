package com.xgs.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.xgs.pojo.Article;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
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

  String getContent(String url) {
    //根据url进行解析
    config = HttpConfig.custom().url(url);

    StringBuilder stringBuilder = new StringBuilder();
    try {
      String html = HttpClientUtil.get(config);


      Document doc = Jsoup.parse(html);
      Element article_main = doc.getElementById("article_main");
      Elements ps = article_main.getElementsByTag("p");
      for (Element p : ps) {
        String s = "";
        Elements span = p.getElementsByTag("span");
        for (Element element : span) {
          s += element.text();
        }
        stringBuilder.append(s);
      }
    } catch (HttpProcessException  e) {
      e.printStackTrace();
    }
    return stringBuilder.toString();
  }

  /**
   * 参数 category：分类 page：指定页数
   */
  public List<Article> getArticles(String category, int page) throws HttpProcessException {

    List<Article> list = new ArrayList<>();
    String suffixp = "NewsList_";
    String suffixs = ".json";
    context.setCookieStore(cookieStore);
    config = HttpConfig.custom().context(context);
    String preUrl = map.get(category);
    String url = preUrl + suffixp + page + suffixs;
    config.url(url);
    String html = HttpClientUtil.get(config);
    JSONArray objects = JSON.parseObject(html).getJSONArray("info");
    for (Object object : objects) {
      Article article = new Article();
      JSONObject ob = JSON.parseObject(object.toString());
      article.setType(category);
      article.setCreateTime(ob.getString("createTime"));
      article.setDescribe(ob.getString("description"));
      article.setId(ob.getString("id"));
      article.setOvtitle(ob.getString("ovtitle"));
      article.setSource(ob.getString("source"));
      article.setThumbImage(ob.getString("thumb_image"));
      article.setUrl(ob.getString("url"));
      article.setContent(getContent(article.getUrl()));
      list.add(article);
    }
    return list;
  }

  public static void main(String[] args) {

    try {
      List<Article> fx = new PolicySpider().getArticles("fx", 0);
      for (Article article : fx) {

        System.out.println(article.getUrl());
        System.out.println(article.getContent().length());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
