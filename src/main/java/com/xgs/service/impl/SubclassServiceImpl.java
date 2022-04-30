package com.xgs.service.impl;

import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.xgs.dao.dataDao.GeneralDao;
import com.xgs.dao.dataDao.SubclassDao;
import com.xgs.pojo.General;
import com.xgs.pojo.Subclass;
import com.xgs.service.SubclassService;
import com.xgs.spider.DataSpider;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


  /*该方法用以插入所有字类
   * */
  @Override
  @Transactional
  public int addSubclasses() {
    List<General> all = generalDao.findAll();
    int count = 0;
    for (General general : all) {
      String pid = general.getPid();
      try {
        List<Subclass> subclasses = dataSpider.getSubclass(pid);
        for (Subclass subclass : subclasses) {
          int insert = subclassDao.insert(subclass);
          count += insert;
        }
      } catch (HttpProcessException e) {
        e.printStackTrace();
      }
    }
    return count;
  }

  @Override
  public List<Subclass> findByPid(String pid) {
    return subclassDao.findByPid(pid);
  }
}
