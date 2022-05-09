package com.xgs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.xgs.dao.UserDao;
import com.xgs.pojo.WechatLoginRequest;
import com.xgs.pojo.WechatUser;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

//客户端会同时将信息都发送过来
@Service
public class UserServiceImpl {

  @Autowired
  StringRedisTemplate stringRedisTemplate;
  private static final String REQUEST_URL = "https://api.weixin.qq.com/sns/jscode2session";
  private static final String PARAM = "authorization_code";
  @Autowired
  UserDao userDao;
  String appId = "*****************";
  String appSecret = "******************";

  //使用map封装响应数据
  //  "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code"
  public Map<String, String> getInfo(WechatLoginRequest wechatLoginRequest)
      throws HttpProcessException {

    System.out.println(wechatLoginRequest.toString());

    Map<String, String> map = new HashMap<>();
    String code = wechatLoginRequest.getCode();
    HttpConfig httpConfig = HttpConfig.custom();
    //创造请求url
    String stringBuilder = REQUEST_URL + "?appid=" + appId + "&secret="
        + appSecret + "&js_code="
        + code + "&grant_type=authorization_code";
    //获取响应来进行解析
    httpConfig.url(stringBuilder);

    String result = HttpClientUtil.get(httpConfig);

    System.out.println(result);

    JSONObject jsonObject = JSON.parseObject(result);
    //解析json获取授权信息并进行存储
    String openid = jsonObject.getString("openid");
    String session_key = jsonObject.getString("session_key");
    //获取对应的openid以及session_id
    map.put("openid", openid);
    map.put("session_key", session_key);
    return map;
  }

  //拿到对应的信息之后,进行解析操作
  //应当判断用户是否是新登陆用户，如果是新登陆用户就返回true，否则返回false
  public WechatUser parseUser(WechatLoginRequest wechatLoginRequest, String openId) {
    System.out.println(openId);
    WechatUser user = userDao.getUserByID(openId);
    String json = wechatLoginRequest.getRawData();
    JSONObject rawData = JSON.parseObject(json);
    //入库
    String nickName = rawData.getString("nickName");
    String avatarUrl = rawData.getString("avatarUrl");
    String gender = rawData.getString("gender");
    String city = rawData.getString("city");
    String country = rawData.getString("country");
    String province = rawData.getString("province");
    //说明是第一次登陆，获取信息将其添加到数据库中
    if (user == null) {
      user = new WechatUser();
      user.setOpenId(openId);
      user.setProvince(province);
      user.setCity(city);
      user.setCountry(country);
      user.setAvatarUrl(avatarUrl);
      user.setGender(Integer.valueOf(gender));
      user.setCreatedAt(new Date().toString());
      user.setUpdatedAt(new Date().toString());
      user.setNickname(nickName);
      userDao.insertUser(user);
      return user;
    } else {
      user.setProvince(province);
      user.setCity(city);
      user.setCountry(country);
      user.setAvatarUrl(avatarUrl);
      user.setGender(Integer.valueOf(gender));
      user.setNickname(nickName);
      user.setUpdatedAt(new Date().toString());
      userDao.updateById(user);
    }
    return user;//返回当前用户的openid，用来获取uuid
  }


  //根据uuid获取openId之后来获取user并返回
  public WechatUser getUser(String UUID) {
    //此处使用redis获取
    String openId = stringRedisTemplate.opsForValue().get(UUID);
    if (openId == null) {
      return null;
    }
    return userDao.getUserByID(openId);
  }

  //思路，用户首先检查本地是否有key，如果有的话就携带key来获取用户信息
  //返回uuid，用户存储在本地，下次请求的时候携带该uuid
  public String getUUid(String openId) {
    UUID uuid = UUID.randomUUID();
    stringRedisTemplate.opsForValue().set(uuid.toString(), openId,600*100, TimeUnit.SECONDS);
    return uuid.toString();
  }

}
