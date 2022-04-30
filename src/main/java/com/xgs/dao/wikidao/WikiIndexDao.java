package com.xgs.dao.wikidao;

import com.xgs.pojo.WikiIndex;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

//该接口用来写入所有的类型

@Repository
public
interface WikiIndexDao {

  int insertIndex(@Param("typename") String typename,@Param("url") String url,@Param("name") String name);

  int getIdByName(@Param("typename") String typename);
}
