package com.xgs.controller;


import com.github.pagehelper.PageHelper;
import com.xgs.dao.wikidao.WikiDao;
import com.xgs.dao.wikidao.WikiIndexDao;
import com.xgs.pojo.Result;
import com.xgs.pojo.Wiki;
import com.xgs.pojo.WikiIndex;
import com.xgs.pojo.WikiType;
import com.xgs.spider.WikiSpider;
import java.util.ArrayList;
import java.util.List;
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
