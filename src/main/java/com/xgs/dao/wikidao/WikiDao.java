package com.xgs.dao.wikidao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgs.pojo.Wiki;
import java.util.List;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.stereotype.Repository;

//写入所有的百科信息
@Repository
public interface WikiDao  extends BaseMapper<Wiki> {

  //插入所有wiki
  int insertWiki(List<Wiki> wikis);

}
