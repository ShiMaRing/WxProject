package com.xgs.spider;

//用来爬取问答数据

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.common.HttpMethods;
import com.arronlong.httpclientutil.common.HttpResult;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.xgs.pojo.Answer;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;

/**
 * @author xgs 问答爬虫，根据提问返回对应的列表
 */

public class QuestionSpider {

  private static final int MAX = 5;
  HttpConfig config;
  HttpClientContext context = new HttpClientContext();
  CookieStore cookieStore = new BasicCookieStore();

  /*  传入问题以及页数以及页面大小*/
  List<Answer> getAnswer(String question,int size) throws HttpProcessException {

    List<Answer> results = new ArrayList<>();
    context.setCookieStore(cookieStore);
    //设置保存cookie
    String index = "http://12316.agri.cn/";
    //访问主页获取cookie
    config = HttpConfig.custom().url(index).context(context);
    HttpClientUtil.get(config);
    String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36";
    Header[] headers = HttpHeader.custom().userAgent(userAgent)
        .referer("http://12316.agri.cn/KnowledgeQAList.html").host("12316.agri.cn").build();

    for (int i = 0; i <= MAX; i++) {
      String url =
          "http://12316.agri.cn/Knowledge/KnowledgeQAListGet.aspx?currPage=" + i + "&keyword="
              + question;

      config = HttpConfig.custom().url(url).headers(headers);
      HttpResult httpResult = HttpClientUtil.sendAndGetResp(config.method(HttpMethods.GET));

      String answers = httpResult.getResult();
      //此时获取了json数据答案

      JSONObject jsonObject = JSON.parseObject(answers);
      JSONArray records = jsonObject.getJSONArray("records");
      for (Object record : records) {
        String recordJson = record.toString();
        Answer answer = JSON.parseObject(recordJson, Answer.class);
        if (answer.getAskContent().contains(question)) {
          results.add(answer);
        }
        if (results.size() >= size) {
          return results;
        }
      }
    }
    return results;
  }

  public static void main(String[] args) {

    try {
      List<Answer> results = new QuestionSpider().getAnswer("玉米",10);

      for (Answer result : results) {
        System.out.println(result.getAskContent());
      }
    } catch (HttpProcessException e) {
      e.printStackTrace();
    }
  }

}
