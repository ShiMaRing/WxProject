package com.xgs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgs.pojo.General;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface GeneralDao  extends BaseMapper<General> {

  @Select("select general_name,pid from general")
  List<General> findAll();

}
