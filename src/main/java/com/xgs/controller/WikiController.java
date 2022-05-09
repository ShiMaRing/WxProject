package com.xgs.controller;


import com.github.pagehelper.PageHelper;
import com.xgs.dao.wikidao.WikiDao;
import com.xgs.dao.wikidao.WikiIndexDao;
import com.xgs.pojo.*;
import com.xgs.spider.WikiSpider;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wiki")
public class WikiController {

  @Autowired
  WikiSpider wikiSpider;
  @Autowired
  WikiIndexDao wikiIndexDao;
  @Autowired
  WikiDao wikiDao;

//为直接搜索出来的内容添加样式
  String addStyleForContent(String contentPast){
    Pattern pattern1 = Pattern.compile("<img{1}");
    Matcher matcher1 = pattern1.matcher(contentPast);

    String result1 = matcher1.replaceAll("<img  style=\" height: auto;  width:80%;   margin: 0 auto; display: block;\"");

    Pattern pattern2 = Pattern.compile("<h3>{1}");
    Matcher matcher2 = pattern2.matcher(result1);

    String result2 = matcher2.replaceAll("<h3 style=\"text-indent: 30px;\n" +
            "    font-size: 20px;\n" +
            "    line-height: 30px;\n" +
            "    color: #EE8262;\">");

    return result2;
  }



  //修改非空的内容  使用一次就不能在使用了 更新数据库中的内容
  @RequestMapping(value = "/changeAllContent",method = RequestMethod.GET)
  public int changeAllContent(){
    int num=0;
    List<Wiki> wikiList = wikiDao.getAll();
    for(Wiki wiki:wikiList){
      String title = wiki.getTitle();

      String contentPast = wiki.getContent();
      if(contentPast == null){
        continue;
      }

      Pattern pattern1 = Pattern.compile("<img{1}");
      Matcher matcher1 = pattern1.matcher(contentPast);

      String result1 = matcher1.replaceAll("<img  style=\"    margin: 0 auto; display: block;\"");

      Pattern pattern2 = Pattern.compile("<h3>{1}");
      Matcher matcher2 = pattern2.matcher(result1);

      String result2 = matcher2.replaceAll("<h3 style=\"text-indent: 30px;\n" +
              "    font-size: 20px;\n" +
              "    line-height: 30px;\n" +
              "    color: #EE8262;\">");
      num+= wikiDao.updateContentByTitle(title,result2);

    }

    return num;
  }




  /**
   * 返回所有的类别
   */
  @RequestMapping(value = "/getType", method = RequestMethod.GET)
  List<WikiType> getIndex() {
    List<WikiIndex> index = wikiSpider.getIndex();
    List<WikiType> wikiTypes = new ArrayList<>();
    for (WikiIndex wikiIndex : index) {
      WikiType wikiType = new WikiType();
      String name = wikiIndex.getTitle();
      wikiType.setName(name);
      List<String> types = new ArrayList<>();
      for (String s : wikiIndex.getMap().keySet()) {
        types.add(s);
      }
      wikiType.setTypes(types);
      wikiTypes.add(wikiType);
    }
    return wikiTypes;
  }

  /**
   * 前端传入get请求,该方法用于获取一开始点进去的文章列表
   */
  @RequestMapping(value = "/getList/{typename}/{page}/{size}", method = RequestMethod.GET)
  List<Wiki> getWikis(@PathVariable("typename") String typeName, @PathVariable("page") int page,
      @PathVariable("size") int size) {
    PageHelper.startPage(page, size);
    return wikiDao.selectNotBlankByType(typeName);
  }

  /**
   * 该方法用来获取用户搜索关键字得到的wiki
   */
  @RequestMapping(value = "/search/{words}/{page}/{size}", method = RequestMethod.GET)
  List<Wiki> searchWikis(@PathVariable("words") String words,  @PathVariable("page") int page,
      @PathVariable("size") int size) {
    PageHelper.startPage(page, size);
    List<Wiki> wikis = wikiDao.selectByTitle(words);
    List<Wiki> result=new ArrayList<>();
    for (Wiki wiki : wikis) {
      if (wiki.isBlank()) {
        Wiki temp=wikiSpider.parse(wiki.getUrl());

        String contentAfter = addStyleForContent(temp.getContent());
        temp.setContent(contentAfter);

        temp.setTitle(wiki.getTitle());
        temp.setUrl(wiki.getUrl());
        result.add(temp);
      }else {
        result.add(wiki);
      }
    }
    return result;
  }
}
