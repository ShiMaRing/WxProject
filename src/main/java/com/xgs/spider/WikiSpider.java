package com.xgs.spider;


import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.xgs.pojo.Wiki;
import com.xgs.pojo.WikiIndex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class WikiSpider {

  CookieStore cookieStore = new BasicCookieStore();
  HttpClientContext context = new HttpClientContext();
  HttpConfig httpConfig;

  public WikiSpider() {
    context.setCookieStore(cookieStore);
    httpConfig = getHttpConfig();
    httpConfig.context(context);
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
  public List<WikiIndex> getIndex() {
    String base = "http://www.nongyie.com";
    List<WikiIndex> wikiIndices = new ArrayList<>();
    String url = "http://www.nongyie.com/templets/nybk/yfiles/sitemap.html";

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
   * 只对前五十条获取图片介绍等元素 对其他的就返回标题以及url 函数直接将值写入数据库中 请求的时候动态获取，部分提前加载提高效率
   */

  /**
   * 需要分三步爬取，第一步获取全部的个数并且获取所有页面的url 第二部进入具体列表页面，判断是否到达最大爬取次数，没有到达就爬取具体内容，达到了就只获取标题以及url
   */
  int max;//每个类别最多爬取50条带具体信息的

  public List<Wiki> getBaseInfo(String url) {
    max = 50;
    List<String> nextUrls = new ArrayList<>();
    httpConfig.url(url);
    String html = "";
    try {
      html = HttpClientUtil.get(httpConfig);
    } catch (HttpProcessException e) {
      e.printStackTrace();
    }
    //先获取页面总个数
    Document document = Jsoup.parse(html);
    Elements pageinfo = document.getElementsByClass("pageinfo");
    Elements strong = pageinfo.get(0).getElementsByTag("strong");
    String text = strong.get(1).text();
    int allCount = Integer.parseInt(text);
    max = Math.min(max, allCount);//获取需要具体属性的最大文章数量
    //首先获取所有的文章列表路径url
    //判断列表集合是否为空，为空则说明没有存储列表，否则需要添加

    Elements urlList = document.getElementsByAttributeValue("name", "sldd");

    Elements options;
    //如果找不到的话，说明只有单页，此时需要另外找方法获取
    if (urlList == null || urlList.size() == 0) {
      //直接把当前主页当作url也行
      nextUrls.add(url);
    } else {
      //有多条可以多条提取
      options = urlList.get(0).getElementsByTag("option");
      for (Element option : options) {
        String nextUrl = url + option.attr("value");
        nextUrls.add(nextUrl);
      }
    }
    List<Wiki> wikis = new ArrayList<>();

    //接下来读取各个nexturl并进行列表查询
    for (String nextUrl : nextUrls) {
      try {
        //获取wikis,需要调用dao将其进行存储
        wikis.addAll(parseList(nextUrl));
      } catch (HttpProcessException e) {
        e.printStackTrace();
      }
    }

    return wikis;
  }

  public List<Wiki> parseList(String url) throws HttpProcessException {
    List<Wiki> wikis = new ArrayList<>();
    String base = "http://www.nongyie.com";
    httpConfig.url(url);
    //获取列表页面，根据是否需要爬取来判断
    String html = HttpClientUtil.get(httpConfig);
    Document document = Jsoup.parse(html);
    Elements lis = document.getElementsByClass("middle").get(0).getElementsByTag("ul").get(0)
        .getElementsByTag("li");
    //拿到所有的li,解析里面的元素
    for (Element li : lis) {
      boolean flag = true;
      Wiki wiki;
      String articleUrl = base + li.getElementsByTag("a").get(0).attr("href");
      String title = li.text();
      if (max > 0) {
        //说明需要爬取具体内容
        try {
          wiki = parse(articleUrl);
        } catch (IndexOutOfBoundsException exception) {
          wiki = new Wiki();
          max++;
          flag = false;
        }
        max--;
      } else {
        //反之只返回url以及title
        wiki = new Wiki();
        wiki.setBlank(true);
      }
      wiki.setTitle(title);
      wiki.setUrl(articleUrl);
      if (flag) {
        wikis.add(wiki);
      }
    }
    System.out.println(wikis);
    return wikis;
  }

  public Wiki parse(String url) {
    //解析具体的url文章，并将其封装返回
    Wiki wiki = new Wiki();
    httpConfig.url(url);
    String result = "";
    try {
      result = HttpClientUtil.get(httpConfig);
    } catch (HttpProcessException e) {
      e.printStackTrace();
    }
    StringBuilder stringBuilder = new StringBuilder();
    Document document = Jsoup.parse(result);
    //解析文章，然后判断是否有下一页
    Element boxbody = document.getElementsByClass("boxbody").get(0);
    String desc = boxbody.getElementsByTag("p").get(0).text();
    wiki.setDesc(desc);
    stringBuilder.append(boxbody.html());
    Elements img = boxbody.getElementsByTag("img");
    if (img != null && img.size() != 0) {
      wiki.setImg(img.get(0).attr("src"));
    }
    Element dede_page = document.getElementsByClass("dede_pages").get(0);
    Elements lis = dede_page.getElementsByTag("li");

    if (lis == null || lis.size() == 0) {
      //直接解析并返回,反之就解析下一页
      wiki.setContent(stringBuilder.toString());
      return wiki;
    } else {
      String nextUrl = lis.get(lis.size() - 1).getElementsByTag("a").get(0).attr("href");//接下来进行拼接处理
      if (nextUrl.equals("#")) {
        wiki.setContent(stringBuilder.toString());
        return wiki;
      }
      //使用正则表达式处理
      String pattern = "(\\w+.html)$";
      Pattern r = Pattern.compile(pattern);
      Matcher matcher = r.matcher(url);
      String next = matcher.replaceFirst(nextUrl);
      Wiki parse = parse(next);
      stringBuilder.append(parse.getContent());
    }
    wiki.setContent(stringBuilder.toString());
    return wiki;
  }

  /*该方法用来解析url,传入的参数为url，需要进行解析，
   * */

  public static void main(String[] args) {
    WikiSpider wIkiSpider = new WikiSpider();
    List<WikiIndex> index = wIkiSpider.getIndex();
    wIkiSpider.getBaseInfo("http://www.nongyie.com/zxuqinyang/");
  }
}
