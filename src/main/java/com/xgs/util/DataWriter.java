package com.xgs.util;

//该类负责数据的写入操作

import com.xgs.dao.wikidao.WikiDao;
import com.xgs.dao.wikidao.WikiIndexDao;
import com.xgs.pojo.Wiki;
import com.xgs.pojo.WikiIndex;
import com.xgs.spider.DataSpider;
import com.xgs.spider.PolicySpider;
import com.xgs.spider.WikiSpider;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataWriter {

  @Autowired
  DataSpider dataSpider;

  @Autowired
  PolicySpider policySpider;

  @Autowired
  WikiSpider wikiSpider;

  @Autowired
  WikiIndexDao wikiIndexDao;

  @Autowired
  WikiDao wikiDao;

  //用来写入所有的index，并且包括了url
  @Transactional
  public int writeWikiIndex() {
    List<WikiIndex> index = wikiSpider.getIndex();
    int count = 0;
    for (WikiIndex wikiIndex : index) {
      String title = wikiIndex.getTitle();
      for (String s : wikiIndex.getMap().keySet()) {
        String value = wikiIndex.getMap().get(s);
        count += wikiIndexDao.insertIndex(s, value, title);
      }
    }
    return count;
  }

  @Transactional
  public int writeWikis() {
    int count = 0;
    List<WikiIndex> index = wikiSpider.getIndex();
    for (WikiIndex wikiIndex : index) {
      Set<String> set = wikiIndex.getMap().keySet();
      for (String s : set) {
        int id=wikiIndexDao.getIdByName(s);
        String url = wikiIndex.getMap().get(s);
        List<Wiki> wikis = wikiSpider.getBaseInfo(url);
        for (Wiki wiki : wikis) {
          wiki.setTypeId(id);
        }
        count += wikiDao.insertWiki(wikis);
        //将所有数据读取并保存入数据库
      }
    }
    return count;
  }


}
