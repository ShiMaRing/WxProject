package com.xgs.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.xgs.dao.ProvinceDao;
import com.xgs.pojo.Province;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 提供单一品种全国平均价 单一品种省份平均价 会将其全部添加至数据库中 需要有品种大类，品种小类，还有周期
 */

@Component
public class DataSpider {

  HttpConfig config;
  CookieStore cookieStore = new BasicCookieStore();
  HttpClientContext context ;

  @Autowired
  ProvinceDao provinceDao;
  public DataSpider() {
    context = new HttpClientContext();
    context.setCookieStore(cookieStore);
    config = HttpConfig.custom().context(context);
    String host = "pfsc.agri.cn";
    String origin = "http://pfsc.agri.cn";
    String referer = "http://pfsc.agri.cn/";
    String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36";
    Header[] origins = HttpHeader.custom().host(host).referer(referer).userAgent(userAgent)
        .other("Origin", origin).contentType("application/json;charset=UTF-8").build();
    config.headers(origins);
  }

  /**
   * 该方法提取所有省份名称以及id
   */
  public List<Province> getProvince() throws HttpProcessException {
    String index = "http://pfsc.agri.cn/#/marketQuotation";
    config.url(index);
    HttpClientUtil.get(config);
    String url = "http://pfsc.agri.cn/price_portal/region/selectList";
    config.url(url);
    Map<String, Object> map = new HashMap<>();
    map.put("pid", 0);
    //网站存在反爬，需要转为json字符串进行发送
    String s = JSON.toJSONString(map);
    config.json(s);
    //此时获取到了相关的响应，进行解析
    String response = HttpClientUtil.post(config);
    List<Province> provinces = new ArrayList<>();
    JSONObject object = JSON.parseObject(response);
    JSONArray jsonArray = JSON.parseArray(object.getString("data"));
    for (Object o : jsonArray) {
      Province province = new Province();
      JSONObject pro = JSON.parseObject(o.toString());
      province.setId(Integer.parseInt(pro.getString("id")));
      province.setName(pro.getString("name"));
      provinces.add(province);
    }
    return provinces;
  }




  public static void main(String[] args) {
    try {
      List<Province> province = new DataSpider().getProvince();
      System.out.println(province);
    } catch (HttpProcessException e) {
      e.printStackTrace();
    }
  }
}
