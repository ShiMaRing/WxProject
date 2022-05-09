package com.xgs.pojo;


import lombok.Data;
import org.apache.ibatis.annotations.SelectKey;

@Data
public class WechatUser {

  private Integer id;

  private String token;

  private String nickname;

  private String avatarUrl;

  private Integer gender;

  private String country;

  private String province;

  private String city;

  private String openId;

  private String createdAt;

  private String updatedAt;
}