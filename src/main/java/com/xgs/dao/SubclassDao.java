package com.xgs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgs.pojo.Subclass;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public  interface SubclassDao extends BaseMapper<Subclass> {


  @Select("select subclass_name from subclass")
  List<String> findAllName();

  @Select("select subclass_name,general_pid from subclass where general_pid=#{pid}")
  List<Subclass> findByPid(String pid);

  @Delete("delete from subclass where subclass_name=#{name}")
  int deleteByName(String name);

}
