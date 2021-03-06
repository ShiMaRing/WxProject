package com.xgs.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.xgs.dao.dataDao.SubclassDao;
import com.xgs.pojo.General;
import com.xgs.pojo.GraphData;
import com.xgs.pojo.Province;
import com.xgs.pojo.Subclass;
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

  CookieStore cookieStore = new BasicCookieStore();
  HttpClientContext context;

  static Map<Integer, String> map;
  @Autowired
  SubclassDao subclassDao;

  static {
    map = new HashMap<>();
    map.put(1, "近7日");
    map.put(2, "近一月");
    map.put(3, "近半年");
    map.put(4, "近一年");
  }

  public DataSpider() {
    context = new HttpClientContext();
    context.setCookieStore(cookieStore);
  }

  /**
   * 该方法提取所有省份名称以及id
   */
  public HttpConfig getConfig() {
    String host = "pfsc.agri.cn";
    String origin = "http://pfsc.agri.cn";
    String referer = "http://pfsc.agri.cn/";
    String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36";
    Header[] origins = HttpHeader.custom().host(host).referer(referer).userAgent(userAgent)
        .other("Origin", origin).contentType("application/json;charset=UTF-8").build();
    return HttpConfig.custom().context(context).headers(origins);
  }

  public List<Province> getProvince() throws HttpProcessException {
    HttpConfig config = getConfig();
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

  /**
   * 该方法用来获取大类
   */
  public List<General> getGenerals() throws HttpProcessException {
    ArrayList<General> generals = new ArrayList<>();
    HttpConfig config = getConfig();
    String url = "http://pfsc.agri.cn/api/priceQuotationController/getVarietyMajorCategories";
    config.url(url);
    String res = HttpClientUtil.post(config);
    JSONArray jsonArray = JSON.parseObject(res).getJSONArray("content");
    for (Object o : jsonArray) {
      General general = new General();
      JSONObject jsonObject = JSON.parseObject(o.toString());
      general.setGeneralName(jsonObject.getString("varietyTypeName"));
      general.setPid(jsonObject.getString("varietyTypeCode"));
      generals.add(general);
    }
    return generals;
  }

  /**
   * 该方法使用大类的pid来查询小类
   */
  public List<Subclass> getSubclass(String pid) throws HttpProcessException {
    HttpConfig config = getConfig();
    ArrayList<Subclass> subclasses = new ArrayList<>();
    String url = "http://pfsc.agri.cn/api/priceQuotationController/getVarietyNameByPid?pid=" + pid;
    config.url(url);
    Map<String, Object> map = new HashMap<>();
    map.put("pid", pid);
    config.json(JSON.toJSONString(map));
    String res = HttpClientUtil.post(config);
    JSONArray lists = JSON.parseArray(JSON.parseObject(res).getString("content"));
    for (Object list : lists) {
      JSONArray objects = JSON.parseArray(list.toString());
      for (Object object : objects) {
        Subclass subclass = new Subclass();
        JSONObject jsonObject = JSON.parseObject(object.toString());
        subclass.setSubclassName(jsonObject.getString("varietyName"));
        subclass.setGeneralPid(pid);
        subclasses.add(subclass);
      }
    }
    //解析res，返回对应的list
    return subclasses;
  }

  //获取省份的价格
  public GraphData getProvincePrice(String name, String provinceId, String type) {
    HttpConfig config = getConfig();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(
        "http://pfsc.agri.cn/api/marketQuotationController/getSingleVarietiesProvince?");
    stringBuilder.append("name=").append(name).append("&cycle=").append(type)
        .append("&provinceCode=").append(provinceId).append("&order=ASC");
    Map<String, Object> map = new HashMap<>();
    map.put("name", name);
    map.put("cycle", type);
    map.put("order", "ASC");
    map.put("provinceCode", provinceId);
    String url = stringBuilder.toString();
    config.url(url).map(map);
    String result = null;
    try {
      result = HttpClientUtil.post(config);
    } catch (HttpProcessException e) {
      e.printStackTrace();
    }
    //拿到了对应的json数据，进行解析封装返回，解析方法应该是通用的
    return parse(result);

  }

  //获取全国的统一价格
  public GraphData getCountryPrice(String name, String type) {
    HttpConfig config = getConfig();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(
        "http://pfsc.agri.cn/api/marketQuotationController/getSingleVarietiesCountry?");
    stringBuilder.append("name=").append(name).append("&cycle=").append(type).append("&order=ASC");
    Map<String, Object> map = new HashMap<>();
    map.put("name", name);
    map.put("cycle", type);
    map.put("order", "ASC");
    String url = stringBuilder.toString();
    config.url(url).map(map);
    String result = null;
    try {
      result = HttpClientUtil.post(config);
    } catch (HttpProcessException e) {
      e.printStackTrace();
    }
    //拿到了对应的json数据，进行解析封装返回，解析方法应该是通用的
    return parse(result);
  }

  public GraphData parse(String result) {
    //传入字符串进行解析
    GraphData graphData = new GraphData();

    JSONObject res = JSON.parseObject(result);
    String content = res.getString("content");
    JSONObject jsonObject = JSON.parseObject(content);
    JSONArray prices = jsonObject.getJSONArray("price");

    if (prices.size() == 0) {
      graphData.setBlank(true);
      return graphData;
    } else {
      graphData.setBlank(false);
    }

    Object min = prices.get(0);
    Map<String, String> minMap = JSON.parseObject(min.toString(), Map.class);
    graphData.setMinPrice(minMap.get("middlePrice"));
    graphData.setMinPlace(minMap.get("marketName"));
    graphData.setMinTime(minMap.get("reportTime"));

    Object max = null;
    if (prices.size() > 1) {
      max = prices.get(1);
    } else {
      max = min;
    }

    Map<String, String> maxMap = JSON.parseObject(max.toString(), Map.class);
    graphData.setMaxPrice(maxMap.get("middlePrice"));
    graphData.setMaxPlace(maxMap.get("marketName"));
    graphData.setMaxTime(maxMap.get("reportTime"));

    String[] x = jsonObject.getObject("x", String[].class);
    double[] y = jsonObject.getObject("y", double[].class);

    String timeRange = x[0] + "--" + x[x.length - 1];
    graphData.setTimeRange(timeRange);

    graphData.setX(x);
    graphData.setY(y);
    return graphData;
  }

  public static void main(String[] args) {
    System.out.println(new DataSpider().getProvincePrice("猪肉(白条猪)", "110000", "近一年"));
  }
}
