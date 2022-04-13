package com.xgs.dao;

import com.xgs.pojo.Province;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author LENOVO 提供省份的查找操作即可，不需要要维护
 */
@Mapper
@Repository
public interface ProvinceDao {

  int getId(String name);

  String getNameById(int id);

  List<Province> findAll();

  int addProvince(List<Province> provinces);
}
