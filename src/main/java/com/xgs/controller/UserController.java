package com.xgs.controller;

import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.xgs.dao.UserDao;
import com.xgs.pojo.Article;
import com.xgs.pojo.Result;
import com.xgs.pojo.WechatLoginRequest;
import com.xgs.pojo.WechatUser;
import com.xgs.service.impl.UserServiceImpl;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  /**
   * 首先拿到code，之后拿着code去请求微信接口，拿到openid以及session_key 生成uuid并将该uuid作为key，openid等作为参数存入redis
   * 前台携带uuid请求后台，拿到uuid之后拿到openid等，
   */

  @Autowired
  UserServiceImpl userService;
  @Autowired
  UserDao userDao;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public WechatUser login(@RequestBody WechatLoginRequest wechatLoginRequest) {
    Map<String, String> info = null;
    try {
      info = userService.getInfo(wechatLoginRequest);
    } catch (HttpProcessException e) {
      e.printStackTrace();
    }
    //用户调取登录请求，查询是否有用户,如果有
    if (info == null) {
      return null;
    }
    String openid = info.get("openid");
    System.out.println(openid);
    WechatUser wechatUser = userService.parseUser(wechatLoginRequest, openid);
    String uUid = userService.getUUid(openid);
    wechatUser.setToken(uUid);
    return wechatUser;
  }


  @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
  public Result getUser(@RequestParam(value = "uid") String uid) {
    WechatUser user = userService.getUser(uid);
    Result result = new Result();
    if (user == null) {
      result.setFlag(false);
      result.setStatusCode(404);
      result.setDesc(uid);
    } else {
      result.setFlag(true);
      result.setData(user);
      result.setStatusCode(200);
    }
    return result;
  }


  //添加收藏
  @RequestMapping(value = "/collect", method = RequestMethod.POST)
  public Result collect(@RequestParam("id") String articleId, @RequestParam("uid") String uid) {
    //收藏文章
    WechatUser user = userService.getUser(uid);
    Result result = new Result();
    if (user == null) {
      result.setDesc("请登陆后操作");
      result.setStatusCode(500);
      result.setFlag(false);
      return result;
    }
    int userId = user.getId();
    int artId = Integer.parseInt(articleId);
    userDao.collect(userId, artId);
    result.setDesc("收藏成功");
    result.setFlag(true);
    result.setStatusCode(200);
    return result;
  }

  //删除收藏
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public Result delete(@RequestParam("id") String articleId, @RequestParam("uid") String uid) {
    //收藏文章
    WechatUser user = userService.getUser(uid);
    Result result = new Result();
    if (user == null) {
      result.setDesc("请登陆后操作");
      result.setStatusCode(500);
      result.setFlag(false);
      return result;
    }
    int userId = user.getId();
    int artId = Integer.parseInt(articleId);
    userDao.delete(userId, artId);
    result.setDesc("删除成功");
    result.setFlag(true);
    result.setStatusCode(200);
    return result;
  }

  //展示所有收藏
  @RequestMapping(value = "/show", method = RequestMethod.POST)
  public Result show(@RequestParam("uid") String uid) {
    //收藏文章
    WechatUser user = userService.getUser(uid);
    Result result = new Result();
    if (user == null) {
      result.setDesc("请登陆后操作");
      result.setStatusCode(500);
      result.setFlag(false);
      return result;
    }
    List<Article> show = userDao.show(user.getId());
    result.setData(show);
    result.setDesc("返回收藏成功");
    result.setFlag(true);
    result.setStatusCode(200);
    return result;
  }

}

