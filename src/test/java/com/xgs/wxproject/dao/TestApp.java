package com.xgs.wxproject.dao;

import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xgs.dao.GeneralDao;
import com.xgs.dao.ProvinceDao;
import com.xgs.pojo.General;
import com.xgs.spider.DataSpider;
import java.util.List;
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
  void selectTest(){

    List<General> generals = generalDao.findAll();
    for (General name : generals) {
      System.out.println(name);
    }

  }
}
