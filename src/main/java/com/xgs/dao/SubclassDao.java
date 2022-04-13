package com.xgs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgs.pojo.Subclass;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public  interface SubclassDao extends BaseMapper<Subclass> {

  List<String> getSubclassNameByPid(String pid);

}
