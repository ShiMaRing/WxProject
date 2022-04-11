package com.xgs.pojo;

import lombok.Data;

@Data
public class Article {
  String createTime;//文章创建时间
  String id;//文章id
  String ovtitle;//存储标题
  String url;//存储具体文章的url
  String source;//存储信息来源
  String thumbImage;//存储图片路径
  String content;//存储文章内容
  String describe;//大致介绍
}
