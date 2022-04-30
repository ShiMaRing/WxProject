package com.xgs.wxproject.dao;

import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.xgs.dao.dataDao.GeneralDao;
import com.xgs.dao.dataDao.ProvinceDao;
import com.xgs.dao.dataDao.SubclassDao;
import com.xgs.pojo.General;
import com.xgs.pojo.Subclass;
import com.xgs.service.SubclassService;
import com.xgs.spider.DataSpider;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

public class TestApp {
  @Autowired
  ProvinceDao provinceDao;
  @Autowired
  DataSpider dataSpider;
  @Autowired
  GeneralDao generalDao;

  @Autowired
  SubclassDao subclassDao;
  @Autowired
  SubclassService subclassService;

  @Test
  void add(){
    try {
      dataSpider=new DataSpider();
      List<General> generals = dataSpider.getGenerals();
      for (General general : generals) {
        generalDao.insert(general);
      }
    } catch (HttpProcessException e) {
      e.printStackTrace();
    }
  }

  @Test
  void change(Integer x){
    x--;
  }
  @Test
  void t(){
    Integer m=10;
    change(m);
    System.out.println(m);
  }


  @Test
  void patternTest(){
    String url="https://www.nongyie.com/zxuqinyang/2022/51212.html";

    String nextUrl="52122_2.html";
    String pattern = "(\\w+.html)$";
    Pattern r = Pattern.compile(pattern);
    Matcher matcher = r.matcher(url);
    String s = matcher.replaceFirst(nextUrl);
    System.out.println(s);
  }

  @Test
  void selectTest(){
    List<Subclass> aa = subclassDao.findByPid("AA");
    System.out.println(aa);
  }
}
