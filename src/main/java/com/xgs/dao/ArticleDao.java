package com.xgs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgs.pojo.Article;
import com.xgs.pojo.Wiki;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cjh
 * @date 2022/5/2 - 16:40
 */
@Repository
@Mapper
public interface ArticleDao extends BaseMapper<Article> {


    List<Article> selectByType(@Param("type")String type);

    List<String> selectAllType();
}
