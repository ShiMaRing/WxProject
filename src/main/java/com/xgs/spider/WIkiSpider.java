package com.xgs.spider;


import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.xgs.pojo.WikiIndex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class WIkiSpider {

  CookieStore cookieStore = new BasicCookieStore();
  HttpClientContext context = new HttpClientContext();
  HttpConfig httpConfig;

  public WIkiSpider() {
    context.setCookieStore(cookieStore);
  }

  HttpConfig getHttpConfig() {
    String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.41 Safari/537.36";
    String refer = "https://www.nongyie.com/zshuichanyu/list_27_3.html";
    Header[] headers = HttpHeader.custom().userAgent(userAgent).referer(refer).build();
    return HttpConfig.custom().headers(headers);
  }

  /**
   * 该方法用来返回标题title
   */
  List<WikiIndex> getIndex() {
    String base = "http://www.nongyie.com";
    List<WikiIndex> wikiIndices = new ArrayList<>();
    String url = "http://www.nongyie.com/templets/nybk/yfiles/sitemap.html";

    httpConfig = getHttpConfig();
    httpConfig.context(context);

    try {
      httpConfig.url(url);
      String html = HttpClientUtil.get(httpConfig);
      Document document = Jsoup.parse(html);
      Elements linkboxs = document.getElementsByClass("linkbox");
      for (Element linkbox : linkboxs) {
        WikiIndex wikiIndex = new WikiIndex();
        Elements h3 = linkbox.getElementsByTag("h3");
        Element a = h3.get(0).getElementsByTag("a").get(0);
        wikiIndex.setTitle(a.text());
        wikiIndex.setMap(new HashMap<>());
        Elements lis = linkbox.getElementsByTag("li");
        for (Element li : lis) {
          String inner = li.getElementsByTag("a").get(0).text();
          String nextUrl = base + li.getElementsByTag("a").get(0).attr("href");
          wikiIndex.getMap().put(inner, nextUrl);
        }
        wikiIndices.add(wikiIndex);
      }
    } catch (HttpProcessException e) {
      e.printStackTrace();
    }
    return wikiIndices;
  }

  /**
   * 只对前五十条获取图片介绍等元素 对其他的就返回标题即可 函数直接将值写入数据库中
   */
  void writeContent(String url) {



  }
  /*该方法用来解析url,传入的参数为url，需要进行解析，
   * */

  public static void main(String[] args) {
    WIkiSpider wIkiSpider = new WIkiSpider();
    List<WikiIndex> index = wIkiSpider.getIndex();
    System.out.println(index);
  }
}
