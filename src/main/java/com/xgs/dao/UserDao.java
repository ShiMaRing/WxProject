package com.xgs.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgs.pojo.Article;
import com.xgs.pojo.WechatUser;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@TableName("wechat_user")
public interface UserDao extends BaseMapper<WechatUser> {

  //根据id获取相应的用户
  public WechatUser getUserByID(String id);

  public int insertUser(WechatUser wechatUser);

  public int collect(@Param("userId") int userId, @Param("articleId") int articleId);

  public int delete(@Param("userId") int userId, @Param("articleId") int articleId);

  public List<Article> show(int userId);

}
