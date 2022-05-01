package com.xgs.dao.wikidao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgs.pojo.Wiki;
import java.util.List;
import org.springframework.stereotype.Repository;

//写入所有的百科信息
//需要提供查询方法，按照类别查询
@Repository
public interface WikiDao extends BaseMapper<Wiki> {

  //插入所有wiki
  int insertWiki(List<Wiki> wikis);

  List<Wiki> selectNotBlankByType(String typename);

  List<Wiki> selectByTitle(String words);

}
