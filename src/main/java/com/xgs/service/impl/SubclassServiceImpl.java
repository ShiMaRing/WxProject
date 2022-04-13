package com.xgs.service.impl;

import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xgs.dao.GeneralDao;
import com.xgs.dao.SubclassDao;
import com.xgs.pojo.General;
import com.xgs.pojo.Subclass;
import com.xgs.service.SubclassService;
import com.xgs.spider.DataSpider;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LENOVO 提供方法，插入数据与根据id查询数据
 */
@Service
public class SubclassServiceImpl implements SubclassService {

  @Autowired
  DataSpider dataSpider;
  @Autowired
  SubclassDao subclassDao;
  @Autowired
  GeneralDao generalDao;

  @Override
  public int addSubclasses() {
    List<General> all = generalDao.findAll();
    for (General general : all) {
      String pid=general.getPid();
      try {
        List<Subclass> subclass = dataSpider.getSubclass(pid);

      } catch (HttpProcessException e) {
        e.printStackTrace();
      }
    }
    return 0;
  }

  @Override
  public List<Subclass> findByPid(String pid) {
    return null;
  }
}
