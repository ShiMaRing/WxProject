package com.xgs.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgs.pojo.WechatUser;
import org.springframework.stereotype.Repository;

@Repository
@TableName("wechat_user")
public interface UserDao extends BaseMapper<WechatUser> {

  //根据id获取相应的用户
  public WechatUser getUserByID(String id);

}
