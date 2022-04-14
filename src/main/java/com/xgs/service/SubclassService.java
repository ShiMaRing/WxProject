package com.xgs.service;

import com.xgs.pojo.Subclass;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author LENOVO
 */
public interface SubclassService {

  /**
   * 向数据库中添加数据
   */
  int addSubclasses();

  /**
   * 使用pid来查询对应的子类，返回子类的列表
   */
  List<Subclass> findByPid(String pid);



}
