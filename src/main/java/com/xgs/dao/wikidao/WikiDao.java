package com.xgs.dao.wikidao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgs.pojo.Wiki;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.stereotype.Repository;

@Repository
public interface WikiDao  extends BaseMapper<Wiki> {

}
