package com.xgs.util;


//该类负责数据的写入操作

import com.xgs.dao.wikidao.WikiIndexDao;
import com.xgs.pojo.WikiIndex;
import com.xgs.spider.DataSpider;
import com.xgs.spider.PolicySpider;
import com.xgs.spider.WikiSpider;
import java.util.List;
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
  WikiSpider wIkiSpider;

  @Autowired
  WikiIndexDao wikiIndexDao;

  //用来写入所有的index，并且包括了url
  @Transactional
  public int writeWikiIndex(){
    List<WikiIndex> index = wIkiSpider.getIndex();
    int count=0;
    for (WikiIndex wikiIndex : index) {
      String title=wikiIndex.getTitle();
      for (String s : wikiIndex.getMap().keySet()) {
        String value=wikiIndex.getMap().get(s);
        count+=wikiIndexDao.insertIndex(s,value,title);
      }
    }
    return count;
  }

  @Transactional
  int writeWikis(){


    return 0;
  }




}
